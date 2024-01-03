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
package us.coffeecode.advent_of_code.util;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.IntPredicate;

import org.springframework.stereotype.Component;

/**
 * Several puzzles require generating a grid of points where points are on or off, and form some pattern. When that
 * pattern represents a series of characters, this class can convert that pattern into character data. These character
 * representations have evolved slightly over the years, and this class can handle all of them. It also contains as many
 * encodings as I was able to find among various reddit threads where people shared their program output. AOC authors
 * have also confirmed that they deliberately avoided using certain characters such as "Q" that look similar to other
 * characters, which is why they are omitted here. As far as I am aware, this is a nearly-complete or complete
 * collection of encodings.<br>
 * <br>
 * If anyone reading this comment knows of something missing, please let me know.
 */
@Component
public class DigitConverter {

  private final Map<Long, Integer> bitsToCodePoints = new HashMap<>(128);

  public DigitConverter() {
    add('A', new String[] { "  ##  ", " #  # ", "#    #", "#    #", "#    #", "######", "#    #", "#    #", "#    #", "#    #" });
    add('A', new String[] { " ##  ", "#  # ", "#  # ", "#### ", "#  # ", "#  # " });
    add('A', new String[] { "  ## ", " #  #", " #  #", " ####", " #  #", " #  #" });
    add('A', new String[] { " ## ", "#  #", "#  #", "####", "#  #", "#  #" });

    add('B', new String[] { "##### ", "#    #", "#    #", "#    #", "##### ", "#    #", "#    #", "#    #", "#    #", "##### " });
    add('B', new String[] { "###  ", "#  # ", "###  ", "#  # ", "#  # ", "###  " });
    add('B', new String[] { " ### ", " #  #", " ### ", " #  #", " #  #", " ### " });
    add('B', new String[] { "### ", "#  #", "### ", "#  #", "#  #", "### " });

    add('C', new String[] { " #### ", "#    #", "#     ", "#     ", "#     ", "#     ", "#     ", "#     ", "#    #", " #### " });
    add('C', new String[] { " ##  ", "#  # ", "#    ", "#    ", "#  # ", " ##  " });
    add('C', new String[] { "  ## ", " #  #", " #   ", " #   ", " #  #", "  ## " });
    add('C', new String[] { " ## ", "#  #", "#   ", "#   ", "#  #", " ## " });

    add('E', new String[] { "######", "#     ", "#     ", "#     ", "##### ", "#     ", "#     ", "#     ", "#     ", "######" });
    add('E', new String[] { "#### ", "#    ", "###  ", "#    ", "#    ", "#### " });
    add('E', new String[] { " ####", " #   ", " ### ", " #   ", " #   ", " ####" });
    add('E', new String[] { "####", "#   ", "### ", "#   ", "#   ", "####" });

    add('F', new String[] { "######", "#     ", "#     ", "#     ", "##### ", "#     ", "#     ", "#     ", "#     ", "#     " });
    add('F', new String[] { "#### ", "#    ", "###  ", "#    ", "#    ", "#    " });
    add('F', new String[] { " ####", " #   ", " ### ", " #   ", " #   ", " #   " });
    add('F', new String[] { "####", "#   ", "### ", "#   ", "#   ", "#   " });

    add('G', new String[] { " #### ", "#    #", "#     ", "#     ", "#     ", "#  ###", "#    #", "#    #", "#   ##", " ### #" });
    add('G', new String[] { " ##  ", "#  # ", "#    ", "# ## ", "#  # ", " ### " });
    add('G', new String[] { "  ## ", " #  #", " #   ", " # ##", " #  #", "  ###" });
    add('G', new String[] { " ## ", "#  #", "#   ", "# ##", "#  #", " ###" });

    add('H', new String[] { "#    #", "#    #", "#    #", "#    #", "######", "#    #", "#    #", "#    #", "#    #", "#    #" });
    add('H', new String[] { "#  # ", "#  # ", "#### ", "#  # ", "#  # ", "#  # " });
    add('H', new String[] { " #  #", " #  #", " ####", " #  #", " #  #", " #  #" });
    add('H', new String[] { "#  #", "#  #", "####", "#  #", "#  #", "#  #" });

    add('I', new String[] { "###  ", " #   ", " #   ", " #   ", " #   ", "###  " });
    add('I', new String[] { "### ", " #  ", " #  ", " #  ", " #  ", "### " });

    add('J', new String[] { "   ###", "    # ", "    # ", "    # ", "    # ", "    # ", "    # ", "    # ", "#   # ", " ###  " });
    add('J', new String[] { "  ## ", "   # ", "   # ", "   # ", "#  # ", " ##  " });
    add('J', new String[] { "   ##", "    #", "    #", "    #", " #  #", "  ## " });
    add('J', new String[] { "  ##", "   #", "   #", "   #", "#  #", " ## " });

    add('K', new String[] { "#    #", "#   # ", "#  #  ", "# #   ", "##    ", "##    ", "# #   ", "#  #  ", "#   # ", "#    #" });
    add('K', new String[] { "#  # ", "# #  ", "##   ", "# #  ", "# #  ", "#  # " });
    add('K', new String[] { " #  #", " # # ", " ##  ", " # # ", " # # ", " #  #" });
    add('K', new String[] { "#  #", "# # ", "##  ", "# # ", "# # ", "#  #" });

    add('L', new String[] { "#     ", "#     ", "#     ", "#     ", "#     ", "#     ", "#     ", "#     ", "#     ", "######" });
    add('L', new String[] { "#    ", "#    ", "#    ", "#    ", "#    ", "#### " });
    add('L', new String[] { " #   ", " #   ", " #   ", " #   ", " #   ", " ####" });
    add('L', new String[] { "#   ", "#   ", "#   ", "#   ", "#   ", "####" });

    add('N', new String[] { "#    #", "##   #", "##   #", "# #  #", "# #  #", "#  # #", "#  # #", "#   ##", "#   ##", "#    #" });

    add('O', new String[] { " ##  ", "#  # ", "#  # ", "#  # ", "#  # ", " ##  " });
    add('O', new String[] { " ## ", "#  #", "#  #", "#  #", "#  #", " ## " });

    add('P', new String[] { "##### ", "#    #", "#    #", "#    #", "##### ", "#     ", "#     ", "#     ", "#     ", "#     " });
    add('P', new String[] { "###  ", "#  # ", "#  # ", "###  ", "#    ", "#    " });
    add('P', new String[] { " ### ", " #  #", " #  #", " ### ", " #   ", " #   " });
    add('P', new String[] { "### ", "#  #", "#  #", "### ", "#   ", "#   " });

    add('R', new String[] { "##### ", "#    #", "#    #", "#    #", "##### ", "#  #  ", "#   # ", "#   # ", "#    #", "#    #" });
    add('R', new String[] { "###  ", "#  # ", "#  # ", "###  ", "# #  ", "#  # " });
    add('R', new String[] { " ### ", " #  #", " #  #", " ### ", " # # ", " #  #" });
    add('R', new String[] { "### ", "#  #", "#  #", "### ", "# # ", "#  #" });

    add('S', new String[] { " ### ", "#    ", "#    ", " ##  ", "   # ", "###  " });
    add('S', new String[] { " ###", "#   ", "#   ", " ## ", "   #", "### " });

    add('U', new String[] { "#  # ", "#  # ", "#  # ", "#  # ", "#  # ", " ##  " });
    add('U', new String[] { " #  #", " #  #", " #  #", " #  #", " #  #", "  ## " });
    add('U', new String[] { "#  #", "#  #", "#  #", "#  #", "#  #", " ## " });

    add('X', new String[] { "#    #", "#    #", " #  # ", " #  # ", "  ##  ", "  ##  ", " #  # ", " #  # ", "#    #", "#    #" });

    add('Y', new String[] { "#   #", "#   #", " # # ", "  #  ", "  #  ", "  #  " });

    add('Z', new String[] { "######", "     #", "     #", "    # ", "   #  ", "  #   ", " #    ", "#     ", "#     ", "######" });
    add('Z', new String[] { "#### ", "   # ", "  #  ", " #   ", "#    ", "#### " });
    add('Z', new String[] { " ####", "    #", "   # ", "  #  ", " #   ", " ####" });
    add('Z', new String[] { "####", "   #", "  # ", " #  ", "#   ", "####" });
  }

