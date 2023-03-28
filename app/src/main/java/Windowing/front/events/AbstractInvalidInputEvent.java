package Windowing.front.events;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyEvent;

public abstract class AbstractInvalidInputEvent extends Event {
    public static final EventType<AbstractInvalidInputEvent> ANY =
            new EventType<>(Event.ANY, "ANY");

    public static final EventType<AbstractInvalidInputEvent> INVALID_VALUE =
            new EventType<>(AbstractInvalidInputEvent.ANY, "INVALID_VALUE");

    public AbstractInvalidInputEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }

    public abstract void invokeHandler(InvalidInputEventHandler handler);
}
