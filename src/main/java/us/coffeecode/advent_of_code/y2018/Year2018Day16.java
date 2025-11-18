/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2021  John Gaughan
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
package us.coffeecode.advent_of_code.y2018;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2018, day = 16)
@Component
public final class Year2018Day16 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final Input input = new Input(il.lines(pc));
    long result = 0;
    for (final Sample sample : input.samples) {
      if (test(sample, null) >= 3) {
        ++result;
      }
    }
    return result;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final Input input = new Input(il.lines(pc));
    // Keep testing until all opcodes are assigned.
    final Map<Long, OpCode> opcodes = new HashMap<>();
    while (opcodes.size() != OpCode.values().length) {
      for (final Sample sample : input.samples) {
        test(sample, opcodes);
      }
    }

    // Now execute the program.
    final long[] registers = new long[] { 0, 0, 0, 0 };
    for (final long[] instruction : input.instructions) {
      final Long key = Long.valueOf(instruction[0]);
      opcodes.get(key)
             .apply(registers, instruction[1], instruction[2], instruction[3]);
    }
    return registers[0];
  }

  /** Test a sample to see if three or more opcodes would have the same effect. */
  private int test(final Sample sample, final Map<Long, OpCode> opcodes) {
    int count = 0;
    OpCode toAssign = null;
    for (final OpCode opCode : OpCode.values()) {
      if ((opcodes != null) && opcodes.values()
                                      .contains(opCode)) {
        continue;
      }
      final long[] registers = Arrays.copyOf(sample.registersBefore, sample.registersBefore.length);
      opCode.apply(registers, sample.instruction[1], sample.instruction[2], sample.instruction[3]);
      if (Arrays.equals(registers, sample.registersAfter)) {
        ++count;
        toAssign = opCode;
      }
    }
    if ((opcodes != null) && (count == 1) && (toAssign != null)) {
      opcodes.put(Long.valueOf(sample.instruction[0]), toAssign);
    }
    return count;
  }

  /** Sample data in the first part of the input file. */
  private static final class Sample {

    final long[] registersBefore;

    final long[] instruction;

    final long[] registersAfter;

    Sample(final long[] _registersBefore, final long[] _instruction, final long[] _registersAfter) {
      registersBefore = _registersBefore;
      instruction = _instruction;
      registersAfter = _registersAfter;
    }
  }

  /** Contains all of the data in the input file. */
  private static final class Input {

    private static final Pattern INSTRUCTION_SPLIT = Pattern.compile(" ");

    private static final Pattern SAMPLE_SPLIT = Pattern.compile(", ");

    private static long[] make(final String line, final Pattern split) {
      final String[] strs = split.split(line);
      return Arrays.stream(strs)
                   .mapToLong(Long::parseLong)
                   .toArray();
    }

    final List<Sample> samples = new ArrayList<>(780);

    final List<long[]> instructions;

    Input(final List<String> input) {
      // Handle four lines at a time, reading in the input for part 1.
      int idx = 0;
      for (; input.get(idx)
                  .startsWith("Before"); idx += 4) {
        String s0 = input.get(idx)
                         .substring(9);
        s0 = s0.substring(0, s0.length() - 1);
        String s1 = input.get(idx + 1);
        String s2 = input.get(idx + 2)
                         .substring(9);
        s2 = s2.substring(0, s2.length() - 1);
        samples.add(new Sample(make(s0, SAMPLE_SPLIT), make(s1, INSTRUCTION_SPLIT), make(s2, SAMPLE_SPLIT)));
      }

      // Consume blank lines
      while (input.get(idx)
                  .isBlank()) {
        ++idx;
      }

      // Read in the instructions for part 2.
      instructions = new ArrayList<>(input.size() - idx + 1);
      for (; idx < input.size(); ++idx) {
        instructions.add(make(input.get(idx), INSTRUCTION_SPLIT));
      }
    }
  }

}
