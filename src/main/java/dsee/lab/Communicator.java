/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dsee.lab;

import dsee.utils.Commands;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class Communicator {

    //for containing the ports that will be found
    private Enumeration ports = null;
    //map the port names to CommPortIdentifiers
    private Map<String, CommPortIdentifier> portMap = new HashMap<>();

    //this is the object that contains the opened port
    private CommPortIdentifier selectedPortIdentifier = null;
    private SerialPort serialPort = null;
    private CommPort commPort = null;

    //input and output streams for sending and receiving data
    private InputStream input = null;
    private OutputStream output = null;

    //just a boolean flag that i use for enabling
    //and disabling buttons depending on whether the program
    //is connected to a serial port or not
    private boolean bConnected = false;

    //the timeout value for connecting with the port
    final private int TIMEOUT = 5000;
    final private int TIMEOUT_BETWEEN_COMMANDS = 500;                    // Because it needs at least 500 ms between commands

    final private String APP_NAME = "Driver";

    public Communicator() {
        searchPorts();
    }

    //open the input and output streams
    //pre: an open port
    //post: initialized intput and output streams for use to communicate data
    public boolean initIOStream() throws IOException {
        //return value for whether opening the streams is successful or not

        try {
            input = serialPort.getInputStream();
            output = serialPort.getOutputStream();

            return true;

        } catch (IOException e) {
            throw new IOException("I/O Streams failed to open. (" + e.toString() + ")\n");
        }
    }

    public void initListener() {

        serialPort.notifyOnDataAvailable(true);
    }

    private void searchPorts() {
        ports = CommPortIdentifier.getPortIdentifiers();
        while (ports.hasMoreElements()) {
            CommPortIdentifier curPort = (CommPortIdentifier) ports.nextElement();

            //get only serial ports
            if (curPort.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                portMap.put(curPort.getName(), curPort);
            }
        }
    }

    public void connect(String port) throws Exception {
        selectedPortIdentifier = portMap.get(port);

        try {
            //the method below returns an object of type CommPort
            commPort = selectedPortIdentifier.open(APP_NAME, TIMEOUT);
            //the CommPort object can be casted to a SerialPort object
            serialPort = (SerialPort) commPort;

        } catch (PortInUseException e) {
            throw new Exception(port + " is in use. (" + e.toString() + ")\n");
        } catch (Exception e) {
            throw new Exception("Failed to open " + port + "(" + e.toString() + ")\n");
        }
        //for controlling GUI elements
        setConnected(true);
    }

    public void disconnect() throws Exception {
        //close the serial port
        try {
            sendDataToDevice(Commands.STOP_DEVICE.getCommand());

            serialPort.removeEventListener();
            serialPort.close();
            commPort.close();
            input.close();
            output.close();
            setConnected(false);

        } catch (Exception e) {
            throw new Exception("Failed to close " + serialPort.getName() + "(" + e.toString() + ")");
        }
    }

    public void sendDataToDevice(byte[] command) throws Exception {
        try {
            output.write(command);
            output.flush();
            Thread.sleep(TIMEOUT_BETWEEN_COMMANDS);
        } catch (NullPointerException npe) {
            throw new NullPointerException("Can not send command to device, because it is not connected");
        } catch (Exception e) {
            throw new Exception("Failed to write data. (" + e.toString() + ")\n");
        }
    }


    public boolean getConnected() {
        return bConnected;
    }

    public void setConnected(boolean bConnected) {
        this.bConnected = bConnected;
    }

    public Map<String, CommPortIdentifier> getPorts() {
        return portMap;
    }

    public void setPortMap(Map<String, CommPortIdentifier> portMap) {
        this.portMap = portMap;
    }
}
