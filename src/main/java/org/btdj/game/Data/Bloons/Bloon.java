package org.btdj.game.Data.Bloons;

import org.btdj.game.Components.BloonsComponent;

interface Bloon {
    BloonsComponent.Type type;
    double speed;
    Bloon parent;
    Bloon child;
    int rbe;

}
