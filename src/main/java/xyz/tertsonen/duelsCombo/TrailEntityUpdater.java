package xyz.tertsonen.duelsCombo;

import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TrailEntityUpdater {
	private final List<TrailEntity> entities = new ArrayList<>();
	private BukkitTask task;

	private class UpdateTask extends BukkitRunnable {
		@Override
		public void run() {
			boolean foundAlive = false;
			for (int i = 0; i < entities.size(); i++) {
				TrailEntity ent = entities.get(i);
				if(ent.getEntity().isDead()) {
					entities.remove(i);
					--i;
					continue;
				}
				foundAlive = true;
				Location current = ent.getLastLocation();
				Vector moveDir = ent.getEntity().getLocation().toVector().subtract(current.toVector());
				double dist = moveDir.length();
				double trailParticleDistance = ent.getTrailParticleDistance();
				moveDir.normalize().multiply(trailParticleDistance);
				for (double currDist = 0; currDist <= dist; currDist += trailParticleDistance) {
					current.add(moveDir);
					Objects.requireNonNull(current.getWorld()).spawnParticle(ent.getParticle(), current, 1);
				}
				ent.setLastLocation(current);
			}
			if(!foundAlive) {
				task.cancel();
				task = null;
			}
		}
	}

	public void addEntity(TrailEntity trailEntity){
		entities.add(trailEntity);
		if(task == null) task = new UpdateTask().runTaskTimer(DuelsCombo.getInstance(), 0L, 1L);
	}
}
