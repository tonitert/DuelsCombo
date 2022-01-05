import mock.MockKitManager;
import org.junit.Assert;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import xyz.tertsonen.duelsCombo.saveData.SaveDataManager;
import xyz.tertsonen.duelsCombo.saveData.SaveHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

public class SaveDataManagerTest {
	private final SaveHandler mockSaveHandler = (bytes, path, saveCallback) -> {
		try{
			Files.write(path, bytes);
		}
		catch(IOException e){
			saveCallback.setException(e);
		}
		saveCallback.run();
	};

	@Test
	public void saveDataManagerNotCreatedIsNullTest() throws IOException {
		TemporaryFolder dataFolder = new TemporaryFolder();
		dataFolder.create();
		MockKitManager mockKitManager = new MockKitManager();
		mockKitManager.create("test");
		SaveDataManager saveDataManager = new SaveDataManager(dataFolder.getRoot(), mockKitManager, mockSaveHandler);
		Assert.assertNull(saveDataManager.getKit("test"));
	}

	@Test
	public void saveDataManagerNotCreatedIsNullTest2() throws IOException {
		TemporaryFolder dataFolder = new TemporaryFolder();
		dataFolder.create();
		MockKitManager mockKitManager = new MockKitManager();
		SaveDataManager saveDataManager = new SaveDataManager(dataFolder.getRoot(), mockKitManager, mockSaveHandler);
		Assert.assertNull(saveDataManager.getKit("test"));
	}

	@Test
	public void saveDataManagerCreateKitDataTest() throws IOException {
		TemporaryFolder dataFolder = new TemporaryFolder();
		dataFolder.create();
		MockKitManager mockKitManager = new MockKitManager();
		mockKitManager.create("test");
		SaveDataManager saveDataManager = new SaveDataManager(dataFolder.getRoot(), mockKitManager, mockSaveHandler);
		Assert.assertNotNull(saveDataManager.getOrCreate("test"));
		Assert.assertEquals(Objects.requireNonNull(saveDataManager.getKit("test")).getKitName(), "test");
	}

}
