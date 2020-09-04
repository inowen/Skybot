package inowen.gui.screens.configTabs;

import inowen.config.SkybotConfig;
import inowen.gui.screens.MainConfigScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;


public class CropBotConfigScreen extends MainConfigScreen {

    private TextFieldWidget newTargetItem = null;

    /**
     * Constructor.
     * @param titleIn Title for the window (can be accessed through this.title)
     */
    public CropBotConfigScreen(ITextComponent titleIn) {
        super(titleIn);
    }

    @Override
    public void init() {
        super.init();

        // Add the input field for target item:
        addTargetInputField("Target Item: " + SkybotConfig.OldCropBot.FARMED_ITEM.value);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        super.render(mouseX, mouseY, partialTicks);

        // Show the target item
        mc.fontRenderer.drawString("Target Item: " + SkybotConfig.OldCropBot.FARMED_ITEM.value, tabExtensionMinX+3, tabExtensionMinY+3, 0xffffff);
    }

    @Override
    public void tick() {
        super.tick();
    }


    /**
     * Add the input text box that reads in a new target item.
     * @param targetItemText The text that shows the target item (just need it to know how wide it is).
     */
    private void addTargetInputField(String targetItemText) {
        int xIn = this.tabExtensionMinX + mc.fontRenderer.getStringWidth(targetItemText) + 15;
        int yIn = this.tabExtensionMinY + 3;

        int textBoxWidth = 100;
        newTargetItem = new TextFieldWidget(mc.fontRenderer, xIn, yIn, textBoxWidth, 20, "");
        this.addButton(newTargetItem);
        this.addButton(new Button(xIn+textBoxWidth + 5, yIn, 80, 20, "Switch target", button -> {
            if (newTargetItem.getText().length()>0) {
                SkybotConfig.OldCropBot.FARMED_ITEM.setValue(newTargetItem.getText());
            }
        }));
    }



}







