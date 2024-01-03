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
package us.coffeecode.advent_of_code.y2015;

import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2015, day = 23, title = "Opening the Turing Lock")
@Component
public final class Year2015Day23 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return runProgram(pc, 0);
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return runProgram(pc, 1);
  }

  private long runProgram(final PuzzleContext pc, final int initialA) {
    final List<Instruction> instructions = il.linesAsObjects(pc, Instruction::make);
    final MachineState state = new MachineState(initialA);
    while ((state.i >= 0) && (state.i < instructions.size())) {
      final Instruction instruction = instructions.get(state.i);
      instruction.op.exec(state, instruction.reg, instruction.arg);
    }
    return "a".equals(pc.getString("register")) ? state.a : state.b;
  }

  /** Tracks the state of the virtual machine. */
  private static final class MachineState {

    /** Register A */
    long a;

    /** Register B */
    long b = 0;

    /** Instruction counter */
    int i = 0;

    MachineState(final int initialA) {
      a = initialA;
    }
  }

  private static record Instruction(Operation op, Register reg, long arg) {

    private static final Pattern SPLIT = Pattern.compile(" ");

    static Instruction make(final String input) {
      final String[] tokens = SPLIT.split(input.replace(",", ""));
      final Operation op = Operation.valueOf(tokens[0]);
      if ("a".equals(tokens[1]) || "b".equals(tokens[1])) {
        return new Instruction(op, Register.valueOf(tokens[1]), tokens.length == 3 ? Integer.parseInt(tokens[2]) : 0);
      }
      return new Instruction(op, null, Integer.parseInt(tokens[1]));
    }

  }

  private static enum Register {
    a,
    b;
  }

  private static enum Operation {

    hlf {

      @Override
      public void exec(final MachineState state, final Register reg, final long arg) {
        // Set the register to half of its current value.
        if (reg == Register.a) {
          state.a >>= 1;
        }
        else {
          state.b >>= 1;
        }
        ++state.i;
      }
    },

    tpl {

      @Override
      public void exec(final MachineState state, final Register reg, final long arg) {
        // Set the register to triple its current value.
        if (reg == Register.a) {
          state.a *= 3;
        }
        else {
          state.b *= 3;
        }
        ++state.i;
      }
    },

    inc {

      @Override
      public void exec(final MachineState state, final Register reg, final long arg) {
        // Increment the register by one.
        if (reg == Register.a) {
          ++state.a;
        }
        else {
          ++state.b;
        }
        ++state.i;
      }
    },

    jmp {

      @Override
      public void exec(final MachineState state, final Register reg, final long arg) {
        // Jump to an instruction offset from the current one.
        state.i += arg;
      }
    },

    jie {

      @Override
      public void exec(final MachineState state, final Register reg, final long arg) {
        // Jump to an instruction offset from the current one if the register's value is even.
        if ((reg == Register.a) && (state.a % 2 == 0)) {
          state.i += arg;
        }
        else if ((reg == Register.b) && (state.b % 2 == 0)) {
          state.i += arg;
        }
        else {
          ++state.i;
        }
      }
    },

    jio {

      @Override
      public void exec(final MachineState state, final Register reg, final long arg) {
        // Jump to an instruction offset from the current one if the register's value is one.
        if ((reg == Register.a) && (state.a == 1)) {
          state.i += arg;
        }
        else if ((reg == Register.b) && (state.b == 1)) {
          state.i += arg;
        }
        else {
          ++state.i;
        }
      }
    };

    /** Execute this instruction for the given machine state and inputs. */
    public abstract void exec(final MachineState state, final Register reg, final long arg);
  }

}
