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
package us.coffeecode.advent_of_code.y2020;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2020, day = 14)
@Component
public final class Year2020Day14 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    ValueBitmask bitmask = null;
    final Map<Long, Long> memory = new HashMap<>();
    for (final Object input : getInput(pc, ValueBitmask::make)) {
      if (input instanceof ValueBitmask) {
        bitmask = (ValueBitmask) input;
      }
      else if (bitmask == null) {
        throw new IllegalStateException("Bitmask was not set before using it");
      }
      else {
        final Long actualValue = Long.valueOf(bitmask.mask(((MemoryUpdate) input).value));
        memory.put(Long.valueOf(((MemoryUpdate) input).address), actualValue);
      }
    }
    return memory.values()
                 .stream()
                 .mapToLong(Long::valueOf)
                 .sum();
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    String bitmask = null;
    // If every X in the mask is a bit, this number can hold the largest number for those Xs.
    int maxReplacement = -1;
    final Map<Long, Long> memory = new HashMap<>();
    for (final Object input : getInput(pc, s -> s)) {
      if (input instanceof String) {
        bitmask = (String) input;
        final int bits = bitmask.replace("1", "")
                                .replace("0", "")
                                .length();
        maxReplacement = 1 << bits;
      }
      else if (bitmask == null) {
        throw new IllegalStateException("Bitmask was not set before using it");
      }
      else {
        final long baseAddress = ((MemoryUpdate) input).address;
        final Long value = Long.valueOf(((MemoryUpdate) input).value);

        // The outer loop iterates all possible replacements for Xs in the mask.
        for (long replacement = 0; replacement < maxReplacement; ++replacement) {
          long address = 0;
          long floatingResidue = replacement;

          // The inner loop iterates all bits in the mask and sets them per the requirements.
          for (int i = 0, addressBit = bitmask.length() - 1; i < bitmask.length(); ++i, --addressBit) {
            address <<= 1;
            final int ch = bitmask.charAt(i);
            if (ch == '0') {
              // 0 -> use the corresponding bit from the base address.
              if ((baseAddress >> addressBit & 1) == 1) {
                ++address;
              }
            }
            else if (ch == '1') {
              // 1 -> override the base address bit with a 1.
              ++address;
            }
            else {
              // X -> floating, which means it can have a zero or a one. The outer loop iterates all possible values.
              if ((floatingResidue & 1) == 1) {
                ++address;
              }
              // Only consume the floating residue when actually used.
              floatingResidue >>= 1;
            }
          }
          memory.put(Long.valueOf(address), value);
        }
      }
    }
    return memory.values()
                 .stream()
                 .mapToLong(Long::valueOf)
                 .sum();
  }

  /** Get the input data for this solution. */
  private List<Object> getInput(final PuzzleContext pc, final Function<String, Object> maskCreator) {
    return il.linesAsObjects(pc, s -> getInput(s, maskCreator));
  }

  /** Parse a line of input into either a Boolean[] or Long. */
  private Object getInput(final String line, final Function<String, Object> maskCreator) {
    if (line.startsWith("mask")) {
      return maskCreator.apply(line.substring(7));
    }
    else {
      final int closeBracket = line.indexOf(']');
      final int addr = Integer.parseInt(line.substring(line.indexOf('[') + 1, closeBracket));
      final int valueStart = line.indexOf(' ', line.indexOf(' ', closeBracket) + 1) + 1;
      final long val = Long.parseLong(line.substring(valueStart));
      return new MemoryUpdate(addr, val);
    }
  }

  /** Represents an input line that updates the current bit mask where the mask applies to the value being stored. */
  private static record ValueBitmask(String mask, long orMask, long andMask) {

    static ValueBitmask make(final String input) {
      return new ValueBitmask(input, Long.parseLong(input.replace('X', '0'), 2), Long.parseLong(input.replace('X', '1'), 2));
    }

    long mask(final long value) {
      long result = value;
      result |= orMask;
      result &= andMask;
      return result;
    }

  }

  /** Represents an input line that updates a memory location. */
  private static record MemoryUpdate(long address, long value) {}
}
