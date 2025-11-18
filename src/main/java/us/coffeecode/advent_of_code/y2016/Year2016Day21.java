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

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2016, day = 21)
@Component
public final class Year2016Day21 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public String calculatePart1(final PuzzleContext pc) {
    String text = pc.getString("input");
    for (final Instruction instruction : il.linesAsObjects(pc, Instruction::make)) {
      text = instruction.transform(text, false);
    }
    return text;
  }

  @Solver(part = 2)
  public String calculatePart2(final PuzzleContext pc) {
    String text = pc.getString("input");
    for (final Instruction instruction : il.linesAsObjectsMutable(pc, Instruction::make)
                                           .reversed()) {
      text = instruction.transform(text, true);
    }
    return text;
  }

  private static record Instruction(Action action, int arg1, int arg2) {

    private static final Pattern SEPARATOR = Pattern.compile(" ");

    static Instruction make(final String input) {
      final String[] tokens = SEPARATOR.split(input);
      if ("swap".equals(tokens[0])) {
        if ("position".equals(tokens[1])) {
          return new Instruction(Action.SWAP_POSITION, Integer.parseInt(tokens[2]), Integer.parseInt(tokens[5]));
        }
        return new Instruction(Action.SWAP_LETTER, tokens[2].codePointAt(0), tokens[5].codePointAt(0));
      }
      else if ("rotate".equals(tokens[0])) {
        if ("based".equals(tokens[1])) {
          return new Instruction(Action.ROTATE_FROM_CHAR, tokens[6].codePointAt(0), -1);
        }
        else if ("right".equals(tokens[1])) {
          return new Instruction(Action.ROTATE_RIGHT, Integer.parseInt(tokens[2]), -1);
        }
        return new Instruction(Action.ROTATE_LEFT, Integer.parseInt(tokens[2]), -1);
      }
      else if ("reverse".equals(tokens[0])) {
        return new Instruction(Action.REVERSE, Integer.parseInt(tokens[2]), Integer.parseInt(tokens[4]));
      }
      return new Instruction(Action.MOVE, Integer.parseInt(tokens[2]), Integer.parseInt(tokens[5]));
    }

    String transform(final String input, final boolean reverse) {
      return action.transform(input, arg1, arg2, reverse);
    }

  }

  private static enum Action {

    SWAP_POSITION {

      @Override
      String transform(final String input, final int arg1, final int arg2, final boolean reverse) {
        // Same forward and reverse.
        final StringBuilder str = new StringBuilder(input.length());
        for (int i = 0; i < input.length(); ++i) {
          if (i == arg1) {
            str.appendCodePoint(input.codePointAt(arg2));
          }
          else if (i == arg2) {
            str.appendCodePoint(input.codePointAt(arg1));
          }
          else {
            str.appendCodePoint(input.codePointAt(i));
          }
        }
        return str.toString();
      }
    },
    SWAP_LETTER {

      @Override
      String transform(final String input, final int arg1, final int arg2, final boolean reverse) {
        // Same forward and reverse.
        final StringBuilder str = new StringBuilder(input.length());
        for (int i = 0; i < input.length(); ++i) {
          final int ch = input.codePointAt(i);
          if (ch == arg1) {
            str.appendCodePoint(arg2);
          }
          else if (ch == arg2) {
            str.appendCodePoint(arg1);
          }
          else {
            str.appendCodePoint(ch);
          }
        }
        return str.toString();
      }
    },
    ROTATE_FROM_CHAR {

      @Override
      String transform(final String input, final int arg1, final int arg2, final boolean reverse) {
        if (reverse) {
          final int idx = input.indexOf(arg1);
          return ROTATE_LEFT.transform(input, REVERSE_CHAR_MAPPING[idx], -1, false);
        }
        int idx = input.indexOf(arg1);
        int rotations = idx + 1;
        if (idx > 3) {
          ++rotations;
        }
        return ROTATE_RIGHT.transform(input, rotations, -1, reverse);
      }
    },
    ROTATE_LEFT {

      @Override
      String transform(final String input, final int arg1, final int arg2, final boolean reverse) {
        if (reverse) {
          return ROTATE_RIGHT.transform(input, arg1, -1, false);
        }
        final StringBuilder str = new StringBuilder(input.length());
        for (int i = 0; i < input.length(); ++i) {
          final int idx = (i + arg1) % input.length();
          str.appendCodePoint(input.codePointAt(idx));
        }
        return str.toString();
      }
    },
    ROTATE_RIGHT {

      @Override
      String transform(final String input, final int arg1, final int arg2, final boolean reverse) {
        if (reverse) {
          return ROTATE_LEFT.transform(input, arg1, -1, false);
        }
        final StringBuilder str = new StringBuilder(input.length());
        for (int i = 0; i < input.length(); ++i) {
          int idx = i - arg1;
          while (idx < 0) {
            idx += input.length();
          }
          str.appendCodePoint(input.codePointAt(idx));
        }
        return str.toString();
      }
    },
    REVERSE {

      @Override
      String transform(final String input, final int arg1, final int arg2, final boolean reverse) {
        // Same forward and reverse.
        final StringBuilder str = new StringBuilder(input.length());
        final int sum = arg1 + arg2;
        for (int i = 0; i < input.length(); ++i) {
          if (i < arg1 || i > arg2) {
            str.appendCodePoint(input.codePointAt(i));
          }
          else {
            str.appendCodePoint(input.codePointAt(sum - i));
          }
        }
        return str.toString();
      }
    },
    MOVE {

      @Override
      String transform(final String input, final int arg1, final int arg2, final boolean reverse) {
        if (reverse) {
          return MOVE.transform(input, arg2, arg1, false);
        }
        final StringBuilder str = new StringBuilder(input);
        final char ch = str.charAt(arg1);
        str.deleteCharAt(arg1);
        str.insert(arg2, ch);
        return str.toString();
      }
    };

    private static final int[] REVERSE_CHAR_MAPPING = new int[] { 1, 1, 6, 2, 7, 3, 0, 4 };

    abstract String transform(String input, int arg1, int arg2, boolean reverse);
  }

}
