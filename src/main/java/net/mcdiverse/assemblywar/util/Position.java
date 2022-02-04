package net.mcdiverse.assemblywar.util;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class Position {

	public double up;
	public double left;
	public double infront;


	////////////////////////////////////////////////////////////
	// コンストラクタ
	////////////////////////////////////////////////////////////
	public Position( double up, double left, double infront ) {
		this.up = up;
		this.left = left;
		this.infront = infront;
	}


	////////////////////////////////////////////////////////////
	// ヨーの方向に傾けた座標を返す
	////////////////////////////////////////////////////////////
	public Location positionLocation(Location loc, double radianYaw) {

		// 座標コピー
		Location positioned = loc.clone();

		// 左右上下の位置調整
		positioned.add(Math.cos(radianYaw) * left, up, Math.sin(radianYaw) * left);

		// 前後の位置調整
		double yaw2 = Math.cos(radianYaw + Math.toRadians(90));
		positioned.add(new Vector(yaw2 * left, 0.0d, yaw2 * left));

		return positioned;
	}


	////////////////////////////////////////////////////////////
	// ヨーの方向に傾けた移動量を返す
	////////////////////////////////////////////////////////////
	public Vector positionVector(double radianYaw) {

		// 座標コピー
		Vector vec = new Vector(0.0d, 0.0d, 0.0d);

		// 左右上下の位置調整
		vec.add(new Vector(Math.cos(radianYaw) * left, up, Math.sin(radianYaw) * left));

		// 前後の位置調整
		double yaw2 = (radianYaw + Math.toRadians(90));
		vec.add(new Vector(Math.cos(yaw2) * infront, 0.0d, Math.sin(yaw2) * infront));

		return vec;
	}

}
