package org.btdj.game.Components;

import com.almasb.fxgl.entity.component.Component;
import javafx.scene.text.Text;
import org.btdj.game.MainApp;

public class ValueDisplayComponent extends Component {
    @Override
    public void onUpdate(double tpf) {
        entity.getViewComponent().getChild(0, Text.class).setText(String.valueOf(MainApp.gameHealth));
    }
}
