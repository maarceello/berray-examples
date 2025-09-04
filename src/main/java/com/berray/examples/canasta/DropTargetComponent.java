package com.berray.examples.canasta;

import com.berray.GameObject;
import com.berray.components.core.Component;
import com.berray.event.Event;

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
    on("drag_enter", this::onDragEnter);
    on("drag_leave", this::onDragLeave);
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
