package COMAvailable;

import gnu.io.*;  //Javadocs for this plugin: https://www.docjava.com/book/cgij/jdoc/gnu/io/SerialPort.html

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;



public class COMAvailableTest {
    //COM Port Stats
    public static final int BAUD_RATE = 9600;

    //Error Codes
    public static final int BAD_PORT_ERROR = 1;
    public static final int COM_WRITE_ERROR = 2;
    public static final int COM_READ_ERROR = 3;

    /**
     * Get an ArrayList of Strings with the name of every available COM port.
     *
     * @return An ArrayList of each connected COM Port
     */
    public static ArrayList<String> getAvailablePorts() {
        Enumeration<CommPortIdentifier> portList = CommPortIdentifier.getPortIdentifiers();
        ArrayList<String> portNames = new ArrayList<String>();

        while(portList.hasMoreElements()) {
            portNames.add(portList.nextElement().getName());
        }

        return portNames;
    }

    /**
     * Close a given SerialPort.  Sets argument to null.
     *
     * @param serialPort The SerialPort to close
     */
    public static void closePort(SerialPort serialPort) {
        if(serialPort != null)
            serialPort.close();
        serialPort = null;
    }

    /**
     * Writes data to a COM port.
     * @param port The COM port down which to write
     * @param data The data to send.
     */
    public static void writeToCOM(SerialPort port, byte[] data) {
        OutputStream outStream = null;
        try {
            outStream = port.getOutputStream(); //open the output stream to the com port
            outStream.write(data);              //write the data
            outStream.flush();                  //and then flush it
            outStream.close();                  //and close it
        } catch (IOException ioe) {
            System.err.println("Error in writeToCOM: " + ioe.toString());
            System.exit(COM_WRITE_ERROR);
        }
    }

    public static byte[] readFromCOM(SerialPort port) {
        InputStream inStream = null;
        byte[] dataRead = null;

        try {
            //Open an input stream from the COM port, and attempt to read from it
            inStream = port.getInputStream();
            System.out.println("1");
            int bufferLength = inStream.available();
            if(bufferLength != 0) {
                System.out.println("2");
                dataRead = new byte[bufferLength];
                System.out.println("3");
                inStream.read(dataRead);
                System.out.println("4");
            }
        } catch(IOException ioe) {
            System.err.println("Error in readFromCOM: " + ioe.toString());
            System.exit(COM_READ_ERROR);
        }

        System.out.println("5");
        return dataRead;
    }

    public static void main(String[] args) {
        ArrayList<String> availablePorts = getAvailablePorts();

        for(String portName: availablePorts) {
            System.out.println("Available Port: " + portName);
        }
    }
}
