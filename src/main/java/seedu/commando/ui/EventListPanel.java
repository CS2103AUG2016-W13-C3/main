package seedu.commando.ui;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seedu.commando.commons.core.LogsCenter;
import seedu.commando.commons.events.ui.ToDoListPanelSelectionChangedEvent;
import seedu.commando.logic.UiToDo;
import seedu.commando.model.todo.ReadOnlyToDo;

import java.util.logging.Logger;

/**
 * Panel containing the list of to-dos
 */
public class EventListPanel extends UiPart {
    private final Logger logger = LogsCenter.getLogger(EventListPanel.class);
    private static final String FXML = "eventListPanel.fxml";
    private VBox panel;
    private AnchorPane placeHolderPane;

    @FXML
    private ListView<UiToDo> eventListView;

    public EventListPanel() {
        super();
    }

    @Override
    public void setNode(Node node) {
        panel = (VBox) node;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }

    @Override
    public void setPlaceholder(AnchorPane pane) {
        this.placeHolderPane = pane;
    }

    public static EventListPanel load(Stage primaryStage, AnchorPane eventListPlaceholder,
                                     ObservableList<UiToDo> list) {
        EventListPanel eventListPanel =
                UiPartLoader.loadUiPart(primaryStage, eventListPlaceholder, new EventListPanel());
        eventListPanel.configure(list);
        return eventListPanel;
    }

    private void configure(ObservableList<UiToDo> toDos) {
        setConnections(toDos);
        addToPlaceholder();
    }

    private void setConnections(ObservableList<UiToDo> list) {
        eventListView.setItems(list);
        eventListView.setCellFactory(listView -> new ToDoListViewCell());
        setEventHandlerForSelectionChangeEvent();
    }

    private void addToPlaceholder() {
        SplitPane.setResizableWithParent(placeHolderPane, false);
        placeHolderPane.getChildren().add(panel);
    }

    private void setEventHandlerForSelectionChangeEvent() {
        eventListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                logger.fine("Selection in to-do list panel changed to : '" + newValue + "'");
                raise(new ToDoListPanelSelectionChangedEvent(newValue));
            }
        });
    }

    public void scrollTo(int index) {
        Platform.runLater(() -> {
            eventListView.scrollTo(index);
            eventListView.getSelectionModel().clearAndSelect(index);
        });
    }

    class ToDoListViewCell extends ListCell<UiToDo> {

        public ToDoListViewCell() {
        }

        @Override
        protected void updateItem(UiToDo toDo, boolean empty) {
            super.updateItem(toDo, empty);

            if (empty || toDo == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(EventCard.load(toDo, toDo.getIndex()).getLayout());
            }
        }
    }
}
