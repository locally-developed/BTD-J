package org.btdj.game.Util;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.scene3d.Model3D;

public class Handler3D {
    public static void run() {
        Model3D obj = FXGL.getAssetLoader().loadModel3D("dartMonkey/dartmonkey.obj");
    }
}
