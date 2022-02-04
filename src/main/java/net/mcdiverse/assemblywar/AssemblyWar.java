package net.mcdiverse.assemblywar;

import co.aikar.commands.PaperCommandManager;
import net.mcdiverse.assemblywar.commands.AssemblyWarCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class AssemblyWar extends JavaPlugin {

	public static AssemblyWar instance;

	public static String worldName;

	@Override
	public void onEnable() {

		// インスタンスセット
		instance = this;

		// ワールドを限定するため、設定を取得する
		worldName = getConfig().getString("worldName");

		// ワールド名が存在しない場合
		if(worldName == null) {
			saveDefaultConfig();
			throw new IllegalArgumentException("設定でワールド名を指定してください。");
		}

		// コマンド作成
		PaperCommandManager manager = new PaperCommandManager(this);
		manager.registerCommand(new AssemblyWarCommand());

		// enable brigadier integration for paper servers
		manager.enableUnstableAPI("brigadier");

		// ヘルプを有効化する
		manager.enableUnstableAPI("help");

		getLogger().info("Assembly war is ready.");
	}

	@Override
	public void onDisable() {

		// 構築済みのアセンブリを全て降機
		Assembly.assemblies.forEach( Assembly::disBoard );

		// 解放
		Assembly.assemblies.clear();
	}

	public static AssemblyWar getInstance() {
		return instance;
	}
}
