package seedu.commando.commons.events.storage;

import seedu.commando.commons.events.BaseEvent;

/**
 * Indicates an exception during a file saving
 */
public class DataSavingExceptionEvent extends BaseEvent {

    public Exception exception;

    public DataSavingExceptionEvent(Exception exception) {
        this.exception = exception;
    }

    @Override
    public String toString(){
        return getClass().getSimpleName() + " with exception: " +exception.toString();
    }

}
