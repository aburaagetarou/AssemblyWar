package net.mcdiverse.assemblywar.control;

import net.mcdiverse.assemblywar.Assembly;
import net.mcdiverse.assemblywar.AssemblyWar;
import net.mcdiverse.assemblywar.parts.*;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

public class PartsSetting {


	////////////////////////////////////////////////////////////
	// パーツIDを得る
	////////////////////////////////////////////////////////////
	public static int getPartsSetting(Player player, Class<? extends Parts> target) {
		NamespacedKey key = new NamespacedKey(AssemblyWar.instance, target.getName());
		if(player == null) return 1;
		Integer value = player.getPersistentDataContainer().get(key, PersistentDataType.INTEGER);
		if(value == null) return 1;
		return value;
	}


	////////////////////////////////////////////////////////////
	// パーツIDをセットする
	////////////////////////////////////////////////////////////
	public static void setPartsSetting(Player player, Class<? extends Parts> target, int value) {
		NamespacedKey key = new NamespacedKey(AssemblyWar.instance, target.getName());
		if(player == null) return;
		player.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, value);
	}


	////////////////////////////////////////////////////////////
	// アセンブリを生成する
	////////////////////////////////////////////////////////////
	public static Assembly makeAssembly(Player player) {
		int headId = getPartsSetting(player, Head.class);
		int bodyId = getPartsSetting(player, Body.class);
		int armId = getPartsSetting(player, Arm.class);
		int legId = getPartsSetting(player, Leg.class);
		int weaponId = getPartsSetting(player, Weapon.class);
		return new Assembly(headId, bodyId, armId, legId, weaponId);
	}
}
