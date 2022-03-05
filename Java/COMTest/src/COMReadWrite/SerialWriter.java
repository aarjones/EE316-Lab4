package COMReadWrite;

import java.io.IOException;
import java.io.OutputStream;

public class SerialWriter implements Runnable {
    OutputStream out;

    /**
     * Create a SerialWriter object around an OuptutStream
     *
     * @param out The stream on which to create the reader
     */
    public SerialWriter ( OutputStream out ) {
        this.out = out;
    }

    /**
     * Start a new thread on which to read from the Serial Port.
     */
    public void run () {
        try {
            int c = 0;
            while ( ( c = System.in.read()) > -1 ) {
                this.out.write(c);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}