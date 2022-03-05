/* EE260_Project5_1.c UART on USART2 Receive at 9600 Baud
 *
 * Receive key strokes from terminal emulator (TeraTerm, hterm, or putty) of the
 * host PC to the USART2 of the Nucleo-F446RE board. 
 * USART2 is connected to the ST-Link virtual COM port.
 * Launch terminal emulator on a PC and hit any key.
 * By default, the clock is running at 16 MHz.
 * The UART2 is configured for 9600 Baud.
 * PA3 - USART2 RX (AF7)
 *
 * This program was tested with Keil uVision v5.24a with DFP v2.11.0
 */
#include "stm32F4xx.h"
#include "string.h"

void delay(void);
//void delayMs(int n);
void keypad_init(void);
char keypad_getkey(void);
char hexKeys[] = {0x0,'D','E','0','F','C','9','8','7','B','6','5','4','A','3','2','1'};
//char keypressed;

void USART2_init(void);
char USART2_read(void);
void USART2_write(int);
void USART2_write_ln(char[]);
void delayMs(int);

/*----------------------------------------------------------------------------
  MAIN function
 *----------------------------------------------------------------------------*/
int main (void) {
		char lineA[] = "Welcome to EE260";
		char lineB[] = "Embedded System";
		char c;
	
    keypad_init();

    USART2_init();                  /* initialize USART2 */


    //USART2_write_ln(lineA);
		//USART2_write_ln(lineB);
		
		while(1) {
			c = USART2_read();
			USART2_write(c);
		}
	
    }

/*----------------------------------------------------------------------------
  Initialize UART pins, Baudrate
 *----------------------------------------------------------------------------*/
void USART2_write_ln(char line[])
{
	int i;
	
	for(i = 0; i < strlen(line); i++)   {
		USART2_write(line[i]);
	}
}

void USART2_init (void) {
    RCC->AHB1ENR |= 1;          /* Enable GPIOA clock */
    RCC->APB1ENR |= 0x20000;    /* Enable USART2 clock */

    /* Configure PA3 for USART2 RX */
    GPIOA->AFR[0] &= ~0xF000;
    GPIOA->AFR[0] |=  0x7000;   /* alt7 for USART2 */
    GPIOA->MODER  &= ~0x00C0;
    GPIOA->MODER  |=  0x0080;   /* enable alternate function for PA3 */
	
	  /* Configure PA2 for USART2_TX */
    GPIOA->AFR[0] &= ~0x0F00;
    GPIOA->AFR[0] |=  0x0700;   /* alt7 for USART2 */
    GPIOA->MODER  &= ~0x0030;
    GPIOA->MODER  |=  0x0020;   /* enable alternate function for PA2 */

	  /* configure UART2, Rx and TX */
	  USART2->BRR = 0x0683;       /* 9600 baud @ 16 MHz */
    USART2->CR1 = 0x0004;       /* enable Rx, 8-bit data */
    USART2->CR1 |= 0x0008;      /* enable Tx, 8-bit data */	
    USART2->CR2 = 0x0000;       /* 1 stop bit */
    USART2->CR3 = 0x0000;       /* no flow control */
    USART2->CR1 |= 0x2000;      /* enable USART2 */
			
}

/* Read a character from USART2 */
char USART2_read(void) {
    while (!(USART2->SR & 0x0020)) {}   // wait until char arrives
    return USART2->DR;
}

/* Write a character to USART2 */
void USART2_write (int ch) {
    while (!(USART2->SR & 0x0080)) {}   // wait until Tx buffer empty
    USART2->DR = (ch & 0xFF);
}

void delayMs(int n) {
    int i;
    for (; n > 0; n--)
        for (i = 0; i < 3591; i++) ;
} 

/* this function initializes PC0-3 (column) and PC4-7 (row).
 * The column pins need to have the pull-up resistors enabled.
 */
void keypad_init(void) {
    RCC->AHB1ENR |=  0x04;	        /* enable GPIOC clock */
    GPIOC->MODER &= ~0x0000FFFF;    /* clear pin mode to input */
    GPIOC->PUPDR =   0x00000055;    /* enable pull up resistors for column pins */
}
/*
 * This is a non-blocking function to read the keypad.
 * If a key is pressed, it returns a unique code for the key.
 * Otherwise, a zero is returned.
 * PC0-3 are used as input and connected to the columns. Pull-up resistors are
 * enabled so when the keys are not pressed, these pins are pulled high.
 * PC4-7 are used as output that drives the keypad rows.
 * First, all rows are driven low and the input pins are read. If no key is
 * pressed, they will read as all one because of the pull up resistors.
 * If they are not all one, some key is pressed.
 * If some key is pressed, the program proceeds to drive only one row low at
 * a time and leave the rest of the rows inactive (float) then read the input pins.
 * Knowing which row is active and which column is active, the program can decide
 * which key is pressed.
 *
 * Only one row is driven so that if multiple keys are pressed and row pins are
 * shorted, the microcontroller will not be damaged. When the row is being
 * deactivated, it is driven high first otherwise the stray capacitance may keep
 * the inactive row low for some time.
 */
char keypad_getkey(void) {
    int row, col;
    const int row_mode[] = {0x00000100, 0x00000400, 0x00001000, 0x00004000}; /* one row is output */
    const int row_low[] =  {0x00100000, 0x00200000, 0x00400000, 0x00800000}; /* one row is low */
    const int row_high[] = {0x00000010, 0x00000020, 0x00000040, 0x00000080}; /* one row is high */

    /* check to see any key pressed */
    GPIOC->MODER = 0x00005500;      /* make all row pins output */
    GPIOC->BSRR =  0x00F00000;      /* drive all row pins low */
		delayMs(1);		
    //delay();                        /* wait for signals to settle */
    col = GPIOC->IDR & 0x000F;      /* read all column pins */
    GPIOC->MODER &= ~0x0000FF00;    /* disable all row pins drive */
    if (col == 0x000F)              /* if all columns are high */
        return 0;                       /* no key pressed */

    /* If a key is pressed, it gets here to find out which key.
     * It activates one row at a time and read the input to see
     * which column is active. */
    for (row = 0; row < 4; row++) {
        GPIOC->MODER &= ~0x0000FF00;    /* disable all row pins drive */
        GPIOC->MODER |= row_mode[row];  /* enable one row at a time */
        GPIOC->BSRR = row_low[row];     /* drive the active row low */
			  delayMs(1);
        //delay();                        /* wait for signal to settle */
        col = GPIOC->IDR & 0x000F;      /* read all columns */
        GPIOC->BSRR = row_high[row];    /* drive the active row high */
        if (col != 0x000F) break;       /* if one of the input is low, some key is pressed. */
    }
    GPIOC->BSRR = 0x000000F0;           /* drive all rows high before disable them */
    GPIOC->MODER &= ~0x0000FF00;        /* disable all rows */
    if (row == 4)
        return 0;                       /* if we get here, no key is pressed */

    /* gets here when one of the rows has key pressed, check which column it is */
    if (col == 0x000E) return row * 4 + 1;    /* key in column 0 */
    if (col == 0x000D) return row * 4 + 2;    /* key in column 1 */
    if (col == 0x000B) return row * 4 + 3;    /* key in column 2 */
    if (col == 0x0007) return row * 4 + 4;    /* key in column 3 */

    return 0;   /* just to be safe */
}
/* make a small delay */
void delay(void) {
    int i;
    for (i = 0; i < 20; i++) ;
}
