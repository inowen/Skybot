package inowen.gui.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import inowen.SkyBotMod;
import inowen.config.SkybotConfig;
import inowen.gui.screens.configTabs.*;
import inowen.moduleSystem.Module;
import inowen.moduleSystem.ModuleManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.CheckboxButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;


/**
 * The main settings screen. Checkboxes to enable/disable modules, and "tabs" to choose the right settings screen
 * to put in the center of the main screen.
 * To create the different kinds of tabs in the middle while still being able to use the exit button and the
 * checkboxes, each tab is actually its own gui that inherits from the MainConfigScreen and adds something.
 */
public class MainConfigScreen extends Screen {

    protected Minecraft mc = Minecraft.getInstance();

    // Coordinates of the different tab extensions
    protected int tabExtensionMinX;
    protected int tabExtensionMaxX;
    protected int tabExtensionMinY;
    protected int tabExtensionMaxY;

    // How wide the enclosure for the tab extensions will be
    public static final int ENCLOSURE_WIDTH = 6;


    // Widgets: Checkbox buttons (need to be class-wide to access .isChecked())
    private CheckboxButton hideNamesCheckBox = null;
    private CheckboxButton whitelistCheckbox = null;
    private CheckboxButton fullBrightCheckbox = null;


    /**
     * Constructor.
     * @param titleIn Title for the window (can be accessed through this.title)
     */
    public MainConfigScreen(ITextComponent titleIn) { super(titleIn); }


    @Override
    public void init() {
        super.init();

        // Create and add the checkboxButtons to toggle the modules (upper left side).
        addModCheckboxes((int)(0.025*this.width), (int)(0.1*this.height));

        // Create and add the buttons to open the different options tabs.
        addTabs();

        // Calculate the position of the enclosure for tab extensions.
        calculateTabExtEnclosureLimits();

        // Create and add save button
        this.addButton(new Button((int)(0.75*this.width)-100, (int)(0.9*this.height), 98, 20, "Save", button -> SkybotConfig.writeConfigFile()));

        // Create and add the exit button
        this.addButton(new Button((int)(0.75*this.width), (int)(0.9*this.height), 100, 20, "Exit", button -> {mc.displayGuiScreen(null);}));
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        renderBackground();
        renderTabExtensionEnclosure();
        super.render(mouseX, mouseY, partialTicks);

        // Draw the title centered above the menu
        this.drawCenteredString(mc.fontRenderer, this.title.getFormattedText(), (int)(0.5*this.width), (int)(0.05*this.height), 0xffffff);

    }


    @Override
    public void tick() {
        super.tick();
        tickCheckboxes();
    }






    /**
     * Create and add to the gui all the checkboxes related to enabling or disabling modules:
     * Things like WhiteList and HideNames, and whatever might be added later.
     * @param x The X coordinate of the mod checkboxes (they are a column, all have the same x)
     * @param topY The Y coordinate of the highest checkbox. The rest are directly below.
     */
    public void addModCheckboxes(int x, int topY) {
        int numModCheckBoxes = 0;

        // Checkbox for HideNames module
        hideNamesCheckBox = new CheckboxButton(x, topY + numModCheckBoxes*20, 20, 20, "HideNames", ModuleManager.getModule("HideNames").isToggled());
        this.addButton(hideNamesCheckBox);
        numModCheckBoxes++;

        // Checkbox for Whitelist module
        whitelistCheckbox = new CheckboxButton(x, topY + numModCheckBoxes*20, 20,20, "WhiteList", ModuleManager.getModule("WhiteList").isToggled());
        this.addButton(whitelistCheckbox);
        numModCheckBoxes++;

        // Add an extra space in between (different categories)
        numModCheckBoxes++;

        // Checkbox for FullBright module
        fullBrightCheckbox = new CheckboxButton(x, topY + numModCheckBoxes*20, 20, 20, "FullBright", ModuleManager.getModule("FullBright").isToggled());
        this.addButton(fullBrightCheckbox);
        numModCheckBoxes++;


    }


    /**
     * Go through all the checkboxes, if they are ticked then enable module, if not disable modules.
     */
    public void tickCheckboxes() {

        Module hideNames = ModuleManager.getModule("HideNames");
        hideNames.toggled = hideNamesCheckBox.isChecked();

        Module fullBright = ModuleManager.getModule("FullBright");
        if (fullBrightCheckbox.isChecked() && !fullBright.isToggled()) {
            fullBright.toggle();
        }
        else if (!fullBrightCheckbox.isChecked() && fullBright.isToggled()) {
            fullBright.toggle();
        }

        Module whitelist = ModuleManager.getModule("WhiteList");
        if (whitelistCheckbox.isChecked()) {
            // Enable module if it isn't already
            if (!whitelist.isToggled()) {
                whitelist.onEnable();
                whitelist.toggled = true;
            }
        }
        else {
            // if module enabled, disable it
            if (whitelist.isToggled()) {
                whitelist.onDisable();
                whitelist.toggled = false;
            }
        }
    }


