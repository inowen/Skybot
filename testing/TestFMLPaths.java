package inowen.testing;


import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod.EventBusSubscriber
public class TestFMLPaths {

    @SubscribeEvent
    public static void printOutPath(TickEvent.ClientTickEvent event) {
        System.out.println("Path to Game directory: " + FMLPaths.GAMEDIR);
    }
}
