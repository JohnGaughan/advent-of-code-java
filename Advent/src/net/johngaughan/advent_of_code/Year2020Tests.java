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

import org.junit.Test;

import net.johngaughan.advent_of_code.y2020.*;

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
public class Year2020Tests {

  @Test
  public void year2020day01part1() {
    final long answer = new Year2020Day01().calculatePart1(Utils.getInput(2020, 1));
    assertEquals(1_014_624, answer);
  }

  @Test
  public void year2020day01part2() {
    final long answer = new Year2020Day01().calculatePart2(Utils.getInput(2020, 1));
    assertEquals(80_072_256, answer);
  }

  @Test
  public void year2020day02part1() {
    final long answer = new Year2020Day02().calculatePart1(Utils.getInput(2020, 2));
    assertEquals(445, answer);
  }

  @Test
  public void year2020day02part2() {
    final long answer = new Year2020Day02().calculatePart2(Utils.getInput(2020, 2));
    assertEquals(491, answer);
  }

  @Test
  public void year2020day03part1() {
    final long answer = new Year2020Day03().calculatePart1(Utils.getInput(2020, 3));
    assertEquals(191, answer);
  }

  @Test
  public void year2020day03part2() {
    final long answer = new Year2020Day03().calculatePart2(Utils.getInput(2020, 3));
    assertEquals(1_478_615_040, answer);
  }

  @Test
  public void year2020day04part1() {
    final long answer = new Year2020Day04().calculatePart1(Utils.getInput(2020, 4));
    assertEquals(260, answer);
  }

  @Test
  public void year2020day04part2() {
    final long answer = new Year2020Day04().calculatePart2(Utils.getInput(2020, 4));
    assertEquals(153, answer);
  }

  @Test
  public void year2020day05part1() {
    final long answer = new Year2020Day05().calculatePart1(Utils.getInput(2020, 5));
    assertEquals(880, answer);
  }

  @Test
  public void year2020day05part2() {
    final long answer = new Year2020Day05().calculatePart2(Utils.getInput(2020, 5));
    assertEquals(731, answer);
  }

  @Test
  public void year2020day06part1() {
    final long answer = new Year2020Day06().calculatePart1(Utils.getInput(2020, 6));
    assertEquals(6_291, answer);
  }

  @Test
  public void year2020day06part2() {
    final long answer = new Year2020Day06().calculatePart2(Utils.getInput(2020, 6));
    assertEquals(3_052, answer);
  }

  @Test
  public void year2020day07part1() {
    final long answer = new Year2020Day07().calculatePart1(Utils.getInput(2020, 7));
    assertEquals(348, answer);
  }

  @Test
  public void year2020day07part2() {
    final long answer = new Year2020Day07().calculatePart2(Utils.getInput(2020, 7));
    assertEquals(18_885, answer);
  }

  @Test
  public void year2020day08part1() {
    final long answer = new Year2020Day08().calculatePart1(Utils.getInput(2020, 8));
    assertEquals(1_859, answer);
  }

  @Test
  public void year2020day08part2() {
    final long answer = new Year2020Day08().calculatePart2(Utils.getInput(2020, 8));
    assertEquals(1_235, answer);
  }

  @Test
  public void year2020day09part1() {
    final long answer = new Year2020Day09().calculatePart1(Utils.getInput(2020, 9));
    assertEquals(177_777_905, answer);
  }

  @Test
  public void year2020day09part2() {
    final long answer = new Year2020Day09().calculatePart2(Utils.getInput(2020, 9));
    assertEquals(23_463_012, answer);
  }

  @Test
  public void year2020day10part1() {
    final long answer = new Year2020Day10().calculatePart1(Utils.getInput(2020, 10));
    assertEquals(2_201, answer);
  }

  @Test
  public void year2020day10part2() {
    final long answer = new Year2020Day10().calculatePart2(Utils.getInput(2020, 10));
    assertEquals(169_255_295_254_528l, answer);
  }

  @Test
  public void year2020day11part1() {
    final long answer = new Year2020Day11().calculatePart1(Utils.getInput(2020, 11));
    assertEquals(2_277, answer);
  }

  @Test
  public void year2020day11part2() {
    final long answer = new Year2020Day11().calculatePart2(Utils.getInput(2020, 11));
    assertEquals(2_066, answer);
  }

  @Test
  public void year2020day12part1() {
    final long answer = new Year2020Day12().calculatePart1(Utils.getInput(2020, 12));
    assertEquals(2_458, answer);
  }

  @Test
  public void year2020day12part2() {
    final long answer = new Year2020Day12().calculatePart2(Utils.getInput(2020, 12));
    assertEquals(145_117, answer);
  }

  @Test
  public void year2020day13part1() {
    final long answer = new Year2020Day13().calculatePart1(Utils.getInput(2020, 13));
    assertEquals(261, answer);
  }

  @Test
  public void year2020day13part2() {
    final long answer = new Year2020Day13().calculatePart2(Utils.getInput(2020, 13));
    assertEquals(807_435_693_182_510L, answer);
  }

}
