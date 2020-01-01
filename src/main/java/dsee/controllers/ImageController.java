package dsee.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;


public class ImageController implements Controllers {
    @FXML
    private Button btnPrevious;
    @FXML
    private Button btnNext;
    @FXML
    private Button btnPlay;
    @FXML
    private ImageView view;
    @FXML
    private BorderPane borderPane;

    @FXML
    public void initialize() {
    }


    public Button getBtnPrevious() {
        return btnPrevious;
    }

    public void setBtnPrevious(Button btnPrevious) {
        this.btnPrevious = btnPrevious;
    }

    public Button getBtnNext() {
        return btnNext;
    }

    public void setBtnNext(Button btnNext) {
        this.btnNext = btnNext;
    }

    public Button getBtnPlay() {
        return btnPlay;
    }

    public void setBtnPlay(Button btnPlay) {
        this.btnPlay = btnPlay;
    }

    public ImageView getView() {
        return view;
    }

    public void setView(ImageView view) {
        this.view = view;
    }

    public BorderPane getBorderPane() {
        return borderPane;
    }

    public void setBorderPane(BorderPane borderPane) {
        this.borderPane = borderPane;
    }

    @Override
    public Button getLeftButton() {
        return getBtnPrevious();
    }

    @Override
    public Button getRightButton() {
        return getBtnNext();
    }

    @Override
    public Button getBottomButton() {
        return getBtnPlay();
    }

    @Override
    public Node getCenterView() {
        return getView();
    }

    @Override
    public BorderPane getPane() {
        return getBorderPane();
    }
}
