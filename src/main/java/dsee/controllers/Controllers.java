package dsee.controllers;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

public interface Controllers {

    Button getBottomButton();

    Button getRightButton();

    Button getLeftButton();

    Node getCenterView();

    BorderPane getPane();
}
