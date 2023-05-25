package org.btdj.game.Data.Bloons;

import org.btdj.game.Components.BloonsComponent;

interface Bloon {
    BloonsComponent.Type type = BloonsComponent.Type.BLUE;
    double speed = 0;
    Bloon parent = this;
    Bloon child;
    int rbe;

}
