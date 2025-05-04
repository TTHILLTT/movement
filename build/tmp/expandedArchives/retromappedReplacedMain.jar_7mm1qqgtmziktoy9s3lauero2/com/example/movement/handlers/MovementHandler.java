package com.example.movement.handlers;

import java.lang.reflect.Field;
import net.minecraft.entity.EntityLivingBase;
import com.example.movement.MovementMod;
import com.example.movement.utils.AntiCheatHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class MovementHandler {
    private int airJumpCounter = 0;
    private long lastJumpTime = 0;
    private static Field jumpTicksField;
    static {
        try {
            // 使用 MCP 9.19 的映射名 "jumpTicks"
            jumpTicksField = EntityLivingBase.class.getDeclaredField("jumpTicks");
            jumpTicksField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            // 详细错误日志输出
            System.err.println("[MovementMod] 反射初始化失败：无法找到 jumpTicks 字段");
            e.printStackTrace();
            jumpTicksField = null; // 显式标记为 null 防止后续误用
        }
    }
    public void handleAirJump(EntityPlayer player) {
        if (MovementMod.airJumpEnabled && !player.field_70122_E) {
            player.field_70181_x = 0.42 * MovementMod.jumpBoost;
        }
    }

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent event) {
        EntityPlayer player = event.player;
        if (event.player == null || !(event.player instanceof EntityPlayer)) return;
        player = (EntityPlayer) event.player;
        if (jumpTicksField != null) {
            try {
                jumpTicksField.setInt(player, 0); // 重置跳跃冷却
            } catch (IllegalAccessException e) {
                System.err.println("[MovementMod] 反射赋值失败");
                e.printStackTrace();
            }
        }
        // Speed功能（带反作弊随机化）
        if (MovementMod.speedEnabled && player.field_70122_E) {
            double randFactor = AntiCheatHelper.getRandomizedFactor();
            player.field_70159_w *= MovementMod.speedMultiplier * randFactor;
            player.field_70179_y *= MovementMod.speedMultiplier * randFactor;
        }

        // Fly功能（模拟自然波动）
        if (MovementMod.flyEnabled) {
            player.field_71075_bZ.field_75101_c = true;
            player.field_71075_bZ.field_75100_b = true;
            player.field_70143_R = 0;
            
            // 添加随机垂直波动
            if (player.field_70173_aa % 5 == 0) {
                player.field_70181_x += AntiCheatHelper.getVerticalNoise();
            }
        }else {
            // 重要：禁用飞行时恢复原版逻辑
            if (!player.field_71075_bZ.field_75098_d) {
                player.field_71075_bZ.field_75101_c = false;
                player.field_71075_bZ.field_75100_b = false;
            }
        }
        if (event.player.field_70170_p.field_72995_K) {
            System.out.println("Client Position: " + event.player.field_70165_t + ", " + event.player.field_70163_u + ", " + event.player.field_70161_v);
        }
        if (jumpTicksField != null) {
            try {
                jumpTicksField.setInt(player, 0);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @SubscribeEvent
    public void onJump(LivingJumpEvent event) {
        if (event.entity instanceof EntityPlayer && MovementMod.longJumpEnabled) {
            event.entity.field_70181_x *= MovementMod.jumpBoost;
        }
    }
}