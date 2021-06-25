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
package us.coffeecode.advent_of_code.y2016.assembunny;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * Machine state in an Assembunny interpreter.
 * </p>
 * <p>
 * Copyright (c) 2021 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class State {

  public static final int OUPUT_BUFFER_SIZE = 10;

  /**
   * Load input data from a file and use it to create an initial state.
   *
   * @param input path to the file to load
   * @return an initialized state instance.
   */
  public static State load(final List<String> input) {
    try {
      return new State(input.stream().map(Instruction::new).toArray(Instruction[]::new));
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  public final Instruction[] instructions;

  public int[] reg = new int[4];

  public int ip = 0;

  /** Previous output for day 25. */
  public int[] out = new int[OUPUT_BUFFER_SIZE];

  public int outUsed = 0;

  public State(final Instruction[] _instructions) {
    instructions = _instructions;
  }

  /** {@inheritDoc} */
  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    else if (!(obj instanceof State)) {
      return false;
    }
    else {
      final State other = (State) obj;
      return ip == other.ip && Arrays.equals(reg, other.reg) && Arrays.equals(instructions, other.instructions);
    }
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    return Objects.hash(reg, instructions, Integer.valueOf(ip));
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    StringBuilder str = new StringBuilder();
    str.append("IP=").append(ip).append(",Registers=").append(Arrays.toString(reg));
    return str.toString();
  }

}
