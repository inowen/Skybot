package com.example.examplemod.eventStuff;

import com.example.examplemod.ExampleMod;
import inowen.SkyBotMod;
import inowen.utils.ForgeKeys;
import net.minecraft.client.GameSettings;
import net.minecraft.client.KeyboardListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid= SkyBotMod.MOD_ID, bus=Mod.EventBusSubscriber.Bus.FORGE, value= Dist.CLIENT)
public class ClientEvent {

    public static Minecraft mc = Minecraft.getInstance();
    public static boolean switchIsToggled = false;
    public static long lastPressed = 0;

    public static final long MIN_DELAY = 600;


    // Something that might be able to figure out whether a key was just pressed down.
    // @SubscribeEvent
        //public static void toggleTheSwitch(InputEvent.KeyInputEvent event) {

        /*
        if (mc.player != null && event.getKey() == ForgeKeys.KEY_J) {
            if (Math.abs(System.currentTimeMillis()-lastPressed) > MIN_DELAY) {
                switchIsToggled = !switchIsToggled;
                lastPressed = System.currentTimeMillis();
                System.out.println("Just toggled the switch!");
            }
        }
    }

    // Put on the screen whether the thing is toggled.
    @SubscribeEvent
    public static void informPlayer(TickEvent.ClientTickEvent event) {
        if (mc.player != null) {

            // Okay, this doesn't draw anything...
            // mc.fontRenderer.drawString("Is currently on? " + (switchIsToggled ? "Yes" : "No"), 1, 1, 0xffffff);
            // System.out.println("Drawing something on screen.");
        }

        // Info: ClientTickEvent happens just the same if not ingame. Can be used for a state machine!


    }

    @SubscribeEvent
    public static void drawOnScreen(RenderGameOverlayEvent event) {
        // mc.fontRenderer.drawString("Is currently on? " + (switchIsToggled ? "Yes" : "No"), 1, 1, 0xffffff);

    }

    // onUpdate is ClientTickEvent
    // OnGui is RenderGameOverlayEvent

    // onEnable and onDisable are called by ModuleManager

    // The keypresses can somehow be handled with InputEvent.KeyInputEvent
    // later find improvements. This is just provisional for the CropFarmingBot (get tons of carrots, make good money,
    // and be rich for when the good bots come up and absolutely destroy the economy)

         */
}
