package inowen.gui.screens.configTabs;

import inowen.config.SkybotConfig;
import inowen.gui.screens.MainConfigScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;

import java.awt.*;


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

        // Add the input field for target item, and the button to accept the input
        addTargetInputField("Target Item: " + SkybotConfig.OldCropBot.FARMED_ITEM.value);

        // Add input fields for planting and breaking range
        addRangeInputFields();
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


    /**
     * Add the input fields for how far the bot should reach to plant things and to break things.
     */
    private void addRangeInputFields() {
        int xIn = this.tabExtensionMinX + 3; // Where the rangeInputFields start.
        int yIn = this.tabExtensionMinY + 30;
        int inputFieldWidth = 100; // How wide the input fields are.
        int spaceBetweenInputAndButton = 8; // Button to accept input is next to the input widget.
        int inputFieldsHeight = 20; // Height of each one of the input fields.
        int spaceBetweenInputFields = 5; // How much vertical space there is between the fields.

        // Input field for breaking range
        double breakingRangeNow = SkybotConfig.OldCropBot.BREAK_REACH.value;
        TextFieldWidget breakingRangeInput = new TextFieldWidget(mc.fontRenderer, xIn, yIn, inputFieldWidth, inputFieldsHeight, ""+breakingRangeNow);
        breakingRangeInput.setText(String.valueOf(breakingRangeNow));
        this.addButton(breakingRangeInput);

        // Button to process value in input field.
        this.addButton(new Button(xIn + inputFieldWidth + spaceBetweenInputAndButton, yIn, 120, inputFieldsHeight, "Accept Break Range", button -> {
            try {
                if (breakingRangeInput.getText().length() > 0) {
                    SkybotConfig.OldCropBot.BREAK_REACH.setValue(breakingRangeInput.getText());
                }
            } catch (Exception ignored) { }
        }));

        // Input field for planting range
        double plantingRangeNow = SkybotConfig.OldCropBot.PLANT_REACH.value;
        TextFieldWidget plantingRangeInput = new TextFieldWidget(mc.fontRenderer, xIn, yIn + inputFieldsHeight+spaceBetweenInputFields, inputFieldWidth, inputFieldsHeight, ""+plantingRangeNow);
        plantingRangeInput.setText(String.valueOf(plantingRangeNow));
        this.addButton(plantingRangeInput);

        // Button to process value in planting input field
        this.addButton(new Button(xIn + inputFieldWidth + spaceBetweenInputAndButton, yIn + inputFieldsHeight+spaceBetweenInputFields, 120, inputFieldsHeight, "Accept plant range", button -> {
            if (plantingRangeInput.getText().length() > 0) {
                try {
                    SkybotConfig.OldCropBot.PLANT_REACH.setValue(plantingRangeInput.getText());
                } catch (Exception ignore) { }
            }
        }));


    }



}







