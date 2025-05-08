package mods.grissess.dragonpotions;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import by.dragonsurvivalteam.dragonsurvival.util.DragonLevel;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = DragonPotions.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    
    private static final ForgeConfigSpec.DoubleValue MINIFY_SIZE = BUILDER
    		.comment("Size, as per dragon-set-size, of 'minified' dragons")
    		.defineInRange("minifySize", 35.0, DragonLevel.NEWBORN.size, 1000.0);

    static final ForgeConfigSpec SPEC = BUILDER.build();
    
    public static double minifySize = 35.0;
    
    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
    	minifySize = MINIFY_SIZE.get();
    }
}
