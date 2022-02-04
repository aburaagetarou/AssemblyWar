package net.mcdiverse.assemblywar.control;

import net.mcdiverse.assemblywar.Assembly;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AWPlayer {

	// アセンブリリスト
	private static final Map<UUID, AWPlayer> playerToAssembly = new HashMap<>();

	private final Player player;
	private Assembly assembly;


	////////////////////////////////////////////////////////////
	// 指定したプレイヤーのアセンブリを返す
	////////////////////////////////////////////////////////////
	public static AWPlayer getPlayer(Player player) {
		if(playerToAssembly.containsKey(player.getUniqueId())){
			return playerToAssembly.get(player.getUniqueId());
		}
		else{
			return new AWPlayer(player);
		}
	}


	////////////////////////////////////////////////////////////
	// コンストラクタ
	////////////////////////////////////////////////////////////
	public AWPlayer(Player player) {

		this.player = player;

		// リストに追加
		playerToAssembly.put(player.getUniqueId(), this);
	}


	////////////////////////////////////////////////////////////
	// アセンブリを作成
	////////////////////////////////////////////////////////////
	public void setupAssembly() {

		// アセンブリを作成
		this.assembly = PartsSetting.makeAssembly(player);
	}


	////////////////////////////////////////////////////////////
	// アセンブリを得る
	////////////////////////////////////////////////////////////
	public Assembly getAssembly() {
		return assembly;
	}
}
