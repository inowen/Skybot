package inowen.skybot.bots.oldCropBot.context;

import inowen.config.SkybotConfig;
import inowen.utils.CoordinateTranslator;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Keeps information about from where to where (x,z) -> (x,z) the farm goes.
 * The corners aren't the stone wall, but the blocks directly before the walls.
 * Scanning goes from minX and keeps going until <= maxX, same for z.
 * 
 * Utility functions and needed stuff about it (is position P inside?)
 * @author PrinceChaos
 *
 */
public class FarmZoneConstraints {
	
	public static int MAX_FARM_SIZE = SkybotConfig.OldCropBot.MAX_FARM_RADIUS.value;
	
	// Four corners of the zone.
	public int minX = 0;
	public int minZ = 0;
	public int maxX = 0;
	public int maxZ = 0;
	
	public int yLevel = 0;
	
	/**
	 * Constructor does nothing. Everything is at 0.
	 */
	public FarmZoneConstraints() {}
	
	
	/**
	 * Assign the constraints of the farm.
	 * @param worldIn
	 * @param posInsideFarm A block position from inside the farm (where a plant should be).
	 * @param barrierBlock
	 * @throws Exception 
	 */
	public void getConstraints(World worldIn, BlockPos posInsideFarm, Block barrierBlock) throws Exception {
		
		// Assign the height level of the farm
		yLevel = posInsideFarm.getY();
		
		// Find minX
		minX = posInsideFarm.getX();
		BlockPos posMinX = new BlockPos(posInsideFarm);
		
		while(( worldIn.getBlockState(posMinX)==null ? 0 : worldIn.getBlockState(posMinX).getBlock()) != barrierBlock) {
			minX--;
			posMinX = new BlockPos(posMinX.getX()-1, posMinX.getY(), posMinX.getZ());
			// Check if the farm could be bigger, or if this is past the biggest size.
			if (minX < posInsideFarm.getX()-MAX_FARM_SIZE) {
				throw new Exception("Couldn't find farm");
			}
		}
		minX++;
		
		// Find minZ
		minZ = posInsideFarm.getZ();
		BlockPos posMinZ = new BlockPos(posInsideFarm);
		
		while(worldIn.getBlockState(posMinZ).getBlock() != barrierBlock) {
			minZ--;
			posMinZ = new BlockPos(posMinZ.getX(), posMinZ.getY(), posMinZ.getZ()-1);
			if (minZ < posInsideFarm.getZ()-MAX_FARM_SIZE) {
				throw new Exception("Couldn't find farm.");
			}
		}
		minZ++;
		
		// Find maxX
		maxX = posInsideFarm.getX();
		BlockPos posMaxX = new BlockPos(posInsideFarm);
		
		while(worldIn.getBlockState(posMaxX).getBlock() != barrierBlock) {
			maxX++;
			posMaxX = new BlockPos(posMaxX.getX()+1, posMaxX.getY(), posMaxX.getZ());
			if (maxX > posInsideFarm.getX()+MAX_FARM_SIZE) {
				throw new Exception("Couldn't find farm.");
			}
		}
		maxX--;
		
		// Find maxZ
		maxZ = posInsideFarm.getZ();
		BlockPos posMaxZ = new BlockPos(posInsideFarm);
		
		while(worldIn.getBlockState(posMaxZ).getBlock() != barrierBlock) {
			maxZ++;
			posMaxZ = new BlockPos(posMaxZ.getX(), posMaxZ.getY(), posMaxZ.getZ()+1);
			if (maxZ > posInsideFarm.getZ()+MAX_FARM_SIZE) {
				throw new Exception("Couldn't find farm.");
			}
		}
		maxZ--;
		
	}
	
	
	/**
	 * String to show the constraints of the zone.
	 */
	@Override
	public String toString() {
		return "Min x and z: (" + minX + " , " + minZ + ")" + " ------------- " + "Max x&z: (" + maxX + " , " + maxZ+ ")";
	}
	
	
	/**
	 * Whether a BlockPos is inside the constraints of the farm.
	 * @param location
	 * @return
	 */
	public boolean containsBlockPos(BlockPos location) {
		boolean onYLevel = Math.abs(location.getY()-yLevel) < 2;
		boolean onXLevel = location.getX()>=minX && location.getX()<=maxX;
		boolean onZLevel = location.getZ()>=minZ && location.getZ()<=maxZ;
		
		return onXLevel && onYLevel && onZLevel;
	}
	
	
	/**
	 * Same thing. If a position vector points to a location that is inside the farm.
	 * @param location
	 * @return
	 */
	public boolean containsVecPos(Vec3d location) {
		return containsBlockPos(CoordinateTranslator.vec3ToBlockPos(location));
	}

	
	/**
	 * How many blocks long the farm is on the xAxis (how many useful blocks there are)
	 * @return Amount of usable blocks on xAxis
	 */
	public int xLength() {
		return maxX - minX + 1;
	}
	
	/**
	 * How many blocks long the farm is on the zAxis (how many usable blocks there are)
	 * @return Amount of usable blocks on zAxis
	 */
	public int zLength() {
		return maxZ - minZ + 1;
	}
	
}




















// END