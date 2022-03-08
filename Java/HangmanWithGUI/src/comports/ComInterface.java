package comports;

import com.fazecast.jSerialComm.SerialPort;

import java.util.ArrayList;

public class ComInterface implements Runnable {
    /**
     * The SerialPort used
     */
    private SerialPort port;

    public ComInterface(String portIdentifier, int baud) {
        this.port = SerialPort.getCommPort(portIdentifier);
        this.port.setBaudRate(baud);
    }

    public void closePort() {
        this.port.closePort();
    }

    public static ArrayList<String> getComPorts() {
        ArrayList<String> portNames = new ArrayList<String>();

        SerialPort[] ports = SerialPort.getCommPorts();
        for(SerialPort port : ports)
            portNames.add(port.getDescriptivePortName());

        return portNames;
    }

    /**
     * Runs a thread which operates on the streams of this SerialPort.
     */
    @Override
    public void run() {

    }
}
