package seedu.commando.logic.commands;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import seedu.commando.commons.core.EventsCenter;
import seedu.commando.commons.core.Messages;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.logic.Logic;
import seedu.commando.testutil.EventsCollector;
import seedu.commando.testutil.ToDoBuilder;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.Assert.*;
import static seedu.commando.logic.LogicManagerTest.initLogic;
import static seedu.commando.testutil.TestHelper.*;

//@@author A0139697H
public class FinishCommandTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private Logic logic;
    private EventsCollector eventsCollector;
    private LocalDateTime now = LocalDateTime.now();

    @Before
    public void setUp() throws IOException {
        logic = initLogic();
        eventsCollector = new EventsCollector();
    }

    @After
    public void tearDown() {
        EventsCenter.clearSubscribers();
    }

    @Test
    public void execute_finishNoSuchIndex_error() {
        CommandResult result = logic.execute("delete 2");
        assertTrue(result.hasError());

        assertEquals(String.format(Messages.TODO_ITEM_INDEX_INVALID, 2), result.getFeedback());
    }

    @Test
    public void execute_finishInvalidIndex_error() {
        CommandResult result = logic.execute("finish 0");
        assertTrue(result.hasError());

        assertEquals(String.format(Messages.TODO_ITEM_INDEX_INVALID, 0), result.getFeedback());
    }
    
    @Test
    public void execute_finishInvalidIndex2_error() {
        CommandResult result = logic.execute("finish -1");
        assertTrue(result.hasError());

        assertEquals(String.format(Messages.TODO_ITEM_INDEX_INVALID, -1), result.getFeedback());
    }

    @Test
    public void execute_finishMissingIndex_error() {
        CommandResult result = logic.execute("finish missing index");
        assertTrue(result.hasError());

        assertEquals(Messages.MISSING_TODO_ITEM_INDEX + "\n" + Messages.getCommandFormatMessage("finish").get(), result.getFeedback());
    }

    @Test
    public void execute_finishInvalidFormat_error() {
        CommandResult result = logic.execute("finish 1 #troll");
        assertTrue(result.hasError());

        assertEquals(String.format(Messages.INVALID_COMMAND_FORMAT, FinishCommand.COMMAND_WORD)
                + "\n" + Messages.getCommandFormatMessage("finish").get(), result.getFeedback());
    }

    @Test
    public void execute_finish_finished() throws IllegalValueException {
        logic.execute("add title");
        logic.execute("add title2");

        eventsCollector.reset();
        assertFalse(wasToDoListChangedEventPosted(eventsCollector));

        assertToDoExists(logic,
            new ToDoBuilder("title2")
                .build());

        CommandResult result = logic.execute("finish 2");
        assertFalse(result.hasError());

        assertTrue(wasToDoListChangedEventPosted(eventsCollector));
        assertToDoExists(logic,
            new ToDoBuilder("title2")
                .build());
        assertToDoExists(logic,
            new ToDoBuilder("title")
                .build());
    }

    @Test
    public void execute_finishFinishedTask_error() throws IllegalValueException {
        logic.execute("add title");
        logic.execute("finish 1");

        eventsCollector.reset();

        CommandResult result = logic.execute("finish 1");
        assertTrue(result.hasError());
        assertFalse(wasToDoListChangedEventPosted(eventsCollector));
    }

    @Test
    public void execute_finishEvent_error() throws IllegalValueException {
        logic.execute("add event from today to tomorrow");

        eventsCollector.reset();

        CommandResult result = logic.execute("finish 1");
        assertTrue(result.hasError());
        assertFalse(wasToDoListChangedEventPosted(eventsCollector));
    }
    
    //@@author A0142230B
    @Test
    public void execute_finishMutipleTaskAndEvent_error() throws IllegalValueException {
        logic.execute("add title1");
        logic.execute("add title2 on 12/12");
        logic.execute("add title3");

        eventsCollector.reset();

        CommandResult result = logic.execute("finish 1-3");
        assertTrue(result.hasError());
        assertFalse(wasToDoListChangedEventPosted(eventsCollector));
        assertEquals(String.format(Messages.FINISH_COMMAND_CANNOT_FINISH_EVENT, "title2"), result.getFeedback());
    }
    
    @Test
    public void execute_deleteMutipleTasks_Finished() throws IllegalValueException {
        logic.execute("add title1 by 12/12/2016 9pm");
        logic.execute("add title2");
        logic.execute("add title3");

        eventsCollector.reset();

        logic.execute("finish 1-3");
        assertTrue(wasToDoListChangedEventPosted(eventsCollector));
        assertToDoExists(logic, new ToDoBuilder("title1").withDueDate(LocalDateTime.of(2016,12,12,21,0)).build());
        assertToDoExists(logic, new ToDoBuilder("title3").build());
        assertToDoExists(logic, new ToDoBuilder("title2").build());
    }
}