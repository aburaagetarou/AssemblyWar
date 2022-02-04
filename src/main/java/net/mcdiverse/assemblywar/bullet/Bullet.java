package net.mcdiverse.assemblywar.bullet;

import net.mcdiverse.assemblywar.Assembly;
import net.mcdiverse.assemblywar.AssemblyWar;
import org.bukkit.Bukkit;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Bullet extends VirtualProjectile {

	double damage = 0.0d;
	double maxDmg = 0.0d;
	double decay = 0.0d;
	int slot = -1;
	Vector fall = new Vector(0, 0, 0);

	public Bullet(int skill, Entity shooter, Vector vector) {
		super( skill, shooter, vector );
	}

	public Bullet(int skill, Entity shooter, double vecX, double vecY, double vecZ) {
		super( skill, shooter, vecX, vecY, vecZ );
	}

	public double getDamage() {
		return damage;
	}

	public void setDamage(double damage) {
		this.damage = damage;
	}

	public double getMaxDamage() {
		return maxDmg;
	}

	public void setMaxDamage(double maxDmg) {
		this.maxDmg = maxDmg;
	}

	public double getDecay() {
		return decay;
	}

	public void setDecay(double decay) {
		this.decay = decay;
	}

	public Vector getFall() {
		return fall;
	}

	public void setFall(Vector fall) {
		this.fall = fall;
	}

	public int getSlot() { return slot; }

	public void setSlot(int slot) { this.slot = slot; }

	@Override
	public void launch() {

		if(location == null) {
			location = shooter.getLocation();
		}
		if(slot < 0 && shooter instanceof Player) {
			slot = ((Player) shooter).getInventory().getHeldItemSlot();
		}

		new BukkitRunnable() {

			int hit = 0;
			int count = 0;
			final Location loc = location;
			final List<UUID> hittedEntities = new ArrayList<>();
			final List<Assembly> hittedAssemblies = new ArrayList<>();
			final Vector vec2 = fall.clone();

			@Override
			public void run() {

				List<Entity> nearbyEntities = new ArrayList<>();
				List<Assembly> nearbyAssemblies = new ArrayList<>();
				for(int i = 0; i < speed; i++ ) {
					try {
						loc.add(vector);
						loc.add(vec2);
						boundingBox.shift(vector);
						boundingBox.shift(vec2);
						vec2.add(fall);

						nearbyEntities.clear();
						nearbyEntities.addAll(loc.getNearbyLivingEntities(5, 5, 5));
						if (nearbyEntities.size() > 0) {
							nearbyEntities.forEach((hitEntity) -> {
								if (!(hitEntity == shooter) &&
										!hittedEntities.contains(hitEntity.getUniqueId()) &&
										hitEntity.getBoundingBox().overlaps(boundingBox)) {
									BulletHitEvent e = new BulletHitEvent(hitEntity, Bullet.this);
									Bukkit.getPluginManager().callEvent(e);
									if (!(e.isCancelled())) {
										hittedEntities.add(hitEntity.getUniqueId());
										if (++hit > penetrate) {
											alive = false;
											this.cancel();
										}
									}
								}
							});
						}

						nearbyAssemblies.clear();
						nearbyAssemblies.addAll(Assembly.getNearbyAssemblies(loc, 10));
						if (nearbyAssemblies.size() > 0) {
							nearbyAssemblies.forEach((assembly) -> {
								if (!(assembly.pilot == shooter)){
									if(hittedAssemblies.stream().noneMatch(assem -> assem.pilot.equals(assembly.pilot))) {
										if (assembly.boundingBox.stream().anyMatch(box -> box.overlaps(boundingBox))) {
											AssemblyBulletHitEvent e = new AssemblyBulletHitEvent(assembly, Bullet.this);
											Bukkit.getPluginManager().callEvent(e);
											if (!(e.isCancelled())) {
												hittedAssemblies.add(assembly);
												if (++hit > penetrate) {
													alive = false;
													this.cancel();
												}
											}
										}
									}
								}
							});
						}

						Vector dir = vector.clone();
						dir.add(vec2);
						RayTraceResult res = loc.getWorld().rayTraceBlocks(loc, dir, 1.0d, FluidCollisionMode.NEVER, true);
						if(!blockPenetrate && res != null) {
							this.cancel();
						}

						particle.location(loc).spawn();

						if( decay > 0 ) {
							if( damage < maxDmg ) damage += decay;
						}else {
							if( damage > maxDmg ) damage += decay;
						}
					} catch (NullPointerException e) {
						alive = false;
						this.cancel();
					}
				}

				if (++count >= range) {
					alive = false;
					this.cancel();
				}
			}

		}.runTaskTimer(AssemblyWar.getInstance(), 0L, 1L);
	}
}
