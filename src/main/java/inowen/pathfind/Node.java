package inowen.pathfind;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class Node {
    public BlockPos globalPosition;
    public BlockState blockState;

    public Node() {
        globalPosition = null;
        blockState = null;
    }

    public Node(BlockPos pos) {
        globalPosition = pos;
    }

    public Node(BlockState bs) {
        blockState = bs;
    }

    public Node(BlockState bs, BlockPos bp) {
        blockState = bs;
        globalPosition = bp;
    }


    public BlockPos getGlobalPosition() {
        return globalPosition;
    }


    public BlockState getBlockState() {
        return blockState;
    }


}