  private void add(final int ch, final String[] lines) {
    final Long key = toBits(lines);
    if (bitsToCodePoints.containsKey(key)) {
      // If there is a collision between different font elements, I need to figure out how to handle it.
      throw new IllegalStateException("Duplicate key for [" + Character.toString(ch) + "]");
    }
    bitsToCodePoints.put(key, Integer.valueOf(ch));
  }

  /**
   * Given a boolean array where true represents pixels that are on, convert a rectangle of it to a code point that
   * represents that character.
   *
   * @param array the full array of pixel data.
   * @param x0 the first column to check for character data.
   * @param width the maximum width of a character.
   * @return the code point representing that digit.
   * @throws NoSuchElementException if there is no mapping for the provided digit.
   */
  public int toCodePoint(final boolean[][] array, final int x0, final int width) {
    return toCodePoint(array, x0, width, array.length);
  }

  /**
   * Given a boolean array where true represents pixels that are on, convert a rectangle of it to a code point that
   * represents that character.
   *
   * @param array the full array of pixel data.
   * @param x0 the first column to check for character data.
   * @param width the maximum width of a character.
   * @return the code point representing that digit.
   * @throws NoSuchElementException if there is no mapping for the provided digit.
   */
  public int toCodePoint(final boolean[][] array, final int x0, final int width, final int height) {
    final Long key = toBits(array, x0, width, height);
    if (!bitsToCodePoints.containsKey(key)) {
      throw new NoSuchElementException("Character not found");
    }
    return bitsToCodePoints.get(key).intValue();
  }

