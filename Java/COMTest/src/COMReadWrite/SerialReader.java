package COMReadWrite;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public  class SerialReader implements Runnable {
    InputStream in;

    /**
     * Create a SerialReader object around an InputStream
     * @param in
     */
    public SerialReader ( InputStream in ) {
        this.in = in;
    }

    /**
     * Create a new thread which continuously reads from the Serial Port
     */
    public void run () {
        byte[] buffer = new byte[1024];
        int len = -1;
        try {
            while ( ( len = this.in.read(buffer)) > -1 ) {
                System.out.print(new String(buffer,0,len, StandardCharsets.US_ASCII));
            }
        }
        catch ( IOException e ) {
            e.printStackTrace();
        }
    }
}