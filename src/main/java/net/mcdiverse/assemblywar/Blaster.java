package net.mcdiverse.assemblywar;

import com.destroystokyo.paper.ParticleBuilder;
import net.mcdiverse.assemblywar.util.Position;
import org.bukkit.Location;

public class Blaster {

	public ParticleBuilder particle;
	public Position position;
	public double vecX;
	public double vecY;
	public double speed;


	public Blaster(ParticleBuilder particle, Position position, double vecX, double vecY, double speed ) {
		this.particle = particle;
		this.position = position;
		this.vecX = vecX;
		this.vecY = vecY;
		this.speed = speed;
	}

	public Blaster( ParticleBuilder particle, Position position ) {
		this.particle = particle;
		this.position = position;
		this.vecX = 0.0d;
		this.vecY = 0.0d;
		this.speed = 0.0d;
	}

	////////////////////////////////////////////////////////////
	// 表示
	////////////////////////////////////////////////////////////
	public void blast(Location loc) {

		// 座標コピー
		Location spawnLoc = loc.clone();

		// 左右上下の位置調整
		double rYaw = Math.toRadians(spawnLoc.getYaw());
		spawnLoc.add(position.positionVector(rYaw));

		double vector = Math.cos(rYaw) * this.vecX;

		// パーティクル表示
		particle.location(spawnLoc)
			.allPlayers()
			.offset( vector, vecY, vector )
			.count( 0 )
			.extra( speed )
			.spawn();
	}
}
