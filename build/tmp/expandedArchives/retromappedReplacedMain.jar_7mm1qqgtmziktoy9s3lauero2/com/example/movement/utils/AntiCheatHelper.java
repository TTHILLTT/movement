package com.example.movement.utils;

import java.util.Random;
import net.minecraft.client.Minecraft;

public class AntiCheatHelper {
    private static final Random random = new Random();
    
    // 生成随机化系数（±5%波动）
    public static float getRandomizedFactor() {
        return 0.95f + random.nextFloat() * 0.1f;
    }
    
    // 生成垂直随机噪声（±0.01-0.03）
    public static double getVerticalNoise() {
        return (random.nextDouble() - 0.5) * 0.04;
    }
    
    // 位置随机偏移
    public static double[] getPositionOffset() {
        return new double[] {
            (random.nextDouble() - 0.5) * 0.05,
            0,
            (random.nextDouble() - 0.5) * 0.05
        };
    }
}