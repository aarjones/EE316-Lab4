library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

entity top_level is
  Port ( 
    --INS
    
    --OUTS
  
  );
end top_level;

architecture Behavioral of top_level is
--components
component debounce IS
  GENERIC(
    counter_size  :  INTEGER := 19); --counter size (19 bits gives 10.5ms with 50MHz clock)
  PORT(
    clk     : IN  STD_LOGIC;  --input clock
    button  : IN  STD_LOGIC;  --input signal to be debounced
    result  : OUT STD_LOGIC); --debounced signal
END component;

component ps2_keyboard IS
  GENERIC(
    clk_freq              : INTEGER := 50_000_000; --system clock frequency in Hz
    debounce_counter_size : INTEGER := 8);         --set such that (2^size)/clk_freq = 5us (size = 8 for 50MHz)
  PORT(
    clk          : IN  STD_LOGIC;                     --system clock
    ps2_clk      : IN  STD_LOGIC;                     --clock signal from PS/2 keyboard
    ps2_data     : IN  STD_LOGIC;                     --data signal from PS/2 keyboard
    ps2_code_new : OUT STD_LOGIC;                     --flag that new PS/2 code is available on ps2_code bus
    ps2_code     : OUT STD_LOGIC_VECTOR(7 DOWNTO 0)); --code received from PS/2
END component;

component ps2_keyboard_to_ascii IS
  GENERIC(
      clk_freq                  : INTEGER := 50_000_000; --system clock frequency in Hz
      ps2_debounce_counter_size : INTEGER := 8);         --set such that 2^size/clk_freq = 5us (size = 8 for 50MHz)
  PORT(
      clk        : IN  STD_LOGIC;                     --system clock input
      ps2_clk    : IN  STD_LOGIC;                     --clock signal from PS2 keyboard
      ps2_data   : IN  STD_LOGIC;                     --data signal from PS2 keyboard
      ascii_new  : OUT STD_LOGIC;                     --output flag indicating new ASCII value
      ascii_code : OUT STD_LOGIC_VECTOR(6 DOWNTO 0)); --ASCII value
END component;

--signals

begin

--inst
Inst_debounce: debounce
  GENERIC map(
    counter_size  => 
  PORT map(
    clk     =>
    button  =>
    result  =>
    ); 
    
 Inst_ps2_keyboard: ps2_keyboard
  GENERIC map(
    clk_freq              =>
    debounce_counter_size =>
  PORT map(
    clk          =>
    ps2_clk      =>
    ps2_data     =>
    ps2_code_new =>
    ps2_code     =>
    ); 
    
 Inst_ps2_keyboard_to_ascii: ps2_keyboard_to_ascii
  GENERIC map(
      clk_freq                  =>
      ps2_debounce_counter_size =>
  PORT map(
      clk        =>
      ps2_clk    =>
      ps2_data   =>
      ascii_new  =>
      ascii_code =>
      );

end Behavioral;
