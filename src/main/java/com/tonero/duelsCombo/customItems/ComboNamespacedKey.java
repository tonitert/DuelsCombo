package com.tonero.duelsCombo.customItems;

import com.tonero.duelsCombo.DuelsCombo;
import org.bukkit.NamespacedKey;

public enum ComboNamespacedKey {

	SHOOTER_BOW_KNOCKBACK("shooterBowKnockback"),
	SWITCH_POSITIONS_ON_HIT("switchPositionsOnHit");

	private final String key;

	private NamespacedKey nKey = null;

	public NamespacedKey getNamespacedKey(){
		if(nKey != null) return nKey;
		nKey = new NamespacedKey(DuelsCombo.getInstance(), key);
		return nKey;
	}

	ComboNamespacedKey(String key){
		this.key = key;
	}
}
