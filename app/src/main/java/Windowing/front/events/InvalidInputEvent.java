package Windowing.front.events;

import javafx.event.Event;
import javafx.event.EventType;

public class InvalidInputEvent extends AbstractInvalidInputEvent {
    public InvalidInputEvent() {
        super(AbstractInvalidInputEvent.INVALID_VALUE);
    }

    @Override
    public void invokeHandler(InvalidInputEventHandler handler) {
        handler.onEvent();
    }
}