package com.berray.examples.canasta;

import com.berray.GameObject;
import com.berray.components.core.Component;
import com.berray.event.CoreEvents;
import com.berray.event.MouseEvent;
import com.berray.math.Vec2;

public class DragComponent extends Component {
  public DragComponent() {
    super("draggable");
  }

  @Override
  public void add(GameObject gameObject) {
    super.add(gameObject);
    // register mouse listener
    on(CoreEvents.DRAG_START, this::onDragStart);
    on(CoreEvents.DRAGGING, this::onDragging);
  }


  private void onDragStart(MouseEvent event) {
    GameObject source = event.getSource();
    source.setProperty("dragDelta", source.<Vec2>get("pos").sub(event.getWindowPos()));
  }

  private void onDragging(MouseEvent event) {
    GameObject source = event.getSource();
    Vec2 dragDelta = source.getProperty("dragDelta");
    source.set("pos", event.getWindowPos().add(dragDelta));
  }

  public static DragComponent draggable() {
    return new DragComponent();
  }

}
