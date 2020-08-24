package inowen.gui.screens;

import inowen.config.SkybotConfig;
import inowen.moduleSystem.Module;
import inowen.moduleSystem.ModuleManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.CheckboxButton;
import net.minecraft.util.text.ITextComponent;

public class MainConfigScreen extends Screen {

    protected Minecraft mc = Minecraft.getInstance();

    // Widgets: Checkbox buttons
    private CheckboxButton hideNamesCheckBox = null;
    private CheckboxButton whitelistCheckbox = null;
    public int topModCheckboxY;
    public int modCheckboxX;

    // Widgets: Tabs buttons (settings for one or the other bot).
    // These lead to another identical gui screen except that there are settings for the chosen bot in the center.
    // TODO: Create configSectionTab class or something to model these tab guis.



    public MainConfigScreen(ITextComponent titleIn) {
        super(titleIn);
    }

    @Override
    public void init() {
        super.init();

        // Determine positions of gui widget groups
        modCheckboxX = (int)(0.025*this.width);
        topModCheckboxY = (int)(0.1*this.height);

        // Create and add the exit button
        this.addButton(new Button((int)(0.75*this.width), (int)(0.9*this.height), 100, 20, "Exit", button -> {mc.displayGuiScreen(null);}));


        int numModCheckBoxes = 0;
        // Checkbox for HideNames module
        hideNamesCheckBox = new CheckboxButton(modCheckboxX, topModCheckboxY + numModCheckBoxes*20, 20, 20, "Hide Names", ModuleManager.getModule("HideNames").isToggled());
        this.addButton(hideNamesCheckBox);
        numModCheckBoxes++;
        // Checkbox for Whitelist module
        whitelistCheckbox = new CheckboxButton(modCheckboxX, topModCheckboxY + numModCheckBoxes*20, 20,20, "WhiteList", ModuleManager.getModule("WhiteList").isToggled());
        this.addButton(whitelistCheckbox);
        numModCheckBoxes++;
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


}
