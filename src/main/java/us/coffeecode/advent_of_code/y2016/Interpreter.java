/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2020  John Gaughan
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package us.coffeecode.advent_of_code.y2016;

/**
 * Assembunny interpreter. Runs the virtual machine while its instruction pointer is valid, then halts.
 */
public class Interpreter {

  public void execute(final State state) {
    // Execute while the instruction pointer is valid, then halt.
    while ((0 <= state.ip) && (state.ip < state.instructions.length)) {
      // The toggle instruction can change instructions such that the arguments don't make sense, for example, "copy 1
      // 2" or "inc 4". This code skips those instructions with nonsense arguments.
      if (state.instructions[state.ip].op == OpCode.cpy) {
        final int value;
        if (state.instructions[state.ip].args[0] instanceof Integer i) {
          value = i.intValue();
        }
        else {
          char srcReg = ((Character) state.instructions[state.ip].args[0]).charValue();
          value = state.reg[srcReg - 'a'];
        }
        if (state.instructions[state.ip].args[1] instanceof Character c) {
          char destReg = c.charValue();
          state.reg[destReg - 'a'] = value;
        }
        ++state.ip;
      }
      else if (state.instructions[state.ip].op == OpCode.dec) {
        if (state.instructions[state.ip].args[0] instanceof Character c) {
          char reg = c.charValue();
          --state.reg[reg - 'a'];
        }
        ++state.ip;
      }
      else if (state.instructions[state.ip].op == OpCode.inc) {
        if (state.instructions[state.ip].args[0] instanceof Character c) {
          char reg = c.charValue();
          ++state.reg[reg - 'a'];
        }
        ++state.ip;
      }
      else if (state.instructions[state.ip].op == OpCode.jnz) {
        final int value;
        if (state.instructions[state.ip].args[0] instanceof Integer i) {
          value = i.intValue();
        }
        else {
          char srcReg = ((Character) state.instructions[state.ip].args[0]).charValue();
          value = state.reg[srcReg - 'a'];
        }
        if (value == 0) {
          ++state.ip;
        }
        else if (state.instructions[state.ip].args[1] instanceof Integer i) {
          state.ip += i.intValue();
        }
        else {
          char jmp = ((Character) state.instructions[state.ip].args[1]).charValue();
          state.ip += state.reg[jmp - 'a'];
        }
      }
      else if (state.instructions[state.ip].op == OpCode.nop) {
        ++state.ip;
      }
      else if (state.instructions[state.ip].op == OpCode.mul) {
        // Synthetic instruction not changed by toggle.
        final int argA;
        if (state.instructions[state.ip].args[0] instanceof Integer i) {
          argA = i.intValue();
        }
        else {
          char srcReg = ((Character) state.instructions[state.ip].args[0]).charValue();
          argA = state.reg[srcReg - 'a'];
        }
        final int argB;
        if (state.instructions[state.ip].args[1] instanceof Integer i) {
          argB = i.intValue();
        }
        else {
          char srcReg = ((Character) state.instructions[state.ip].args[1]).charValue();
          argB = state.reg[srcReg - 'a'];
        }
        if (state.instructions[state.ip].args[2] instanceof Character c) {
          char destReg = c.charValue();
          state.reg[destReg - 'a'] = argA * argB;
        }
        ++state.ip;
      }
      else if (state.instructions[state.ip].op == OpCode.out) {
        final int value;
        if (state.instructions[state.ip].args[0] instanceof Integer i) {
          value = i.intValue();
        }
        else {
          char srcReg = ((Character) state.instructions[state.ip].args[0]).charValue();
          value = state.reg[srcReg - 'a'];
        }
        state.out[state.outUsed] = value;
        ++state.outUsed;
        if (state.outUsed == state.out.length) {
          return;
        }
        ++state.ip;
      }
      else if (state.instructions[state.ip].op == OpCode.tgl) {
        final int value;
        if (state.instructions[state.ip].args[0] instanceof Integer i) {
          value = i.intValue();
        }
        else {
          char srcReg = ((Character) state.instructions[state.ip].args[0]).charValue();
          value = state.reg[srcReg - 'a'];
        }
        final int newIp = value + state.ip;
        // "If an attempt is made to toggle an instruction outside the program, nothing happens."
        if (0 <= newIp && newIp < state.instructions.length) {
          // "For one-argument instructions, inc becomes dec, and all other one-argument instructions become inc."
          if (state.instructions[newIp].op == OpCode.inc) {
            state.instructions[newIp].op = OpCode.dec;
          }
          else if (state.instructions[newIp].op == OpCode.dec) {
            state.instructions[newIp].op = OpCode.inc;
          }
          else if (state.instructions[newIp].op == OpCode.tgl) {
            state.instructions[newIp].op = OpCode.inc;
          }
          // "For two-argument instructions, jnz becomes cpy, and all other two-instructions become jnz."
          else if (state.instructions[newIp].op == OpCode.jnz) {
            state.instructions[newIp].op = OpCode.cpy;
          }
          else if (state.instructions[newIp].op == OpCode.cpy) {
            state.instructions[newIp].op = OpCode.jnz;
          }
          else {
            throw new IllegalStateException("Unknown instruction [" + state.instructions[value].op + "]");
          }
        }
        ++state.ip;
      }
      else {
        throw new IllegalStateException("Unknown instruction [" + state.instructions[state.ip].op + "]");
      }
    }
  }

}
