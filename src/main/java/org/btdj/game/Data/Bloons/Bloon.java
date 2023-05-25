package org.btdj.game.Data.Bloons;

import org.btdj.game.Components.BloonsComponent;

public class Bloon {
    public final BloonsComponent.Type TYPE;
    public final double SPEED;
    public final String PARENT;
    public final String CHILD;
    public final int RBE;

    public Bloon(BloonsComponent.Type type, double speed, String parent, String child, int rbe) {
        this.TYPE = type;
        this.SPEED = speed;
        this.PARENT = parent;
        this.CHILD = child;
        this.RBE = rbe;
    }
}