    /**
     * Create buttons for the menu. Add them to the list of widgets
     * for this menu.
     */
    public void addTabs() {
        int tabsButtonsHeight = (int)(0.09*this.height);
        int tabsButtonsXOffset = (int)(0.175*this.width);
        int buttonWidth = 82;

        // Add the tabs.
        this.addButton(new Button(tabsButtonsXOffset, tabsButtonsHeight, buttonWidth, 20, "CropBot", button -> {
            mc.displayGuiScreen(new CropBotConfigScreen(new StringTextComponent("CropBot Settings")));
        }));
        this.addButton(new Button(tabsButtonsXOffset+buttonWidth*1, tabsButtonsHeight, buttonWidth, 20, "SugarcaneBot", button -> {
            mc.displayGuiScreen((new SugarcaneBotConfigScreen(new StringTextComponent("SugarcaneBot Settings"))));
        }));
        this.addButton(new Button(tabsButtonsXOffset+buttonWidth*2, tabsButtonsHeight, buttonWidth, 20, "MelonPumpkinBot", button -> {
            mc.displayGuiScreen(new MelonPumpkinBotConfigScreen(new StringTextComponent("MelonPumpkinBot Settings")));
        }));
        this.addButton(new Button(tabsButtonsXOffset+buttonWidth*3, tabsButtonsHeight, buttonWidth, 20, "SeedsCropBot", button -> {
            mc.displayGuiScreen(new SeedsCropBotConfigScreen(new StringTextComponent("SeedsCropBot Settings")));
        }));
        this.addButton(new Button(tabsButtonsXOffset + buttonWidth*4, tabsButtonsHeight, buttonWidth, 20, "NetherwartsBot", button -> {
            mc.displayGuiScreen(new NetherwartBotConfigScreen(new StringTextComponent("NetherwartsBot Settings")));
        }));
        this.addButton(new Button(tabsButtonsXOffset + buttonWidth*5, tabsButtonsHeight, buttonWidth, 20, "ChorusBot", button -> {
            mc.displayGuiScreen(new ChorusBotConfigScreen(new StringTextComponent("ChorusBot Settings")));
        }));
    }


    /**
     * Render the box within which the options for each tab open up
     * when pressing the button to open their page.
     */
    public void renderTabExtensionEnclosure() {
        // Draw background
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        getMinecraft().getTextureManager().bindTexture(new ResourceLocation(SkyBotMod.MOD_ID, "tab_extension_background.png"));

        int x = this.tabExtensionMinX;
        int y = this.tabExtensionMinY;
        int offsetX = 0;
        int offsetY = 0;
        int sizeX = tabExtensionMaxX - tabExtensionMinX;
        int sizeY = tabExtensionMaxY - tabExtensionMinY;
        this.blit(x, y, offsetX, offsetY, sizeX, sizeY);

        // Draw the borders (rectangle)
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        getMinecraft().getTextureManager().bindTexture(new ResourceLocation(SkyBotMod.MOD_ID, "tab_extension_border.jpg"));

        //... left pillar
        this.blit(tabExtensionMinX-ENCLOSURE_WIDTH, tabExtensionMinY-ENCLOSURE_WIDTH, 0, 0, ENCLOSURE_WIDTH, tabExtensionMaxY-tabExtensionMinY + 2*ENCLOSURE_WIDTH);

        //... right pillar
        this.blit(tabExtensionMaxX, tabExtensionMinY-ENCLOSURE_WIDTH, 0, 0, ENCLOSURE_WIDTH, tabExtensionMaxY-tabExtensionMinY+ENCLOSURE_WIDTH);

        //... ceiling
        this.blit(tabExtensionMinX-ENCLOSURE_WIDTH, tabExtensionMinY-ENCLOSURE_WIDTH, 0, 0, tabExtensionMaxX-tabExtensionMinX+ENCLOSURE_WIDTH, ENCLOSURE_WIDTH);

        //... floor
        this.blit(tabExtensionMinX, tabExtensionMaxY, 0, 0, tabExtensionMaxX-tabExtensionMinX + ENCLOSURE_WIDTH, ENCLOSURE_WIDTH);

    }


    /**
     * Find the coordinates where the tab extension enclosure should be rendered.
     */
    public void calculateTabExtEnclosureLimits() {
        tabExtensionMinX = (int)(0.175*this.width) + this.ENCLOSURE_WIDTH;
        tabExtensionMaxX = (int)(0.95*this.width) - this.ENCLOSURE_WIDTH;
        tabExtensionMinY = (int)(0.0975*this.height) + this.ENCLOSURE_WIDTH + 20;
        tabExtensionMaxY = (int)(0.85*this.height) - this.ENCLOSURE_WIDTH;
    }

}
