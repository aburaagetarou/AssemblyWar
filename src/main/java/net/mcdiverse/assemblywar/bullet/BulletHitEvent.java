package net.mcdiverse.assemblywar.bullet;

import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Objects;

public class BulletHitEvent extends Event {

	private static final HandlerList handlers = new HandlerList();

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	Entity victim;
	Bullet bullet;
	private boolean isCancelled;

	public BulletHitEvent(Entity victim, Bullet bullet) {
		this.victim = victim;
		this.bullet = bullet;
		this.isCancelled = false;
	}

	public boolean isCancelled() {
		return isCancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.isCancelled = cancelled;
	}

	public Entity getVictim() {
		return victim;
	}

	public void setVictim(Entity victim) {
		this.victim = victim;
	}

	public Bullet getBullet() {
		return bullet;
	}

	public void setBullet(Bullet bullet) {
		this.bullet = bullet;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		BulletHitEvent that = (BulletHitEvent) o;
		return Objects.equals(victim, that.victim) &&
				Objects.equals(bullet, that.bullet);
	}
}
