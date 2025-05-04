package com.example.movement.handlers;

import java.io.IOException;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import com.example.movement.MovementMod;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;

public class GUIHandler {
    // 配置界面
    public static class MovementConfigGUI extends GuiConfig {
        public MovementConfigGUI(GuiScreen parent) {
            super(parent,
                new ConfigElement(MovementMod.config.getCategory("General")).getChildElements(),
                "movement",
                false,
                false,
                "Movement Mod Configuration");
        }
    }

    // 实时控制界面
    public static class MovementControlGUI extends GuiScreen {
        private int speedButtonX1 = 140, speedButtonY1 = 80, speedButtonX2 = 160, speedButtonY2 = 90;
        protected void func_73864_a(int mouseX, int mouseY, int mouseButton) {
            try {
                super.func_73864_a(mouseX, mouseY, mouseButton); // 添加异常处理
                MovementMod.speedEnabled = !MovementMod.speedEnabled;
                // 强制立即保存配置
                MovementMod.config.get("General", "Speed", false).set(MovementMod.speedEnabled);
                MovementMod.config.save();
                Minecraft.func_71410_x().func_147118_V().func_147682_a(
                    PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F)
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
            // super.mouseClicked(mouseX, mouseY, mouseButton);
        }
        public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
            func_146276_q_();
            func_73734_a(speedButtonX1, speedButtonY1, speedButtonX2, speedButtonY2, 
                    MovementMod.speedEnabled ? 0xFF00FF00 : 0xFFFF0000);
            // 添加文字居中显示
            String status = MovementMod.speedEnabled ? "ON" : "OFF";
            int textWidth = field_146289_q.func_78256_a(status);
            field_146289_q.func_78276_b(status, 
                speedButtonX1 + (speedButtonX2 - speedButtonX1 - textWidth)/2, 
                speedButtonY1 + 3, 
                0xFFFFFF);
            // 绘制功能开关
            drawButton(50, 50, "Speed: " + MovementMod.speedEnabled);
            drawButton(50, 70, "LongJump: " + MovementMod.longJumpEnabled);
            drawButton(50, 90, "Fly: " + MovementMod.flyEnabled);
            drawButton(50, 110, "AirJump: " + MovementMod.airJumpEnabled);
            
            // 参数滑动条
            drawSlider(150, 50, "Speed Multiplier", MovementMod.speedMultiplier, 1.0f, 2.0f);
            drawSlider(150, 70, "Jump Boost", (float) MovementMod.jumpBoost, 1.0f, 3.0f);
            
            super.func_73863_a(mouseX, mouseY, partialTicks);
        }

        private void drawButton(int x, int y, String text) {
            int width = 100;
            int height = 20;
            func_73734_a(x, y, x + width, y + height, 0x80000000);
            field_146289_q.func_78276_b(text, x + 5, y + 6, 0xFFFFFF);
        }

        private void drawSlider(int x, int y, String label, float value, float min, float max) {
            // 滑动条实现逻辑（需处理鼠标拖动事件）
        }
    }
}
