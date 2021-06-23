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
package us.coffeecode.advent_of_code;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import us.coffeecode.advent_of_code.y2017.*;

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
public class Year2017Tests {

  @Test
  public void year2017day01part1() {
    final long answer = new Year2017Day01().calculatePart1();
    assertEquals(1_182, answer);
  }

  @Test
  public void year2017day01part2() {
    final long answer = new Year2017Day01().calculatePart2();
    assertEquals(1_152, answer);
  }

  @Test
  public void year2017day02part1() {
    final long answer = new Year2017Day02().calculatePart1();
    assertEquals(32_121, answer);
  }

  @Test
  public void year2017day02part2() {
    final long answer = new Year2017Day02().calculatePart2();
    assertEquals(197, answer);
  }

  @Test
  public void year2017day03part1() {
    final long answer = new Year2017Day03().calculatePart1();
    assertEquals(480, answer);
  }

  @Test
  public void year2017day03part2() {
    final long answer = new Year2017Day03().calculatePart2();
    assertEquals(349_975, answer);
  }

  @Test
  public void year2017day04part1() {
    final long answer = new Year2017Day04().calculatePart1();
    assertEquals(383, answer);
  }

  @Test
  public void year2017day04part2() {
    final long answer = new Year2017Day04().calculatePart2();
    assertEquals(265, answer);
  }

  @Test
  public void year2017day05part1() {
    final long answer = new Year2017Day05().calculatePart1();
    assertEquals(359_348, answer);
  }

  @Test
  public void year2017day05part2() {
    final long answer = new Year2017Day05().calculatePart2();
    assertEquals(27_688_760, answer);
  }

  @Test
  public void year2017day06part1() {
    final long answer = new Year2017Day06().calculatePart1();
    assertEquals(3_156, answer);
  }

  @Test
  public void year2017day06part2() {
    final long answer = new Year2017Day06().calculatePart2();
    assertEquals(1_610, answer);
  }

  @Test
  public void year2017day07part1() {
    final String answer = new Year2017Day07().calculatePart1();
    assertEquals("aapssr", answer);
  }

  @Test
  public void year2017day07part2() {
    final long answer = new Year2017Day07().calculatePart2();
    assertEquals(1_458, answer);
  }

  @Test
  public void year2017day08part1() {
    final long answer = new Year2017Day08().calculatePart1();
    assertEquals(4_448, answer);
  }

  @Test
  public void year2017day08part2() {
    final long answer = new Year2017Day08().calculatePart2();
    assertEquals(6_582, answer);
  }

  @Test
  public void year2017day09part1() {
    final long answer = new Year2017Day09().calculatePart1();
    assertEquals(20_530, answer);
  }

  @Test
  public void year2017day09part2() {
    final long answer = new Year2017Day09().calculatePart2();
    assertEquals(9_978, answer);
  }

  @Test
  public void year2017day10part1() {
    final long answer = new Year2017Day10().calculatePart1();
    assertEquals(38_628, answer);
  }

  @Test
  public void year2017day10part2() {
    final String answer = new Year2017Day10().calculatePart2();
    assertEquals("e1462100a34221a7f0906da15c1c979a", answer);
  }

  @Test
  public void year2017day11part1() {
    final long answer = new Year2017Day11().calculatePart1();
    assertEquals(834, answer);
  }

  @Test
  public void year2017day11part2() {
    final long answer = new Year2017Day11().calculatePart2();
    assertEquals(1_569, answer);
  }

  @Test
  public void year2017day12part1() {
    final long answer = new Year2017Day12().calculatePart1();
    assertEquals(169, answer);
  }

  @Test
  public void year2017day12part2() {
    final long answer = new Year2017Day12().calculatePart2();
    assertEquals(179, answer);
  }

  @Test
  public void year2017day13part1() {
    final long answer = new Year2017Day13().calculatePart1();
    assertEquals(1_300, answer);
  }

  @Test
  public void year2017day13part2() {
    final long answer = new Year2017Day13().calculatePart2();
    assertEquals(3_870_382, answer);
  }

  @Test
  public void year2017day14part1() {
    final long answer = new Year2017Day14().calculatePart1();
    assertEquals(8_190, answer);
  }

  @Test
  public void year2017day14part2() {
    final long answer = new Year2017Day14().calculatePart2();
    assertEquals(1_134, answer);
  }

  @Test
  public void year2017day15part1() {
    final long answer = new Year2017Day15().calculatePart1();
    assertEquals(594, answer);
  }

  @Test
  public void year2017day15part2() {
    final long answer = new Year2017Day15().calculatePart2();
    assertEquals(328, answer);
  }

  @Test
  public void year2017day16part1() {
    final String answer = new Year2017Day16().calculatePart1();
    assertEquals("hmefajngplkidocb", answer);
  }

  @Test
  public void year2017day16part2() {
    final String answer = new Year2017Day16().calculatePart2();
    assertEquals("fbidepghmjklcnoa", answer);
  }

  @Test
  public void year2017day17part1() {
    final long answer = new Year2017Day17().calculatePart1();
    assertEquals(777, answer);
  }

  @Test
  public void year2017day17part2() {
    final long answer = new Year2017Day17().calculatePart2();
    assertEquals(39_289_581, answer);
  }

  @Test
  public void year2017day18part1() {
    final long answer = new Year2017Day18().calculatePart1();
    assertEquals(4_601, answer);
  }

  @Test
  public void year2017day18part2() {
    final long answer = new Year2017Day18().calculatePart2();
    assertEquals(6_858, answer);
  }

  @Test
  public void year2017day19part1() {
    final String answer = new Year2017Day19().calculatePart1();
    assertEquals("GINOWKYXH", answer);
  }

  @Test
  public void year2017day19part2() {
    final long answer = new Year2017Day19().calculatePart2();
    assertEquals(16_636, answer);
  }

  @Test
  public void year2017day20part1() {
    final long answer = new Year2017Day20().calculatePart1();
    assertEquals(157, answer);
  }

  @Test
  public void year2017day20part2() {
    final long answer = new Year2017Day20().calculatePart2();
    assertEquals(499, answer);
  }

  @Test
  public void year2017day21part1() {
    final long answer = new Year2017Day21().calculatePart1();
    assertEquals(194, answer);
  }

  @Test
  public void year2017day21part2() {
    final long answer = new Year2017Day21().calculatePart2();
    assertEquals(2_536_879, answer);
  }

  @Test
  public void year2017day22part1() {
    final long answer = new Year2017Day22().calculatePart1();
    assertEquals(5_256, answer);
  }

  @Test
  public void year2017day22part2() {
    final long answer = new Year2017Day22().calculatePart2();
    assertEquals(2_511_345, answer);
  }

  @Test
  public void year2017day23part1() {
    final long answer = new Year2017Day23().calculatePart1();
    assertEquals(6_724, answer);
  }

  @Test
  public void year2017day23part2() {
    final long answer = new Year2017Day23().calculatePart2();
    assertEquals(903, answer);
  }

  @Test
  public void year2017day24part1() {
    final long answer = new Year2017Day24().calculatePart1();
    assertEquals(1_906, answer);
  }

  @Test
  public void year2017day24part2() {
    final long answer = new Year2017Day24().calculatePart2();
    assertEquals(1_824, answer);
  }

  @Test
  public void year2017day25part1() {
    final long answer = new Year2017Day25().calculatePart1();
    assertEquals(3_145, answer);
  }

}
