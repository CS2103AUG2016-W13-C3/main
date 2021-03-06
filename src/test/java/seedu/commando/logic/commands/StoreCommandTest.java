package seedu.commando.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import org.controlsfx.tools.Platform;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import seedu.commando.commons.core.EventsCenter;
import seedu.commando.commons.core.Messages;
import seedu.commando.commons.events.logic.ToDoListFilePathChangeRequestEvent;
import seedu.commando.commons.events.storage.ToDoListSavedEvent;
import seedu.commando.logic.Logic;
import seedu.commando.logic.LogicManager;
import seedu.commando.model.Model;
import seedu.commando.model.ModelManager;
import seedu.commando.model.UserPrefs;
import seedu.commando.storage.Storage;
import seedu.commando.storage.StorageManager;
import seedu.commando.testutil.EventsCollector;
//@@author A0142230B
public class StoreCommandTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private Model model;
    private Logic logic;
    private Storage storage;
    private EventsCollector eventsCollector;
    private File toDoListFile;

    @Before
    public void setUp() throws IOException {
        model = new ModelManager();

        toDoListFile = folder.newFile();
        File userPrefsFile = folder.newFile();
        storage = new StorageManager(
            toDoListFile.getAbsolutePath(),
            userPrefsFile.getAbsolutePath()
        );
        logic = new LogicManager(model, storage, new UserPrefs());

        eventsCollector = new EventsCollector();
    }

    @After
    public void tearDown() {
        EventsCenter.clearSubscribers();
    }

    @Test
    public void execute_storeEmptyPath_error() {
        CommandResult result = logic.execute("store");
        assertTrue(result.hasError());
        assertEquals(Messages.MISSING_STORE_PATH
                + "\n" + Messages.getCommandFormatMessage("store").get(), result.getFeedback());
        result = logic.execute("store    ");
        assertTrue(result.hasError());
        assertEquals(Messages.MISSING_STORE_PATH
                + "\n" + Messages.getCommandFormatMessage("store").get(), result.getFeedback());
    }

    @Test
    public void execute_storeInvalidPath_error() {
        CommandResult result = logic.execute("store 2\\");
        assertTrue(result.hasError());
        assertEquals(Messages.MISSING_STORE_FILE, result.getFeedback());
        result = logic.execute("store awe@#$\\");
        assertTrue(result.hasError());
        assertEquals(Messages.MISSING_STORE_FILE, result.getFeedback());
    }
    
    @Test
    public void execute_storeValidPath_filePathChanged() throws IOException {
        String storeFilePath = folder.getRoot() + "/test.xml";

        CommandResult result = logic.execute("store " + storeFilePath);
        assertFalse(result.hasError());
        assertTrue(storage.getToDoListFilePath().equals(storeFilePath));
    }

    @Test
    public void execute_storeValidPath_fileSaved() throws IOException, InterruptedException {
        String storeFilePath = folder.getRoot() + "/test.xml";

        CommandResult result = logic.execute("store " + storeFilePath);
        assertFalse(result.hasError());
        assertEquals(String.format(Messages.STORE_COMMAND, storeFilePath), result.getFeedback());
        assertTrue(eventsCollector.hasCollectedEvent(ToDoListFilePathChangeRequestEvent.class));

        // Storage should save to-do list in <= 1s
        Thread.sleep(1000);
        assertTrue(eventsCollector.hasCollectedEvent(ToDoListSavedEvent.class));
    }

    @Test
    public void execute_storeToExistingFile_error() throws IOException, InterruptedException {
        File file = folder.newFile();

        CommandResult result = logic.execute("store " + file.getPath());
        assertTrue(result.hasError());
        assertEquals(String.format(Messages.STORE_COMMAND_FILE_EXIST, file.getPath()), result.getFeedback());
    }

    @Test
    public void execute_storeToExistingFileButOverride_fileSaved() throws IOException, InterruptedException {
        String storeFilePath = folder.newFile().getPath();

        CommandResult result = logic.execute("store " + storeFilePath + " override");
        assertFalse(result.hasError());
        assertEquals(String.format(Messages.STORE_COMMAND, storeFilePath), result.getFeedback());
        assertTrue(eventsCollector.hasCollectedEvent(ToDoListFilePathChangeRequestEvent.class));

        // Storage should save to-do list in <= 1s
        Thread.sleep(1000);
        assertTrue(eventsCollector.hasCollectedEvent(ToDoListSavedEvent.class));
        assertTrue(Arrays.equals(Files.readAllBytes(toDoListFile.toPath()), Files.readAllBytes(Paths.get(storeFilePath))));
    }

}
