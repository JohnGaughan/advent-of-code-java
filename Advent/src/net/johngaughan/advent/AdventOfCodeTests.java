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
package net.johngaughan.advent;

import static org.junit.Assert.assertEquals;

import java.nio.file.Path;

import org.junit.Test;

import net.johngaughan.advent.y2015.day1.Year2015Day1Part1;
import net.johngaughan.advent.y2015.day1.Year2015Day1Part2;
import net.johngaughan.advent.y2015.day2.Year2015Day2Part1;
import net.johngaughan.advent.y2015.day2.Year2015Day2Part2;
import net.johngaughan.advent.y2015.day3.Year2015Day3Part1;
import net.johngaughan.advent.y2015.day3.Year2015Day3Part2;
import net.johngaughan.advent.y2020.day1.Year2020Day1Part1;
import net.johngaughan.advent.y2020.day1.Year2020Day1Part2;
import net.johngaughan.advent.y2020.day2.Year2020Day2Part1;
import net.johngaughan.advent.y2020.day2.Year2020Day2Part2;
import net.johngaughan.advent.y2020.day3.Year2020Day3Part1;
import net.johngaughan.advent.y2020.day3.Year2020Day3Part2;
import net.johngaughan.advent.y2020.day4.Year2020Day4Part1;
import net.johngaughan.advent.y2020.day4.Year2020Day4Part2;
import net.johngaughan.advent.y2020.day5.Year2020Day5Part1;
import net.johngaughan.advent.y2020.day5.Year2020Day5Part2;
import net.johngaughan.advent.y2020.day6.Year2020Day6Part1;
import net.johngaughan.advent.y2020.day6.Year2020Day6Part2;

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
public class AdventOfCodeTests {

  private static Path getInput(final int year, final int day) {
    return Path.of("input", Integer.toString(year), "day" + day + ".txt");
  }

  /////////////////////////////////////////////////////////////////////////////////////////////////
  // 2015
  /////////////////////////////////////////////////////////////////////////////////////////////////

  @Test
  public void year2015day1part1() {
    long answer = new Year2015Day1Part1().calculate(getInput(2015, 1));
    assertEquals(138, answer);
  }

  @Test
  public void year2015day1part2() {
    long answer = new Year2015Day1Part2().calculate(getInput(2015, 1));
    assertEquals(1771, answer);
  }

  @Test
  public void year2015day2part1() {
    long answer = new Year2015Day2Part1().calculate(getInput(2015, 2));
    assertEquals(1588178, answer);
  }

  @Test
  public void year2015day2part2() {
    long answer = new Year2015Day2Part2().calculate(getInput(2015, 2));
    assertEquals(3783758, answer);
  }

  @Test
  public void year2015day3part1() {
    long answer = new Year2015Day3Part1().calculate(getInput(2015, 3));
    assertEquals(2592, answer);
  }

  @Test
  public void year2015day3part2() {
    long answer = new Year2015Day3Part2().calculate(getInput(2015, 3));
    assertEquals(2360, answer);
  }

  /////////////////////////////////////////////////////////////////////////////////////////////////
  // 2020
  /////////////////////////////////////////////////////////////////////////////////////////////////

  @Test
  public void year2020day1part1() {
    long answer = new Year2020Day1Part1().calculate(getInput(2020, 1));
    assertEquals(1014624, answer);
  }

  @Test
  public void year2020day1part2() {
    long answer = new Year2020Day1Part2().calculate(getInput(2020, 1));
    assertEquals(80072256, answer);
  }

  @Test
  public void year2020day2part1() {
    long answer = new Year2020Day2Part1().calculate(getInput(2020, 2));
    assertEquals(445, answer);
  }

  @Test
  public void year2020day2part2() {
    long answer = new Year2020Day2Part2().calculate(getInput(2020, 2));
    assertEquals(491, answer);
  }

  @Test
  public void year2020day3part1() {
    long answer = new Year2020Day3Part1().calculate(getInput(2020, 3));
    assertEquals(191, answer);
  }

  @Test
  public void year2020day3part2() {
    long answer = new Year2020Day3Part2().calculate(getInput(2020, 3));
    assertEquals(1478615040, answer);
  }

  @Test
  public void year2020day4part1() {
    long answer = new Year2020Day4Part1().calculate(getInput(2020, 4));
    assertEquals(260, answer);
  }

  @Test
  public void year2020day4part2() {
    long answer = new Year2020Day4Part2().calculate(getInput(2020, 4));
    assertEquals(153, answer);
  }

  @Test
  public void year2020day5part1() {
    long answer = new Year2020Day5Part1().calculate(getInput(2020, 5));
    assertEquals(880, answer);
  }

  @Test
  public void year2020day5part2() {
    long answer = new Year2020Day5Part2().calculate(getInput(2020, 5));
    assertEquals(731, answer);
  }

  @Test
  public void year2020day6part1() {
    long answer = new Year2020Day6Part1().calculate(getInput(2020, 6));
    assertEquals(6291, answer);
  }

  @Test
  public void year2020day6part2() {
    long answer = new Year2020Day6Part2().calculate(getInput(2020, 6));
    assertEquals(3052, answer);
  }

}
