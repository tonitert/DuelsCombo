package xyz.tertsonen.duelsCombo.saveData;

import lombok.Setter;

public abstract class SaveCallback implements Runnable {

	@Setter
	Exception exception = null;


}
