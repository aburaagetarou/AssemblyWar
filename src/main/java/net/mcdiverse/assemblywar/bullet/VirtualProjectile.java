package net.mcdiverse.assemblywar.bullet;

import com.destroystokyo.paper.ParticleBuilder;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

public abstract class VirtualProjectile {

	int skill = 0;
	boolean alive = true;
	Entity shooter;
	Vector vector;
	int range = 0;
	int speed = 0;
	int penetrate = 0;
	boolean blockPenetrate = false;
	ParticleBuilder particle;
	Location location;
	BoundingBox boundingBox;

	public VirtualProjectile(int skill, Entity shooter, Vector vector) {
		this.skill = skill;
		this.shooter = shooter;
		this.vector = vector;
		location = shooter.getLocation();
		boundingBox = BoundingBox.of(location, 0.1d, 0.1d, 0.1d);
	}

	public VirtualProjectile(int skill, Entity shooter, double x, double y, double z) {
		this(skill, shooter, new Vector(x, y, z));
	}

	public int getSkill() {
		return skill;
	}

	public void setSkill(int skill) {
		this.skill = skill;
	}

	public boolean isAlive() {
		return alive;
	}

	public Entity getShooter() {
		return shooter;
	}

	public void setShooter(Entity shooter) {
		this.shooter = shooter;
	}

	public Vector getVector() {
		return vector;
	}

	public void setVector(Vector vector) {
		this.vector = vector;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getPenetrate() {
		return penetrate;
	}

	public void setPenetrate(int penetrate) {
		this.penetrate = penetrate;
	}

	public ParticleBuilder getParticle() {
		return particle;
	}

	public void setParticle(ParticleBuilder particle) {
		this.particle = particle;
	}

	public boolean isBlockPenetrate() {
		return blockPenetrate;
	}

	public void setBlockPenetrate(boolean blockPenetrate) {
		this.blockPenetrate = blockPenetrate;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
		if(this.particle != null)
			this.particle.location(location);
		if(this.boundingBox != null)
			this.boundingBox = BoundingBox.of(location, 0.1d, 0.1d, 0.1d);
	}

	public BoundingBox getBoundingBox() {
		return boundingBox;
	}

	public void setBoundingBox(BoundingBox boundingBox) {
		this.boundingBox = boundingBox;
	}

	public void setBoundingBox(Location loc, double x, double y, double z) {
		this.boundingBox = BoundingBox.of(loc, x, y, z);
	}

	abstract void launch();

	@Override
	public boolean equals(Object obj) {
		if( obj instanceof VirtualProjectile ) {
			return false;
		} else {
			VirtualProjectile vp = (VirtualProjectile)obj;
			return(    skill     == vp.skill
					&& speed     == vp.speed
					&& range     == vp.range
					&& penetrate == vp.penetrate
					&& shooter.    equals(vp.shooter)
					&& vector.     equals(vp.vector)
					&& particle.   equals(vp.particle)
					&& location.   equals(vp.location)
					&& boundingBox.equals(vp.boundingBox)
			);
		}
	}
}
