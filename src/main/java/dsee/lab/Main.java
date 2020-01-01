package dsee.lab;

import dsee.controllers.Controllers;
import dsee.controllers.MenuController;
import dsee.utils.Commands;
import dsee.utils.PropertyReader;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;


public class Main extends Application {

    private Node view;
    private Stage stage;
    private int position;
    private List<File> fileList;
    private boolean isNeedShowSideButtons;
    private boolean isReadyToStartMainScene;

    private BorderPane videoPane;
    private BorderPane imagePane;
    private BorderPane mainPane;

    private Communicator communicator;
    private MenuController menuController;

    private Scene mainScene;
    private Scene controlScene;

    private TextArea textArea;
    private FXMLLoader controlLoader;
    private FXMLLoader imageLoader;
    private FXMLLoader videoLoader;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        position = 0;
        isReadyToStartMainScene = false;
        stage = primaryStage;
        stage.setTitle("Player");

        controlLoader = new FXMLLoader(getClass().getResource("/fxml/ControlMenuView.fxml"));
        imageLoader = new FXMLLoader(getClass().getResource("/fxml/ImageView.fxml"));
        videoLoader = new FXMLLoader(getClass().getResource("/fxml/VideoView.fxml"));
        stage.setOnCloseRequest(e -> {
            if (stage.getScene().getUserData() != null && stage.getScene().getUserData().equals("ControlScene")) {
                disconnect();
                System.exit(0);
            } else {
                e.consume();
                if (mainPane.getCenter() instanceof MediaView) {
                    ((MediaView) view).getMediaPlayer().stop();
                }
                disconnect();
                stage.setScene(controlScene);
                stage.setWidth(500);
                stage.setHeight(500);
            }
        });
        prepareControlScene();
        stage.setScene(controlScene);
        stage.show();

