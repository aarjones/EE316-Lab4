# 
# Synthesis run script generated by Vivado
# 

set TIME_start [clock seconds] 
proc create_report { reportName command } {
  set status "."
  append status $reportName ".fail"
  if { [file exists $status] } {
    eval file delete [glob $status]
  }
  send_msg_id runtcl-4 info "Executing : $command"
  set retval [eval catch { $command } msg]
  if { $retval != 0 } {
    set fp [open $status w]
    close $fp
    send_msg_id runtcl-5 warning "$msg"
  }
}
set_param xicom.use_bs_reader 1
create_project -in_memory -part xc7z010clg400-1

set_param project.singleFileAddWarning.threshold 0
set_param project.compositeFile.enableAutoGeneration 0
set_param synth.vivado.isSynthRun true
set_property webtalk.parent_dir C:/Users/jones/Desktop/EE316-Lab4/VHDL/Full/BetterProject/BetterProject.cache/wt [current_project]
set_property parent.project_path C:/Users/jones/Desktop/EE316-Lab4/VHDL/Full/BetterProject/BetterProject.xpr [current_project]
set_property default_lib xil_defaultlib [current_project]
set_property target_language VHDL [current_project]
set_property ip_output_repo c:/Users/jones/Desktop/EE316-Lab4/VHDL/Full/BetterProject/BetterProject.cache/ip [current_project]
set_property ip_cache_permissions {read write} [current_project]
read_vhdl -library xil_defaultlib {
  C:/Users/jones/Desktop/EE316-Lab4/VHDL/Full/BetterProject/BetterProject.srcs/sources_1/imports/Cole/Documents/GitHub/EE316-Lab4/VHDL/Full/LCD_Transmitter.vhd
  C:/Users/jones/Desktop/EE316-Lab4/VHDL/Full/BetterProject/BetterProject.srcs/sources_1/imports/Cole/Documents/GitHub/EE316-Lab4/VHDL/Keyboard/project_4.srcs/sources_1/new/debounce.vhd
  C:/Users/jones/Desktop/EE316-Lab4/VHDL/Full/BetterProject/BetterProject.srcs/sources_1/imports/Lab2/i2c_master.vhd
  C:/Users/jones/Desktop/EE316-Lab4/VHDL/Full/BetterProject/BetterProject.srcs/sources_1/imports/Cole/Documents/GitHub/EE316-Lab4/VHDL/Keyboard/project_4.srcs/sources_1/new/ps2_keyboard.vhd
  C:/Users/jones/Desktop/EE316-Lab4/VHDL/Full/BetterProject/BetterProject.srcs/sources_1/imports/Cole/Documents/GitHub/EE316-Lab4/VHDL/Keyboard/project_4.srcs/sources_1/new/ps2_keyboard_to_ascii.vhd
  C:/Users/jones/Desktop/EE316-Lab4/VHDL/Full/BetterProject/BetterProject.srcs/sources_1/imports/Cole/EE316_Project4/uart.vhd
  C:/Users/jones/Desktop/EE316-Lab4/VHDL/Full/BetterProject/BetterProject.srcs/sources_1/imports/Cole/EE316_Project4/EE316_Project4.srcs/sources_1/new/uart_user.vhd
  C:/Users/jones/Desktop/EE316-Lab4/VHDL/Full/BetterProject/BetterProject.srcs/sources_1/new/System.vhd
}
# Mark all dcp files as not used in implementation to prevent them from being
# stitched into the results of this synthesis run. Any black boxes in the
# design are intentionally left as such for best results. Dcp files will be
# stitched into the design at a later time, either when this synthesis run is
# opened, or when it is stitched into a dependent implementation run.
foreach dcp [get_files -quiet -all -filter file_type=="Design\ Checkpoint"] {
  set_property used_in_implementation false $dcp
}
read_xdc C:/Users/jones/Desktop/EE316-Lab4/VHDL/Full/BetterProject/BetterProject.srcs/constrs_1/imports/digilent-xdc-master/Cora-Z7-10-Master.xdc
set_property used_in_implementation false [get_files C:/Users/jones/Desktop/EE316-Lab4/VHDL/Full/BetterProject/BetterProject.srcs/constrs_1/imports/digilent-xdc-master/Cora-Z7-10-Master.xdc]

set_param ips.enableIPCacheLiteLoad 1
close [open __synthesis_is_running__ w]

synth_design -top System -part xc7z010clg400-1


# disable binary constraint mode for synth run checkpoints
set_param constraints.enableBinaryConstraints false
write_checkpoint -force -noxdef System.dcp
create_report "synth_1_synth_report_utilization_0" "report_utilization -file System_utilization_synth.rpt -pb System_utilization_synth.pb"
file delete __synthesis_is_running__
close [open __synthesis_is_complete__ w]
