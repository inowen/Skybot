package inowen.pathfind;

import inowen.utils.CoordinateTranslator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;

/**
 * Saves a sequence of nodes that the player has to go through step by step to
 * get from the current position to a certain goal.
 */
public class Path {

    public final double INFINITY = Node.INFINITY;
    private ArrayList<BlockPos> steps;

    /**
     * Create empty path.
     */
    public Path() { steps = new ArrayList<>(); }

    /**
     * Add step to the path.
     * @param nextStep
     */
    public void add(BlockPos nextStep) { steps.add(nextStep); }

    /**
     * Delete all steps.
     */
    public void clear() { steps.clear(); }


    public ArrayList<BlockPos> getSteps() { return steps; }


    /**
     * Return the BlockPos in the path that is closest to the reference position.
     * (or null if there is none).
     * @param reference The BlockPos returned is closest to this position.
     * @return BlockPos or null.
     */
    public BlockPos closestPosContained(Vec3d reference) {
        BlockPos closestPos = null;
        double minDistance = INFINITY;

        // Go through steps, find the one with the position closest to the reference point.
        if (steps != null) {
            for (BlockPos step : steps) {
                Vec3d posVecCurrentStep = CoordinateTranslator.blockPosToVectorPosition(step);
                double distFromRef = reference.subtract(posVecCurrentStep).length();
                if (distFromRef < minDistance) {
                    minDistance = distFromRef;
                    closestPos = step;
                }
            }
        }

        return closestPos;
    }


    /**
     * How many steps there are in the path.
     * @return
     */
    public int numSteps() {
        return steps.size();
    }




}
