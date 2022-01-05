package xyz.tertsonen.duelsCombo.saveData;

import java.nio.file.Path;

public interface SaveHandler {
	void saveBytes(byte[] bytes, Path path, SaveCallback saveCallback);
}
