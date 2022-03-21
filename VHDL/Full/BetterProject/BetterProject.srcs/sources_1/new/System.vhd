library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

entity System is
    port(
        clock : in std_logic;
        reset_h : in std_logic;
        
        --UART
        tx : out std_logic;
        rx : in std_logic;
        
        --ps2
        ps2_clk : in std_logic;
        ps2_data : in std_logic;
        
        --LCD
        sda : inout std_logic;
        scl : inout std_logic 
    );
end System;

architecture Behavioral of System is

component LCD_Transmitter is
	GENERIC (
		CONSTANT input_clock : integer := 125_000_000); 
	PORT(
		clk       : IN    STD_LOGIC;                     --system clock
		reset_n   : IN    STD_LOGIC;
        indata	  : IN    STD_LOGIC_VECTOR(255 downto 0);
        
--		run_clk   : IN    STD_LOGIC;  --is the clock on
--		adc_sel   : IN    STD_LOGIC_VECTOR(1 DOWNTO 0);
		
		sda       : inout std_logic;                     --i2c data
		scl       : inout std_logic                      --i2c clock
    );                   
END component;

component ps2_keyboard_to_ascii IS
  GENERIC(
      clk_freq                  : INTEGER := 125_000_000; --system clock frequency in Hz
      ps2_debounce_counter_size : INTEGER := 9);         --set such that 2^size/clk_freq = 5us (size = 8 for 50MHz)
  PORT(
      clk        : IN  STD_LOGIC;                     --system clock input
      ps2_clk    : IN  STD_LOGIC;                     --clock signal from PS2 keyboard
      ps2_data   : IN  STD_LOGIC;                     --data signal from PS2 keyboard
      ascii_new  : OUT STD_LOGIC;                     --output flag indicating new ASCII value
      ascii_code : OUT STD_LOGIC_VECTOR(7 DOWNTO 0); --ASCII value
      ps2_break : out std_logic);
END component;

component uart_user is
    generic(
        data_length : integer := 32
    );
    port(
        clock : in std_logic;
        reset_n : in std_logic;
        inData : in std_logic_vector(7 downto 0);
        keypressed : in std_logic;
        rx      : in std_logic;
        tx      : out std_logic;        
        odataArray : out std_logic_vector(data_length*8 - 1 downto 0);
        data_valid : out std_logic       
        );
end component;

signal reset_n : std_logic;
signal ascii_data : std_logic_vector(7 downto 0);
signal keyboard_valid : std_logic;
signal lcd_data : std_logic_vector(255 downto 0);
signal uart_valid : std_logic;
signal lcd_reset : std_logic;

signal ascii_to_send : std_logic_vector(7 downto 0);
signal send_uart : std_logic;
signal keyboard_valid_prev : std_logic;

begin

process(clock) begin  
    if rising_edge(clock) then
        if keyboard_valid_prev = '0' and keyboard_valid = '1' then
            send_uart <= '1';
        else
            send_uart <= '0';
        end if;
        --send_uart <= (not keyboard_valid_prev) and keyboard_valid;
        --send_uart <= keyboard_valid_prev and (not keyboard_valid);
        keyboard_valid <= keyboard_valid_prev;
    end if;
end process;

reset_n <= not reset_h;
lcd_reset <= not(reset_h or uart_valid);

Inst_LCD: LCD_Transmitter 
	PORT MAP(
		clk       => clock,                     --system clock
		reset_n   => reset_n,
        indata	  => lcd_data,
        
--		run_clk   : IN    STD_LOGIC;  --is the clock on
--		adc_sel   : IN    STD_LOGIC_VECTOR(1 DOWNTO 0);
		
		sda       => sda,                     --i2c data
		scl       => scl                     --i2c clock
    );                   

Inst_keyboard: ps2_keyboard_to_ascii
  PORT MAP(
      clk        => clock,                    --system clock input
      ps2_clk    => ps2_clk,                 --clock signal from PS2 keyboard
      ps2_data   => ps2_data,              --data signal from PS2 keyboard
      ascii_new  =>  keyboard_valid_prev,                   --output flag indicating new ASCII value
      ascii_code =>  ascii_data,                  --ASCII value
      ps2_break => open
    );

Inst_uart: uart_user
    port map(
        clock => clock,
        reset_n => reset_n,
        inData => ascii_data,
        keypressed => send_uart,
        rx      => rx,
        tx      => tx,    
        odataArray => lcd_data,
        data_valid => uart_valid
     );


end Behavioral;
