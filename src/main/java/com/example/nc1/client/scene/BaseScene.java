package com.example.nc1.client.scene;

import com.example.nc1.OnDestroy;
import com.example.nc1.client.Scenes;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.paint.Paint;

public class BaseScene extends Scene implements OnDestroy {

    public BaseScene(Parent root) {
        super(root);
    }

    public BaseScene(Parent root, double width, double height) {
        super(root, width, height);
    }

    public BaseScene(Parent root, Paint fill) {
        super(root, fill);
    }

    public BaseScene(Parent root, double width, double height, Paint fill) {
        super(root, width, height, fill);
    }

    public BaseScene(Parent root, double width, double height, boolean depthBuffer) {
        super(root, width, height, depthBuffer);
    }

    public BaseScene(Parent root, double width, double height, boolean depthBuffer, SceneAntialiasing antiAliasing) {
        super(root, width, height, depthBuffer, antiAliasing);
    }


    @Override
    public void onDestroy() {

    }
}
