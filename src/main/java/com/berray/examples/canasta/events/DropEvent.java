package com.berray.examples.canasta.events;

import com.berray.GameObject;
import com.berray.event.Event;

import java.util.Arrays;

/**
 * Event send when a dragged {@link GameObject} is dropped over another object.
 * Note that the event can be {@link #consume() consumed}
 */
public class DropEvent extends Event {
    public DropEvent(GameObject source) {
        super("drag_drop", Arrays.asList(source, false));
    }

    public boolean isConsumed() {
        return getParameter(1);
    }

    public void consume() {
        setParameter(1, true);
    }
}
