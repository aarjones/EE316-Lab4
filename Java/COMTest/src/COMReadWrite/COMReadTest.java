package COMReadWrite;

import gnu.io.*;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serial;

public class COMReadTest {
    //COM Port Stats
    public static final int BAUD_RATE = 9600;

    //Error Codes
    public static final int BAD_PORT_ERROR = 1;

    /**
     * Opens a SerialPort
     *
     * @param portName A String denoting which port to open (eg. COM3)
     * @param baud The baud rate to use
     * @param dataBits The number of data bits to use
     * @param stopbits The number of stop bits to use
     * @param parity The number of parity bits to use
     *
     * @return The opened SerialPort
     */
    public static SerialPort openPort(String portName, int baud, int dataBits, int stopbits, int parity) {
        try {
            //Open the COM Port
            CommPortIdentifier portID = CommPortIdentifier.getPortIdentifier(portName);
            CommPort commPort = portID.open(portName, baud);

            //Try and open the COM port as a Serial Port
            if(commPort instanceof SerialPort serialPort) {
                serialPort.setSerialPortParams(baud, dataBits, stopbits, parity);
                return serialPort;
            } else {
                System.err.println("Error in openPort: " + portName + "is not a Serial Port.");
                System.exit(BAD_PORT_ERROR);
            }

            //Catch exceptions
        } catch(NoSuchPortException nsp) {
            System.err.println("Error in openPort: " + portName + " does not exist.");
            System.exit(BAD_PORT_ERROR);
        } catch(PortInUseException piu) {
            System.err.println("Error in openPort: " + portName + " is in use.");
            System.exit(BAD_PORT_ERROR);
        } catch(UnsupportedCommOperationException uco) {
            System.err.println("Error in openPort: Unsupported COM settings.");
            System.err.println(uco.toString());
            System.exit(BAD_PORT_ERROR);
        }

        //Should never reach this point, but return null in case
        return null;
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

    public static void main(String[] args) {
        SerialPort port = openPort("COM4", BAUD_RATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
        System.out.println("opened");
        InputStream inStream = null;
        OutputStream outStream = null;

        try {
            inStream = port.getInputStream();
            outStream = port.getOutputStream();
            System.out.println("made streams");
        } catch(IOException ioe) {
            System.err.println("IO Exception in main(): " + ioe.getMessage());
        }
        System.out.println("start in");
        (new Thread(new SerialReader(inStream))).start();
        System.out.println("start out");
        (new Thread(new SerialWriter(outStream))).start();
        System.out.println("out open");
        try {
            Thread.sleep(10000);
        } catch(InterruptedException ie) {
            System.err.print("Interrupted: " + ie.getMessage());
        }

        port.close();
        System.out.println("Successfully closed.");

    }
}
