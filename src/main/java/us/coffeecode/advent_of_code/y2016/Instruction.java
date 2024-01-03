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

import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * One Assembunny instruction, including both an opcode and its parameters.
 */
public final class Instruction {

  private static final Pattern SPLIT = Pattern.compile(" ");

  private static Object makeArg(final String input) {
    if (Character.isAlphabetic(input.charAt(0))) {
      return Character.valueOf(input.charAt(0));
    }
    return Integer.valueOf(input);
  }

  public OpCode op;

  public final Object[] args;

  public Instruction(final String input) {
    final String tokens[] = SPLIT.split(input);
    op = OpCode.valueOf(tokens[0]);
    if (tokens.length == 1) {
      args = new Object[0];
    }
    else if (tokens.length == 2) {
      args = new Object[] { makeArg(tokens[1]) };
    }
    else if (tokens.length == 3) {
      args = new Object[] { makeArg(tokens[1]), makeArg(tokens[2]) };
    }
    else {
      args = new Object[] { makeArg(tokens[1]), makeArg(tokens[2]), makeArg(tokens[3]) };
    }
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    else if (obj instanceof Instruction o) {
      return (op == o.op) && Arrays.equals(args, o.args);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(args, op);
  }

  @Override
  public String toString() {
    return op + Arrays.toString(args);
  }

}
