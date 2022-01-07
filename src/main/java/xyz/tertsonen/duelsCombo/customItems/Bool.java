package xyz.tertsonen.duelsCombo.customItems;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class Bool implements PersistentDataType<java.lang.Boolean, java.lang.Boolean> {

	public static Bool BOOL = new Bool();

	@Override
	public @NotNull Class<java.lang.Boolean> getPrimitiveType() {
		return java.lang.Boolean.class;
	}

	@Override
	public @NotNull Class<java.lang.Boolean> getComplexType() {
		return java.lang.Boolean.class;
	}

	@NotNull
	@Override
	public java.lang.Boolean toPrimitive(@NotNull java.lang.Boolean complex, @NotNull PersistentDataAdapterContext context) {
		return complex;
	}

	@NotNull
	@Override
	public java.lang.Boolean fromPrimitive(@NotNull java.lang.Boolean primitive, @NotNull PersistentDataAdapterContext context) {
		return primitive;
	}
}