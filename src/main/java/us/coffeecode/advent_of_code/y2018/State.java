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
package us.coffeecode.advent_of_code.y2018;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Encapsulates program state for an elf assembly program.
 */
final class State {

  private static final Pattern SPLIT = Pattern.compile(" ");

  public final int ip_reg;

  public final long[] register = new long[6];

  public final List<Instruction> instructions;

  public State(final List<String> lines) {
    int _ip_reg = 0;
    List<Instruction> _instructions = new ArrayList<>(lines.size() - 1);
    for (final String line : lines) {
      final String[] tokens = SPLIT.split(line);
      if ("#ip".equals(tokens[0])) {
        _ip_reg = Integer.parseInt(tokens[1]);
      }
      else {
        final OpCode opcode = OpCode.valueOf(tokens[0]);
        _instructions.add(
          new Instruction(opcode, Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3])));
      }
    }
    ip_reg = _ip_reg;
    instructions = Collections.unmodifiableList(_instructions);
  }

  public State(final State state) {
    ip_reg = state.ip_reg;
    // Registers always start with zeroes.
    // Instructions are immutable. No need to build a new list or clone anything.
    instructions = state.instructions;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    else if (obj instanceof State o) {
      return (ip_reg == o.ip_reg) && Arrays.equals(register, o.register) && Objects.equals(instructions, o.instructions);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(Integer.valueOf(ip_reg), Integer.valueOf(Arrays.hashCode(register)), instructions);
  }

  @Override
  public String toString() {
    return Arrays.toString(register);
  }

}
