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

import static org.junit.Assert.assertEquals;

import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.junit.Test;

import us.coffeecode.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2016/day/21">Year 2016, day 21</a>. This is a problem about scrambling text in a
 * non-destructive way. Part one runs a bunch of transformations on an input string. Part two has us start with the
 * output work the rules backward to get the input.
 * </p>
 * <p>
 * Running forward was not too difficult, although some of the indexing was a little tricky. Running backward was also
 * not too bad. Some rules are strictly reflexive and need no modifications. Others need some simple changes to the
 * input, or calling other rules (for example, rotating left when rotating right in reverse). The only moderately tricky
 * rule was rotating based on the character in a given position where its position prior to rotating changes the amount
 * rotated. To solve this I sketched out a matrix of where a given letter will be in the output for each position in the
 * input. Thankfully, there was a unique mapping. Since there is no simple formula to calculate this, I saved the
 * mapping in an array and the reverse logic looks up the value needed to reverse the original rotation.
 * </p>
 * <p>
 * Copyright (c) 2021 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2016Day21 {

  public String calculatePart1() {
    String text = "abcdefgh";
    for (Instruction instruction : getInput()) {
      text = instruction.transform(text, false);
    }
    return text;
  }

  public String calculatePart2() {
    final List<Instruction> instructions = getInput();
    Collections.reverse(instructions);
    String text = "fbgdceah";
    for (Instruction instruction : instructions) {
      text = instruction.transform(text, true);
    }
    return text;
  }

  /** Get the input data for this solution. */
  private List<Instruction> getInput() {
    try {
      return Files.readAllLines(Utils.getInput(2016, 21)).stream().map(Instruction::new).collect(Collectors.toList());
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private static final class Instruction {

    private static final Pattern SEPARATOR = Pattern.compile(" ");

    final Action action;

    final int arg1;

    final int arg2;

    Instruction(final String input) {
      final String[] tokens = SEPARATOR.split(input);
      if ("swap".equals(tokens[0])) {
        if ("position".equals(tokens[1])) {
          action = Action.SWAP_POSITION;
          arg1 = Integer.parseInt(tokens[2]);
          arg2 = Integer.parseInt(tokens[5]);
        }
        else {
          action = Action.SWAP_LETTER;
          arg1 = tokens[2].codePointAt(0);
          arg2 = tokens[5].codePointAt(0);
        }
      }

      else if ("rotate".equals(tokens[0])) {
        if ("based".equals(tokens[1])) {
          action = Action.ROTATE_FROM_CHAR;
          arg1 = tokens[6].codePointAt(0);
        }
        else if ("right".equals(tokens[1])) {
          action = Action.ROTATE_RIGHT;
          arg1 = Integer.parseInt(tokens[2]);
        }
        else {
          action = Action.ROTATE_LEFT;
          arg1 = Integer.parseInt(tokens[2]);
        }
        arg2 = -1;
      }

      else if ("reverse".equals(tokens[0])) {
        action = Action.REVERSE;
        arg1 = Integer.parseInt(tokens[2]);
        arg2 = Integer.parseInt(tokens[4]);
      }

      else {
        action = Action.MOVE;
        arg1 = Integer.parseInt(tokens[2]);
        arg2 = Integer.parseInt(tokens[5]);
      }
    }

    String transform(final String input, final boolean reverse) {
      return action.transform(input, arg1, arg2, reverse);
    }

    @Override
    public String toString() {
      final StringBuilder str = new StringBuilder();
      str.append(action.name()).append("(").append(arg1);
      if (arg1 > -1) {
        str.append(",").append(arg2);
      }
      str.append(")");
      return str.toString();
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

  @Test
  public void testSwapPositionReverses() {
    String input = "abcdefgh";
    String str = Action.SWAP_POSITION.transform(input, 2, 4, false);
    str = Action.SWAP_POSITION.transform(str, 2, 4, true);
    assertEquals(input, str);
  }

  @Test
  public void testSwapLetterReverses() {
    String input = "abcdefgh";
    String str = Action.SWAP_LETTER.transform(input, 'b', 'f', false);
    str = Action.SWAP_LETTER.transform(str, 'b', 'f', true);
    assertEquals(input, str);
  }

  @Test
  public void testRotateFromLetterReverses() {
    String input = "abcdefgh";
    String str = Action.ROTATE_FROM_CHAR.transform(input, 'c', -1, false);
    str = Action.ROTATE_FROM_CHAR.transform(str, 'c', -1, true);
    assertEquals(input, str);
  }

  @Test
  public void testRotateLeftReverses() {
    String input = "abcdefgh";
    String str = Action.ROTATE_LEFT.transform(input, 3, -1, false);
    str = Action.ROTATE_LEFT.transform(str, 3, -1, true);
    assertEquals(input, str);
  }

  @Test
  public void testRotateRightReverses() {
    String input = "abcdefgh";
    String str = Action.ROTATE_RIGHT.transform(input, 5, -1, false);
    str = Action.ROTATE_RIGHT.transform(str, 5, -1, true);
    assertEquals(input, str);
  }

  @Test
  public void testReverseReverses() {
    String input = "abcdefgh";
    String str = Action.REVERSE.transform(input, 3, 6, false);
    str = Action.REVERSE.transform(str, 3, 6, true);
    assertEquals(input, str);
  }

  @Test
  public void testMoveReverses() {
    String input = "abcdefgh";
    String str = Action.MOVE.transform(input, 1, 5, false);
    str = Action.MOVE.transform(str, 1, 5, true);
    assertEquals(input, str);
  }

}
