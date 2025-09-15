package com.berray.examples.canasta;

import com.berray.GameObject;
import com.berray.components.core.Component;
import com.berray.event.Event;
import com.berray.examples.canasta.events.DragEvent;

public class DropTargetComponent extends Component {
  private final String[] acceptedTags;

  private boolean dropHovered = false;

  public DropTargetComponent(String ... acceptedTags) {
    super("dropTarget", "area");
    this.acceptedTags = acceptedTags;
  }

  @Override
  public void add(GameObject gameObject) {
    super.add(gameObject);
    on(DragEvent.EVENT_DRAG_ENTER, this::onDragEnter);
    on(DragEvent.EVENT_DRAG_LEAVE, this::onDragLeave);
    registerBoundProperty("dropHovered", this::isDropHovered, this::setDropHovered);
  }

  private void onDragEnter(Event event) {
    this.dropHovered = true;
  }

  private void onDragLeave(Event event) {
    this.dropHovered = false;
  }

  public boolean isDropHovered() {
    return dropHovered;
  }

  public void setDropHovered(boolean dropHovered) {
    this.dropHovered = dropHovered;
  }

  public static DropTargetComponent dropTarget(String ... acceptedTags) {
    return new DropTargetComponent(acceptedTags);
  }
}
