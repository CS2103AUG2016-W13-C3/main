package seedu.commando.model.todo;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableStringValue;
import seedu.commando.commons.exceptions.IllegalValueException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

//@@author A0122001M

/**
 * Represents a to-do.
 */
public class ToDo implements ReadOnlyToDo {
    private Title title;
    private DueDate dueDate;
    private DateRange dateRange;
    private Set<Tag> tags;
    private LocalDateTime dateCreated;
    private StringProperty value = new ReadOnlyStringWrapper();

    // null if to-do is not finished
    private LocalDateTime dateFinished;

    /**
     * Constructs a to-do with title of {@param title}.
     * Asserts that title is non-null.
     */
    public ToDo(Title title) {
        assert title != null;

        this.title = title;
        dateCreated = LocalDateTime.now();
        updateValue();
    }

    /**
     * Copy constructor
     */
    public ToDo(ReadOnlyToDo toDo) {
        assert toDo != null;

        this.title = new Title(toDo.getTitle());
        this.dateCreated = toDo.getDateCreated();

        if (toDo.getDueDate().isPresent()) {
            this.dueDate = new DueDate(toDo.getDueDate().get());
        }

        if (toDo.getDateRange().isPresent()) {
            this.dateRange = new DateRange(toDo.getDateRange().get());
        }

        if (toDo.getTags().size() > 0) {
            this.tags = toDo.getTags().stream().map(Tag::new).collect(Collectors.toSet());
        }

        if (toDo.getDateFinished().isPresent()) {
            this.dateFinished = toDo.getDateFinished().get();
        }

        updateValue();
    }

    /**
     * Sets the title of the to-do, non-null.
     *
     * @param title title to set
     * @return current to-do, for method chaining
     */
    public ToDo setTitle(Title title) {
        assert title != null;

        this.title = title;
        updateValue();

        return this;
    }

    /**
     * Sets the due date of the to-do, non-null.
     *
     * @param dueDate due date to set
     * @return current to-do, for method chaining
     */
    public ToDo setDueDate(DueDate dueDate) {
        assert dueDate != null;

        this.dueDate = dueDate;
        updateValue();

        return this;
    }

    /**
     * Sets the date range of the to-do, non-null.
     *
     * @param dateRange date range to set
     * @return current to-do, for method chaining
     */
    public ToDo setDateRange(DateRange dateRange) {
        assert dateRange != null;

        this.dateRange = dateRange;
        updateValue();

        return this;
    }

    /**
     * Clears any due date or date range set.
     *
     * @return current to-do, for method chaining
     */
    public ToDo clearTimeConstraint() {
        dateRange = null;
        dueDate = null;
        updateValue();

        return this;
    }

    /**
     * Replaces the set of tags of the to-do, non-null.
     *
     * @param tags the set of tags to replace the current, which will be copied
     * @return current to-do, for method chaining
     */
    public ToDo setTags(Set<Tag> tags) {
        assert tags != null;

        this.tags = tags.stream().collect(Collectors.toSet());
        updateValue();

        return this;
    }

    /**
     * Sets the date finished for the to-do.
     *
     * If there is a recurring date range, this won't have any effect,
     * since the recurrence will not allow the to-do to finish.
     *
     * If there is a non-recurring date range, this won't have any effect, since
     * the date finished will be automatically the end of the date range.
     *
     * If there is a recurring due date, this will advance the due date
     * by the recurrence interval until it's after {@param dateFinished}
     *
     * @param dateFinished date finished to set
     * @return current to-do, for method chaining
     */
    public ToDo setDateFinished(LocalDateTime dateFinished) {
        if (dateRange != null) {
            return this;
        }

        if (dueDate != null && dueDate.recurrence != Recurrence.None) {
            advanceDueDate(dateFinished);
        } else {
            this.dateFinished = dateFinished;
        }

        updateValue();

        return this;
    }

