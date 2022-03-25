
package comports;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import gui.Window;
import javafx.application.Platform;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class ComInterface implements Runnable {
    /**
     * The SerialPort used
     */
    private SerialPort port;
    /**
     * The InputStream associated with this ComInterface's SerialPort
     */
    private InputStream inputStream;
    /**
     * The Output Stream associated with this ComInterface's SerialPort.
     */
    private OutputStream outputStream;
    /**
     * The window currently interacting with this ComInterface
     */
    private Window window;

    /**
     * Sets up a ComInterface with the given COM Port and Baud Rate
     *
     * @param portIdentifier The Port Identifier associated with the desired COM port.
     * @param baud The baud rate to use with this SerialPort
     */
    public ComInterface(String portIdentifier, int baud) {
        this.port = SerialPort.getCommPort(portIdentifier);
        this.port.setBaudRate(baud);
        this.port.openPort();

        /* ***** FOR JAVA ***** */
        this.inputStream = System.in;
        this.outputStream = System.out;

        /* ***** FOR FPGA ***** */
        //this.inputStream = this.port.getInputStream();
        //this.outputStream = this.port.getOutputStream();
    }

    public ComInterface() {
        this.port = null;

        this.inputStream = System.in;
        this.outputStream = System.out;
    }

    /**
     * Get a list of every available SerialPort's Port Identifier
     *
     * @return An ArrayList of Strings which represent the DescriptivePortName of each SerialPort connected to the system.
     */
    public static ArrayList<String> getComPorts() {
        ArrayList<String> portNames = new ArrayList<>();

        SerialPort[] ports = SerialPort.getCommPorts();
        for(SerialPort port : ports)
            portNames.add(port.getSystemPortName());

        return portNames;
    }

    /**
     * Send data down this ComInterface's outputStream.
     *
     * @param data The data to send
     */
    public synchronized void sendData(char[] data) {
        for(char c : data) {
            try {
                if(c == '\0') c = ' ';
                outputStream.write((byte) c);
                outputStream.flush();
            } catch(IOException ioe) {
                System.err.println("IOException in ComInterface's sendData: " + ioe.getMessage());
            }
        }
    }

    /**
     * Updates this ComInterface's window.
     *
     * @param window
     */
    public synchronized void updateWindow(Window window) {
        this.window = window;
    }

    /**
     * Runs a thread which operates on the streams of this SerialPort.
     */
    @Override
    public void run() {
        this.port.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
            }

            @Override
            public void serialEvent(SerialPortEvent serialPortEvent) {
                Window oldWindow = window;
                byte[] newData = serialPortEvent.getReceivedData();
                for (byte currentByte : newData) {
                    if(oldWindow == window)
                        writeData((char)currentByte);
                }
            }
        });

        /*
        Scanner sc = new Scanner(this.inputStream);

        while(sc.hasNext()) {
            String s = sc.next().toUpperCase();
            for(int i = 0; i < s.length(); i++) {
                int tmpi = i;
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        window.keyPressed(s.charAt(tmpi));
                    }
                });
            }
        }
        System.out.println("closing scanner");
        sc.close();
        */
    }

    /**
     * Send a received character to the currently active Window.
     *
     * @param c The character just received.
     */
    private synchronized void writeData(char c) {
        if(Character.isAlphabetic(c))
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    window.keyPressed(Character.toUpperCase(c));
                }
            });
    }

    /**
     * Closes this ComInterface's SerialPort
     */
    public void closePort() {
        if(this.port != null)
            this.port.closePort();
    }
}