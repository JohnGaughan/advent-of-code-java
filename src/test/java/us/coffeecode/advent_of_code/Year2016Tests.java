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
package us.coffeecode.advent_of_code;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import us.coffeecode.advent_of_code.y2016.*;

/**
 * <p>
 * Test harness that runs all of the individual day and part functions and validates their answers.
 * </p>
 * <p>
 * Copyright (c) 2021 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public class Year2016Tests {

  @Test
  public void year2016day01part1() {
    final long answer = new Year2016Day01().calculatePart1();
    assertEquals(271, answer);
  }

  @Test
  public void year2016day01part2() {
    final long answer = new Year2016Day01().calculatePart2();
    assertEquals(153, answer);
  }

  @Test
  public void year2016day02part1() {
    final String answer = new Year2016Day02().calculatePart1();
    assertEquals("61529", answer);
  }

  @Test
  public void year2016day02part2() {
    final String answer = new Year2016Day02().calculatePart2();
    assertEquals("C2C28", answer);
  }

  @Test
  public void year2016day03part1() {
    final long answer = new Year2016Day03().calculatePart1();
    assertEquals(862, answer);
  }

  @Test
  public void year2016day03part2() {
    final long answer = new Year2016Day03().calculatePart2();
    assertEquals(1_577, answer);
  }

  @Test
  public void year2016day04part1() {
    final long answer = new Year2016Day04().calculatePart1();
    assertEquals(361_724, answer);
  }

  @Test
  public void year2016day04part2() {
    final long answer = new Year2016Day04().calculatePart2();
    assertEquals(482, answer);
  }

  @Test
  public void year2016day05part1() {
    final String answer = new Year2016Day05().calculatePart1();
    assertEquals("1a3099aa", answer);
  }

  @Test
  public void year2016day05part2() {
    final String answer = new Year2016Day05().calculatePart2();
    assertEquals("694190cd", answer);
  }

  @Test
  public void year2016day06part1() {
    final String answer = new Year2016Day06().calculatePart1();
    assertEquals("xhnqpqql", answer);
  }

  @Test
  public void year2016day06part2() {
    final String answer = new Year2016Day06().calculatePart2();
    assertEquals("brhailro", answer);
  }

  @Test
  public void year2016day07part1() {
    final long answer = new Year2016Day07().calculatePart1();
    assertEquals(110, answer);
  }

  @Test
  public void year2016day07part2() {
    final long answer = new Year2016Day07().calculatePart2();
    assertEquals(242, answer);
  }

  @Test
  public void year2016day08part1() {
    final long answer = new Year2016Day08().calculatePart1();
    assertEquals(119, answer);
  }

  @Test
  public void year2016day08part2() {
    final String answer = new Year2016Day08().calculatePart2();
    assertEquals("ZFHFSFOGPO", answer);
  }

  @Test
  public void year2016day09part1() {
    final long answer = new Year2016Day09().calculatePart1();
    assertEquals(110_346, answer);
  }

  @Test
  public void year2016day09part2() {
    final long answer = new Year2016Day09().calculatePart2();
    assertEquals(10_774_309_173L, answer);
  }

  @Test
  public void year2016day10part1() {
    final long answer = new Year2016Day10().calculatePart1();
    assertEquals(27, answer);
  }

  @Test
  public void year2016day10part2() {
    final long answer = new Year2016Day10().calculatePart2();
    assertEquals(13_727, answer);
  }

  @Test
  public void year2016day11part1() {
    final long answer = new Year2016Day11().calculatePart1();
    assertEquals(31, answer);
  }

  @Test
  public void year2016day11part2() {
    final long answer = new Year2016Day11().calculatePart2();
    assertEquals(55, answer);
  }

  @Test
  public void year2016day12part1() {
    final long answer = new Year2016Day12().calculatePart1();
    assertEquals(318_083, answer);
  }

  @Test
  public void year2016day12part2() {
    final long answer = new Year2016Day12().calculatePart2();
    assertEquals(9_227_737, answer);
  }

  @Test
  public void year2016day13part1() {
    final long answer = new Year2016Day13().calculatePart1();
    assertEquals(90, answer);
  }

  @Test
  public void year2016day13part2() {
    final long answer = new Year2016Day13().calculatePart2();
    assertEquals(135, answer);
  }

  @Test
  public void year2016day14part1() {
    final long answer = new Year2016Day14().calculatePart1();
    assertEquals(16_106, answer);
  }

  @Test
  public void year2016day14part2() {
    final long answer = new Year2016Day14().calculatePart2();
    assertEquals(22_423, answer);
  }

  @Test
  public void year2016day15part1() {
    final long answer = new Year2016Day15().calculatePart1();
    assertEquals(203_660, answer);
  }

  @Test
  public void year2016day15part2() {
    final long answer = new Year2016Day15().calculatePart2();
    assertEquals(2_408_135, answer);
  }

  @Test
  public void year2016day16part1() {
    final String answer = new Year2016Day16().calculatePart1();
    assertEquals("10010101010011101", answer);
  }

  @Test
  public void year2016day16part2() {
    final String answer = new Year2016Day16().calculatePart2();
    assertEquals("01100111101101111", answer);
  }

  @Test
  public void year2016day17part1() {
    final String answer = new Year2016Day17().calculatePart1();
    assertEquals("RLDRUDRDDR", answer);
  }

  @Test
  public void year2016day17part2() {
    final long answer = new Year2016Day17().calculatePart2();
    assertEquals(498, answer);
  }

  @Test
  public void year2016day18part1() {
    final long answer = new Year2016Day18().calculatePart1();
    assertEquals(1_974, answer);
  }

  @Test
  public void year2016day18part2() {
    final long answer = new Year2016Day18().calculatePart2();
    assertEquals(19_991_126, answer);
  }

  @Test
  public void year2016day19part1() {
    final long answer = new Year2016Day19().calculatePart1();
    assertEquals(1_808_357, answer);
  }

  @Test
  public void year2016day19part2() {
    final long answer = new Year2016Day19().calculatePart2();
    assertEquals(1_407_007, answer);
  }

  @Test
  public void year2016day20part1() {
    final long answer = new Year2016Day20().calculatePart1();
    assertEquals(31_053_880, answer);
  }

  @Test
  public void year2016day20part2() {
    final long answer = new Year2016Day20().calculatePart2();
    assertEquals(117, answer);
  }

  @Test
  public void year2016day21part1() {
    final String answer = new Year2016Day21().calculatePart1();
    assertEquals("gbhcefad", answer);
  }

  @Test
  public void year2016day21part2() {
    final String answer = new Year2016Day21().calculatePart2();
    assertEquals("gahedfcb", answer);
  }

  @Test
  public void year2016day22part1() {
    final long answer = new Year2016Day22().calculatePart1();
    assertEquals(960, answer);
  }

  @Test
  public void year2016day22part2() {
    final long answer = new Year2016Day22().calculatePart2();
    assertEquals(225, answer);
  }

  @Test
  public void year2016day23part1() {
    final long answer = new Year2016Day23().calculatePart1();
    assertEquals(13_685, answer);
  }

  @Test
  public void year2016day23part2() {
    final long answer = new Year2016Day23().calculatePart2();
    assertEquals(479_010_245, answer);
  }

  @Test
  public void year2016day24part1() {
    final long answer = new Year2016Day24().calculatePart1();
    assertEquals(456, answer);
  }

  @Test
  public void year2016day24part2() {
    final long answer = new Year2016Day24().calculatePart2();
    assertEquals(704, answer);
  }

  @Test
  public void year2016day25part1() {
    final long answer = new Year2016Day25().calculate();
    assertEquals(198, answer);
  }

}
