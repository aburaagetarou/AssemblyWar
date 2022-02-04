package net.mcdiverse.assemblywar.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.mcdiverse.assemblywar.control.AWPlayer;
import org.bukkit.entity.Player;

@CommandAlias("aw|assemblywar")
public class AssemblyWarCommand extends BaseCommand {

	@Default
	@HelpCommand
	@Subcommand("help")
	@CommandCompletion("@assemblywarlist")
	@Description("Assembly War管理コマンド")
	@CommandPermission("assemblywar.help")
	public void help(Player player, CommandHelp help){
		player.sendMessage(TextComponent.ofChildren(
					Component.text("-----").color(NamedTextColor.GRAY),
					Component.text(" Assembly War Help ").color(NamedTextColor.AQUA),
					Component.text("-----").color(NamedTextColor.GRAY))
		);
		help.showHelp();
	}

	@Subcommand("board")
	@CommandPermission("assemblywar.board")
	@Description("Assemblyに搭乗する")
	public void board(Player player){
		AWPlayer awp = AWPlayer.getPlayer(player);
		awp.setupAssembly();
		awp.getAssembly().board(player);
	}
}
