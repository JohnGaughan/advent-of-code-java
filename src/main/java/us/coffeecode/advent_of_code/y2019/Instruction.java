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
package us.coffeecode.advent_of_code.y2019;

import java.util.Arrays;

/**
 * Represents one instruction in an IntCode computer. An instruction is just the opcode, minus any modes encoded in the
 * same code.
 */
enum Instruction {

  ADD(1) {

    @Override
    public ExecutionResult execute(final IntCode state, final Mode[] modes) {
      final long x = modes[0].getValue(state, state.instructionPointer + 1);
      final long y = modes[1].getValue(state, state.instructionPointer + 2);
      modes[2].setValue(state, state.instructionPointer + 3, x + y);
      state.instructionPointer += 4;
      return ExecutionResult.COMPLETE;
    }
  },

  MULTIPLY(2) {

    @Override
    public ExecutionResult execute(final IntCode state, final Mode[] modes) {
      final long x = modes[0].getValue(state, state.instructionPointer + 1);
      final long y = modes[1].getValue(state, state.instructionPointer + 2);
      modes[2].setValue(state, state.instructionPointer + 3, x * y);
      state.instructionPointer += 4;
      return ExecutionResult.COMPLETE;
    }
  },

  INPUT(3) {

    @Override
    public ExecutionResult execute(final IntCode state, final Mode[] modes) {
      if (state.hasOption(ExecutionOption.BLOCK_UNTIL_INPUT_AVAILABLE) && state.getInput().isEmpty()) {
        return ExecutionResult.BLOCK;
      }
      modes[0].setValue(state, state.instructionPointer + 1, state.getInput().remove());
      state.instructionPointer += 2;
      return ExecutionResult.COMPLETE;
    }
  },

  OUTPUT(4) {

    @Override
    public ExecutionResult execute(final IntCode state, final Mode[] modes) {
      final long value = modes[0].getValue(state, state.instructionPointer + 1);
      state.getOutput().add(value);
      state.instructionPointer += 2;
      if (state.hasOption(ExecutionOption.BLOCK_AFTER_THREE_OUTPUTS) && state.getOutput().size() >= 3) {
        return ExecutionResult.BLOCK;
      }
      return ExecutionResult.COMPLETE;
    }
  },

  JUMP_IF_TRUE(5) {

    @Override
    public ExecutionResult execute(final IntCode state, final Mode[] modes) {
      final long x = modes[0].getValue(state, state.instructionPointer + 1);
      final long y = modes[1].getValue(state, state.instructionPointer + 2);
      if (x == 0) {
        state.instructionPointer += 3;
      }
      else {
        state.instructionPointer = y;
      }
      return ExecutionResult.COMPLETE;
    }
  },

  JUMP_IF_FALSE(6) {

    @Override
    public ExecutionResult execute(final IntCode state, final Mode[] modes) {
      final long x = modes[0].getValue(state, state.instructionPointer + 1);
      final long y = modes[1].getValue(state, state.instructionPointer + 2);
      if (x == 0) {
        state.instructionPointer = y;
      }
      else {
        state.instructionPointer += 3;
      }
      return ExecutionResult.COMPLETE;
    }
  },

  LESS_THAN(7) {

    @Override
    public ExecutionResult execute(final IntCode state, final Mode[] modes) {
      final long x = modes[0].getValue(state, state.instructionPointer + 1);
      final long y = modes[1].getValue(state, state.instructionPointer + 2);
      modes[2].setValue(state, state.instructionPointer + 3, x < y ? 1 : 0);
      state.instructionPointer += 4;
      return ExecutionResult.COMPLETE;
    }
  },

  EQUALS(8) {

    @Override
    public ExecutionResult execute(final IntCode state, final Mode[] modes) {
      final long x = modes[0].getValue(state, state.instructionPointer + 1);
      final long y = modes[1].getValue(state, state.instructionPointer + 2);
      modes[2].setValue(state, state.instructionPointer + 3, x == y ? 1 : 0);
      state.instructionPointer += 4;
      return ExecutionResult.COMPLETE;
    }
  },

  ADJUST_RELATIVE_BASE(9) {

    @Override
    public ExecutionResult execute(final IntCode state, final Mode[] modes) {
      final long x = modes[0].getValue(state, state.instructionPointer + 1);
      state.relativeBase += x;
      state.instructionPointer += 2;
      return ExecutionResult.COMPLETE;
    }
  },

  HALT(99) {

    @Override
    public ExecutionResult execute(final IntCode state, final Mode[] modes) {
      return ExecutionResult.HALT;
    }

  };

  public static Instruction valueOf(final long intcode) {
    final long _code = intcode % 100;
    return Arrays.stream(values()).filter(i -> i.code == _code).findFirst().orElse(null);
  }

  private final long code;

  private Instruction(final long _code) {
    code = _code % 100;
  }

  public abstract ExecutionResult execute(final IntCode state, final Mode[] modes);
}
