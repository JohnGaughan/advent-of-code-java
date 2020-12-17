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
package net.johngaughan.advent_of_code.y2015;

import java.nio.file.Files;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import net.johngaughan.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2015/day/23">Year 2015, day 23</a>. This problem requires running a program through
 * a simple virtual machine and getting the value of an output register once the program halts. Part one asks for the
 * value in register B when register A is initialized to zero: part two initializes register B to one.
 * </p>
 * <p>
 * The solution is a simple loop that performs the changes to the VM state as stipulated in the problem statement.
 * Changes are made by telling the operation to perform its logic on the state. Once the instruction pointer no longer
 * points to any instructions, the program halts and returns the value in register B.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@johngaughan.net&gt;
 */
public final class Year2015Day23 {

  public int calculatePart1() {
    return runProgram(0);
  }

  public int calculatePart2() {
    return runProgram(1);
  }

  private int runProgram(final int initialA) {
    final List<Instruction> instructions = getInput();
    final MachineState state = new MachineState(initialA);
    while (state.i >= 0 && state.i < instructions.size()) {
      final Instruction instruction = instructions.get(state.i);
      instruction.op.exec(state, instruction.reg, instruction.arg);
    }
    return state.b;
  }

  /** Get the input data for this solution. */
  private List<Instruction> getInput() {
    try {
      return Files.readAllLines(Utils.getInput(2015, 23)).stream().map(Instruction::new).collect(Collectors.toList());
    }
    catch (final RuntimeException ex) {
      throw ex;
    }
    catch (final Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  /** Tracks the state of the virtual machine. */
  private static final class MachineState {

    /** Register A */
    int a;

    /** Register B */
    int b = 0;

    /** Instruction counter */
    int i = 0;

    MachineState(final int initialA) {
      a = initialA;
    }
  }

  private static final class Instruction {

    private static final Pattern SPLIT = Pattern.compile(" ");

    final Operation op;

    final Register reg;

    final int arg;

    Instruction(final String input) {
      final String[] tokens = SPLIT.split(input.replace(",", ""));
      op = Operation.valueOf(tokens[0]);
      if ("a".equals(tokens[1]) || "b".equals(tokens[1])) {
        reg = Register.valueOf(tokens[1]);
        if (tokens.length == 3) {
          arg = Integer.parseInt(tokens[2]);
        }
        else {
          arg = 0;
        }
      }
      else {
        reg = null;
        arg = Integer.parseInt(tokens[1]);
      }
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
      return "[op=" + op.name() + ",reg=" + (reg == null ? "" : reg.name()) + ",arg=" + arg + "]";
    }
  }

  private static enum Register {
    a,
    b;
  }

  private static enum Operation {

    hlf {

      @Override
      public void exec(final MachineState state, final Register reg, final int arg) {
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
      public void exec(final MachineState state, final Register reg, final int arg) {
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
      public void exec(final MachineState state, final Register reg, final int arg) {
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
      public void exec(final MachineState state, final Register reg, final int arg) {
        // Jump to an instruction offset from the current one.
        state.i += arg;
      }
    },

    jie {

      @Override
      public void exec(final MachineState state, final Register reg, final int arg) {
        // Jump to an instruction offset from the current one if the register's value is even.
        if (reg == Register.a && state.a % 2 == 0) {
          state.i += arg;
        }
        else if (reg == Register.b && state.b % 2 == 0) {
          state.i += arg;
        }
        else {
          ++state.i;
        }
      }
    },

    jio {

      @Override
      public void exec(final MachineState state, final Register reg, final int arg) {
        // Jump to an instruction offset from the current one if the register's value is one.
        if (reg == Register.a && state.a == 1) {
          state.i += arg;
        }
        else if (reg == Register.b && state.b == 1) {
          state.i += arg;
        }
        else {
          ++state.i;
        }
      }
    };

    /** Execute this instruction for the given machine state and inputs. */
    public abstract void exec(final MachineState state, final Register reg, final int arg);
  }

}
