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
    final long answer = new Year2020Day01().calculatePart1();
    assertEquals(1_014_624, answer);
  }

  @Test
  public void year2020day01part2() {
    final long answer = new Year2020Day01().calculatePart2();
    assertEquals(80_072_256, answer);
  }

  @Test
  public void year2020day02part1() {
    final long answer = new Year2020Day02().calculatePart1();
    assertEquals(445, answer);
  }

  @Test
  public void year2020day02part2() {
    final long answer = new Year2020Day02().calculatePart2();
    assertEquals(491, answer);
  }

  @Test
  public void year2020day03part1() {
    final long answer = new Year2020Day03().calculatePart1();
    assertEquals(191, answer);
  }

  @Test
  public void year2020day03part2() {
    final long answer = new Year2020Day03().calculatePart2();
    assertEquals(1_478_615_040, answer);
  }

  @Test
  public void year2020day04part1() {
    final long answer = new Year2020Day04().calculatePart1();
    assertEquals(260, answer);
  }

  @Test
  public void year2020day04part2() {
    final long answer = new Year2020Day04().calculatePart2();
    assertEquals(153, answer);
  }

  @Test
  public void year2020day05part1() {
    final long answer = new Year2020Day05().calculatePart1();
    assertEquals(880, answer);
  }

  @Test
  public void year2020day05part2() {
    final long answer = new Year2020Day05().calculatePart2();
    assertEquals(731, answer);
  }

  @Test
  public void year2020day06part1() {
    final long answer = new Year2020Day06().calculatePart1();
    assertEquals(6_291, answer);
  }

  @Test
  public void year2020day06part2() {
    final long answer = new Year2020Day06().calculatePart2();
    assertEquals(3_052, answer);
  }

  @Test
  public void year2020day07part1() {
    final long answer = new Year2020Day07().calculatePart1();
    assertEquals(348, answer);
  }

  @Test
  public void year2020day07part2() {
    final long answer = new Year2020Day07().calculatePart2();
    assertEquals(18_885, answer);
  }

  @Test
  public void year2020day08part1() {
    final long answer = new Year2020Day08().calculatePart1();
    assertEquals(1_859, answer);
  }

  @Test
  public void year2020day08part2() {
    final long answer = new Year2020Day08().calculatePart2();
    assertEquals(1_235, answer);
  }

  @Test
  public void year2020day09part1() {
    final long answer = new Year2020Day09().calculatePart1();
    assertEquals(177_777_905, answer);
  }

  @Test
  public void year2020day09part2() {
    final long answer = new Year2020Day09().calculatePart2();
    assertEquals(23_463_012, answer);
  }

  @Test
  public void year2020day10part1() {
    final long answer = new Year2020Day10().calculatePart1();
    assertEquals(2_201, answer);
  }

  @Test
  public void year2020day10part2() {
    final long answer = new Year2020Day10().calculatePart2();
    assertEquals(169_255_295_254_528L, answer);
  }

  @Test
  public void year2020day11part1() {
    final long answer = new Year2020Day11().calculatePart1();
    assertEquals(2_277, answer);
  }

  @Test
  public void year2020day11part2() {
    final long answer = new Year2020Day11().calculatePart2();
    assertEquals(2_066, answer);
  }

  @Test
  public void year2020day12part1() {
    final long answer = new Year2020Day12().calculatePart1();
    assertEquals(2_458, answer);
  }

  @Test
  public void year2020day12part2() {
    final long answer = new Year2020Day12().calculatePart2();
    assertEquals(145_117, answer);
  }

  @Test
  public void year2020day13part1() {
    final long answer = new Year2020Day13().calculatePart1();
    assertEquals(261, answer);
  }

  @Test
  public void year2020day13part2() {
    final long answer = new Year2020Day13().calculatePart2();
    assertEquals(807_435_693_182_510L, answer);
  }

  @Test
  public void year2020day14part1() {
    final long answer = new Year2020Day14().calculatePart1();
    assertEquals(6_559_449_933_360L, answer);
  }

  @Test
  public void year2020day14part2() {
    final long answer = new Year2020Day14().calculatePart2();
    assertEquals(3_369_767_240_513L, answer);
  }

  @Test
  public void year2020day15part1() {
    final long answer = new Year2020Day15().calculatePart1();
    assertEquals(273, answer);
  }

  @Test
  public void year2020day15part2() {
    final long answer = new Year2020Day15().calculatePart2();
    assertEquals(47_205, answer);
  }

  @Test
  public void year2020day16part1() {
    final long answer = new Year2020Day16().calculatePart1();
    assertEquals(20_048, answer);
  }

  @Test
  public void year2020day16part2() {
    final long answer = new Year2020Day16().calculatePart2();
    assertEquals(4_810_284_647_569L, answer);
  }

}
