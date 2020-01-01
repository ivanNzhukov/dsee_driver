package dsee.controllers;

import dsee.lab.Communicator;
import dsee.utils.Commands;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class MenuController {

    @FXML
    private Button btnConnection;
    @FXML
    private Button btnPlay;
    @FXML
    private Button btnStartDevice;
    @FXML
    private Button btnShowHide;
    @FXML
    private ComboBox<String> boxSerialPort;
    @FXML
    public TextArea textArea;
    @FXML
    private GridPane gridPane;
    @FXML
    private SplitPane splitPane;
    @FXML
    private VBox vBox;

    private Communicator com;
    private boolean isItNeedsToShowSideButtons;
    private boolean isItStartWithException = false;

    private final String CONNECT = "Connect";
    private final String DISCONNECT = "Disconnect";
    private final String START_DEVICE = "Start Device";
    private final String STOP_DEVICE = "Stop Device";
    private final String SHOW_BUTTONS = "Press to show side buttons on MediaPlayer";
    private final String HIDE_BUTTONS = "Press to hide side buttons on MediaPlayer";

    public MenuController() {
        try {
            this.com = new Communicator();
        } catch (UnsatisfiedLinkError e) {
            isItStartWithException = true;
        }
    }

    @FXML
    public void initialize() {
        if (!isItStartWithException) {
            boxSerialPort.setItems(FXCollections.observableArrayList(com.getPorts().keySet()));
        }
        btnConnection.setText(CONNECT);
        btnShowHide.setText(SHOW_BUTTONS);
        btnStartDevice.setText(START_DEVICE);

        if (isItStartWithException) {
            textArea.appendText("BEFORE START APPLICATION\n" +
                    "It needs to get from jar files: \n" +
                    "rxtx64\\rxtxParallel.dll and rxtx64\\rxtxSerial.dll or \n" +
                    "rxtx86\\rxtxParallel.dll and rxtx86\\rxtxSerial.dll \n" +
                    "and copy them into your JavaFolder, for example \n" +
                    "C:\\Program Files\\Java\\jdk1.8.0_221\\bin \n" +
                    "After that restart your application");

        }
    }


    @FXML
    public void startOrStopDevice() {
        try {
            if (btnStartDevice.getText().equals(START_DEVICE)) {
                com.sendDataToDevice(Commands.START_DEVICE.getCommand());
                btnStartDevice.setText(STOP_DEVICE);
            } else {
                com.sendDataToDevice(Commands.STOP_DEVICE.getCommand());
                btnStartDevice.setText(START_DEVICE);
            }
        } catch (Exception e) {
            textArea.appendText(e.getMessage() + "\n");
        }
    }

    @FXML
    public void connectOrDisconnect() {
        try {
            if (btnConnection.getText().equals(CONNECT)) {
                com.connect(boxSerialPort.getValue());
                if (com.getConnected()) {
                    textArea.appendText("Connected \n");
                    if (com.initIOStream()) {
                        textArea.appendText("IOStream init \n");
                        btnConnection.setText(DISCONNECT);
                        com.initListener();
                        textArea.appendText("Listen port \n");
                    }
                } else {
                    textArea.appendText("Could not connect to port " + boxSerialPort.getValue() + "\n");
                }
            } else {
                startOrStopDevice();
                com.disconnect();
                btnConnection.setText(CONNECT);
            }
        } catch (Exception e) {
            textArea.appendText("Could not connect to port " + boxSerialPort.getValue() + "\n");
            textArea.appendText(e.toString());
        }
    }

    @FXML
    public void showOrHideSideButtons() {
        if (btnShowHide.getText().equals(SHOW_BUTTONS)) {
            isItNeedsToShowSideButtons = true;
            btnShowHide.setText(HIDE_BUTTONS);
        } else {
            isItNeedsToShowSideButtons = false;
            btnShowHide.setText(SHOW_BUTTONS);
        }
    }

    public TextArea getTextArea() {
        return textArea;
    }

    public void setTextArea(TextArea textArea) {
        this.textArea = textArea;
    }

    public GridPane getGridPane() {
        return gridPane;
    }

    public void setGridPane(GridPane gridPane) {
        this.gridPane = gridPane;
    }

    public SplitPane getSplitPane() {
        return splitPane;
    }

    public void setSplitPane(SplitPane splitPane) {
        this.splitPane = splitPane;
    }

    public Button getBtnConnection() {
        return btnConnection;
    }

    public void setBtnConnection(Button btnConnection) {
        this.btnConnection = btnConnection;
    }

    public Button getBtnPlay() {
        return btnPlay;
    }

    public void setBtnPlay(Button btnPlay) {
        this.btnPlay = btnPlay;
    }

    public Button getBtnStartDevice() {
        return btnStartDevice;
    }

    public void setBtnStartDevice(Button btnStartDevice) {
        this.btnStartDevice = btnStartDevice;
    }

    public ComboBox<String> getBoxSerialPort() {
        return boxSerialPort;
    }

    public void setBoxSerialPort(ComboBox<String> boxSerialPort) {
        this.boxSerialPort = boxSerialPort;
    }

    public Communicator getCom() {
        return com;
    }

    public void setCom(Communicator com) {
        this.com = com;
    }

    public VBox getvBox() {
        return vBox;
    }

    public void setvBox(VBox vBox) {
        this.vBox = vBox;
    }

    public boolean isItNeedsToShowSideButtons() {
        return isItNeedsToShowSideButtons;
    }
}
