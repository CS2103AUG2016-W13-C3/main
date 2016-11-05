package seedu.commando.ui;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import seedu.commando.commons.core.DateTimePrettifier;
import seedu.commando.model.todo.ReadOnlyToDo;
import seedu.commando.model.todo.Recurrence;
import seedu.commando.model.todo.Tag;

public class TaskCard extends UiPart {

    private static final String FXML = "TaskCard.fxml";
    private boolean isFinished;

    @FXML
    private HBox taskPane;
    @FXML
    private FlowPane tagsPane;
    @FXML
    private VBox datePane;
    @FXML
    private Label titleLabel;
    @FXML
    private Label indexLabel;
    @FXML
    private Label dueLabel;
    @FXML
    private Label tagsLabel;
    @FXML
    private Label recurrenceLabel;

    private ReadOnlyToDo toDo;
    private int index;
    private boolean containsTags;
    private boolean containsRecurrence;

    public TaskCard() {
    }

    public static TaskCard load(ReadOnlyToDo toDo, int index) {
        TaskCard card = new TaskCard();
        card.toDo = toDo;
        card.index = index;

        return UiPartLoader.loadUiPart(card);
    }

    @FXML
    public void initialize() {
        titleLabel.setText(toDo.getTitle().value);
        indexLabel.setText(String.valueOf(index));

        setDateTimeLabel();
        setRecurrenceLabel();
        setTagLabel();
    }

    // @@author A0139080J
    private void setTagLabel() {
        if (!toDo.getTags().isEmpty()) {
            for (Tag tag : toDo.getTags()) {
                Label label = new Label();
                label.setText("#" + tag.value);
                label.setId("tagsLabel");
                label.setMaxWidth(100);
                label.getStyleClass().add("cell_big_label");
                label.setAlignment(Pos.CENTER);
                label.setPadding(new Insets(0, 3, 0, 3));
                
                tagsPane.getChildren().add(label);
            }
            containsTags = true;
        } else {
            containsTags = false;
            tagsPane.setManaged(false);
        }
    }

    private void setRecurrenceLabel() {
        if (toDo.getDueDate().isPresent() && toDo.getDueDate().get().recurrence != Recurrence.None) {
            recurrenceLabel.setText(toDo.getDueDate().get().recurrence.toString());
            containsRecurrence = true;
        } else {
            recurrenceLabel.setManaged(false);
            containsRecurrence = false;
        }
    }

    /**
     * Sets value for the date time label and colours it according to the
     * proximity of the date to today with red being the closest, and green
     * being the furthest
     */
    private void setDateTimeLabel() {
        if (toDo.getDueDate().isPresent()) {
            final LocalDateTime due = toDo.getDueDate().get().value;
            final long dayDifference = ChronoUnit.DAYS.between(LocalDateTime.now(), due);

            dueLabel.setText("by " + DateTimePrettifier.prettifyDateTime(due));
            dueLabel.setStyle("-fx-text-fill: " + ToDoCardStyleManager.getDateProximityGreen((int) dayDifference));
        } else {
            dueLabel.setManaged(false);
            datePane.setManaged(false);
        }
    }

    /*
     * Different CSS styles for different states
     */
    protected HBox getLayoutState(boolean isNew, boolean isFinished) {
        this.isFinished = isFinished;
        if (isNew) {
            setRecentlyModifiedState();
        }
        if (isFinished) {
            setFinishedState();
        }
        return taskPane;
    }

    /**
     * Every recently modified event will have a red border This includes
     * modification via undo, edit, add
     */
    private void setRecentlyModifiedState() {
        ToDoCardStyleManager.addStyleAll("recently-modified", taskPane);
    }

    /**
     * Tints a finished event gray
     */
    private void setFinishedState() {
        ToDoCardStyleManager.addStyleAll("finished", taskPane, datePane, indexLabel);
    }

    /**
     * Tints a hovered event a slight gray
     */
    @FXML
    private void activateHoverState() {
        if (!isFinished) {
            ToDoCardStyleManager.addStyleAll("hover", taskPane, indexLabel);
        }
    }

    @FXML
    private void deactivateHoverState() {
        if (!isFinished) {
            ToDoCardStyleManager.removeStyleAll("hover", taskPane, indexLabel);
        }
    }
    // @@author

    @Override
    public void setNode(Node node) {
        taskPane = (HBox) node;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }
}
