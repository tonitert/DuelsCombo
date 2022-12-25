package xyz.tertsonen.duelsCombo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;

@AllArgsConstructor
@Getter
public class TrailEntity {
	private final Entity entity;
	private final Particle particle;
	@Setter
	private Location lastLocation;
	private final double trailParticleDistance;
}
