package guitests;

import com.google.common.collect.Sets;
import org.junit.Test;

import guitests.guihandles.ToDoListPanelHandle;
import seedu.commando.commons.core.Messages;
import seedu.commando.model.todo.ToDo;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertTrue;

//@@author A0122001M

public class RecallCommandTest extends CommanDoGuiTest {

    @Test
    public void recallCommand_nonEmptyList() {
        //recall when no finished items
        commandBox.runCommand("recall");
        assertListSize(0);
        
        //go back to current list
        commandBox.runCommand("find");
    }

    @Test
    public void recallCommand_emptyList() {
        //recall on empty list
        commandBox.runCommand("clear");
        assertRecallResult("recall title", Sets.newHashSet("title"), Collections.emptySet()); //no results
    }

    @Test
    public void recallCommand_invalidCommand_reportErrorMessage() {
        commandBox.runCommand("recalltest");
        assertResultMessage(String.format(Messages.UNKNOWN_COMMAND, "recalltest"));
    }
    
    /**
     * Runs the recall command to filter ToDoList according to given keywords
     * 
     * @param command        The recall command to be executed.
     * @param keywords       Keywords that are used
     * @param tags           Tags that are used
     * @param expectedHits   The expected result list after filtering.
     */
    private void assertRecallResult(String command, Set<String> keywords, Set<String> tags, ToDo... expectedHits ) {
        commandBox.runCommand(command);
        assertListSize(expectedHits.length);  //number of expected todos = number of listed todos

        if (expectedHits.length == 0) {
            assertResultMessage(String.format(Messages.RECALL_COMMAND_NO_TODOS, getSearchString(keywords, tags)));
        } else {
            assertResultMessage(String.format(Messages.RECALL_COMMAND, getSearchString(keywords, tags)));
        }

        assertTrue(ToDoListPanelHandle.isBothListMatching(eventListPanel, taskListPanel, expectedHits));
    }
    
    /**
     * Get the result message string for find command
     * 
     * @param keywords
     * @param tags
     * @return expected message string of indices
     */
    private String getSearchString(Set<String> keywords, Set<String> tags) {
        Stream<String> keywordsStream = new TreeSet<>(keywords).stream();
        Stream<String> tagsStream = new TreeSet<>(tags).stream();

        return "[" + Stream.concat(keywordsStream, tagsStream).collect(Collectors.joining(", ")) + "]";
    }
}
