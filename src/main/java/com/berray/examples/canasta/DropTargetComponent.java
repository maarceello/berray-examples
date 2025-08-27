package com.berray.examples.canasta;

import com.berray.components.core.Component;

public class DropTargetComponent extends Component {
  private final String[] acceptedTags;

  public DropTargetComponent(String ... acceptedTags) {
    super("droptarget", "area");
    this.acceptedTags = acceptedTags;
  }
}
