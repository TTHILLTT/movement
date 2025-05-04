package com.example.movement;

import net.minecraft.client.Minecraft;
import com.example.movement.handlers.GUIHandler;

public class ClientProxy extends CommonProxy {
    @Override
    public void displayGUI() {
        Minecraft.func_71410_x().func_147108_a(
            new GUIHandler.MovementControlGUI()
        );
    }
}