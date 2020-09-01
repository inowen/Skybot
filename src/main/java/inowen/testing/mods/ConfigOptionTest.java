package inowen.testing.mods;

import inowen.config.ConfigOption;
import inowen.config.SkybotConfig;
import inowen.moduleSystem.Module;
import inowen.utils.ForgeKeys;
import sun.security.krb5.Config;

public class ConfigOptionTest extends Module {


    public ConfigOptionTest() {
        super(ConfigOptionTest.class.getCanonicalName(), ForgeKeys.KEY_T);
    }

     @Override
    public void onEnable() {
         System.out.println("---------------------------------------");
         System.out.println("---------------------------------------");
        System.out.println("Config fields in SkybotConfig:");
        for (Object opt : SkybotConfig.getConfigOptions()) {
            System.out.println("\t" + ((ConfigOption<?>)opt).name + " ::: " + ((ConfigOption<?>)opt).value);
        }

        // The sub-classes
         System.out.println("SubClasses: ");
         Class[] classes = SkybotConfig.class.getClasses();
        for (Class cls : classes) {
            System.out.println("\t\t\t" + cls.getName());
        }

        System.out.println("---------------------------------------");
        System.out.println("---------------------------------------");
     }
}
