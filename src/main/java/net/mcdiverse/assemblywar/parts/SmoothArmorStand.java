package net.mcdiverse.assemblywar.parts;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class SmoothArmorStand {

	public ArmorStand armorStand;
	public AreaEffectCloud vehicle;

	public SmoothArmorStand(World world) {

		// AreaEffectCloudをスポーン
		Location spawnLoc = new Location( world, 0.0d, 2.0d, 0.0d );
		this.vehicle = (AreaEffectCloud) world.spawnEntity(
				spawnLoc,
				EntityType.AREA_EFFECT_CLOUD,
				CreatureSpawnEvent.SpawnReason.CUSTOM
		);


		// パーツ用アーマースタンドをスポーン
		this.armorStand = (ArmorStand) world.spawnEntity(
				spawnLoc,
				EntityType.ARMOR_STAND,
				CreatureSpawnEvent.SpawnReason.CUSTOM
		);

		// 動きをなめらかにするため、AreaEffectCloudに載せる
		this.vehicle.addPassenger(armorStand);
	}


	////////////////////////////////////////////////////////////
	// テレポート
	////////////////////////////////////////////////////////////
	public void teleport(Location loc){
		this.vehicle.teleport(loc);
	}


	////////////////////////////////////////////////////////////
	// 消去
	////////////////////////////////////////////////////////////
	public void remove(){
		this.armorStand.remove();
		this.vehicle.remove();
	}


	////////////////////////////////////////////////////////////
	// 本体の座標を得る
	////////////////////////////////////////////////////////////
	public Location getLocation(){
		return this.armorStand.getLocation();
	}
}
