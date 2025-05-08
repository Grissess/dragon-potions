package mods.grissess.dragonpotions.effect;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import by.dragonsurvivalteam.dragonsurvival.common.capability.DragonStateHandler;
import by.dragonsurvivalteam.dragonsurvival.network.NetworkHandler;
import by.dragonsurvivalteam.dragonsurvival.network.player.SyncSize;
import by.dragonsurvivalteam.dragonsurvival.util.DragonUtils;
import mods.grissess.dragonpotions.Config;
import mods.grissess.dragonpotions.DragonPotions;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = DragonPotions.MODID)
public class MinifyEffect extends MobEffect {
	protected static Map<UUID, Double> originalSizes = new HashMap<UUID, Double>();  // TODO: serialize
	
	public MinifyEffect(MobEffectCategory cat, int color) {
		super(cat, color);
	}
	
	@SubscribeEvent
	public static void onEffectApplicable(MobEffectEvent.Applicable event) {
		if(event.getEffectInstance().getEffect() != DragonPotions.MINIFY_EFFECT.get()) return;
		
		LivingEntity entity = event.getEntity();
		DragonStateHandler dragon = DragonUtils.getHandler(entity);
		if(!dragon.isDragon()) {
			event.setResult(Result.DENY);
		} else {
			event.setResult(Result.ALLOW);
		}
	}
	
	@SubscribeEvent
	public static void onEffectStart(MobEffectEvent.Added event) {
		if(event.getEffectInstance().getEffect() != DragonPotions.MINIFY_EFFECT.get()) return;
		
		LivingEntity entity = event.getEntity();
		DragonStateHandler dragon = DragonUtils.getHandler(entity);
		if(!dragon.isDragon() || !(entity instanceof Player)) return;
		startEffect((Player) entity, dragon);
	}
	
	@SubscribeEvent
	public static void onEffectEnded(MobEffectEvent.Expired event) {
		if(event.getEffectInstance().getEffect() != DragonPotions.MINIFY_EFFECT.get()) return;
		
		tryEndEffect(event.getEntity());
	}
	
	@SubscribeEvent
	public static void onEffectRemoved(MobEffectEvent.Remove event) {
		if(event.getEffectInstance().getEffect() != DragonPotions.MINIFY_EFFECT.get()) return;
		
		tryEndEffect(event.getEntity());
	}
	
	public static void startEffect(Player ply, DragonStateHandler dragon) {
		DragonPotions.LOGGER.info("Starting effect for {}", ply);
		if(!originalSizes.containsKey(ply.getUUID()))  // don't overwrite a previous saved size if we begin again
			originalSizes.put(ply.getUUID(), dragon.getSize());
		setSize(ply, dragon, Config.minifySize);
	}
	
	public static void tryEndEffect(LivingEntity ent) {
		if(!(ent instanceof Player)) return;
		Player ply = (Player) ent;
		if(!originalSizes.containsKey(ply.getUUID())) return;
		DragonStateHandler dragon = DragonUtils.getHandler(ply);
		if(!dragon.isDragon()) return;
		endEffect(ply, dragon);
	}
	
	public static void endEffect(Player ply, DragonStateHandler dragon) {
		DragonPotions.LOGGER.info("Ending effect for {}", ply);
		setSize(ply, dragon, originalSizes.getOrDefault(ply.getUUID(), Config.minifySize));
		originalSizes.remove(ply.getUUID());  // enable another size to be stored after effect end
	}
	
	public static void setSize(Player ply, DragonStateHandler dragon, double size) {
		dragon.setSize(size, ply);
		NetworkHandler.CHANNEL.send(
				PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> ply),
				new SyncSize(ply.getId(), size)
		);
	}
}