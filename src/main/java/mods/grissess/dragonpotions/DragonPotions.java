package mods.grissess.dragonpotions;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import by.dragonsurvivalteam.dragonsurvival.registry.DSItems;
import mods.grissess.dragonpotions.effect.MinifyEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(DragonPotions.MODID)
public class DragonPotions
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "dragonpotions";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<MobEffect> MOB_EFFECTS = 
    		DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, MODID);
    public static final DeferredRegister<Potion> POTIONS =
    		DeferredRegister.create(ForgeRegistries.POTIONS, MODID);
    
    public static final RegistryObject<MinifyEffect> MINIFY_EFFECT = MOB_EFFECTS.register("minify",
    		() -> new MinifyEffect(MobEffectCategory.NEUTRAL, 0xffaaaa)
    );
    
    public static final RegistryObject<Potion> DRACONIC_POTION = POTIONS.register("draconic", 
    		() -> new Potion()
    );
    public static final RegistryObject<Potion> MINIFY_POTION = POTIONS.register("minify",
    		() -> new Potion(new MobEffectInstance(MINIFY_EFFECT.get(), 72000))
    );
    
    public DragonPotions() {
    	LOGGER.info("Hello from Dragon Potions!  - Grissess :D");
    	IEventBus modbus = FMLJavaModLoadingContext.get().getModEventBus();
    	MOB_EFFECTS.register(modbus);
    	POTIONS.register(modbus);
    	MinecraftForge.EVENT_BUS.register(this);
    	modbus.register(this);
    	ModLoadingContext.get().registerConfig(Type.COMMON, Config.SPEC);
    }
    
    @SubscribeEvent
    public void setup(FMLCommonSetupEvent event) {
    	event.enqueueWork(this::registerBrewing);
    }
    
    public void registerBrewing() {
    	BrewingRecipeRegistry.addRecipe(
    			Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER)),
    			Ingredient.of(DSItems.elderDragonDust),
    			PotionUtils.setPotion(new ItemStack(Items.POTION), DRACONIC_POTION.get())
		);
    	
    	BrewingRecipeRegistry.addRecipe(
    			Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), DRACONIC_POTION.get())),
    			Ingredient.of(Items.IRON_NUGGET),
    			PotionUtils.setPotion(new ItemStack(Items.POTION), MINIFY_POTION.get())
    	);
    }
}
