package comports;

import com.fazecast.jSerialComm.SerialPort;

import java.util.ArrayList;

public class comInterface {
    public static ArrayList<String> getComPorts() {
        ArrayList<String> portNames = new ArrayList<String>();

        SerialPort[] ports = SerialPort.getCommPorts();
        for(SerialPort port : ports)
            portNames.add(port.getPortDescription());

        return portNames;
    }
}
