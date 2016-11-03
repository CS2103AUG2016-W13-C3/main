package seedu.commando.logic.commands;

import seedu.commando.commons.core.Messages;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.model.Model;
import seedu.commando.model.todo.ToDoListChange;
import seedu.commando.model.todo.Tag;
import seedu.commando.model.todo.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

//@@author A0139697H

/**
 * Adds a to-do to the to-do list.
 */
public class AddCommand extends Command {
    public static final String COMMAND_WORD = "add";

    private final Title title;
    public DateRange dateRange;
    public DueDate dueDate;
    public Set<Tag> tags = Collections.emptySet();

    public AddCommand(Title title) {
        assert title != null;

        this.title = title;
    }

    @Override
    public CommandResult execute() throws NoModelException {
        Model model = getModel();

        // Create the to-do to add
        ToDo toDo = new ToDo(title);

        // Set fields if exist
        if (dueDate != null) {
            toDo.setDueDate(dueDate);
        }

        if (dateRange != null) {
            toDo.setDateRange(dateRange);
        }

        if (tags != null) {
            toDo.setTags(tags);
        }

        // Ensure to-do doesn't have both duedate and daterange
        if (toDo.getDateRange().isPresent() && toDo.getDueDate().isPresent()) {
            return new CommandResult(Messages.TODO_CANNOT_HAVE_DUEDATE_AND_DATERANGE, true);
        }

        try {
            model.changeToDoList(new ToDoListChange(
                new ToDoList().add(toDo),
                new ToDoList()
            ));
        } catch (IllegalValueException exception) {
            return new CommandResult(exception.getMessage(), true);
        }

        String feedback = String.format(Messages.ADD_COMMAND, toDo.getTitle().toString());

        // If event already over, warn user
        if (toDo.getDateRange().isPresent()
            && toDo.getDateRange().get().endDate.isBefore(LocalDateTime.now())) {
            feedback += "\n" + Messages.ADD_COMMAND_EVENT_OVER;
        }

        return new CommandResult(feedback);
    }
}
