package inowen.gui.screens;

import inowen.config.SkybotConfig;
import inowen.moduleSystem.Module;
import inowen.moduleSystem.ModuleManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.CheckboxButton;
import net.minecraft.util.text.ITextComponent;


/**
 * The main settings screen. Checkboxes to enable/disable modules, and "tabs" to choose the right settings screen
 * to put in the center of the main screen.
 * To create the different kinds of tabs in the middle while still being able to use the exit button and the
 * checkboxes, each tab is actually its own gui that inherits from the MainConfigScreen and adds something.
 */
public class MainConfigScreen extends Screen {

    protected Minecraft mc = Minecraft.getInstance();

    // Widgets: Checkbox buttons (need to be class-wide to access .isChecked())
    private CheckboxButton hideNamesCheckBox = null;
    private CheckboxButton whitelistCheckbox = null;
    private CheckboxButton fullBrightCheckbox = null;


    // Widgets: Tabs buttons (settings for one or the other bot).
    // These lead to another identical gui screen except that there are settings for the chosen bot in the center.
    // TODO: Create configSectionTab class or something to model these tab guis.



    public MainConfigScreen(ITextComponent titleIn) {
        super(titleIn);
    }

    @Override
    public void init() {
        super.init();

        // Create and add the checkboxButtons to toggle the modules.
        int modCheckboxX = (int)(0.025*this.width);
        int topModCheckboxY = (int)(0.1*this.height);
        createAndAddModCheckboxes(modCheckboxX, topModCheckboxY);

        // Create and add the buttons to open the different options tabs.
        int tabsButtonsHeight = (int)(0.09*this.height);
        int tabsButtonsXOffset = (int)(0.175*this.width);
        int buttonWidth = 82;
        this.addButton(new Button(tabsButtonsXOffset, tabsButtonsHeight, buttonWidth, 20, "CropBot", button -> {
            System.out.println("TestButtonPressed");
        }));
        this.addButton(new Button(tabsButtonsXOffset+buttonWidth*1, tabsButtonsHeight, buttonWidth, 20, "SugarcaneBot", button -> {
            System.out.println("TestButtonPressed");
        }));
        this.addButton(new Button(tabsButtonsXOffset+buttonWidth*2, tabsButtonsHeight, buttonWidth, 20, "MelonPumpkinBot", button -> {
            System.out.println("TestButtonPressed");
        }));
        this.addButton(new Button(tabsButtonsXOffset+buttonWidth*3, tabsButtonsHeight, buttonWidth, 20, "SeedsCropBot", button -> {
            System.out.println("TestButtonPressed");
        }));
        this.addButton(new Button(tabsButtonsXOffset + buttonWidth*4, tabsButtonsHeight, buttonWidth, 20, "NetherwartsBot", button -> {
            System.out.println("TestButtonPressed");
        }));
        this.addButton(new Button(tabsButtonsXOffset + buttonWidth*5, tabsButtonsHeight, buttonWidth, 20, "ChorusBot", button -> {
            System.out.println("TestButtonPressed");
        }));

        // Testing how it would look, surround the area with the settings for each bot with long button-lines.
        this.addButton(new Button(tabsButtonsXOffset, tabsButtonsHeight + 30, 5, 500, "", button -> {}));
        this.addButton(new Button(tabsButtonsXOffset, tabsButtonsHeight + 30, 1000, 5, "", button -> {}));
        this.addButton(new Button(this.width-10, tabsButtonsHeight + 30, 5, 500, "", button -> {}));


        // Create and add the exit button
        this.addButton(new Button((int)(0.75*this.width), (int)(0.9*this.height), 100, 20, "Exit", button -> {mc.displayGuiScreen(null);}));

    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.drawCenteredString(mc.fontRenderer, this.title.getFormattedText(), (int)(0.5*this.width), (int)(0.05*this.height), 0xffffff);
    }


    @Override
    public void tick() {
        super.tick();
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
     * Create and add to the gui all the checkboxes related to enabling or disabling modules:
     * Things like WhiteList and HideNames, and whatever might be added later.
     * @param x The X coordinate of the mod checkboxes (they are a column, all have the same x)
     * @param topY The Y coordinate of the highest checkbox. The rest are directly below.
     */
    public void createAndAddModCheckboxes(int x, int topY) {
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


}
