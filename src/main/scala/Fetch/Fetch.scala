package Fetch

import chisel3._
import chisel3.util._

class Fetch_IO extends Bundle
{
    // Input pins
    val nPC_in: UInt = Input(UInt(32.W))
    val nPC_en: Bool = Input(Bool())
    val forward_PC_in = Input(Bool())
    val HazardDetection_PC_in = Input(UInt(32.W))

    // Output pins
    val inst_out: UInt = Output(UInt(24.W))
    val PC_out: UInt = Output(UInt(32.W))
    val nPC_out: UInt = Output(UInt(32.W))
}
class Fetch extends Module
{
    // Initializing the signals and modules
    val io: Fetch_IO = IO(new Fetch_IO)
    val PC: UInt = dontTouch(RegInit(0.U(32.W)))
    val nPC_in: UInt = dontTouch(WireInit(io.nPC_in))
    val nPC_en: Bool = dontTouch(WireInit(io.nPC_en))
    val nPC: UInt = dontTouch(WireInit(PC + 4.U))
    val inst_out: UInt = dontTouch(WireInit(Cat(PC(25, 2))))
    val PC_out: UInt = dontTouch(WireInit(PC))
    val nPC_out: UInt = dontTouch(WireInit(PC + 4.U))
    val forward_PC_in = dontTouch(WireInit(io.forward_PC_in))
    val HazardDetection_PC_in = dontTouch(WireInit(io.HazardDetection_PC_in))
    
    // Wiring the outputs
    io.inst_out := inst_out
    io.PC_out := PC_out
    io.nPC_out := nPC
    
    // Next instruction address
    PC := Mux(forward_PC_in, HazardDetection_PC_in, Mux(
        io.nPC_en, io.nPC_in, nPC
    ))
}