        EventQueue.invokeLater(() -> {
            try {
                prepareMainScene();
            } catch (Exception e) {
                textArea.appendText(e.getMessage());
            }
        });
    }

    private void disconnect() {
        try {
            if (communicator.getConnected()) {
                menuController.connectOrDisconnect();
            }
        } catch (Exception ex) {
            textArea.appendText(ex.toString());
        }
    }

    private void prepareControlScene() {
        try {
            stage.setWidth(500);
            stage.setHeight(500);

            controlScene = new Scene(controlLoader.load());
            controlScene.setUserData("ControlScene");

            menuController = controlLoader.getController();
            menuController.getBtnPlay().setOnAction(e -> {
                if (isReadyToStartMainScene) {
                    stage.setWidth(800);
                    stage.setHeight(600);
                    isNeedShowSideButtons = menuController.isItNeedsToShowSideButtons();
                    startFirstElement();
                }
            });
            communicator = menuController.getCom();
            textArea = menuController.getTextArea();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void prepareMainScene() throws Exception {
        fileList = PropertyReader.read();
        try {
            prepareImagePane();
            prepareVideoPane();
        } catch (IOException e) {
            e.printStackTrace();
        }

        isReadyToStartMainScene();
    }

    private void prepareVideoPane() throws IOException {
        videoPane = videoLoader.load();
        videoPane.setId("videoPane");
        initPane(videoLoader.getController());
    }

    private void prepareImagePane() throws IOException {
        imagePane = imageLoader.load();
        imagePane.setId("imagePane");
        initPane(imageLoader.getController());
    }

    private void isReadyToStartMainScene() {
        if (fileList != null && !fileList.isEmpty()) {
            isReadyToStartMainScene = true;
        } else {
            textArea.appendText("No files in the path \n");
            for (File file : fileList) {
                textArea.appendText(file.getPath() + "\n");
            }
            isReadyToStartMainScene = false;
        }
    }

    private void initPane(Controllers controller) {
        BorderPane pane = controller.getPane();
        pane.setOnSwipeLeft(e -> goNext());
        pane.setOnSwipeRight(e -> goBack());
        pane.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

        controller.getCenterView().setOnMouseClicked(this::doubleClick);
        controller.getLeftButton().setOnAction(e -> goBack());
        controller.getRightButton().setOnAction(e -> goNext());
        controller.getBottomButton().setOnAction(e -> {
            try {
                sendCommandToDeviceToPlayVideo();
            } catch (Exception ex) {
                textArea.appendText(ex.toString());
            }
        });
    }

    private void startFirstElement() {
        File file = fileList.get(position);
        try {
            if (getExtension(file).equals("video")) {
                loadVideo(file);
            } else {
                loadImage(file);
            }

            if (mainScene == null) {
                mainScene = new Scene(mainPane);
            }

            mainScene.setUserData("MainScene");
            stage.setScene(mainScene);
            setPosition();
        } catch (Exception e) {
            textArea.appendText(e.getMessage());
        }
    }

    private void loadImage(File file) {
        try {
            Image image = SwingFXUtils.toFXImage(ImageIO.read(file), null);
            view = imagePane.getCenter();
            ((ImageView) view).setImage(image);
            mainPane = imagePane;
        } catch (Exception e) {
            textArea.appendText(" Can not read image \n");
        }
    }

    private void loadVideo(File file) {
        try {
            Media media = new Media(file.toURI().toString());
            MediaPlayer player = new MediaPlayer(media);
            player.setCycleCount(Integer.MAX_VALUE);
            player.play();
            view = videoPane.getCenter();
            ((MediaView) view).setMediaPlayer(player);
            mainPane = videoPane;
        } catch (Exception e) {
            textArea.appendText("  Can not read video  \n");
        }
    }

    private void setPosition() {

        BorderPane.setMargin(mainPane.getBottom(), new Insets(10, 10, 10, 10));
        BorderPane.setMargin(mainPane.getCenter(), new Insets(0, 20, 20, 20));
        if (mainPane.getId().equals("videoPane")) {
            ((MediaView) view).setFitWidth(stage.getWidth() - stage.getWidth() / 10);
            ((MediaView) view).setFitHeight(stage.getHeight() - stage.getWidth() / 10);
            ((MediaView) view).setPreserveRatio(true);
        } else if (mainPane.getId().equals("imagePane")) {
            ((ImageView) view).setFitWidth(stage.getWidth() - stage.getWidth() / 10);
            ((ImageView) view).setFitHeight(stage.getHeight() - stage.getWidth() / 10);
            ((ImageView) view).setPreserveRatio(true);
        }
        if (isNeedShowSideButtons) {
            mainPane.getLeft().setVisible(true);
            mainPane.getRight().setVisible(true);
        } else {
            mainPane.getLeft().setVisible(false);
            mainPane.getRight().setVisible(false);
        }
    }

    private void sendCommandToDeviceToPlayVideo() throws Exception {
        Commands commands = Commands.SWITCH_TO_N_VIDEO_AND_PAUSE;
        commands.setFourthByte((byte) position);
        communicator.sendDataToDevice(commands.getCommand());
        communicator.sendDataToDevice(Commands.PAUSE_OR_RESTART_PLAY.getCommand());
    }

    private void doubleClick(MouseEvent e) {
        if (e.getClickCount() == 2) {
            if (stage.isFullScreen()) {
                //   stage.setFullScreen(false); // To prevent People make double Tap on laptop and exit from FullScreen
            } else {
                stage.setFullScreen(true);
            }
            setPosition();
        }
    }

    private void goNext() {
        if (position < fileList.size() - 1) {
            File file = fileList.get(position + 1);
            try {
                setView(file);
            } catch (Exception e) {
                textArea.appendText(e.getMessage() + "\n");
            }
            position++;
        }
    }

    private void goBack() {
        if (position > 0) {
            File file = fileList.get(position - 1);
            try {
                setView(file);
            } catch (Exception e) {
                textArea.appendText(e.getMessage());
            }
            position--;
        }
    }

    private void setView(File file) throws Exception {
        if (view instanceof MediaView) {
            ((MediaView) view).getMediaPlayer().stop();
            if (getExtension(file).equals("video")) {
                loadVideo(file);
            } else {
                loadImage(file);
                mainScene.setRoot(mainPane);
            }
        } else if (view instanceof ImageView) {
            if (getExtension(file).equals("video")) {
                loadVideo(file);
                mainScene.setRoot(mainPane);
            } else {
                loadImage(file);
            }
        }
        setPosition();
    }

    private String getExtension(File file) throws Exception {
        String filename = file.getName().toLowerCase();
        if (filename.endsWith("mp4")) {
            return "video";
        } else if (filename.endsWith(".jpg") || filename.endsWith(".png") || filename.endsWith(".bmp")) {
            return "image";
        } else {
            throw new Exception("Can not get or open file " + file.getPath() + " \n");
        }
    }
}