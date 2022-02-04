package net.mcdiverse.assemblywar;

import com.destroystokyo.paper.ParticleBuilder;
import net.mcdiverse.assemblywar.parts.*;
import net.mcdiverse.assemblywar.util.Position;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Assembly {

	// 構築済みのアセンブリリスト
	public static List<Assembly> assemblies = new ArrayList<>();

	// 各パーツID
	public int headId, bodyId, armId, legId, weaponId;

	// 機体ステータス
	double hp = 0.0d;
	double ep = 0.0d;
	double dodgeSpeed = 9.0d;

	// ヒット判定
	public static final int NOT_HIT = 0;
	public static final int HIT     = 1;
	public static final int DODGED  = 2;

	// 可動域
	double motionRange = 180.0d;

	// 回避中
	boolean isDodge = false;

	// 実体としてスポーンしているか
	boolean alive = false;

	// パイロット
	public Player pilot = null;

	// 各パーツクラス
	Head head;
	Body body;
	Arm larm;
	Arm rarm;
	Leg lleg;
	Leg rleg;
	Weapon weapon;

	// 各パーツアレイ
	List<Parts> parts = new ArrayList<>();

	// 当たり判定
	public List<Position> hitBox = new ArrayList<>();
	public List<BoundingBox> boundingBox = new ArrayList<>();
	private Position centerPos;

	// 攻撃発射位置、ブラスター
	private Position launchPosition = new Position(0.0d,0.0d,0.0d);
	public List<Blaster> blasters = new ArrayList<>();

	////////////////////////////////////////////////////////////
	// コンストラクタ
	////////////////////////////////////////////////////////////
	public Assembly( int headId, int bodyId, int armId, int legId, int weaponId ) {

		// IDセット
		this.headId = headId;
		this.bodyId = bodyId;
		this.armId  = armId;
		this.legId  = legId;
		this.weaponId = weaponId;
	}


	////////////////////////////////////////////////////////////
	// 初期化
	////////////////////////////////////////////////////////////
	public void init( World world, double left, double infront, double bottom, double legHeight, double armY, double bodyHeight ) {

		// 指定ワールド以外にはアセンブリを作成しない
		if(AssemblyWar.worldName.length() > 0 && !world.getName().equalsIgnoreCase(AssemblyWar.worldName)){
			throw new IllegalStateException("設定したワールド以外にはアセンブリを作成できません。");
		}

		// パーツを作成
		this.head   = new Head  ( world, this.headId   );
		this.body   = new Body  ( world, this.bodyId   );
		this.larm   = new Arm   ( world, this.armId    );
		this.rarm   = new Arm   ( world, this.armId + 10000 );
		this.lleg   = new Leg   ( world, this.legId    );
		this.rleg   = new Leg   ( world, this.legId + 100000 );
		this.weapon = new Weapon( world, this.weaponId );

		// パーツ位置セット
		this.lleg.pos.up    = bottom;
		this.rleg.pos.up    = bottom;
		this.body.pos.up    = bottom + legHeight;
		this.larm.pos.up    = bottom + legHeight + armY;
		this.rarm.pos.up    = bottom + legHeight + armY;
		this.weapon.pos.up  = bottom + legHeight + armY;
		this.head.pos.up    = bottom + legHeight + bodyHeight;

		// パーツリストに追加
		this.parts.add( this.head   );
		this.parts.add( this.body   );
		this.parts.add( this.larm   );
		this.parts.add( this.rarm   );
		this.parts.add( this.lleg   );
		this.parts.add( this.rleg   );
//		this.parts.add( this.weapon );

		// パーツ横位置セット
		this.parts.forEach( part -> part.pos.left = left );
		this.parts.forEach( part -> part.pos.infront = infront );

		centerPos = new Position( ((bottom + legHeight + bodyHeight) / 3), left, infront );
	}


	////////////////////////////////////////////////////////////
	// 股の広さをセット
	////////////////////////////////////////////////////////////
	public void addLegWidth(double legWid) {
		this.lleg.pos.left += legWid;
		this.rleg.pos.left += -legWid;
	}


	////////////////////////////////////////////////////////////
	// 肩幅セット
	////////////////////////////////////////////////////////////
	public void addArmWidth(double armWid) {
		this.larm.pos.left += armWid;
		this.rarm.pos.left += -armWid;
	}


	////////////////////////////////////////////////////////////
	// 正面への脚の変位をセット
	////////////////////////////////////////////////////////////
	public void setLegInfront(double infront) {
		this.lleg.pos.infront += infront;
		this.rleg.pos.infront += infront;
	}


	////////////////////////////////////////////////////////////
	// 正面への肩の変位をセット
	////////////////////////////////////////////////////////////
	public void setArmInfront(double infront) {
		this.larm.pos.infront += infront;
		this.rarm.pos.infront += infront;
	}


	////////////////////////////////////////////////////////////
	// 搭乗
	////////////////////////////////////////////////////////////
	public void board( Player pilot ) {

		// パイロットセット
		this.pilot = pilot;

		// 構築済みのアセンブリリストに追加
		assemblies.add(this);

		this.alive = true;

		// パーティクル生成
		ParticleBuilder builder = new ParticleBuilder( Particle.CLOUD );
		builder .count( 10  )
				.extra( 0.1 );

		// 機体を動かす
		// 2tickおきに移動
		new BukkitRunnable() {

			Player pilot;
			Location playerLoc;
			Location locDiff, locMoveTo;
			Location locBlaster;
			Location playerLocKeep = null;

			@Override
			public void run() {

				// パイロットを取得
				pilot = Assembly.this.pilot;

				// パイロット存在チェック
				if( pilot == null )  {

					// パイロットがいない場合終了
					parts.forEach(Parts::destroy);
					weapon.destroy();
					alive = false;
					this.cancel();
					return;
				}

				// パイロットのオンラインチェック
				if( !pilot.isOnline() ) {
					Assembly.this.disBoard();
				}

				// 座標取得
				playerLoc = pilot.getLocation();
				if( playerLocKeep == null ) {
					playerLocKeep = playerLoc;
				}

				// 移動量取得
				locDiff = playerLoc.clone().subtract( playerLocKeep );

				// 移動量調整
				// 動きがぼろぼろになるため、行わない
//				locDiff.setX( locDiff.getX() * 2 );
//				locDiff.setY( locDiff.getY() * 2 );
//				locDiff.setZ( locDiff.getZ() * 2 );

				// ブラスター座標セット
				locBlaster = body.smoothAS.getLocation().clone();
				locBlaster.subtract( locDiff );

				// 移動先セット
				locMoveTo = playerLoc.clone();
				locMoveTo.add(locDiff);

				// yaw -> vector
				double yaw = Math.toRadians( playerLoc.getYaw() );
				Vector vec = new Vector( Math.cos( yaw ), 0.0d, Math.sin( yaw ) ).normalize();

				// 座標間の角度を算出
				double radMove = Math.atan2( (playerLocKeep.getZ() - playerLoc.getZ()), (playerLocKeep.getX() - playerLoc.getX()) );
				double radYaw  = Math.atan2( vec.getZ(), vec.getX() );
				double deg     = Math.toDegrees( radMove - radYaw );
				if( deg <= -0.000001d ) deg += 360.0d;

				// 移動した場合
				if( playerLocKeep.distance(playerLoc) >= 0.1d ) {

					// 前進
					if( deg >= 180 && deg <= 360 ) {
						parts.forEach( part -> part.moveForward( locMoveTo ) );
						weapon.moveForward(getLaunchPosition());

						// ブラスターエフェクト
						blasters.forEach( blaster -> {
							blaster.blast( locBlaster );
						});
					}

					// 後進
					else {
						parts.forEach( part -> part.moveBehind( locMoveTo ) );
						weapon.moveBehind(getLaunchPosition());
					}
				}
				else {
					parts.forEach( part -> part.stop( locMoveTo ) );
					weapon.stop(getLaunchPosition());
				}

				boundingBox.clear();
				double pYaw = Math.toRadians(pilot.getLocation().getYaw());

				hitBox.forEach(position -> {
					Vector pos = position.positionVector(pYaw);
					boundingBox.add(BoundingBox.of(body.smoothAS.getLocation(), pos.getX(), pos.getY(), pos.getZ()));
				});

				// プレイヤー座標をキープ
				playerLocKeep = playerLoc;
			}
		}.runTaskTimer( AssemblyWar.getInstance(), 4L, 0L );
	}


	////////////////////////////////////////////////////////////
	// パーティクル出力
	////////////////////////////////////////////////////////////
	public void showParticle( ParticleBuilder builder, Location loc, boolean isBack ) {

		// x,z方向ベクトル算出
		double x = Math.cos( Math.toRadians( loc.getYaw() ) );
		double z = Math.sin( Math.toRadians( loc.getYaw() ) );

		// 後方に進む場合、ベクトルを反転
		x *= -1;
		z *= -1;

		builder	.location( loc )
				.offset( x, -0.2, z );
	}

	////////////////////////////////////////////////////////////
	// 降機
	////////////////////////////////////////////////////////////
	public void disBoard() {

		// パイロットをリセット
		this.pilot = null;

		// 構築済みのアセンブリリストから除去
		assemblies.remove(this);
	}


	////////////////////////////////////////////////////////////
	// 命中したかどうか
	////////////////////////////////////////////////////////////
	public int isHit( BoundingBox boundingBox ) {

		// 戻り値宣言
		int ret = NOT_HIT;

		// 当たり判定を全てループ
 		for( BoundingBox box : this.boundingBox ) {

			// 機体の当たり判定に命中しているか
			if( box.overlaps( boundingBox ) ) {

				// 回避中であれば回避成功を返す
				if (this.isDodge) {
					ret = DODGED;
				}

				// 回避中でなければ命中を返す
				else {
					ret = HIT;
				}

				break;
			}
		}
		return ret;
	}


	////////////////////////////////////////////////////////////
	// スキル発射地点を得る
	////////////////////////////////////////////////////////////
	public Location getLaunchPosition() {

		// パイロット座標を取得
		if(this.pilot == null || this.launchPosition == null) return null;
		Location loc = this.rarm.smoothAS.getLocation().clone();

		// ラジアンに変換
		double yaw   = Math.toRadians(loc.getYaw() + 90);
		double pitch = Math.toRadians((loc.getPitch() + this.motionRange) / 2);

		// 極座標公式より発射地点を算出
		double x = Math.cos( yaw   ) * Math.sin( pitch ) * this.launchPosition.infront;
		double y = Math.cos( pitch ) * this.launchPosition.infront;
		double z = Math.sin( yaw   ) * Math.sin( pitch ) * this.launchPosition.infront;

		// 横方向へのズレ
		double yaw2 = Math.toRadians(loc.getYaw());
		x += Math.cos(yaw2) * this.launchPosition.left;
		z += Math.sin(yaw2) * this.launchPosition.left;

		// 座標に加算
		loc.add(0.0d, + this.launchPosition.up, 0.0d);
		loc.add(x, y, z);

		return loc;
	}


	////////////////////////////////////////////////////////////
	// スキル発射地点をセットする
	// X:角度, Y:高さ, Z:距離
	////////////////////////////////////////////////////////////
	public void setLaunchPosition(Position launchPosition, double motionRange) {
		this.launchPosition = launchPosition;
		this.motionRange = motionRange;
	}


	////////////////////////////////////////////////////////////
	// 回避行動
	////////////////////////////////////////////////////////////
	public void dodge() {

		// パイロットが存在しない場合何もしない
		if(this.pilot == null) return;

		// プレイヤー座標をキープ(減算用)
		Location playerLocKeep = this.pilot.getLocation().clone();

		this.isDodge = true;

		new BukkitRunnable() {

			@Override
			public void run() {

				// 移動
				Vector vec = pilot.getLocation().clone().subtract(playerLocKeep).toVector();
				vec = vec.normalize().add(vec);
				pilot.setVelocity(vec);

				blasters.forEach(blaster -> {
					for( int i = 0; i < 3; i++ ) {
						blaster.blast( body.smoothAS.getLocation() );
					}
				});
			}
		}.runTaskLater(AssemblyWar.getInstance(), 2L);
	}


	////////////////////////////////////////////////////////////
	// 当たり判定を追加する
	////////////////////////////////////////////////////////////
	public void addBoundingBox( double top, double left, double infront ) {
		this.hitBox.add( new Position(top, left, infront) );
	}


	////////////////////////////////////////////////////////////
	// 指定距離内のアセンブリを取得する
	////////////////////////////////////////////////////////////
	public static List<Assembly> getNearbyAssemblies( Location loc, double distance ) {

		// 戻り値に使用するリスト
		List<Assembly> assemblies = new ArrayList<>();

		// アセンブリを全ループ
		Assembly.assemblies.forEach( assembly -> {

			// 距離を計測し、指定未満だったら戻り値に追加
			if( assembly.body.smoothAS.getLocation().distance(loc) <= distance ||
					assembly.body.smoothAS.getLocation().distance(loc)  <= distance ) {
				assemblies.add( assembly );
			}
		});

		// リスト->配列にして返す
		return ( assemblies );
	}
}
