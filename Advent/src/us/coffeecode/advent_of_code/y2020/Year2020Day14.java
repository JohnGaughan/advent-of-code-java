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

import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import us.coffeecode.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2020/day/14">Year 2020, day 14</a>. This problem describes a simple computer, and
 * we need to set memory at various addresses to specific values, then get the sum of those values. It has us use a bit
 * mask to do so, but in different ways for each part. Part one requires us to use a bit mask to modify the values set,
 * while part two uses the bit mask to modify the memory address. Part two not only changes how to interpret the bit
 * mask, but the mask can actually return multiple values because Xs can be either zero or one.
 * </p>
 * <p>
 * Part one is fairly simple, with the logical leap being that the mask encapsulates both bitwise <code>and</code> and
 * bitwise <code>or</code>. Then it is a simple matter of applying both operations to the input and returning a single
 * value. Part two instead has us modifying addresses, and reinterpreting the mask. The solution below is fairly
 * efficient and straightforward, and comments explain the algorithm.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2020Day14 {

  public long calculatePart1() {
    ValueBitmask bitmask = null;
    final Map<Long, Long> memory = new HashMap<>();
    for (final Object input : getInput(ValueBitmask::new)) {
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
    return memory.values().stream().mapToLong(Long::valueOf).sum();
  }

  public long calculatePart2() {
    String bitmask = null;
    // If every X in the mask is a bit, this number can hold the largest number for those Xs.
    int maxReplacement = -1;
    final Map<Long, Long> memory = new HashMap<>();
    for (final Object input : getInput(s -> s)) {
      if (input instanceof String) {
        bitmask = (String) input;
        final int bits = bitmask.replace("1", "").replace("0", "").length();
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
    return memory.values().stream().mapToLong(Long::valueOf).sum();
  }

  /** Get the input data for this solution. */
  private List<Object> getInput(final Function<String, Object> maskCreator) {
    try {
      return Files.readAllLines(Utils.getInput(2020, 14)).stream().map(s -> getInput(s, maskCreator)).collect(
        Collectors.toList());
    }
    catch (final RuntimeException ex) {
      throw ex;
    }
    catch (final Exception ex) {
      throw new RuntimeException(ex);
    }
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
  private final class ValueBitmask {

    private final String mask;

    private final long orMask;

    private final long andMask;

    ValueBitmask(final String input) {
      mask = input;
      orMask = Long.parseLong(mask.replace('X', '0'), 2);
      andMask = Long.parseLong(mask.replace('X', '1'), 2);
    }

    long mask(final long value) {
      long result = value;
      result |= orMask;
      result &= andMask;
      return result;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
      return "[" + mask + "]";
    }
  }

  /** Represents an input line that updates a memory location. */
  private final class MemoryUpdate {

    final long address;

    final long value;

    MemoryUpdate(final long a, final long v) {
      address = a;
      value = v;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
      return "[" + address + "=" + value + "]";
    }
  }
}
