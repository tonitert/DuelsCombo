package xyz.tertsonen.duelsCombo.customItems;

import org.bukkit.persistence.PersistentDataType;

/**
 * Wrapper for the PersistentDataType class to make an enum equivalent
 */
public enum DataType {
	BOOL(PersistentDataType.SHORT),
	INT(PersistentDataType.INTEGER),
	DOUBLE(PersistentDataType.DOUBLE),
	STRING(PersistentDataType.STRING);

	public final PersistentDataType dataType;

	DataType(PersistentDataType<?, ?> dataType) {
		this.dataType = dataType;
	}
}
