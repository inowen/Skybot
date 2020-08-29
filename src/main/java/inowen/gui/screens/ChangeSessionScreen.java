package inowen.gui.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.OptionSlider;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;

public class ChangeSessionScreen extends Screen {

    private static final Minecraft mc = Minecraft.getInstance();

    // The TextField from which the desired username is read.
    TextFieldWidget desiredUsername = null;

    /**
     * Constructor for the screen.
     * @param titleIn Screen´s title.
     */
    public ChangeSessionScreen(ITextComponent titleIn) {
        super(titleIn);
    }

    @Override
    public void init() {
        super.init();

        // Add the exit button at the lower right side of the screen.
        this.addButton(new Button((int)(0.75*this.width), (int)(0.875*this.height), 100, 20, "Exit", button -> {
            mc.displayGuiScreen(null);
        }));

        // Create the text field to input the new username and add it to the widget list.
        desiredUsername = new TextFieldWidget(mc.fontRenderer, this.width/2-75, (int)(0.2*this.height)+25, 150, 20, "");
        this.addButton(desiredUsername);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        renderDirtBackground(0);
        super.render(mouseX, mouseY, partialTicks);

        // Draw title at the top of the screen.
        this.drawCenteredString(mc.fontRenderer, this.title.getString(), this.width/2, (int)(0.05*this.height), 0xffffff);

        // Draw instructions of what to do on this screen
        String msg = "Input desired username (current: " + mc.getSession().getUsername() + ")";
        this.drawCenteredString(mc.fontRenderer, msg, this.width/2, (int)(0.2*this.height), 0xffffff);

    }

    @Override
    public void tick() {
        super.tick();

    }
}
