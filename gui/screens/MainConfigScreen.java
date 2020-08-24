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

    // Widgets
    private CheckboxButton hideNamesCheckBox = null;
    private CheckboxButton whitelistCheckbox = null;

    public MainConfigScreen(ITextComponent titleIn) {
        super(titleIn);
    }

    @Override
    public void init() {
        super.init();
        this.addButton(new Button((int)(0.75*this.width), (int)(0.9*this.height), 100, 20, "Exit", button -> {mc.displayGuiScreen(null);}));
        // Checkbox for HideNames module
        hideNamesCheckBox = new CheckboxButton((int)(0.1*this.width), (int)(0.1*this.height), 20, 20, "Hide Names", ModuleManager.getModule("HideNames").isToggled());
        this.addButton(hideNamesCheckBox);
        // Checkbox for Whitelist module
        whitelistCheckbox = new CheckboxButton((int)(0.1*this.width), (int)(0.1*this.height)+20, 20,20, "WhiteList", ModuleManager.getModule("WhiteList").isToggled());
        this.addButton(whitelistCheckbox);
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