    /**
     * Does the following:
     *
     * If {@param isFinished} is true:
     * - If it has a recurring due date, its due date will be advanced by its recurrence once,
     * - If it has a date range, it will have no effect.
     * - Otherwise, its date finished is set to now.
     *
     * Else, sets remove to-do's date finished, if there is.
     *
     * @param isFinished whether to set the to-do as finished
     * @return current to-do, for method chaining
     */
    public ToDo setIsFinished(boolean isFinished) {
        if (isFinished) {
            if (dueDate != null && dueDate.recurrence != Recurrence.None) {
                setDateFinished(dueDate.value);
            } else {
                setDateFinished(LocalDateTime.now());
            }
        } else {
            // remove date finished if unfinish
            dateFinished = null;
        }

        updateValue();

        return this;
    }

    /**
     * Sets the date created of the to-do, non-null.
     *
     * @param dateCreated date created to set
     * @return current to-do, for method chaining
     */
    public ToDo setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;

        updateValue();

        return this;
    }

    @Override
    public Optional<DueDate> getDueDate() {
        return Optional.ofNullable(dueDate);
    }

    @Override
    public Optional<DateRange> getDateRange() {
        // advance based on recurring date range, if applicable
        advanceDateRange(LocalDateTime.now());

        return Optional.ofNullable(dateRange);
    }

    @Override
    public Set<Tag> getTags() {
        // Return a copy of the set for defensiveness
        if (tags == null) {
            return Collections.emptySet();
        } else {
            return new TreeSet<>(tags);
        }
    }

    @Override
    public Optional<LocalDateTime> getDateFinished() {
        if (dateFinished != null) {
            return Optional.of(dateFinished);
        } else if (dateRange != null) {
            // we need to use the latest date range which considers recurrence
            advanceDateRange(LocalDateTime.now());

            // If date range is after its end date
            // return its end date as date finished automatically
            if (LocalDateTime.now().isAfter(dateRange.endDate)) {
                return Optional.of(dateRange.endDate);
            }
        }

        return Optional.empty();
    }

    @Override
    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    @Override
    public ObservableStringValue getObservableValue() {
        return value;
    }

    @Override
    public Title getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object other) {
        // check if identical object first then check if contains same attributes
        return other == this
            || (other instanceof ReadOnlyToDo
            && this.isSameStateAs((ReadOnlyToDo) other));
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, dueDate, dateRange, tags);
    }

    @Override
    public String toString() {
        return value.getValue();
    }

    /**
     * Called when any of its fields are updated
     */
    private void updateValue() {
        // Reset "invalidated" state of observable value and update
        value.getValue();
        value.setValue(getText());
    }

    //@@author A0139697H

    /**
     * Called when a to-do is to advance its date range to after {@param dateUntil}
     * based on its recurrence.
     * Will only have an effect if to-do has a date range with a recurrence.
     */
    private void advanceDateRange(LocalDateTime dateUntil) {
        if (dateRange == null
            || dateUntil.isBefore(dateRange.endDate)
            || dateRange.recurrence == Recurrence.None) {
            return;
        }

        // The initial if-else should cover this
        assert dateRange != null && dateRange.recurrence != Recurrence.None;

        // Keep moving date forward based on recurrence interval
        // until it is not before the current date
        LocalDateTime startDate = dateRange.startDate;
        LocalDateTime endDate = dateRange.endDate;
        while (!endDate.isAfter(dateUntil)) {
            startDate = dateRange.recurrence.getNextDate(startDate);
            endDate = dateRange.recurrence.getNextDate(endDate);
        }
        try {
            dateRange = new DateRange(startDate, endDate, dateRange.recurrence);
        } catch (IllegalValueException exception) {
            assert false : "new date range should be valid";
        }

        updateValue();
    }

    /**
     * Called when a to-do is to advance its due date to after {@param dateUntil}
     * based on its recurrence.
     * Will only have an effect if to-do has a due date with a recurrence.
     */
    private void advanceDueDate(LocalDateTime dateUntil) {
        if (dueDate == null
            || dateUntil.isBefore(dueDate.value)
            || dueDate.recurrence == Recurrence.None) {
            return;
        }

        // The initial if-else should cover this
        assert dueDate != null && dueDate.recurrence != Recurrence.None;

        // Keep moving date forward based on recurrence interval
        // until it is not before the current date
        LocalDateTime date = dueDate.value;
        while (!date.isAfter(dateUntil)) {
            date = dueDate.recurrence.getNextDate(date);
        }
        dueDate = new DueDate(date, dueDate.recurrence);

        updateValue();
    }
}
