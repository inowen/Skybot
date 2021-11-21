package inowen.skybot.bots.oldCropBot.context;

import java.util.ArrayList;

import inowen.config.SkybotConfig;
import inowen.moduleSystem.Module;
import inowen.moduleSystem.ModuleManager;
import inowen.utils.CoordinateTranslator;
import inowen.utils.RayTraceHelper;
import net.minecraft.block.*;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * CONTAINER CLASS FOR INFORMATION Sort of the interface between the world and
 * the bot. Information about: - Environment blocks (farmZone, blocks, farmland,
 * crops+age), -> BlockState matrix - Floor items inside the zone. - Player
 * inventory and resources. - Position of containers. Everything is static here
 * (singleton).
 * 
 * @Limitation Limited to crops where the seeds are the same thing as the dropped item.
 * 			   Simple to change that, just not worth it right now.
 * 
 * @author PrinceChaos
 *
 */
public class ContextManager {

	protected static Minecraft mc = Minecraft.getInstance();

	// The item that the bot should be farming
	public static Item farmedItem = null; // The HFSM module sets this.
	public static Block BARRIER_BLOCK = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(SkybotConfig.OldCropBot.BARRIER_BLOCK.value));

	// The information about the farm in general (how big, contents).
	public static FarmSlot[][] farmSlots = null; // x , z
	public static FarmZoneConstraints zoneConstraints = new FarmZoneConstraints();

	// How the player's inventory is doing, needed information.
	public static InventoryContext inventoryContext;

	// The drops on the floor inside the farmZone.
	public static ArrayList<ItemEntity> itemsToRecollect = new ArrayList<ItemEntity>();
	
	// If this farm was initialized correctly
	public static boolean constraintsInitialized = false;


	/**
	 * Gather relevant session +data, constraints of farm etc. This will only be
	 * called once (in onEnable() of the given module).
	 */
	public static void init() {
		// Get the constaints (corners of the farm)
		try {
			zoneConstraints.getConstraints(mc.world, CoordinateTranslator.vec3ToBlockPos(mc.player.getPositionVector().add(new Vec3d(0,0.1,0))), BARRIER_BLOCK);
			constraintsInitialized = true;
		} catch (Exception e) {
			Module botModule = ModuleManager.getModule("CropsFarmingBot");
			if (botModule.isToggled()) {
				ModuleManager.getModule("CropsFarmingBot").toggle();
			}
			System.out.println("Error: Couldn't find any farm!");
			return;
		}

		// Context information
		inventoryContext = new InventoryContext(farmedItem, mc.player);
		itemsToRecollect.clear();
		farmSlots = new FarmSlot[zoneConstraints.xLength()][zoneConstraints.zLength()];
	}

	/**
	 * Update the information that might change (assuming the location and size of
	 * the farm won't change). - Content of FarmSlots - Content of inventory - Floor
	 * items
	 * 
	 * Wastes computing power, updates everything and not just what is needed, every frame.
	 */
	public static void update() {
		if (constraintsInitialized) {
			// Update items located inside the farmzone.
			itemsToRecollect = itemsInsideFarmzone(zoneConstraints);
			updateFarmSlots(zoneConstraints);
			if (inventoryContext == null) {
				System.out.println("PROBLEM: inventoryContext == null");
			}
			inventoryContext.update(mc.player);
		}
		else {
			System.out.println("OnUpdate not doing anything (no farm). Shutting off.");
			Module theMod = ModuleManager.getModule("CropFarmBot");
			if (theMod.isToggled()) {
				theMod.onDisable();
				theMod.setToggled(false);
			}
		}
	}
	
	
	
	// ---------------------- UTILITY -----------------------------------------------------------//
	
	/**
	 * Whether the constraints of the farm were found and set correctly.
	 * @return
	 */
	public static boolean areConstraintsInitialized() {
		return constraintsInitialized;
	}

	/**
	 * Gets list of the instances of farmedItem currently available within the farm's constraints.
	 * @param fzc
	 * @return List of useful items inside the farm
	 */
	public static ArrayList<ItemEntity> itemsInsideFarmzone(FarmZoneConstraints fzc) {
		ArrayList<ItemEntity> itemsInside = new ArrayList<ItemEntity>();

		// Go through all the entities in the world, filter out ItemEntity.
		for (Object element : mc.world.getAllEntities()) {
			if (element instanceof ItemEntity) {
				ItemEntity theItem = (ItemEntity) element;
				// Check that the item is inside the farm and on the floor.
				if ((theItem.getItem().getItem() == farmedItem) && zoneConstraints.containsVecPos(theItem.getPositionVector())) {
					itemsInside.add(theItem);
				}
			}
		}
		return itemsInside;
	}

	
	
	/**
	 * Update the information about the content of all the FarmSlots in the matrix
	 * of this class. Use FarmSlotContent enum.
	 * 
	 * The farmslot matrix is a member field of the ContextManager. 
	 * This won't create a new one, just update the slots.
	 * 
	 * @param fzc aka FarmZoneConstraints
	 */
	public static void updateFarmSlots(FarmZoneConstraints fzc) {
		
		int matrixX = 0;
		int matrixZ = 0;
		
		// Go through all the blocks in the matrix
		for (int x=fzc.minX; x<=fzc.maxX; x++) {
			for (int z=fzc.minZ; z<=fzc.maxZ; z++) {
				
				// For each Slot:
				FarmSlot currentSlot = new FarmSlot();
				BlockPos currentBlockPosition = new BlockPos(x, fzc.yLevel, z);
				currentSlot.globalBlockPos = currentBlockPosition;
				currentSlot.positionVectorCenter = CoordinateTranslator.blockPosToVectorPosition(currentBlockPosition);
				// Determine the content of the slot: Grown / growing / air / coversWater
				BlockState currentBlockBlockState = mc.world.getBlockState(currentBlockPosition);
				Block currentSlotContentBlock = currentBlockBlockState.getBlock();
				
				// If the current block is air.
				if (currentSlotContentBlock == Blocks.AIR) {
					// There are two options: If there is farmland below, it is empty and has to be replanted.
					// In other cases, we consider this block to be covering water.
					boolean blockBelowIsWater = mc.world.getBlockState(currentBlockPosition.down()).getBlock().getMaterial(mc.world.getBlockState(currentBlockPosition.down())).isLiquid();
					boolean blockBelowIsFarmland = mc.world.getBlockState(currentBlockPosition.down()).getBlock() == Blocks.FARMLAND;
					if (blockBelowIsWater) {
						currentSlot.content = FarmSlotContent.COVERS_WATER;
					}
					else if (blockBelowIsFarmland) {
						currentSlot.content = FarmSlotContent.EMPTY_USABLE;
					}
					else {
						currentSlot.content = FarmSlotContent.NON_IDENTIFIED;
					}
				}
				
				// If the current block is a half slab
				else if (currentSlotContentBlock instanceof SlabBlock) {
					currentSlot.content = FarmSlotContent.COVERS_WATER;
				}
				
				// If the current block is a crop (like something that you would plant)
				else if (currentSlotContentBlock instanceof CropsBlock) {
					boolean stillGrowing = ((CropsBlock) currentSlotContentBlock).canGrow(null, null, currentBlockBlockState, true);
					if (stillGrowing) {
						currentSlot.content = FarmSlotContent.GROWING_CROP;
					}
					else {
						currentSlot.content = FarmSlotContent.GROWN_CROP;
					}
				}
				
				
				// If this throws an arrayOutOfBounds exception, something is weird with the zoneConstaints (probably).
				farmSlots[matrixX][matrixZ] = currentSlot;
				
				// Go towards the next cell in the matrix' current row.
				matrixZ++;
			}
			
			// Next row in the matrix, from the beginning.
			matrixZ = 0;
			matrixX++;
		}
	}
	
	
	/**
	 * Print out the list of items.
	 * Purpose: Debugging items inside farmZone.
	 * 		Checking out if it gets the borders.
	 */
	public static void printOutItemList() {
		System.out.println("List of items inside the FarmZone: " );
		for (ItemEntity item : itemsToRecollect) {
			System.out.println("\t" + item);
		}
	}
	

	
	/**
	 * This is here for debugging. Leaving it just in case I need some of it again.
	 */
	public static void debugMessage() {
		System.out.println("-------- Debug -------------");
		System.out.println("Zone constraints: " + zoneConstraints.toString());
		System.out.println("Player position vector inside farm? " + (zoneConstraints.containsVecPos(mc.player.getPositionVector()) ? "Yes" : "No"));
		System.out.println("Player position BlockPos inside farm? " + (zoneConstraints.containsBlockPos(mc.player.getPosition()) ? "Yes" : "No"));
		System.out.println(" --- Dimensions: ");
		System.out.println("\tLengthX = " + zoneConstraints.xLength());
		System.out.println("\tLengthZ = " + zoneConstraints.zLength());
		System.out.println("List of items inside the FarmZone. Amount: " + itemsToRecollect.size());
		for (ItemEntity item : itemsToRecollect) {
			System.out.println("\t" + item.toString());
		}
		System.out.println("--------- Debug end ---------");
	}


	/**
	 * Print out the content of the slot matrix inside the console (for debugging).
	 * 
	 */
	public static void printOutSlotMatrix() {
		if (farmSlots != null) {
			for (int r=0; r<zoneConstraints.xLength(); r++) {
				for (int col=0; col<zoneConstraints.zLength(); col++) {
					if (farmSlots[r][col].content == FarmSlotContent.COVERS_WATER) {
						System.out.print("W ");
					}
					else if (farmSlots[r][col].content == FarmSlotContent.GROWING_CROP) {
						System.out.print("G ");
					}
					else if (farmSlots[r][col].content == FarmSlotContent.GROWN_CROP) {
						System.out.print("C ");
					}
					else if (farmSlots[r][col].content == FarmSlotContent.EMPTY_USABLE) {
						System.out.print("E ");
					}
					else {
						System.out.print("? ");
					}
				}
				System.out.println("");
			}
			
			System.out.println("----------------");
			System.out.println("Grown crops: " + numGrownCrops());
			System.out.println("Empty usable spots: " + numEmptyUsableSpots());
			System.out.println("----------------");
		}
	}
	
	
	
	/// ----------- INFO ABOUT FARM_SLOTS CONTENT ------------------------- ///
	
	/**
	 * Amount of farmland spots with fully grown crops
	 * @return
	 */
	public static int numGrownCrops() {
		int fullyGrownCrops = 0;
		for (int x=0; x<zoneConstraints.xLength(); x++) {
			for (int z=0; z<zoneConstraints.zLength(); z++) {
				if (farmSlots[x][z].content == FarmSlotContent.GROWN_CROP) {
					fullyGrownCrops++;
				}
			}
		}
		return fullyGrownCrops;
	}
	
	/**
	 * Number of empty spots of farmland, where things can be planted.
	 * @return
	 */
	public static int numEmptyUsableSpots() {
		int freeSpots = 0;
		for (int x=0; x<zoneConstraints.xLength(); x++) {
			for (int z=0; z<zoneConstraints.zLength(); z++) {
				//DEBUGGING: REMOVE THIS WHOLE SECTION
				if (farmSlots[x][z] == null) {
					System.out.println("Debug: farmSlots[x][z] is null here");
				}
				// This message comes out. How does farmSlots[x][z] become null?
				//DEBUGGING: END OF THE SECTION TO BE REMOVED
				if (farmSlots[x][z].content == FarmSlotContent.EMPTY_USABLE) {
					freeSpots++;
				}
			}
		}
		return freeSpots;
	}
	
	
	
	/**
	 * Get the position of the item that is closest to a certain reference.
	 * @param reference 
	 * @return The position of the item closest to the reference.
	 * 			Returns null if there is no item in the environment.
	 */
	public static Vec3d closestItemPosition(Vec3d reference) {
		
		if (ContextManager.itemsToRecollect.size() == 0) {
			return null;
		}
		
		Vec3d referenceToTarget = ContextManager.itemsToRecollect.get(0).getPositionVector().subtract(reference);
		double minDistance = referenceToTarget.length();
		Vec3d closestPosition = ContextManager.itemsToRecollect.get(0).getPositionVector();
		// Go through all the items and find the one with the smallest distance to the reference position.
		
		for (int i=1; i<ContextManager.itemsToRecollect.size(); i++) {
			referenceToTarget = ContextManager.itemsToRecollect.get(i).getPositionVector().subtract(reference);
			double distCurrentItem = referenceToTarget.length();
			
			if (distCurrentItem < minDistance) {
				minDistance = distCurrentItem;
				closestPosition = ContextManager.itemsToRecollect.get(i).getPositionVector();
			}
		}
		
		return closestPosition;
	}
	
	
	/**
	 * Return the block in the farm that is closest to the reference position, 
	 * and has the given @p content.
	 * @param content
	 * @return
	 */
	public static BlockPos getClosestWithGivenContent(FarmSlotContent content, Vec3d referencePos) {
		BlockPos closestPosition = null;
		
		// If the constraints aren't initialized, there is no closest position because we have no information about any positions.
		if (areConstraintsInitialized()) {
			double closestDistance = 999999D;
			
			for (int x=0; x<zoneConstraints.xLength(); x++) {
				for (int z=0; z<zoneConstraints.zLength(); z++) {
					// Allow only slots with content that matches the one that is being searched.
					if (farmSlots[x][z].content == content) {
						Vec3d targetPositionVector = farmSlots[x][z].positionVectorCenter;
						double distanceCurrentSlot = targetPositionVector.subtract(referencePos).length();
						if (closestDistance > distanceCurrentSlot) {
							closestDistance = distanceCurrentSlot;
							closestPosition = farmSlots[x][z].globalBlockPos;
						}
					}
				}
			}
		}
		
		return closestPosition;
	}


	/**
	 * Return the block that is closest to the player AND whose center on the upper face is
	 * visible from the player's perspective.
	 * @param content
	 * @return
	 */
	public static BlockPos getClosestVisibleWithGivenContent(FarmSlotContent content) {
		BlockPos closestPosition = null;
		double heightOffset = ((content == FarmSlotContent.GROWING_CROP) || (content == FarmSlotContent.GROWN_CROP) || (content == FarmSlotContent.EMPTY_USABLE) ? -0.0625 : 0D);

		// If the constraints aren't initialized, there is no closest position because we have no information about any positions.
		if (areConstraintsInitialized()) {
			double closestDistance = 999999D;

			for (int x=0; x<zoneConstraints.xLength(); x++) {
				for (int z=0; z<zoneConstraints.zLength(); z++) {
					// Allow only slots with content that matches the one that is being searched.
					if (farmSlots[x][z].content == content) {
						// Check if the player can actually see that location, or if there is something blocking vision.
						Vec3d slotCenter = CoordinateTranslator.blockPosToVectorPosition(farmSlots[x][z].globalBlockPos).add(new Vec3d(0, heightOffset, 0));
						Vec3d fromHeadToSlot = slotCenter.subtract(CoordinateTranslator.offsetFromFeetToHeadHeight(mc.player.getPositionVector()));
						BlockRayTraceResult tracingResult = RayTraceHelper.firstSeenBlockInDirection(fromHeadToSlot, 20D);
						boolean directLineToPlayer;

						// There is a direct line from the player to the block if the first block that matches is the crop
						// that should be growing there, or if it's empty, the first found block should be the farmland directly below.
						if (content == FarmSlotContent.GROWING_CROP || content == FarmSlotContent.GROWN_CROP) {
							directLineToPlayer = areSameBlockPos(tracingResult.getPos(), farmSlots[x][z].globalBlockPos);
						}
						else if (content == FarmSlotContent.EMPTY_USABLE) {
							directLineToPlayer = areSameBlockPos(tracingResult.getPos(), farmSlots[x][z].globalBlockPos.down());
						}
						else {
							System.out.println("[ContextManager]: Warning. Weird thing happened with raytracing.");
							directLineToPlayer = false;
						}


						// If that is the case, this slot is eligible, and distance is checked.
						if (directLineToPlayer) {
							Vec3d targetPositionVector = farmSlots[x][z].positionVectorCenter;
							double distanceCurrentSlot = targetPositionVector.subtract(mc.player.getEyePosition(0F)).length();
							if (closestDistance > distanceCurrentSlot) {
								closestDistance = distanceCurrentSlot;
								closestPosition = farmSlots[x][z].globalBlockPos;
							}
						}
					}
				}
			}
		}

		return closestPosition;
	}
	
	
	/**
	 * Returns the BlockPos of the closest fully grown crop (inside the farmzone)... 
	 * closest to the referencePos position.
	 * @param referencePos Position vector.
	 * @return
	 */
	public static BlockPos getClosestFullyGrown(Vec3d referencePos) {
		return getClosestWithGivenContent(FarmSlotContent.GROWN_CROP, referencePos);
	}
	
	
	/**
	 * Returns the BlockPos of the closest empty usable farmland slot (closest to referencePos)
	 * @param referencePos Position vector.
	 * @return
	 */
	public static BlockPos getClosestEmptyFarmland(Vec3d referencePos) {
		return getClosestWithGivenContent(FarmSlotContent.EMPTY_USABLE, referencePos);
	}


	/**
	 * Auxiliary function to check if two blockPos refer to the same position.
	 * @param a
	 * @param b
	 * @return
	 */
	private static boolean areSameBlockPos(BlockPos a, BlockPos b) {
		return (a.getX() == b.getX() && a.getY() == b.getY() && a.getZ() == b.getZ());
	}
	
}


















// END