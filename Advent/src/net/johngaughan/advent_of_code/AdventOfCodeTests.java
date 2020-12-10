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
package net.johngaughan.advent_of_code;

import static org.junit.Assert.assertEquals;

import java.nio.file.Path;

import org.junit.Test;

import net.johngaughan.advent_of_code.y2015.*;
import net.johngaughan.advent_of_code.y2020.Year2020Day01;
import net.johngaughan.advent_of_code.y2020.Year2020Day02;
import net.johngaughan.advent_of_code.y2020.Year2020Day03;
import net.johngaughan.advent_of_code.y2020.Year2020Day04;
import net.johngaughan.advent_of_code.y2020.Year2020Day05;
import net.johngaughan.advent_of_code.y2020.Year2020Day06;
import net.johngaughan.advent_of_code.y2020.Year2020Day07;
import net.johngaughan.advent_of_code.y2020.Year2020Day08;
import net.johngaughan.advent_of_code.y2020.Year2020Day09;

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
public final class AdventOfCodeTests {

  private static Path getInput(final int year, final int day) {
    return Path.of("input", Integer.toString(year), "day" + (day < 10 ? "0" + day : day) + ".txt");
  }

  /////////////////////////////////////////////////////////////////////////////////////////////////
  // 2015
  /////////////////////////////////////////////////////////////////////////////////////////////////

  @Test
  public void year2015day01part1() {
    final long answer = new Year2015Day01().calculatePart1(getInput(2015, 1));
    assertEquals(138, answer);
  }

  @Test
  public void year2015day01part2() {
    final long answer = new Year2015Day01().calculatePart2(getInput(2015, 1));
    assertEquals(1_771, answer);
  }

  @Test
  public void year2015day02part1() {
    final long answer = new Year2015Day02().calculatePart1(getInput(2015, 2));
    assertEquals(1_588_178, answer);
  }

  @Test
  public void year2015day02part2() {
    final long answer = new Year2015Day02().calculatePart2(getInput(2015, 2));
    assertEquals(3_783_758, answer);
  }

  @Test
  public void year2015day03part1() {
    final long answer = new Year2015Day03().calculatePart1(getInput(2015, 3));
    assertEquals(2_592, answer);
  }

  @Test
  public void year2015day03part2() {
    final long answer = new Year2015Day03().calculatePart2(getInput(2015, 3));
    assertEquals(2_360, answer);
  }

  @Test
  public void year2015day04part1() {
    final long answer = new Year2015Day04().calculatePart1();
    assertEquals(117_946, answer);
  }

  @Test
  public void year2015day04part2() {
    final long answer = new Year2015Day04().calculatePart2();
    assertEquals(3_938_038, answer);
  }

  @Test
  public void year2015day05part1() {
    final long answer = new Year2015Day05().calculatePart1(getInput(2015, 5));
    assertEquals(238, answer);
  }

  @Test
  public void year2015day05part2() {
    final long answer = new Year2015Day05().calculatePart2(getInput(2015, 5));
    assertEquals(69, answer);
  }

  @Test
  public void year2015day06part1() {
    final long answer = new Year2015Day06().calculatePart1(getInput(2015, 6));
    assertEquals(377_891, answer);
  }

  @Test
  public void year2015day06part2() {
    final long answer = new Year2015Day06().calculatePart2(getInput(2015, 6));
    assertEquals(14_110_788, answer);
  }

  @Test
  public void year2015day07part1() {
    final long answer = new Year2015Day07().calculatePart1(getInput(2015, 7));
    assertEquals(16_076, answer);
  }

  @Test
  public void year2015day07part2() {
    final long answer = new Year2015Day07().calculatePart2(getInput(2015, 7));
    assertEquals(2_797, answer);
  }

  @Test
  public void year2015day08part1() {
    final long answer = new Year2015Day08().calculatePart1(getInput(2015, 8));
    assertEquals(1_350, answer);
  }

  @Test
  public void year2015day08part2() {
    final long answer = new Year2015Day08().calculatePart2(getInput(2015, 8));
    assertEquals(2_085, answer);
  }

  @Test
  public void year2015day09part1() {
    final long answer = new Year2015Day09().calculatePart1(getInput(2015, 9));
    assertEquals(207, answer);
  }

  @Test
  public void year2015day09part2() {
    final long answer = new Year2015Day09().calculatePart2(getInput(2015, 9));
    assertEquals(804, answer);
  }

  @Test
  public void year2015day10part1() {
    final long answer = new Year2015Day10().calculatePart1();
    assertEquals(360_154, answer);
  }

  @Test
  public void year2015day10part2() {
    final long answer = new Year2015Day10().calculatePart2();
    assertEquals(5_103_798, answer);
  }

  @Test
  public void year2015day11part1() {
    final String answer = new Year2015Day11().calculatePart1();
    assertEquals("vzbxxyzz", answer);
  }

  @Test
  public void year2015day11part2() {
    final String answer = new Year2015Day11().calculatePart2();
    assertEquals("vzcaabcc", answer);
  }

  @Test
  public void year2015day12part1() {
    final long answer = new Year2015Day12().calculatePart1(getInput(2015, 12));
    assertEquals(191_164, answer);
  }

