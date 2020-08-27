package inowen.testing.mods;

import inowen.moduleSystem.Module;
import inowen.pathfind.Graph;
import inowen.utils.ForgeKeys;

public class TestPathfindGraph extends Module {

    public Graph theGraph = null;

    public TestPathfindGraph() {
        super("TestPathfindGraph", ForgeKeys.KEY_G);
    }

    @Override
    public void onEnable() {
        theGraph = new Graph(mc.player.getPosition(), 3, mc.world);
    }

    @Override
    public void onUpdate() {
        theGraph = new Graph(mc.player.getPosition(), 3, mc.world);
    }

    @Override
    public void onGui() {
        if (theGraph != null) {
            mc.fontRenderer.drawString("Nodes in the graph: " + theGraph.nodes.size(), 50, 50, 0xffffff);
        }
    }

    @Override
    public void onDisable() {
        theGraph = null;
    }
}
