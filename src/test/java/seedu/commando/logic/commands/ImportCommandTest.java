package seedu.commando.logic.commands;

import static org.junit.Assert.*;
import static seedu.commando.testutil.TestHelper.wasToDoListChangedEventPosted;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import seedu.commando.commons.core.EventsCenter;
import seedu.commando.commons.core.Messages;
import seedu.commando.logic.Logic;
import seedu.commando.logic.LogicManager;
import seedu.commando.model.Model;
import seedu.commando.model.ModelManager;
import seedu.commando.model.UserPrefs;
import seedu.commando.storage.StorageManager;
import seedu.commando.testutil.EventsCollector;

public class ImportCommandTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private Logic logic;
    private EventsCollector eventsCollector;
    private File toDoListFile;

    @Before
    public void setup() throws IOException {
        toDoListFile = folder.newFile();
        File userPrefsFile  = folder.newFile();
        Model model = new ModelManager();

        logic = new LogicManager(model, new StorageManager(
            toDoListFile.getAbsolutePath(),
            userPrefsFile.getAbsolutePath()
        ), new UserPrefs());

        eventsCollector = new EventsCollector();
    }

    @After
    public void teardown() {
        EventsCenter.clearSubscribers();
    }

    @Test
    public void execute_import_emptyPath() {
        CommandResult result = logic.execute("import");
        assertTrue(result.hasError());
        assertEquals(Messages.MISSING_IMPORT_PATH, result.getFeedback());
        result = logic.execute("import    ");
        assertTrue(result.hasError());
        assertEquals(Messages.MISSING_IMPORT_PATH, result.getFeedback());
    }

    @Test
    public void execute_import_invalidPath() {
        CommandResult result = logic.execute("import 2\\");
        assertTrue(result.hasError());
        assertEquals(Messages.MISSING_IMPORT_FILE, result.getFeedback());
        result = logic.execute("import awe@#$\\");
        assertTrue(result.hasError());
        assertEquals(Messages.MISSING_IMPORT_FILE, result.getFeedback());
        result = logic.execute("import this cant be there.XMl");
        assertTrue(result.hasError());
        assertEquals(Messages.IMPORT_COMMAND_FILE_NOT_EXIST, result.getFeedback());
    }

    @Test
    public void execute_import_validPath() throws IOException {
        logic.execute("add test1");
        logic.execute("add test2");
        logic.execute("export test.xml");
        logic.execute("clear");
        assertTrue(wasToDoListChangedEventPosted(eventsCollector));
        assertTrue(logic.getToDoList().getToDos().size() == 0);

        CommandResult result = logic.execute("import test.xml");
        assertFalse(result.hasError());
        assertTrue(wasToDoListChangedEventPosted(eventsCollector));
        assertTrue(logic.getToDoList().getToDos().size() == 2);

        Files.delete(Paths.get("test.xml"));
    }
}
