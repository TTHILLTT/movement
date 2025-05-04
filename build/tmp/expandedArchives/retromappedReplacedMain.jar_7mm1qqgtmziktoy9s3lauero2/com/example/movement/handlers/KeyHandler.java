package com.example.movement.handlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraft.client.Minecraft;
import com.example.movement.handlers.MovementHandler;
import com.example.movement.MovementMod;
import com.example.movement.utils.AntiCheatHelper;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

public class KeyHandler {

    private final MovementHandler movementHandler = new MovementHandler();
    // 按键绑定定义
    public static KeyBinding openGUI = new KeyBinding("Open Settings", Keyboard.KEY_RSHIFT, "Movement Mod");
    public static KeyBinding airJumpKey = new KeyBinding("Air Jump", Keyboard.KEY_SPACE, "Movement Mod");
    
    private static final int JUMP_COOLDOWN = 10; // 反作弊冷却时间（ticks）
    private int jumpTimer = 0;
    
    public KeyHandler() {
        // 注册按键
        ClientRegistry.registerKeyBinding(openGUI);
        ClientRegistry.registerKeyBinding(airJumpKey);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        // 获取玩家实例
        EntityPlayer player = Minecraft.func_71410_x().field_71439_g;
    if (player != null) {
        movementHandler.handleAirJump(player); // 传递player参数
    }
        
        // 打开GUI界面
        if (openGUI.func_151468_f()) {
            MovementMod.proxy.displayGUI();
        }
        if (Keyboard.isKeyDown(airJumpKey.func_151463_i())) {
            player = Minecraft.func_71410_x().field_71439_g;
            movementHandler.handleAirJump(player); 
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        if (MovementMod.flyEnabled && Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            Minecraft.func_71410_x().field_71439_g.field_70181_x = 0.5; // 自定义飞行上升速度
        }
        
        EntityPlayer player = Minecraft.func_71410_x().field_71439_g;
        if (player != null) {
            movementHandler.handleAirJump(player); // 传递player参数
        }
        // 空中跳跃逻辑（带反作弊保护）
        if (MovementMod.airJumpEnabled && airJumpKey.func_151470_d()) {
            if (jumpTimer <= 0 && !event.player.field_70122_E) {
                // 模拟合法跳跃数据
                event.player.field_70181_x = 0.42 * MovementMod.jumpBoost * AntiCheatHelper.getRandomizedFactor();
                
                // 添加位置随机偏移
                double[] offset = AntiCheatHelper.getPositionOffset();
                event.player.func_70107_b(
                    event.player.field_70165_t + offset[0],
                    event.player.field_70163_u + offset[1],
                    event.player.field_70161_v + offset[2]
                );
                
                jumpTimer = JUMP_COOLDOWN; // 设置冷却
            }
        } else {
            if (jumpTimer > 0) jumpTimer--;
        }
    }
}