  @Test
  public void year2015day12part2() {
    final long answer = new Year2015Day12().calculatePart2(getInput(2015, 12));
    assertEquals(87_842, answer);
  }

  @Test
  public void year2015day13part1() {
    final long answer = new Year2015Day13().calculatePart1(getInput(2015, 13));
    assertEquals(733, answer);
  }

  @Test
  public void year2015day13part2() {
    final long answer = new Year2015Day13().calculatePart2(getInput(2015, 13));
    assertEquals(725, answer);
  }

  @Test
  public void year2015day14part1() {
    final long answer = new Year2015Day14().calculatePart1(getInput(2015, 14));
    assertEquals(2_696, answer);
  }

  @Test
  public void year2015day14part2() {
    final long answer = new Year2015Day14().calculatePart2(getInput(2015, 14));
    assertEquals(1_084, answer);
  }

  @Test
  public void year2015day15part1() {
    final long answer = new Year2015Day15().calculatePart1(getInput(2015, 15));
    assertEquals(21367368, answer);
  }

  @Test
  public void year2015day15part2() {
    final long answer = new Year2015Day15().calculatePart2(getInput(2015, 15));
    assertEquals(1766400, answer);
  }

  @Test
  public void year2015day16part1() {
    final long answer = new Year2015Day16().calculatePart1(getInput(2015, 16));
    assertEquals(40, answer);
  }

  @Test
  public void year2015day16part2() {
    final long answer = new Year2015Day16().calculatePart2(getInput(2015, 16));
    assertEquals(241, answer);
  }

  @Test
  public void year2015day17part1() {
    final long answer = new Year2015Day17().calculatePart1(getInput(2015, 17));
    assertEquals(4372, answer);
  }

  @Test
  public void year2015day17part2() {
    final long answer = new Year2015Day17().calculatePart2(getInput(2015, 17));
    assertEquals(4, answer);
  }

  /////////////////////////////////////////////////////////////////////////////////////////////////
  // 2020
  /////////////////////////////////////////////////////////////////////////////////////////////////

  @Test
  public void year2020day01part1() {
    final long answer = new Year2020Day01().calculatePart1(getInput(2020, 1));
    assertEquals(1_014_624, answer);
  }

  @Test
  public void year2020day01part2() {
    final long answer = new Year2020Day01().calculatePart2(getInput(2020, 1));
    assertEquals(80_072_256, answer);
  }

  @Test
  public void year2020day02part1() {
    final long answer = new Year2020Day02().calculatePart1(getInput(2020, 2));
    assertEquals(445, answer);
  }

  @Test
  public void year2020day02part2() {
    final long answer = new Year2020Day02().calculatePart2(getInput(2020, 2));
    assertEquals(491, answer);
  }

  @Test
  public void year2020day03part1() {
    final long answer = new Year2020Day03().calculatePart1(getInput(2020, 3));
    assertEquals(191, answer);
  }

  @Test
  public void year2020day03part2() {
    final long answer = new Year2020Day03().calculatePart2(getInput(2020, 3));
    assertEquals(1_478_615_040, answer);
  }

  @Test
  public void year2020day04part1() {
    final long answer = new Year2020Day04().calculatePart1(getInput(2020, 4));
    assertEquals(260, answer);
  }

  @Test
  public void year2020day04part2() {
    final long answer = new Year2020Day04().calculatePart2(getInput(2020, 4));
    assertEquals(153, answer);
  }

  @Test
  public void year2020day05part1() {
    final long answer = new Year2020Day05().calculatePart1(getInput(2020, 5));
    assertEquals(880, answer);
  }

  @Test
  public void year2020day05part2() {
    final long answer = new Year2020Day05().calculatePart2(getInput(2020, 5));
    assertEquals(731, answer);
  }

  @Test
  public void year2020day06part1() {
    final long answer = new Year2020Day06().calculatePart1(getInput(2020, 6));
    assertEquals(6_291, answer);
  }

  @Test
  public void year2020day06part2() {
    final long answer = new Year2020Day06().calculatePart2(getInput(2020, 6));
    assertEquals(3_052, answer);
  }

  @Test
  public void year2020day07part1() {
    final long answer = new Year2020Day07().calculatePart1(getInput(2020, 7));
    assertEquals(348, answer);
  }

  @Test
  public void year2020day07part2() {
    final long answer = new Year2020Day07().calculatePart2(getInput(2020, 7));
    assertEquals(18_885, answer);
  }

  @Test
  public void year2020day08part1() {
    final long answer = new Year2020Day08().calculatePart1(getInput(2020, 8));
    assertEquals(1_859, answer);
  }

  @Test
  public void year2020day08part2() {
    final long answer = new Year2020Day08().calculatePart2(getInput(2020, 8));
    assertEquals(1_235, answer);
  }

  @Test
  public void year2020day09part1() {
    final long answer = new Year2020Day09().calculatePart1(getInput(2020, 9));
    assertEquals(177_777_905, answer);
  }

  @Test
  public void year2020day09part2() {
    final long answer = new Year2020Day09().calculatePart2(getInput(2020, 9));
    assertEquals(23_463_012, answer);
  }

}
