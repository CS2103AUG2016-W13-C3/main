package seedu.commando.logic.commands;

import seedu.commando.commons.core.Messages;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.commons.util.CollectionUtil;
import seedu.commando.model.ui.UiToDo;
import seedu.commando.model.Model;
import seedu.commando.model.ToDoListChange;
import seedu.commando.model.todo.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Edits a to-do in the current to-do list
 * Public fields are initially null and are optional parameters for the command
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    private final int toDoIndex;
    public String title;
    public LocalDateTime dateRangeStart;
    public LocalDateTime dateRangeEnd;
    public LocalDateTime dueDate;
    public Set<String> tags = null;

    public EditCommand(int toDoIndex) {
        this.toDoIndex = toDoIndex;
    }

    public CommandResult execute()
        throws IllegalValueException, NoModelException {
        Model model = getModel();

        Optional<UiToDo> toDoToEdit = model.getUiToDoAtIndex(toDoIndex);

        if (!toDoToEdit.isPresent()) {
            return new CommandResult(String.format(Messages.TODO_ITEM_INDEX_INVALID, toDoIndex), true);
        }

        // Copy original to-do
        ToDo newToDo = new ToDo(toDoToEdit.get());

        // Set fields if exist
        if (title != null) {
            newToDo.setTitle(new Title(title));
        }
        if (dueDate != null) {
            newToDo.setDueDate(new DueDate(dueDate));
        }

        if (dateRangeStart != null && dateRangeEnd != null) {
            newToDo.setDateRange(new DateRange(dateRangeStart, dateRangeEnd));
        } else if (dateRangeEnd != null) {
            throw new IllegalValueException(Messages.MISSING_TODO_DATERANGE_START);
        } else if (dateRangeStart != null) {
            throw new IllegalValueException(Messages.MISSING_TODO_DATERANGE_END);
        }

        if (tags != null) {
            newToDo.setTags(tags.stream().map(Tag::new).collect(Collectors.toSet()));
        }

        // Check if to-do has changed
        if (newToDo.isSameStateAs(toDoToEdit.get())) {
            return new CommandResult(Messages.TODO_NO_EDITS, true);
        }

        // Ensure to-do doesn't have both duedate and daterange
        if (newToDo.getDateRange().isPresent() && newToDo.getDueDate().isPresent()) {
            throw new IllegalValueException(Messages.TODO_CANNOT_HAVE_DUEDATE_AND_DATERANGE);
        }

        model.changeToDoList(new ToDoListChange(
            Collections.singletonList(newToDo),
            Collections.singletonList(toDoToEdit.get())
        ));

        return new CommandResult(String.format(Messages.TODO_EDITED, newToDo.getTitle().toString()));
    }
}