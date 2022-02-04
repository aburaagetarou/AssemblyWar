package net.mcdiverse.assemblywar.parts;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.EulerAngle;

public class Arm extends Parts {


	////////////////////////////////////////////////////////////
	// コンストラクタ
	////////////////////////////////////////////////////////////
	public Arm(World world, int customModelData) {

		// スーパークラスのコンストラクタを実行
		super( world );

		// カスタムモデルデータ付きのアイテムを生成
		ItemStack item = new ItemStack( Material.NETHERITE_PICKAXE );
		ItemMeta meta = item.getItemMeta();
		meta.setCustomModelData( customModelData );
		item.setItemMeta( meta );

		// 初期設定
		this.init(item);
	}


	////////////////////////////////////////////////////////////
	// ポーズをセットする
	////////////////////////////////////////////////////////////
	private void pose() {

		// 角度をラジアンに変換
		double radPitch = Math.toRadians(this.pitch);

		// 角度を計算
		// 腕を大きく曲げないようにする
		double x = radPitch / 2;
		double y = 0.0d;
		double z = 0.0d;

		// 頭の角度をセット(ラジアン)
		this.smoothAS.armorStand.setHeadPose( new EulerAngle( x, y, z ));
	}


	////////////////////////////////////////////////////////////
	// 前進時のポーズ
	////////////////////////////////////////////////////////////
	@Override
	protected void poseForward() {

		// ポーズセット
		pose();
	}


	////////////////////////////////////////////////////////////
	// 後進時のポーズ
	////////////////////////////////////////////////////////////
	@Override
	protected void poseBehind() {

		// ポーズセット
		pose();
	}


	////////////////////////////////////////////////////////////
	// 停止時のポーズ
	////////////////////////////////////////////////////////////
	@Override
	protected void poseStop() {

		// ポーズセット
		pose();
	}
}
