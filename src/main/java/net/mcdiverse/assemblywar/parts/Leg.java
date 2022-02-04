package net.mcdiverse.assemblywar.parts;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Leg extends Parts {

	ItemStack normal;
	ItemStack forward;
	ItemStack behind;

	////////////////////////////////////////////////////////////
	// コンストラクタ
	////////////////////////////////////////////////////////////
	public Leg( World world, int customModelData ) {

		// スーパークラスのコンストラクタを実行
		super( world );

		// カスタムモデルデータ付きのアイテムを生成
		ItemStack item = new ItemStack( Material.NETHERITE_SHOVEL );
		ItemMeta meta = item.getItemMeta();
		meta.setCustomModelData( customModelData );
		item.setItemMeta( meta );

		// 初期設定
		this.init(item);

		// 通常時アイテムをセット
		this.normal = item;

		// カスタムモデルデータ付きのアイテムを生成（前進時用）
		ItemStack forward = new ItemStack( Material.NETHERITE_SHOVEL );
		ItemMeta forwardMeta = forward.getItemMeta();
		forwardMeta.setCustomModelData( customModelData + 10000 );
		forward.setItemMeta( forwardMeta );
		this.forward = forward;

		// カスタムモデルデータ付きのアイテムを生成（後進時用）
		ItemStack behind = new ItemStack( Material.NETHERITE_SHOVEL );
		ItemMeta behindMeta = behind.getItemMeta();
		behindMeta.setCustomModelData( customModelData + 20000 );
		behind.setItemMeta( behindMeta );
		this.behind = behind;
	}


	////////////////////////////////////////////////////////////
	// 前進時のポーズ
	////////////////////////////////////////////////////////////
	@Override
	protected void poseForward() {

		// アマスタの頭に生成したアイテムをセット
		this.smoothAS.armorStand.getEquipment().setHelmet( forward );
	}


	////////////////////////////////////////////////////////////
	// 後進時のポーズ
	////////////////////////////////////////////////////////////
	@Override
	protected void poseBehind() {

		// アマスタの頭に生成したアイテムをセット
		this.smoothAS.armorStand.getEquipment().setHelmet( behind );
	}


	////////////////////////////////////////////////////////////
	// 停止時のポーズ
	////////////////////////////////////////////////////////////
	@Override
	protected void poseStop() {

		// アマスタの頭に生成したアイテムをセット
		this.smoothAS.armorStand.getEquipment().setHelmet( normal );
	}
}
