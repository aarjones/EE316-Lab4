package comports;

import java.util.Arrays;

public class LcdController implements Runnable {
    private static final int LCD_SIZE = 16;

    /**
     * The ComInterface to which this LCD Controller should write
     */
    private ComInterface comPort;
    /**
     * The top line to display on the LCD
     */
    private char[] topLine;
    /**
     * The bottom line to display on the LCD
     */
    private char[] bottomLine;

    /**
     * Create a new LcdController which will write topLine and bottomLine to the LCD connected via the given ComInterface
     *
     * @param comPort The ComInterface to which this LCD controller will write.
     * @param topLine The topLine to display on the LCD
     * @param bottomLine The bottomLine to display on the LCD
     */
    public LcdController(ComInterface comPort, String topLine, String bottomLine) {
        this.comPort = comPort;

        //initialize the arrays
        int lengthTop = topLine.length();
        int lengthBottom = bottomLine.length();

        if(Math.max(lengthTop, lengthBottom) > LCD_SIZE) {
            this.topLine = new char[Math.max(lengthTop, lengthBottom)];
            this.bottomLine = new char[Math.max(lengthTop, lengthBottom)];
        } else {
            this.topLine = new char[LCD_SIZE];
            this.bottomLine = new char[LCD_SIZE];
        }

        //fill them with blanks
        Arrays.fill(this.topLine, ' ');
        Arrays.fill(this.bottomLine, ' ');

        //set their contents to be equal to the provided Strings
        for(int i = 0; i < topLine.length(); i++) {
            this.topLine[i] = topLine.charAt(i);
        }

        for(int i = 0; i < bottomLine.length(); i++) {
            this.bottomLine[i] = bottomLine.charAt(i);
        }
    }

    @Override
    public void run() {
        updateLCD();
    }

    private void updateLCD() {
        if(this.topLine.length > 16) {
            for(int i = 0; i <= this.topLine.length - 16; i++) {
                //set up lines to write on this iteration
                char[] top = new char[16];
                char[] bottom = new char[16];
                for(int j = 0; j < 16; j++) {
                    top[j] = this.topLine[i + j];
                    bottom[j] = this.bottomLine[i + j];
                }

                //write them
                writeOnce(top, bottom);

                //and add a delay
                try {
                    if(i == 0)
                        Thread.sleep(2000);
                    else
                        Thread.sleep(250);
                } catch(InterruptedException ie) {
                    System.err.println("Error in updateLCD(): " + ie.getMessage());
                }
            }
        } else {
            writeOnce(this.topLine, this.bottomLine);
        }
    }

    private void writeOnce(char[] top, char[] bottom) {
        char[] total = new char[32];
        for(int i = 0; i < 31; i++) {
            if(i <= 15)
                total[i] = top[i];
            else
                total[i] = bottom[i - 16];
        }

        this.comPort.sendData(total);
    }
}
