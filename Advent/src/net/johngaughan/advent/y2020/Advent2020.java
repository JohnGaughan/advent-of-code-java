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
package net.johngaughan.advent.y2020;

import static org.junit.Assert.assertEquals;

import java.nio.file.Path;

import org.junit.Test;

import net.johngaughan.advent.y2020.day1.Day1Part1;
import net.johngaughan.advent.y2020.day1.Day1Part2;
import net.johngaughan.advent.y2020.day2.Day2Part1;
import net.johngaughan.advent.y2020.day2.Day2Part2;
import net.johngaughan.advent.y2020.day3.Day3Part1;
import net.johngaughan.advent.y2020.day3.Day3Part2;
import net.johngaughan.advent.y2020.day4.Day4Part1;
import net.johngaughan.advent.y2020.day4.Day4Part2;
import net.johngaughan.advent.y2020.day5.Day5Part1;
import net.johngaughan.advent.y2020.day5.Day5Part2;
import net.johngaughan.advent.y2020.day6.Day6Part1;
import net.johngaughan.advent.y2020.day6.Day6Part2;

/**
 * <p>
 * Test harness that runs all of the individual day and part functions and validates their answers.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@johngaughan.net&gt;
 */
public class Advent2020 {

  private static Path getInput(final int day) {
    return Path.of("input", "2020", "day" + day + ".txt");
  }

  @Test
  public void day1Part1() {
    long answer = new Day1Part1().calculate(getInput(1));
    assertEquals(1014624, answer);
  }

  @Test
  public void day1Part2() {
    long answer = new Day1Part2().calculate(getInput(1));
    assertEquals(80072256, answer);
  }

  @Test
  public void day2Part1() {
    long answer = new Day2Part1().calculate(getInput(2));
    assertEquals(445, answer);
  }

  @Test
  public void day2Part2() {
    long answer = new Day2Part2().calculate(getInput(2));
    assertEquals(491, answer);
  }

  @Test
  public void day3Part1() {
    long answer = new Day3Part1().calculate(getInput(3));
    assertEquals(191, answer);
  }

  @Test
  public void day3Part2() {
    long answer = new Day3Part2().calculate(getInput(3));
    assertEquals(1478615040, answer);
  }

  @Test
  public void day4Part1() {
    long answer = new Day4Part1().calculate(getInput(4));
    assertEquals(260, answer);
  }

  @Test
  public void day4Part2() {
    long answer = new Day4Part2().calculate(getInput(4));
    assertEquals(153, answer);
  }

  @Test
  public void day5Part1() {
    long answer = new Day5Part1().calculate(getInput(5));
    assertEquals(880, answer);
  }

  @Test
  public void day5Part2() {
    long answer = new Day5Part2().calculate(getInput(5));
    assertEquals(731, answer);
  }

  @Test
  public void day6Part1() {
    long answer = new Day6Part1().calculate(getInput(6));
    assertEquals(6291, answer);
  }

  @Test
  public void day6Part2() {
    long answer = new Day6Part2().calculate(getInput(6));
    assertEquals(3052, answer);
  }

}
