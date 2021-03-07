package inowen.testing;


import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod.EventBusSubscriber
public class TestFMLPaths {

    private static final boolean SHOULD_PRINT = false;

    @SubscribeEvent
    public static void printOutPath(TickEvent.ClientTickEvent event) {
        if (SHOULD_PRINT) {
            System.out.println("Path to Game directory: " + FMLPaths.GAMEDIR.get().toString());
            System.out.println("Path to config folder: " + FMLPaths.CONFIGDIR.get().toString());
        }
    }

}
