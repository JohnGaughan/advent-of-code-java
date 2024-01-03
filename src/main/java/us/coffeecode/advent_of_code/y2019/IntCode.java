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
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

/**
 * Implementation of an IntCode computer.
 */
final class IntCode {

  private static final Pattern SPLIT = Pattern.compile(",");

  /** The memory buffer, dynamically resizable, of the IntCode computer. */
  final DynamicLongArray memory;

  /** Memory location of the current or next instruction to execute. */
  long instructionPointer = 0;

  /** Relative base for modifying pointers in relative mode. */
  long relativeBase = 0;

  /** Producer for input values. */
  private IntCodeIoQueue input;

  /** Output of this program is stored FIFO in this queue. */
  private final IntCodeIoQueue output = new LongQueue();

  /** Execution options that can make modifications to how the program is executed. */
  private final Set<ExecutionOption> options = new HashSet<>(2);

  IntCode(final InputLoader il, final PuzzleContext pc, final ExecutionOption... _options) {
    this(il, pc, new LongQueue(), _options);
  }

  IntCode(final InputLoader il, final PuzzleContext pc, final IntCodeIoQueue _input, final ExecutionOption... _options) {
    memory = new DynamicLongArray(il.fileAsLongsFromSplit(pc, SPLIT));
    options.addAll(Arrays.asList(_options));
    input = _input;
  }

  IntCode(final IntCode original, final ExecutionOption... _options) {
    memory = new DynamicLongArray(original.memory);
    instructionPointer = original.instructionPointer;
    relativeBase = original.relativeBase;
    input = new LongQueue();
    options.addAll(original.options);
    options.addAll(Arrays.asList(_options));
  }

  IntCode(final IntCode original, final IntCodeIoQueue _input, final ExecutionOption... _options) {
    this(original, _options);
    input = _input;
  }

  public DynamicLongArray getMemory() {
    return memory;
  }

  public ExecutionResult exec() {
    ExecutionResult result = null;
    long instructionsSinceOutput = 0;
    while (true) {
      validate(instructionPointer);
      final long opcode = memory.get(instructionPointer);
      final Instruction inst = Instruction.valueOf(opcode);

      if (inst == null) {
        break;
      }

      final Mode[] modes = Mode.valuesOf(opcode);

      result = inst.execute(this, modes);
      if (result.isBlock() || options.contains(ExecutionOption.ONE_INSTRUCTION_PER_EXEC)) {
        break;
      }

      if (options.contains(ExecutionOption.BLOCK_IF_EXCESSIVE_RUNTIME)) {
        if (instructionsSinceOutput > 12_000) {
          break;
        }
        else {
          ++instructionsSinceOutput;
        }
      }
    }
    return result;
  }

  private void validate(final long position) {
    assert (0 <= position) && (position <= Integer.MAX_VALUE);
  }

  public IntCodeIoQueue getInput() {
    return input;
  }

  public void setInput(final IntCodeIoQueue _input) {
    input = _input;
  }

  public IntCodeIoQueue getOutput() {
    return output;
  }

  public boolean hasOption(final ExecutionOption option) {
    return options.contains(option);
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    else if (obj instanceof IntCode o) {
      return (instructionPointer == o.instructionPointer) && (input == o.input) && Objects.equals(memory, o.memory)
        && Objects.equals(output, o.output);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(Long.valueOf(instructionPointer), output, memory, input);
  }

}
