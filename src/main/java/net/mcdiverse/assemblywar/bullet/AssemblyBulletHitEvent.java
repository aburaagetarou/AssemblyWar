package net.mcdiverse.assemblywar.bullet;

import net.mcdiverse.assemblywar.Assembly;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Objects;

public class AssemblyBulletHitEvent extends Event {

	private static final HandlerList handlers = new HandlerList();

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	Assembly victim;
	Bullet bullet;
	private boolean isCancelled;

	public AssemblyBulletHitEvent(Assembly victim, Bullet bullet) {
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

	public Assembly getVictim() {
		return victim;
	}

	public void setVictim(Assembly victim) {
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
		AssemblyBulletHitEvent that = (AssemblyBulletHitEvent) o;
		return Objects.equals(victim, that.victim) &&
				Objects.equals(bullet, that.bullet);
	}
}
