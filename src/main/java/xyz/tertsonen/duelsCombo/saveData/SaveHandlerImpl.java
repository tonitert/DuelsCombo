package xyz.tertsonen.duelsCombo.saveData;

import org.bukkit.scheduler.BukkitRunnable;
import xyz.tertsonen.duelsCombo.DuelsCombo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SaveHandlerImpl implements SaveHandler{

	@Override
	public void saveBytes(byte[] bytes, Path path, SaveCallback saveCallback) {
		new BukkitRunnable() {
			@Override
			public void run() {
				try {
					Files.write(path, bytes);
				} catch (IOException e) {
					saveCallback.setException(e);
				}
				saveCallback.run();
			}
		}.runTaskAsynchronously(DuelsCombo.getInstance());

	}
}
