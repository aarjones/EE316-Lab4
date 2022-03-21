import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;

public class ReadWrite {
    /**
     * Sends System.in to COM Port (defined on Line 17), and reads it back.  Read is based on event listener, and is essentially interrupt.
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        SerialPort comPort = SerialPort.getCommPort("COM18");
        comPort.openPort();
        comPort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_RECEIVED; }
            @Override
            public void serialEvent(SerialPortEvent event)
            {
                byte[] newData = event.getReceivedData();
                //System.out.println("Received data of size: " + newData.length);
                for (int i = 0; i < newData.length; ++i)
                    System.out.print((char)newData[i]);
                //System.out.println("\n");
            }
        });

        OutputStream out = comPort.getOutputStream();
        Scanner input = new Scanner(System.in);

        while(true) {
            if(input.hasNextLine()) {
                String s = input.nextLine();
                for(int i = 0; i < s.length(); i++)
                    out.write(s.charAt(i));
                System.out.println();
            }
        }
    }
}
