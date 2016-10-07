package seedu.address.model.todo;

import java.util.Date;

import seedu.address.commons.core.Messages;
import seedu.address.commons.exceptions.IllegalValueException;

/**
 * Represents the Date Range of a to-do
 * Guarantees: immutable; is valid as declared in {@link #isValid(String)}
 */
public class DateRange {

    public final Date startDate, endDate;

    /**
     * Constructor for a title
     * Asserts title is not null
     * @throws IllegalValueException if given title is invalid
     */
    public DateRange(Date startDate, Date endDate){
        assert startDate != null;
        assert endDate != null;
       
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return this.startDate.toString() + " " + this.endDate.toString();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DateRange // instanceof handles nulls
                && (startDate.equals(((DateRange) other).startDate) 
                &&  endDate.equals(((DateRange) other).endDate)
                        )); // state check
    }

    @Override
    public int hashCode() {
        return startDate.hashCode() + endDate.hashCode();
    }

}