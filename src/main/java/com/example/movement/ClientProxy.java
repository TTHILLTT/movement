package com.example.movement;

import net.minecraft.client.Minecraft;
import com.example.movement.handlers.GUIHandler;

public class ClientProxy extends CommonProxy {
    @Override
    public void displayGUI() {
        Minecraft.getMinecraft().displayGuiScreen(
            new GUIHandler.MovementControlGUI()
        );
    }
}