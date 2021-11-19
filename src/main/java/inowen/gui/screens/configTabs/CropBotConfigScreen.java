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

        // Add the input field for target item, and the button to accept the input
        addTargetInputField("Target Item: " + SkybotConfig.OldCropBot.FARMED_ITEM.value);

        // Add input fields for planting and breaking range
        addRangeInputFields();

        // Add input field and accept button for barrier block
        // (the String is only given to it to know how much space to leave to its left).
        addBarrierBlockInput("Barrier Block: " + SkybotConfig.OldCropBot.BARRIER_BLOCK.value);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        super.render(mouseX, mouseY, partialTicks);

        // Show the target item
        mc.fontRenderer.drawString("Target Item: " + SkybotConfig.OldCropBot.FARMED_ITEM.value, tabExtensionMinX+3, tabExtensionMinY+3, 0xffffff);

        // Render separation below which the BARRIER_BLOCK is chosen
        String msg = "------------------ Farm Enclosure type ---------------";
        this.drawString(mc.fontRenderer, msg, tabExtensionMinX, tabExtensionMinY+100, 0x1111cc);

        // Show current BARRIER_BLOCK
        String msg2 = "Barrier Block: " + SkybotConfig.OldCropBot.BARRIER_BLOCK.value;
        this.drawString(mc.fontRenderer, msg2, tabExtensionMinX+3, tabExtensionMinY+120, 0xffffff);
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
        newTargetItem.setText(SkybotConfig.OldCropBot.FARMED_ITEM.value);
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


    /**
     * Add TextFieldWidget to read the type of block to expect as farm enclosure.
     * @param previousMessage The string to show current barrier type (to know how much space to leave at its left).
     */
    private void addBarrierBlockInput(String previousMessage) {
        int xIn = this.tabExtensionMinX + mc.fontRenderer.getStringWidth(previousMessage) + 10;
        int yIn = this.tabExtensionMinY + 120;
        int textFieldWidth = 100;
        int widgetsHeight = 20;
        int buttonWidth = 80;
        int spaceBtwButtonAndField = 8;

        // The input field
        TextFieldWidget blockInput = new TextFieldWidget(mc.fontRenderer, xIn, yIn, textFieldWidth, widgetsHeight, "");
        blockInput.setText(SkybotConfig.OldCropBot.BARRIER_BLOCK.value);
        this.addButton(blockInput);

        // The accept button
        int btnX = xIn+textFieldWidth+spaceBtwButtonAndField;
        this.addButton(new Button(btnX, yIn, 120, widgetsHeight, "Accept barrier block", button -> {
            if (blockInput.getText().length() > 0) {
                try {
                    SkybotConfig.OldCropBot.BARRIER_BLOCK.setValue(blockInput.getText());
                }
                catch (Exception ignore) { }
            }
        }));
    }

}







