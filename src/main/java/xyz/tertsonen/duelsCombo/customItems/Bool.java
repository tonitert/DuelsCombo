package xyz.tertsonen.duelsCombo.customItems;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class Bool implements PersistentDataType<Short, java.lang.Boolean> {

	public static Bool BOOL = new Bool();

	@Override
	public @NotNull Class<Short> getPrimitiveType() {
		return Short.class;
	}

	@Override
	public @NotNull Class<java.lang.Boolean> getComplexType() {
		return java.lang.Boolean.class;
	}

	@NotNull
	@Override
	public Short toPrimitive(@NotNull java.lang.Boolean complex, @NotNull PersistentDataAdapterContext context) {
		return complex ? (short) 1 : (short) 0;
	}

	@NotNull
	@Override
	public Boolean fromPrimitive(@NotNull Short primitive, @NotNull PersistentDataAdapterContext context) {
		return primitive == 1;
	}
}