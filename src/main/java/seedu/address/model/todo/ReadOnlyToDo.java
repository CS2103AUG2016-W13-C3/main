package seedu.address.model.todo;

import java.util.Optional;
import java.util.Set;

import seedu.address.model.tag.Tag;

/**
 * A read-only immutable interface for a to-do
 * Implementations should guarantee: details are present and not null, field values are validated.
 */
public interface ReadOnlyToDo {

    Title getTitle();
    Optional<DateRange> getDateRange();
    Optional<DueDate> getDueDate();
    Set<Tag> getTags();

    /**
     * Returns true if both have the same state. (interfaces cannot override .equals)
     */
    default boolean isSameStateAs(ReadOnlyToDo other) {
        return other == this // short circuit if same object
                || (other != null // this is first to avoid NPE below
                && other.getTitle().equals(getTitle())
                && other.getDateRange().equals(getDateRange())
                && other.getDueDate().equals(getDueDate())
                && other.getTags().equals(getTags())); // state checks here onwards
    }

    /**
     * Formats the to-do as text, showing all details
     */
    default String getAsText() {
        final StringBuilder builder = new StringBuilder();

        builder.append(String.join(", ",
            "Title: " + getTitle().toString(),
            "Date Range: " + (getDateRange().isPresent() ? getDateRange().get().toString() : "none"),
            "Due Date: " + (getDueDate().isPresent() ? getDueDate().get().toString() : "none"),
            "Tags: " + getTags().toString()));
        return builder.toString();
    }
}