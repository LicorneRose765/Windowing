package Windowing.front.events;

import javafx.event.EventHandler;

public abstract class InvalidInputEventHandler implements EventHandler<InvalidInputEvent> {
    @Override
    public void handle(InvalidInputEvent event) {
        event.invokeHandler(this);
    }

    public abstract void onEvent();
}
