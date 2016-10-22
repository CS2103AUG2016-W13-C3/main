package guitests.utils;

import java.time.LocalDateTime;

import seedu.commando.commons.exceptions.IllegalValueException;
import seedu.commando.model.todo.*;
import seedu.commando.testutil.ToDoBuilder;

/**
 *
 */
public class TypicalTestToDos {

    public static ToDo toDoItem1, toDoItem2, toDoItem3, toDoItem4, toDoItem5;

    public TypicalTestToDos() {
        try {

            toDoItem1 = new ToDoBuilder("title").build();
            toDoItem2 = new ToDoBuilder("title 2").withTags("tag1", "tag2")
                    .withDateRange(LocalDateTime.of(2016, 11, 1, 20, 1), LocalDateTime.of(2016, 12, 1, 20, 1))
                    .isFinished(true).build();
            toDoItem3 = new ToDoBuilder("title 3").withTags("tag1", "tag3")
                    .withDateRange(LocalDateTime.of(2017, 1, 1, 20, 1), LocalDateTime.of(2017, 2, 1, 20, 1))
                    .isFinished(true).build();
            toDoItem4 = new ToDoBuilder("title 4").withDueDate(LocalDateTime.of(2016, 12, 1, 20, 1)).isFinished(true)
                    .build();
            toDoItem5 = new ToDoBuilder("title 5").withTags("tag1", "tag2")
                    .withDueDate(LocalDateTime.of(2017, 11, 1, 20, 1)).isFinished(true).build();

        } catch (IllegalValueException e) {
            e.printStackTrace();
            assert false : "not possible";
        }
    }

    public static void loadToDoListWithSampleData(ToDoList todoList) {
        try {
            todoList
                .add(new ToDo(toDoItem1))
                .add(new ToDo(toDoItem2))
                .add(new ToDo(toDoItem3))
                .add(new ToDo(toDoItem4))
                .add(new ToDo(toDoItem5));
        } catch (IllegalValueException e) {
            assert false : "impossible";
        }

    }

    public ToDo[] getTypicalToDos() {
        return new ToDo[] { toDoItem1, toDoItem2, toDoItem3, toDoItem4, toDoItem5 };
    }

    public ToDoList getTypicalToDoList() {
        ToDoList todoList = new ToDoList();
        loadToDoListWithSampleData(todoList);
        return todoList;
    }
    
    public ToDo[] getEmptyToDos(){
        return new ToDo[] {};
    }
}
