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
package us.coffeecode.advent_of_code.y2019;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.DigitConverter;

@AdventOfCodeSolution(year = 2019, day = 8, title = "Space Image Format")
@Component
public final class Year2019Day08 {

  final int WIDTH = 25;

  final int HEIGHT = 6;

  final int LAYER_SIZE = WIDTH * HEIGHT;

  final int BLACK = 0;

  final int WHITE = 1;

  final int TRANSPARENT = 2;

  final int LETTER_WIDTH = 5;

  @Autowired
  private InputLoader il;

  @Autowired
  private DigitConverter dc;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final var layers = getLayers(pc);
    int fewestZeros = Integer.MAX_VALUE;
    int score = 0;
    for (final int[][] layer : layers) {
      final int count[] = count(layer);
      if (count[0] < fewestZeros) {
        fewestZeros = count[0];
        score = count[1] * count[2];
      }
    }
    return score;
  }

  @Solver(part = 2)
  public String calculatePart2(final PuzzleContext pc) {
    final var layers = getLayers(pc);
    final int[][] squished = squish(layers);
    final StringBuilder result = new StringBuilder(5);
    for (int i = 0; i < WIDTH; i += LETTER_WIDTH) {
      final boolean[][] letter = new boolean[HEIGHT][LETTER_WIDTH];
      for (int y = 0; y < HEIGHT; ++y) {
        for (int x = 0; x < LETTER_WIDTH; ++x) {
          if (squished[y][x + i] == WHITE) {
            letter[y][x] = true;
          }
        }
      }
      result.appendCodePoint(dc.toCodePoint(letter, 0, LETTER_WIDTH));
    }
    return result.toString();
  }

  /** Squish all layers down to a single layer. */
  private int[][] squish(final int[][][] layers) {
    final int[][] squished = new int[HEIGHT][WIDTH];
    for (int y = 0; y < HEIGHT; ++y) {
      for (int x = 0; x < WIDTH; ++x) {
        // Find the first non-transparent pixel going top to bottom.
        for (int i = 0; i < layers.length; ++i) {
          if (layers[i][y][x] != TRANSPARENT) {
            squished[y][x] = layers[i][y][x];
            break;
          }
        }
      }
    }
    return squished;
  }

  /** Count the number of zeros, ones, and twos in the array. Counts are stored in their array positions. */
  private int[] count(final int[][] array) {
    final int[] count = new int[3];
    for (final int[] row : array) {
      for (final int element : row) {
        ++count[element];
      }
    }
    return count;
  }

  /** Get the input, then parse them into the image layers. */
  private int[][][] getLayers(final PuzzleContext pc) {
    final int[] input = il.fileAsIntsFromDigits(pc);
    final int[][][] layers = new int[input.length / LAYER_SIZE][HEIGHT][WIDTH];
    for (int i = 0; i < layers.length; ++i) {
      for (int y = 0; y < HEIGHT; ++y) {
        for (int x = 0; x < WIDTH; ++x) {
          layers[i][y][x] = input[i * LAYER_SIZE + y * WIDTH + x];
        }
      }
    }
    return layers;
  }

}
