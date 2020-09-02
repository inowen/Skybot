package inowen.testing.mods;

import inowen.moduleSystem.Module;
import inowen.pathfind.Graph;
import inowen.pathfind.Node;
import inowen.utils.ForgeKeys;

public class TestPathfindGraph extends Module {

    public Graph theGraph = null;
    public int numNeighbors = 0;

    public TestPathfindGraph() {
        super("TestPathfindGraph", ForgeKeys.KEY_NONE);
    }

    @Override
    public void onEnable() {
        theGraph = new Graph(mc.player.getPosition(), 50, mc.world);
        // Test adjacency
        theGraph.calcNeighbours(mc.world);
        //... count total number of neighbours.
        numNeighbors = 0;
        for (Node node : theGraph.nodes) {
            numNeighbors += node.neighbours.size();
        }
    }

    @Override
    public void onUpdate() {
        // theGraph = new Graph(mc.player.getPosition(), 3, mc.world);
    }

    @Override
    public void onGui() {
        if (theGraph != null) {
            mc.fontRenderer.drawString("Nodes in the graph: " + theGraph.nodes.size(), 50, 50, 0xffffff);
            mc.fontRenderer.drawString("Average amount of neighbors: " + (double)numNeighbors/theGraph.nodes.size(), 50, 60, 0xffffff);
            mc.fontRenderer.drawString("Edges in the graph: " + (double)numNeighbors/2, 50, 70, 0xffffff);
        }
    }

    @Override
    public void onDisable() {
        theGraph = null;
    }
}
