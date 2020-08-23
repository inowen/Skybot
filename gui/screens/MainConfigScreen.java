package inowen.gui.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;

public class MainConfigScreen extends Screen {

    protected Minecraft mc = Minecraft.getInstance();

    public MainConfigScreen(ITextComponent titleIn) {
        super(titleIn);
    }

    @Override
    public void init() {
        super.init();
        this.addButton(new Button((int)(0.75*this.width), (int)(0.9*this.height), 100, 20, "Exit", button->{mc.displayGuiScreen(null);}));
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        renderBackground();
        super.render(mouseX, mouseY, partialTicks);
    }


}
