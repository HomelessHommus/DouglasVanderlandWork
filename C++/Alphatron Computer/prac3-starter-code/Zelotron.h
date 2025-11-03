//
// Created by richa on 21/03/2024.
//

#ifndef NEW_PRAC3_ZELOTRON_H
#define NEW_PRAC3_ZELOTRON_H

#include <string>

class Zelotron {
public:
    void memory_dump();
    void read_program();
    int run_program();

private:
    // INSTRUCTION SET OPCODES
    static const int ADD = 10;
    static const int SUB = 11;
    static const int LOAD = 12;
    static const int STORE = 13;
    static const int INC = 20;
    static const int DEC = 21;
    static const int OUTPUT = 30;
    static const int INPUT = 31;
    static const int JUMP = 40;
    static const int JUMPPOS = 41;
    static const int JUMPZERO = 42;
    static const int HALT = 99;
    static const int SKIP = 0;  // 00 - not really used

    // The following function may be useful for debugging
    static std::string opcode_to_mnemonic(int opcode) {
        switch(opcode){
            case ADD: return "ADD"; break;
            case SUB: return "SUB"; break;
            case LOAD: return "LOAD"; break;
            case STORE: return "STORE"; break;
            case INC: return "INC"; break;
            case DEC: return "DEC"; break;
            case OUTPUT: return "OUTPUT"; break;
            case INPUT: return "INPUT"; break;
            case JUMP: return "JUMP"; break;
            case JUMPPOS: return "JUMPPOS"; break;
            case JUMPZERO: return "JUMPZERO"; break;
            case HALT: return "HALT"; break;
            case SKIP: return "SKIP"; break;
            default: return "INVALID OPCODE";
        }
    }

    // REGISTERS
    int accumulator = 0;    // General-purpose computer register
    int program_counter = 0;// the line in the program to execute next

    // INTERNAL STORAGE IN THE COMPUTER  (how should these arrays be initialised?)
    int memory[100];          // input memory

};


#endif //NEW_PRAC3_ZELOTRON_H
