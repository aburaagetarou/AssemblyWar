package net.mcdiverse.assemblywar.parts;

import net.mcdiverse.assemblywar.util.Position;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public abstract class Parts {

	public int customModelData;
	public float yaw;
	public float pitch;
	public SmoothArmorStand smoothAS;

	public Position pos;

	public final double DEG90_RAD = Math.toRadians(90);
	public final double GLOBAL_Y_POS = -3.0d;

	////////////////////////////////////////////////////////////
	// コンストラクタ
	////////////////////////////////////////////////////////////
	public Parts(World world) {

		// アーマースタンドを生成
		this.smoothAS = new SmoothArmorStand(world);

		// ポジション初期化
		this.pos = new Position(0.0d, 0.0d, 0.0d);
	}


	////////////////////////////////////////////////////////////
	// 初期設定
	////////////////////////////////////////////////////////////
	public void init(ItemStack helmet) {

		// アーマースタンドプロパティ設定
		this.smoothAS.armorStand.setMarker( true );
		this.smoothAS.armorStand.setVisible( false );
		this.smoothAS.armorStand.setGravity( false );
		this.smoothAS.armorStand.setSilent( true );

		// 見た目をセット
		this.smoothAS.armorStand.getEquipment().setHelmet(helmet);
	}

	////////////////////////////////////////////////////////////
	// 移動
	////////////////////////////////////////////////////////////
	public void move( Location loc ) {

		// 座標をコピー
		Location move = loc.clone();

		// パーツごとの座標変位
		Vector delta = getDelta(
				move,
				(float) Math.toRadians( move.getYaw() )
		);

		// 移動
		move.add( delta );

		// ヨー／ピッチを保存
		this.yaw = move.getYaw();
		this.pitch = move.getPitch();

		// テレポート
		this.smoothAS.teleport( move );

		// テレポートで頭を回転させないようにする
		move.setPitch( 0.0f );

		/*
		// エンティティ削除パケット ======================
		PacketContainer packetDestroy = new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY);

		// Entity IDセット
		packetDestroy.getIntegerArrays().write(0, new int[] {this.armorStand.getEntityId()});
		// エンティティ削除パケット end ==================

		// スポーンパケット  ============================
		PacketContainer packetSpawn = new PacketContainer(PacketType.Play.Server.SPAWN_ENTITY_LIVING);

		// Entity IDセット
		packetSpawn.getIntegers().write(0, this.armorStand.getEntityId());

		// エンティティタイプセット
		packetSpawn.getIntegers().write(1, 1 );

		// UUIDセット
		packetSpawn.getUUIDs().write(0, this.armorStand.getUniqueId());

		// 座標セット
		packetSpawn.getDoubles().write( 0, move.getX() );
		packetSpawn.getDoubles().write( 1, move.getY() );
		packetSpawn.getDoubles().write( 2, move.getZ() );
		packetSpawn.getBytes().write( 0, ((byte)(int)(move.getYaw()   * 256.0f / 360.0F) ));
		packetSpawn.getBytes().write( 1, ((byte)(int)(move.getPitch() * 256.0f / 360.0F) ));
		packetSpawn.getBytes().write( 2, (byte)0 );

		// ベクトル
		packetSpawn.getIntegers().write( 2, 0 );
		packetSpawn.getIntegers().write( 3, 0 );
		packetSpawn.getIntegers().write( 4, 0 );
		// スポーンパケット end =========================

		// 装備パケット  ===============================
		PacketContainer packetEquip = new PacketContainer(PacketType.Play.Server.ENTITY_EQUIPMENT);

		// Entity IDセット
		packetEquip.getIntegers().write(0, this.armorStand.getEntityId());

		// アイテムセット
		List<Pair<EnumWrappers.ItemSlot, ItemStack>> list = new ArrayList<>();
		Pair<EnumWrappers.ItemSlot,ItemStack> pair =
				new Pair<>(EnumWrappers.ItemSlot.HEAD, this.armorStand.getItem(EquipmentSlot.HEAD));
		list.add(pair);
		packetEquip.getSlotStackPairLists().write(0, list);
		// 装備パケット end ============================

		Collection<Player> players = move.getNearbyEntitiesByType( Player.class, 50 );
		if( players.size() > 0 ) {
			players.forEach(player -> {
				try {
					AssemblyWar.protocolManager.sendServerPacket(player, packetDestroy);
					AssemblyWar.protocolManager.sendServerPacket(player, packetSpawn  );
					AssemblyWar.protocolManager.sendServerPacket(player, packetEquip  );
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			});
		}
 		*/
	}


	////////////////////////////////////////////////////////////
	// 前進
	////////////////////////////////////////////////////////////
	public void moveForward( Location loc ) {

		// 移動
		move( loc );

		// ポーズ変更
		poseForward();
	}


	////////////////////////////////////////////////////////////
	// 後進
	////////////////////////////////////////////////////////////
	public void moveBehind(Location loc) {

		// 移動
		move( loc );

		// ポーズ変更
		poseBehind();
	}


	////////////////////////////////////////////////////////////
	// 停止
	////////////////////////////////////////////////////////////
	public void stop( Location loc ) {

		// 移動
		move( loc );

		// ポーズ変更
		poseStop();
	}


	////////////////////////////////////////////////////////////
	// 破壊
	////////////////////////////////////////////////////////////
	public void destroy() {

		// アマスタ削除
		this.smoothAS.remove();
	}


	////////////////////////////////////////////////////////////
	// 座標変位値を取得する
	////////////////////////////////////////////////////////////
	protected Vector getDelta( Location loc, float yaw ) {
		return this.pos.positionVector( yaw );
	}


	////////////////////////////////////////////////////////////
	// 前進時のポーズを設定する抽象メソッド
	////////////////////////////////////////////////////////////
	protected abstract void poseForward();


	////////////////////////////////////////////////////////////
	// 後進時のポーズを設定する抽象メソッド
	////////////////////////////////////////////////////////////
	protected abstract void poseBehind();


	////////////////////////////////////////////////////////////
	// 停止時のポーズを設定する抽象メソッド
	////////////////////////////////////////////////////////////
	protected abstract void poseStop();
}
