package org.btdj.game.Data;

public class Bloon {
    public final String TYPE;
    public final double SPEED;
    public final String PARENT;
    public final String CHILD;
    public final int RBE;

    public Bloon(String type, double speed, String parent, String child, int rbe) {
        this.TYPE = type;
        this.SPEED = speed;
        this.PARENT = parent;
        this.CHILD = child;
        this.RBE = rbe;
    }
}
