package inowen.testing.mods;

import inowen.config.ConfigOption;
import inowen.config.SkybotConfig;
import inowen.moduleSystem.Module;
import inowen.utils.ForgeKeys;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.*;

public class ConfigOptionTest extends Module {


    public ConfigOptionTest() {
        super(ConfigOptionTest.class.getCanonicalName(), ForgeKeys.KEY_NONE);
    }

     @Override
    public void onEnable() {
         System.out.println("---------------------------------------");

         File configFolder = FMLPaths.CONFIGDIR.get().toFile();
         File textFile = new File(configFolder, "something.txt");
         File textFile2 = new File(configFolder, "newFile.txt");

         try {
             textFile2.createNewFile();
             textFile2.delete();
         } catch (IOException e) {
             e.printStackTrace();
         }

         try {
             FileReader reader = new FileReader(textFile);
             BufferedReader breader = new BufferedReader(reader);
             while(breader.ready()) {
                 System.out.println(breader.readLine());
             }
         } catch (Exception e) {
             e.printStackTrace();
         }

         System.out.println("---------------------------------------");

        System.out.println("Config fields in SkybotConfig:");
        for (Object opt : SkybotConfig.getConfigOptions()) {
            System.out.println("\t" + ((ConfigOption<?>)opt).name + " :--: " + ((ConfigOption<?>)opt).value);
        }


        System.out.println("---------------------------------------");

        System.out.println("Configs detected, as strings: ");
        for (String s : SkybotConfig.getConfigsAsStrings()) {
            System.out.println("\t\t" + s);
        }

        System.out.println("---------------------------------------");
     }
}
