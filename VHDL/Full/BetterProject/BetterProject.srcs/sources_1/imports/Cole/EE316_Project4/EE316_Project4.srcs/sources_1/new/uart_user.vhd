library IEEE;
use IEEE.STD_LOGIC_1164.ALL;


entity uart_user is
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
end uart_user;

architecture Behavioral of uart_user is

component uart IS
  GENERIC(
    clk_freq  :  INTEGER    := 125_000_000;  --frequency of system clock in Hertz
    baud_rate :  INTEGER    := 9_600;      --data link baud rate in bits/second
    os_rate   :  INTEGER    := 16;          --oversampling rate to find center of receive bits (in samples per baud period)
    d_width   :  INTEGER    := 8;           --data bus width
    parity    :  INTEGER    := 0;           --0 for no parity, 1 for parity
    parity_eo :  STD_LOGIC  := '0');        --'0' for even, '1' for odd parity
  PORT(
    clk      :  IN   STD_LOGIC;                             --system clock
    reset_n  :  IN   STD_LOGIC;                             --ascynchronous reset
    tx_ena   :  IN   STD_LOGIC;                             --initiate transmission
    tx_data  :  IN   STD_LOGIC_VECTOR(d_width-1 DOWNTO 0);  --data to transmit
    rx       :  IN   STD_LOGIC;                             --receive pin
    rx_busy  :  OUT  STD_LOGIC;                             --data reception in progress
    rx_error :  OUT  STD_LOGIC;                             --start, parity, or stop bit error detected
    rx_data  :  OUT  STD_LOGIC_VECTOR(d_width-1 DOWNTO 0);  --data received
    tx_busy  :  OUT  STD_LOGIC;                             --transmission in progress
    tx       :  OUT  STD_LOGIC);                            --transmit pin
END component;

type state_type is (init, ready, write);
signal state : state_type := init;

signal tx_en : std_logic;
signal tx_busy : std_logic;
signal rx_busy : std_logic;
signal rx_busy_prev : std_logic;
signal rx_error : std_logic;
signal outData : std_logic_vector(7 downto 0);
signal sel  : integer range 0 to data_length := data_length;
signal oData : std_logic_vector(255 downto 0);



begin

--outData <= outData;
--tx_busy <= tx_busy;
--rx_busy <= rx_busy;
--rx_error <= rx_error;

oDataArray <= oData;

process(clock) 
begin
    if rising_edge(clock) then
        rx_busy <= rx_busy_prev;
        if rx_busy_prev = '0' and rx_busy = '1' then
            oData(255 downto 8) <= oData(247 downto 0);
            oData(7 downto 0) <= outData;
            data_valid <= '1';
        else
            data_valid <= '0';
        end if;
    
    
        if reset_n = '0' then
            sel <= data_length;
            state <= init;
            oData <= (x"2020202020202020202020202020202020202020202020202020202020202020");
            data_valid <= '0';
        else
            case(state) is 
                when init => 
                    data_valid <= '0';
                    tx_en <= '0';
                    state <= ready;
                    
                when ready => 
--                    if rx_busy = '1' then
--                        state <= read;
--                    end if;
                    if keypressed = '1' then
                        state <= write;
                        tx_en <= '1';
                    else
                        state <= ready;
                    end if;
                    
--                when read => 
--                    if rx_busy = '0' and rx_error = '0' then
--                        if sel /= 1 then
--                            --odataArray(sel*8 - 1 downto (sel-1)*8) <= outData;
--                            sel <= sel - 1;
--                        else
--                            sel <= data_length;
--                            data_valid <= '1';
--                            state <= ready;
--                        end if;
--                    end if;
                    
                when write =>
                    tx_en <= '0';
                    if tx_busy = '1' then
                        state <= write;
                    else
                        state <= ready;
                    end if; 
            end case;
        end if;
    end if;
end process;

--process(rx_busy)
--begin
--    if falling_edge(rx_busy) and rx_error = '0' then
--        if sel /= 0 then
--            odataArray(sel*8 downto (sel-1)*8 + 1) <= outData;
--            sel <= sel - 1;
--        else
--            sel <= data_length;
--        end if;
   
--    end if;
--end process;


--process(keypressed)
--begin
--    if keypressed = '1' then
--        if tx_busy = '0' then
--            tx_en <= '1';
--        end if;    
--    else
--        tx_en <= '0';    
--    end if;
--end process;

Inst_uart: uart
  PORT MAP(
    clk      =>  clock,                           --system clock
    reset_n  =>  reset_n,                           --ascynchronous reset
    tx_ena   =>  tx_en,                           --initiate transmission
    tx_data  =>  inData,                           --data to transmit
    rx       =>  rx,                           --receive pin
    rx_busy  =>  rx_busy_prev,                           --data reception in progress
    rx_error =>  rx_error,                          --start, parity, or stop bit error detected
    rx_data  =>  outData,                           --data received
    tx_busy  =>  tx_busy,                           --transmission in progress
    tx       =>  tx                          --transmit pin
    );
    


end Behavioral;
