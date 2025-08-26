package com.berray.examples.canasta;

import com.berray.GameObject;
import com.berray.components.core.Component;
import com.berray.math.MathUtil;
import com.berray.math.Vec2;
import com.berray.math.Vec3;

public class FlipComponent extends Component {

    /** 1.0 - show front side, -1.0 - show back side*/
    private float sideVisible = 1.0f;

    private int frontSideFrame;
    private int backSideFrame;

    public FlipComponent(int frontSideFrame, int backSideFrame) {
        super("flipCard", "scale", "sprite");
        this.frontSideFrame = frontSideFrame;
        this.backSideFrame = backSideFrame;
    }

    @Override
    public void add(GameObject gameObject) {
        super.add(gameObject);
        registerBoundProperty("sideVisible", this::getSideVisible, this::setSideVisible);
    }

    public float getSideVisible() {
        return sideVisible;
    }

    public void setSideVisible(float sideVisible) {
        this.sideVisible = MathUtil.clamp(sideVisible, -1.0f, 1.0f);
        // set side to show
        gameObject.set("frame", sideVisible > 0 ? frontSideFrame: backSideFrame);
        // set new width scaling
        Vec3 scale = gameObject.get("scale");
        gameObject.set("scale", new Vec2(Math.abs(this.sideVisible), scale.getY()));
    }
}
