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

import net.johngaughan.advent.y2015.Year2015Day1;
import net.johngaughan.advent.y2015.Year2015Day2;
import net.johngaughan.advent.y2015.Year2015Day3;
import net.johngaughan.advent.y2015.Year2015Day4;
import net.johngaughan.advent.y2015.Year2015Day5;
import net.johngaughan.advent.y2015.Year2015Day6;
import net.johngaughan.advent.y2015.Year2015Day7;
import net.johngaughan.advent.y2020.Year2020Day1;
import net.johngaughan.advent.y2020.Year2020Day2;
import net.johngaughan.advent.y2020.Year2020Day3;
import net.johngaughan.advent.y2020.Year2020Day4;
import net.johngaughan.advent.y2020.Year2020Day5;
import net.johngaughan.advent.y2020.Year2020Day6;
import net.johngaughan.advent.y2020.Year2020Day7;

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
    final long answer = new Year2015Day1().calculatePart1(getInput(2015, 1));
    assertEquals(138, answer);
  }

  @Test
  public void year2015day1part2() {
    final long answer = new Year2015Day1().calculatePart2(getInput(2015, 1));
    assertEquals(1771, answer);
  }

  @Test
  public void year2015day2part1() {
    final long answer = new Year2015Day2().calculatePart1(getInput(2015, 2));
    assertEquals(1588178, answer);
  }

  @Test
  public void year2015day2part2() {
    final long answer = new Year2015Day2().calculatePart2(getInput(2015, 2));
    assertEquals(3783758, answer);
  }

  @Test
  public void year2015day3part1() {
    final long answer = new Year2015Day3().calculatePart1(getInput(2015, 3));
    assertEquals(2592, answer);
  }

  @Test
  public void year2015day3part2() {
    final long answer = new Year2015Day3().calculatePart2(getInput(2015, 3));
    assertEquals(2360, answer);
  }

  @Test
  public void year2015day4part1() {
    final long answer = new Year2015Day4().calculatePart1(getInput(2015, 4));
    assertEquals(117946, answer);
  }

  @Test
  public void year2015day4part2() {
    final long answer = new Year2015Day4().calculatePart2(getInput(2015, 4));
    assertEquals(3938038, answer);
  }

  @Test
  public void year2015day5part1() {
    final long answer = new Year2015Day5().calculatePart1(getInput(2015, 5));
    assertEquals(238, answer);
  }

  @Test
  public void year2015day5part2() {
    final long answer = new Year2015Day5().calculatePart2(getInput(2015, 5));
    assertEquals(69, answer);
  }

  @Test
  public void year2015day6part1() {
    final long answer = new Year2015Day6().calculatePart1(getInput(2015, 6));
    assertEquals(377891, answer);
  }

  @Test
  public void year2015day6part2() {
    final long answer = new Year2015Day6().calculatePart2(getInput(2015, 6));
    assertEquals(14110788, answer);
  }

  @Test
  public void year2015day7part1() {
    final long answer = new Year2015Day7().calculatePart1(getInput(2015, 7));
    assertEquals(16076, answer);
  }

  @Test
  public void year2015day7part2() {
    final long answer = new Year2015Day7().calculatePart2(getInput(2015, 7));
    assertEquals(2797, answer);
  }

  /////////////////////////////////////////////////////////////////////////////////////////////////
  // 2020
  /////////////////////////////////////////////////////////////////////////////////////////////////

  @Test
  public void year2020day1part1() {
    final long answer = new Year2020Day1().calculatePart1(getInput(2020, 1));
    assertEquals(1014624, answer);
  }

  @Test
  public void year2020day1part2() {
    final long answer = new Year2020Day1().calculatePart2(getInput(2020, 1));
    assertEquals(80072256, answer);
  }

  @Test
  public void year2020day2part1() {
    final long answer = new Year2020Day2().calculatePart1(getInput(2020, 2));
    assertEquals(445, answer);
  }

  @Test
  public void year2020day2part2() {
    final long answer = new Year2020Day2().calculatePart2(getInput(2020, 2));
    assertEquals(491, answer);
  }

  @Test
  public void year2020day3part1() {
    final long answer = new Year2020Day3().calculatePart1(getInput(2020, 3));
    assertEquals(191, answer);
  }

  @Test
  public void year2020day3part2() {
    final long answer = new Year2020Day3().calculatePart2(getInput(2020, 3));
    assertEquals(1478615040, answer);
  }

  @Test
  public void year2020day4part1() {
    final long answer = new Year2020Day4().calculatePart1(getInput(2020, 4));
    assertEquals(260, answer);
  }

  @Test
  public void year2020day4part2() {
    final long answer = new Year2020Day4().calculatePart2(getInput(2020, 4));
    assertEquals(153, answer);
  }

  @Test
  public void year2020day5part1() {
    final long answer = new Year2020Day5().calculatePart1(getInput(2020, 5));
    assertEquals(880, answer);
  }

  @Test
  public void year2020day5part2() {
    final long answer = new Year2020Day5().calculatePart2(getInput(2020, 5));
    assertEquals(731, answer);
  }

  @Test
  public void year2020day6part1() {
    final long answer = new Year2020Day6().calculatePart1(getInput(2020, 6));
    assertEquals(6291, answer);
  }

  @Test
  public void year2020day6part2() {
    final long answer = new Year2020Day6().calculatePart2(getInput(2020, 6));
    assertEquals(3052, answer);
  }

  @Test
  public void year2020day7part1() {
    final long answer = new Year2020Day7().calculatePart1(getInput(2020, 7));
    assertEquals(348, answer);
  }

  @Test
  public void year2020day7part2() {
    final long answer = new Year2020Day7().calculatePart2(getInput(2020, 7));
    assertEquals(18885, answer);
  }

}