  /**
   * Given a, int array where "not whitespace" represents pixels that are on, convert a rectangle of it to a code point
   * that represents that character.
   *
   * @param array the full array of pixel data.
   * @param x0 the first column to check for character data.
   * @param width the maximum width of a character.
   * @return the code point representing that digit.
   * @throws NoSuchElementException if there is no mapping for the provided digit.
   */
  public int toCodePoint(final int[][] array, final int x0, final int width) {
    return toCodePoint(array, x0, width, array.length);
  }

  /**
   * Given a, int array where "not whitespace" represents pixels that are on, convert a rectangle of it to a code point
   * that represents that character.
   *
   * @param array the full array of pixel data.
   * @param x0 the first column to check for character data.
   * @param width the maximum width of a character.
   * @return the code point representing that digit.
   * @throws NoSuchElementException if there is no mapping for the provided digit.
   */
  public int toCodePoint(final int[][] array, final int x0, final int width, final IntPredicate truth) {
    return toCodePoint(array, x0, width, array.length, truth);
  }

  /**
   * Given a, int array where "not whitespace" represents pixels that are on, convert a rectangle of it to a code point
   * that represents that character.
   *
   * @param array the full array of pixel data.
   * @param x0 the first column to check for character data.
   * @param width the maximum width of a character.
   * @return the code point representing that digit.
   * @throws NoSuchElementException if there is no mapping for the provided digit.
   */
  public int toCodePoint(final int[][] array, final int x0, final int width, final int height) {
    return toCodePoint(array, x0, width, array.length, i -> !Character.isWhitespace(i));
  }

  /**
   * Given a, int array where "not whitespace" represents pixels that are on, convert a rectangle of it to a code point
   * that represents that character.
   *
   * @param array the full array of pixel data.
   * @param x0 the first column to check for character data.
   * @param width the maximum width of a character.
   * @return the code point representing that digit.
   * @throws NoSuchElementException if there is no mapping for the provided digit.
   */
  public int toCodePoint(final int[][] array, final int x0, final int width, final int height, final IntPredicate truth) {
    final Long key = toBits(array, x0, width, height, truth);
    if (!bitsToCodePoints.containsKey(key)) {
      throw new NoSuchElementException("Character not found");
    }
    return bitsToCodePoints.get(key).intValue();
  }

  private static Long toBits(final boolean[][] array, final int x0, final int width, final int height) {
    long bits = 0;
    for (int y = 0; y < height; ++y) {
      for (int x = x0; x < x0 + width; ++x) {
        bits <<= 1;
        if (array[y][x]) {
          ++bits;
        }
      }
    }
    return Long.valueOf(bits);
  }

  private static Long toBits(final int[][] array, final int x0, final int width, final int height, final IntPredicate truth) {
    long bits = 0;
    for (int y = 0; y < height; ++y) {
      for (int x = x0; x < x0 + width; ++x) {
        bits <<= 1;
        if (truth.test(array[y][x])) {
          ++bits;
        }
      }
    }
    return Long.valueOf(bits);
  }

  private static Long toBits(final String[] letter) {
    long bitmap = 0;
    for (final String line : letter) {
      for (int ch : line.codePoints().toArray()) {
        bitmap <<= 1;
        if (!Character.isWhitespace(ch)) {
          ++bitmap;
        }
      }
    }
    return Long.valueOf(bitmap);
  }

}
