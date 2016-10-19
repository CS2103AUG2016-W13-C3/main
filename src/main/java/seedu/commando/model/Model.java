package seedu.commando.model;

import seedu.commando.commons.core.UnmodifiableObservableList;
import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.model.todo.*;
import seedu.commando.model.ui.UiToDo;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * The API of the Model component.
 */
public interface Model {
    /** Returns the to-do list */
    ReadOnlyToDoList getToDoList();

    /** Changes the to-do list */
    void changeToDoList(ToDoListChange change) throws IllegalValueException;

    /** Undos the last change to the to-do list, returns true if successful */
    boolean undoToDoList();

    /** Redos the last undo to the to-do list, returns true if successful */
    boolean redoToDoList();

    /**
     * Returns observable list of UI events happening today
     * Events are in chronological order, with those finished at the bottom
     * */
    UnmodifiableObservableList<UiToDo> getUiEventsToday();

    /**
     * Returns observable list of UI events happening after today
     * Events are in chronological order, with those finished at the bottom
     * */
    UnmodifiableObservableList<UiToDo> getUiEventsUpcoming();

    /**
     *  Return observable list of UI tasks
     *  Tasks are in chronological order, with those finished at the bottom,
     *  with those with DueDate on top
     * */
    UnmodifiableObservableList<UiToDo> getUiTasks();

    /**
     *  Gets the UI to-do with {@link UiToDo#getIndex()} == {@param toDoIndex}
     * */
    Optional<UiToDo> getUiToDoAtIndex(int index);

    /**
     * Clears the filter on the UI to-do list
     */
    void clearUiToDoListFilter();

    /**
     * Sets a filter on the UI to-do list
     */
    void setUiToDoListFilter(Set<String> keywords, Set<String> tags);
}
