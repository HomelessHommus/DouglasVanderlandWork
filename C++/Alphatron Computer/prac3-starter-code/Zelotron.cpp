//
// Created by richa on 30/03/2024.
//

#include "Zelotron.h"
#include <iostream>
#include <iomanip>
using namespace std;

void Zelotron::memory_dump() {
    char prev = std::cout.fill('0');
    for (int row = 0; row < 10; row++) {
        for (int col = 0; col < 9; col++) {
            std::cout << std::setw(4) << memory[10 * row + col] << " ";
        }
        std::cout << std::setw(4) << memory[10 * row + 9] << std::endl;
    }
    std::cout.fill(prev);
}


void Zelotron::read_program() {
    for (int i = 0; i < 100; i++) memory[i]=0;

    string line;
    int memcounter = 1;
    while (getline(cin, line) && line.find("RUN") != 0) {
        if(line != "RUN") {
            int command = stoi(line);
            memory[memcounter++] = command;
            program_counter++;
        }
    }
//    memory_dump();
}

int Zelotron::run_program() {
    string codeBefore;

    string opcodeMedium;
    string operandMedium;

    int opcode;
    int operand;

    for(int i = 1; i < program_counter; i++) {
        codeBefore = to_string(memory[i]);
        opcodeMedium = codeBefore[0];
        opcodeMedium = opcodeMedium + codeBefore [1];
        opcode = stoi(opcodeMedium);
        // converts first half of code to an opcode int

        operandMedium = codeBefore[2];
        operandMedium = operandMedium + codeBefore[3];
        operand = stoi(operandMedium);
        // converts second half of code to an operand int

        if(opcode == OUTPUT) {
           if(operand == 00) {
               cout << accumulator << endl;
           }
           else {
               cout << memory[operand] << endl;
           }
        }
        else if(opcode == INPUT) {
            if(operand == 00) {
                cin >> accumulator;
            }
            else {
                cin >> memory[operand];
            }
        }
        else if(opcode == INC) {
            accumulator++;
        }
        else if(opcode == DEC) {
            accumulator--;
        }
        else if(opcode == ADD) {
            accumulator = accumulator + memory[operand];
        }
        else if(opcode == SUB) {
            accumulator = accumulator - memory[operand];
        }
        else if(opcode == LOAD) {
            accumulator = memory[operand];
        }
        else if(opcode == STORE) {
            memory[operand] = accumulator;
        }
        else if(opcode == JUMP) {
            i = memory[operand];
        }
        else if(opcode == JUMPPOS) {
            if(accumulator > 0) {
                i = memory[operand];
            }        }
        else if(opcode == JUMPZERO) {
            if(accumulator == 0) {
                i = memory[operand];
            }
        }
        else if(opcode == HALT) {
            return 99;
        }
        else if(opcode == SKIP) {

        }
    }
    return 0;
}

