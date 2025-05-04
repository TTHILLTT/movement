package com.example.movement;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.SidedProxy;
import com.example.movement.handlers.MovementHandler;
import com.example.movement.handlers.GUIHandler;
import com.example.movement.handlers.KeyHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = MovementMod.MODID, name = MovementMod.NAME, version = MovementMod.VERSION)
public class MovementMod {
    public static final String MODID = "movement";
    public static final String NAME = "Movement Mod";
    public static final String VERSION = "1.0";
    
    public static Configuration config;
    
    // 功能开关
    public static boolean speedEnabled = false;
    public static boolean longJumpEnabled = false;
    public static boolean flyEnabled = false;
    public static boolean airJumpEnabled = false;
    
    // 参数配置
    public static float speedMultiplier = 1.2f;
    public static double jumpBoost = 1.5;
    public static int maxAirJumps = 1;

    public static void syncConfig() {
        speedEnabled = config.getBoolean("Speed", "General", false, "Enable Speed");
        longJumpEnabled = config.getBoolean("LongJump", "General", false, "Enable Long Jump");
        flyEnabled = config.getBoolean("Fly", "General", false, "Enable Fly");
        airJumpEnabled = config.getBoolean("AirJump", "General", false, "Enable Air Jump");
        
        speedMultiplier = config.getFloat("SpeedMultiplier", "Params", 1.2f, 1.0f, 2.0f, "Speed multiplier");
        jumpBoost = config.getFloat("JumpBoost", "Params", 1.5f, 1.0f, 3.0f, "Jump boost multiplier");
        maxAirJumps = config.getInt("MaxAirJumps", "Params", 1, 0, 5, "Max air jumps allowed");
    }

    // 在每次GUI操作后调用
    public static void onConfigChanged() {
        syncConfig();
        MovementMod.onConfigChanged();
    }
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        config = new Configuration(event.getSuggestedConfigurationFile());
        loadConfig();
    }

    private void loadConfig() {
        speedEnabled = config.getBoolean("Speed", "General", false, "Enable Speed");
        longJumpEnabled = config.getBoolean("LongJump", "General", false, "Enable Long Jump");
        flyEnabled = config.getBoolean("Fly", "General", false, "Enable Fly");
        airJumpEnabled = config.getBoolean("AirJump", "General", false, "Enable Air Jump");
        
        speedMultiplier = config.getFloat("SpeedMultiplier", "Params", 1.2f, 1.0f, 2.0f, "Speed multiplier");
        jumpBoost = config.getFloat("JumpBoost", "Params", 1.5f, 1.0f, 3.0f, "Jump boost multiplier");
        maxAirJumps = config.getInt("MaxAirJumps", "Params", 1, 0, 5, "Max air jumps allowed");
        
        if (config.hasChanged()) MovementMod.onConfigChanged();
    }
    // 添加代理系统
    @SidedProxy(
        clientSide = "com.example.movement.ClientProxy",
        serverSide = "com.example.movement.CommonProxy"
    )
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new MovementHandler());
        MinecraftForge.EVENT_BUS.register(new GUIHandler());
        MinecraftForge.EVENT_BUS.register(new KeyHandler()); 
    }
}