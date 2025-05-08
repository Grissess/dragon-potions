package mods.grissess.dragonpotions.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.common.util.INBTSerializable;

public class PlayerAttachedData extends SavedData {
	public static PlayerAttachedData get(MinecraftServer server) {
		return server.overworld().getDataStorage().computeIfAbsent(
				PlayerAttachedData::load,
				PlayerAttachedData::create,
				"playerAttachedData"
		);
	}

	public static class PlayerAttachedDatum implements INBTSerializable<CompoundTag> {
		public Optional<Double> originalSize = Optional.empty();

		@Override
		public CompoundTag serializeNBT() {
			CompoundTag tag = new CompoundTag();
			originalSize.ifPresent((size) -> tag.putDouble("originalSize", size));
			return tag;
		}

		@Override
		public void deserializeNBT(CompoundTag tag) {
			if(tag.contains("originalSize"))
				originalSize = Optional.of(tag.getDouble("originalSize"));
		}
	}
	
	protected Map<UUID, PlayerAttachedDatum> map = new HashMap<UUID, PlayerAttachedDatum>();
	
	public void setDatum(UUID uuid, PlayerAttachedDatum datum) {
		map.put(uuid,  datum);
		setDirty();
	}
	public void setDatum(ServerPlayer ply, PlayerAttachedDatum datum) {
		setDatum(ply.getUUID(), datum);
	}
	public void setDatum(Player ply, PlayerAttachedDatum datum) {
		setDatum(ply.getUUID(), datum);
	}
	
	public PlayerAttachedDatum removeDatum(UUID uuid) {
		PlayerAttachedDatum datum = map.remove(uuid);
		setDirty();
		return datum;
	}
	public PlayerAttachedDatum removeDatum(ServerPlayer ply) {
		return removeDatum(ply.getUUID());
	}
	public PlayerAttachedDatum removeDatum(Player ply) {
		return removeDatum(ply.getUUID());
	}
	
	public PlayerAttachedDatum getDatum(UUID uuid) {
		return map.getOrDefault(uuid, new PlayerAttachedDatum());
	}
	public PlayerAttachedDatum getDatum(ServerPlayer ply) {
		return getDatum(ply.getUUID());
	}
	public PlayerAttachedDatum getDatum(Player ply) {
		return getDatum(ply.getUUID());
	}
	
	public PlayerAttachedDatum updateDatum(UUID uuid, Consumer<PlayerAttachedDatum> func) {
		PlayerAttachedDatum datum = getDatum(uuid);
		func.accept(datum);
		setDatum(uuid, datum);
		return datum;
	}
	public PlayerAttachedDatum updateDatum(ServerPlayer ply, Consumer<PlayerAttachedDatum> func) {
		return updateDatum(ply.getUUID(), func);
	}
	public PlayerAttachedDatum updateDatum(Player ply, Consumer<PlayerAttachedDatum> func) {
		return updateDatum(ply.getUUID(), func);
	}
	
	public static PlayerAttachedData create() {
		return new PlayerAttachedData();
	}
	
	public static PlayerAttachedData load(CompoundTag tag) {
		PlayerAttachedData data = create();
		tag.getAllKeys().forEach(
				(key) -> {
					UUID uuid = UUID.fromString(key);
					PlayerAttachedDatum datum = new PlayerAttachedDatum();
					datum.deserializeNBT(tag.getCompound(key));
					data.map.put(uuid,  datum);
				}
		);
		return data;
	}

	@Override
	public CompoundTag save(CompoundTag tag) {
		map.forEach(
				(uuid, datum) -> tag.put(uuid.toString(), datum.serializeNBT())
		);
		return tag;
	}

}
