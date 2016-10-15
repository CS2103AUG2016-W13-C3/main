package seedu.commando.logic.commands;

import seedu.commando.commons.core.EventsCenter;
import seedu.commando.commons.core.Messages;
import seedu.commando.commons.core.UnmodifiableObservableList;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.model.Model;
import seedu.commando.model.ToDoListChange;
import seedu.commando.model.todo.ReadOnlyToDo;
import seedu.commando.model.todo.ToDo;
import sun.plugin2.message.Message;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Marks a to-do item as done
 */
public class FinishCommand extends Command {

    public static final String COMMAND_WORD = "finish";

    public final int toDoIndex;

    public FinishCommand(int toDoIndex) {
        this.toDoIndex = toDoIndex;
    }

    @Override
    public CommandResult execute(List<ReadOnlyToDo> toDoAtIndices, Model model, EventsCenter eventsCenter) {
        assert model != null;
        assert toDoAtIndices != null;

        Optional<ReadOnlyToDo> toDoToFinish = getToDoAtIndex(toDoAtIndices, toDoIndex);

        if (!toDoToFinish.isPresent()) {
            return new CommandResult(String.format(Messages.MESSAGE_TODO_ITEM_INDEX_INVALID, toDoIndex), true);
        }

        if (toDoToFinish.get().isFinished()) {
            return new CommandResult(String.format(Messages.MESSAGE_TODO_ALREADY_FINISHED, toDoToFinish.get().getTitle().toString()), true);
        }

        // Mark as finished
        ToDo finishedToDo = new ToDo(toDoToFinish.get());
        finishedToDo.setIsFinished(true);

        try {
            model.changeToDoList(new ToDoListChange(
                Collections.singletonList(finishedToDo),
                Collections.singletonList(toDoToFinish.get())
            ));
        } catch (IllegalValueException exception) {
            return new CommandResult(exception.getMessage(), true);
        }

        return new CommandResult(String.format(Messages.MESSAGE_TODO_FINISHED, toDoToFinish.get().getTitle().toString()));
    }

}
