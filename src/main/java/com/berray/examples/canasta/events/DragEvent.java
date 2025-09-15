package com.berray.examples.canasta.events;

import com.berray.GameObject;
import com.berray.event.Event;

import java.util.Arrays;

/**
 * Event send when a dragged {@link GameObject} is dropped over another object.
 * Note that the event can be {@link #consume() consumed}
 *
 * Params:
 *  0 - source - the object which is dragged
 *  0 - draggedObject - alias for source: the object which is dragged
 *  1 - target - The game object which this object would be dropped to when the drag stops. In Enter and Leave events this is the object the draggedObject enters or leaves.
 *  2 - consumed - set to true when the listener consumed the event and the event should not be propagated to other listeners
 */
public class DragEvent extends Event {
    public static final String EVENT_DRAG_LEAVE = "drag_leave";
    public static final String EVENT_DRAG_ENTER = "drag_enter";
    public static final String EVENT_DRAG_DROP = "drag_drop";

    public DragEvent(String name, GameObject source, GameObject dropTarget) {
        super(name, Arrays.asList(source, dropTarget, false));
    }

    public GameObject getDraggedObject() {
        return getParameter(0);
    }

    public GameObject getTarget() {
        return getParameter(1);
    }

    public boolean isConsumed() {
        return getParameter(2);
    }


    public void consume() {
        setParameter(2, true);
    }
}
