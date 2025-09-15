package com.berray.examples.canasta;

import com.berray.GameObject;
import com.berray.components.core.Component;
import com.berray.event.*;
import com.berray.examples.canasta.events.DragEvent;
import com.berray.math.Vec2;

import java.util.HashMap;
import java.util.Map;

public class DragComponent extends Component {
  private String dragLayer = "dragLayer";

  /** true when the component is currently dragged. */
  private boolean dragging = false;
  private Map<Integer, GameObject> hoveredDropTargets = new HashMap<>();

  public DragComponent() {
    super("draggable");
  }

  @Override
  public void add(GameObject gameObject) {
    super.add(gameObject);
    // register mouse listener
    on(CoreEvents.DRAG_START, this::onDragStart);
    on(CoreEvents.DRAGGING, this::onDragging);
    on(CoreEvents.DRAG_FINISH, this::onDragFinish);
    on(CoreEvents.PHYSICS_COLLIDE, this::onCollide);
    on(CoreEvents.PHYSICS_COLLIDE_END, this::onCollideEnd);
  }


  private void onDragStart(MouseEvent event) {
    dragging = true;
    hoveredDropTargets.clear();
    GameObject source = event.getSource();
    source.setProperty("drag/delta", source.<Vec2>get("pos").sub(event.getWindowPos()));
    if (source.isWritable("layer") && gameObject.getGame().getLayers().contains(dragLayer)) {
      source.setProperty("drag/layer", source.<Vec2>get("layer"));
      source.set("layer", dragLayer);
    }

  }

  private void onDragging(MouseEvent event) {
    GameObject source = event.getSource();
    Vec2 dragDelta = source.getProperty("drag/delta");
    source.set("pos", event.getWindowPos().add(dragDelta));
  }

  private void onDragFinish(MouseEvent event) {
    dragging = false;
    GameObject source = event.getSource();
    String oldLayer = source.getProperty("drag/layer");
    if (source.isWritable("layer") && oldLayer != null) {
      source.set("layer", oldLayer);
    }

    // Check if we are releasing the drag over some objects (ie this is a drop)
    if (!hoveredDropTargets.isEmpty()) {
      // First tell all current hovered drop targets that the drag is finished
      for (GameObject dropTarget : hoveredDropTargets.values()) {
        dropTarget.trigger(new DragEvent(DragEvent.EVENT_DRAG_LEAVE, gameObject, dropTarget));
      }

      // Then send drop event to the drop targets. When the drop event is consumed, stop notifying the remaining objects
      for (GameObject dropTarget : hoveredDropTargets.values()) {
        DragEvent dragEvent = new DragEvent(DragEvent.EVENT_DRAG_DROP, gameObject, dropTarget);
        dropTarget.trigger(dragEvent);
        if (dragEvent.isConsumed()) {
          break;
        }
      }
    }
    // Clear list of hovered objects
    hoveredDropTargets.clear();
  }

  private void onCollide(PhysicsCollideEvent event) {
    if (!dragging) {
      return;
    }
    GameObject other = event.getCollisionPartner();
    hoveredDropTargets.put(other.getId(), other);
    // send collision partner a drag_enter event
    event.getCollisionPartner().trigger(new DragEvent(DragEvent.EVENT_DRAG_ENTER, gameObject, other));
  }

  private void onCollideEnd(PhysicsCollideEndEvent event) {
    if (!dragging) {
      return;
    }
    GameObject other = event.getCollision().getOther();
    if (other != null) {
      hoveredDropTargets.remove(other.getId());
      other.trigger(new DragEvent(DragEvent.EVENT_DRAG_LEAVE, gameObject, other));
    }
  }


  public static DragComponent draggable() {
    return new DragComponent();
  }

}
