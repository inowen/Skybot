package inowen.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.Vec3d;

/**
 * @class PlayerMovementHelper
 * @author PrinceChaos
 * 
 * Some methods that deal with the angle calculations and such to adjust where the player is looking at,
 * how the player should move, etc.
 * 
 * No initialization required, all the methods will be static.
 * 
 * @note Doesn't sprint on its own yet.
 * 
 */
public class PlayerMovementHelper {
	
	private static Minecraft mc = Minecraft.getInstance();
	
	
	/**
	 * Make the player look at a position (given in form of its position vector)
	 * The player will be looking at the head of the position vector. 
	 * (Old code. This only looks in that direction, pitch isn't changed.
	 * if you want the pitch too, use getPitchToLookAt(Vec3 target) ).
	 * @param target
	 */
	public static void lookAtPosition(Vec3d target) {
		// Get the rotationYaw necessary to look at that target
		double rotationYaw = getYawToLookAt(target);
		
		// Make the player look in the given direction.
		mc.player.rotationYaw = (float) rotationYaw;
	}
	
	
	/**
	 * Calculate the rotationYaw necessary to look at a certain position vector from where the player is standing.
	 * Used in lookAtPosition(Vec3 target) 
	 * @param targetPosition
	 * @return
	 */
	public static double getYawToLookAt(Vec3d targetPosition) {
		Vec3d playerPosition = mc.player.getPositionVector();
		// Find the look vector (the vector that goes from playerPosition to target)
		Vec3d lookVector = targetPosition.subtract(playerPosition);
		if (lookVector.length() < 1.0E-4D) {
			return 0; // Nobody cares. It will never try to look at something this close. 
						// (As long as PathFollower.ACCURACY is set higher than 1.0E-4D)
 		}
		// Normalize means divide each coordinate by the length of the vector, making the total length 1
		lookVector = new Vec3d(lookVector.getX(), 0, lookVector.getZ()); // Imagine it being a Vec2D. Not using yCoord, and it would mess up the normalize.
		lookVector = lookVector.normalize();
		
		// These would be sin and cos that define this same angle in a standard XZ coordinate system
		// (Minecraft is quirky like that)
		double sinYaw = -lookVector.getX();
		double cosYaw = lookVector.getZ();
		double rotationYaw = 0;  // Easier to debug.
		
		// Depending on the quadrant that the yaw angle is in (Q1, Q2, Q3, Q4), use properties
		// of asin, acos, sin and cos to find out the yaw angle.
		if (sinYaw>=0 && cosYaw>=0) {
			// Q1
			rotationYaw = Math.toDegrees(Math.asin(sinYaw));
		}
		else if (sinYaw>=0 && cosYaw<0) {
			// Q2
			rotationYaw = Math.toDegrees(Math.acos(cosYaw));
		}
		else if (sinYaw<0 && cosYaw<0) {
			// Q3
			rotationYaw = Math.toDegrees(Math.asin(-sinYaw)) - 180.0D;
		}
		else if (sinYaw<0 && cosYaw>=0) {
			// Q4
			rotationYaw = Math.toDegrees(Math.asin(sinYaw));
		}
		else {
			System.out.println("This should never happen. Check ifs plz");
		}
		
		// Make sure the angles are inside the accepted range for yaw: [-180, 180)
		// The angles will never be far off, so the while loops aren't inefficient.
		while (rotationYaw >= 180) {
			rotationYaw -= 180;
		}
		while(rotationYaw < -180) {
			rotationYaw += 180;
		}
		return rotationYaw;
	}
	
	
	/**
	 * The pitch to look at a vector position
	 * (knowing that the player's head is at 1+5/8 blocks above the floor,
	 *  and knowing that the mc.playerPositionVector().yCoord is the max bound of
	 *  the block that the player is standing on)
	 * @param target The position vector of the target
	 * @return Pitch to look at that location.
	 */
	public static double getPitchToLookAt(Vec3d target) {
		Vec3d playerPosition = mc.player.getPositionVector();
		// This would be easier/more accurate with mc.player.getEyePosition() but whatever, old code.
		Vec3d headPosition = CoordinateTranslator.offsetFromFeetToHeadHeight(playerPosition);
		
		Vec3d desiredLookVector = target.subtract(headPosition);
		double distFromYAxis = Math.sqrt(desiredLookVector.getX()*desiredLookVector.getX() +
				desiredLookVector.getZ()*desiredLookVector.getZ());
		
		double pitch = 90.0D;
		if (distFromYAxis > 0.000001D) {
			double sineOfPitch = -desiredLookVector.getY() / desiredLookVector.length();
			pitch = Math.toDegrees(Math.asin(sineOfPitch));
		}
		
		return pitch;
	}
	
	
	
	/**
	 * Set forward keybind to pressed.
	 */
	public static void runForward() {
		mc.gameSettings.keyBindForward.setPressed(true);
	}
	
	/**
	 * Unpress the forward keybind.
	 */
	public static void stopRunning() {
		mc.gameSettings.keyBindForward.setPressed(false);
	}
	
	/**
	 * Set jump key to pressed
	 */
	public static void jump() {
		mc.gameSettings.keyBindJump.setPressed(true);
	}
	
	/**
	 * Unset jump keybind.
	 * PathFollower will do this every time it is collided vertically but not horizontally.
	 */
	public static void stopJumping() {
		mc.gameSettings.keyBindJump.setPressed(false);
	}

	public static void goRight() {
		mc.gameSettings.keyBindRight.setPressed(true);
	}

	public static void stopGoingRight() {
		mc.gameSettings.keyBindRight.setPressed(false);
	}

	public static void goLeft() {
		mc.gameSettings.keyBindLeft.setPressed(true);
	}

	public static void stopGoingLeft() {
		mc.gameSettings.keyBindLeft.setPressed(false);
	}
	
	
	/**
	 * This "unpresses" all keybinds that the bot could potentially be using. 
	 * **********************************************************************
	 * Use this to ensure that after finishing the path it won't continue running in a random direction,
	 * or to stop it when it is supposed to fall and not do anything else.
	 */
	public static void desetAllkeybinds() {
		mc.gameSettings.keyBindForward.setPressed(false);
		mc.gameSettings.keyBindSprint.setPressed(false);
		mc.gameSettings.keyBindJump.setPressed(false);
		mc.gameSettings.keyBindUseItem.setPressed(false);
		mc.gameSettings.keyBindAttack.setPressed(false);
		mc.gameSettings.keyBindLeft.setPressed(false);
		mc.gameSettings.keyBindRight.setPressed(false);
	}
	
}
