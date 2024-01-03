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
package us.coffeecode.advent_of_code.y2018;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import us.coffeecode.advent_of_code.AbstractTests;

public class TestOpCode
extends AbstractTests {

  @Test
  public void test_addr() {
    final long[] expected = new long[] { 1, 2, 3 };
    final long[] actual = new long[] { 1, 2, 0 };
    OpCode.addr.apply(actual, 0, 1, 2);
    Assertions.assertArrayEquals(expected, actual);
  }

  @Test
  public void test_addi() {
    final long[] expected = new long[] { 1, 1, 8 };
    final long[] actual = new long[] { 1, 1, 1 };
    OpCode.addi.apply(actual, 0, 7, 2);
    Assertions.assertArrayEquals(expected, actual);
  }

  @Test
  public void test_mulr() {
    final long[] expected = new long[] { 7, 3, 21 };
    final long[] actual = new long[] { 7, 3, 19 };
    OpCode.mulr.apply(actual, 0, 1, 2);
    Assertions.assertArrayEquals(expected, actual);
  }

  @Test
  public void test_muli() {
    final long[] expected = new long[] { 11, 0, 77 };
    final long[] actual = new long[] { 11, 0, 0 };
    OpCode.muli.apply(actual, 0, 7, 2);
    Assertions.assertArrayEquals(expected, actual);
  }

  @Test
  public void test_banr() {
    final long[] expected = new long[] { 4, 15, 4 };
    final long[] actual = new long[] { 4, 15, 0 };
    OpCode.banr.apply(actual, 0, 1, 2);
    Assertions.assertArrayEquals(expected, actual);
  }

  @Test
  public void test_bani() {
    final long[] expected = new long[] { 7, 0, 1 };
    final long[] actual = new long[] { 7, 0, 0 };
    OpCode.bani.apply(actual, 0, 1, 2);
    Assertions.assertArrayEquals(expected, actual);
  }

  @Test
  public void test_borr() {
    final long[] expected = new long[] { 4, 11, 15 };
    final long[] actual = new long[] { 4, 11, -1 };
    OpCode.borr.apply(actual, 0, 1, 2);
    Assertions.assertArrayEquals(expected, actual);
  }

  @Test
  public void test_bori() {
    final long[] expected = new long[] { 8, 3, 9 };
    final long[] actual = new long[] { 8, 3, 16 };
    OpCode.bori.apply(actual, 0, 1, 2);
    Assertions.assertArrayEquals(expected, actual);
  }

  @Test
  public void test_setr() {
    final long[] expected = new long[] { 12, 0, 12 };
    final long[] actual = new long[] { 12, 0, 0 };
    OpCode.setr.apply(actual, 0, 1, 2);
    Assertions.assertArrayEquals(expected, actual);
  }

  @Test
  public void test_seti() {
    final long[] expected = new long[] { 5, 7, 0 };
    final long[] actual = new long[] { 5, 7, 9 };
    OpCode.seti.apply(actual, 0, 1, 2);
    Assertions.assertArrayEquals(expected, actual);
  }

  @Test
  public void test_gtir() {
    final long[] expected = new long[] { 1, 2, 0 };
    final long[] actual = new long[] { 1, 2, -1 };
    OpCode.gtir.apply(actual, 0, 1, 2);
    Assertions.assertArrayEquals(expected, actual);
  }

  @Test
  public void test_gtri() {
    final long[] expected = new long[] { 7, 2, 1 };
    final long[] actual = new long[] { 7, 2, -1 };
    OpCode.gtri.apply(actual, 0, 1, 2);
    Assertions.assertArrayEquals(expected, actual);
  }

  @Test
  public void test_gtrr() {
    final long[] expected = new long[] { 4, 5, 0 };
    final long[] actual = new long[] { 4, 5, 99 };
    OpCode.gtrr.apply(actual, 0, 1, 2);
    Assertions.assertArrayEquals(expected, actual);
  }

  @Test
  public void test_eqir() {
    final long[] expected = new long[] { 0, 0, 1 };
    final long[] actual = new long[] { 0, 0, -1 };
    OpCode.eqir.apply(actual, 0, 1, 2);
    Assertions.assertArrayEquals(expected, actual);
  }

  @Test
  public void test_eqri() {
    final long[] expected = new long[] { 1, 0, 1 };
    final long[] actual = new long[] { 1, 0, 2 };
    OpCode.eqri.apply(actual, 0, 1, 2);
    Assertions.assertArrayEquals(expected, actual);
  }

  @Test
  public void test_eqrr() {
    final long[] expected = new long[] { 7, 7, 1 };
    final long[] actual = new long[] { 7, 7, 0 };
    OpCode.eqrr.apply(actual, 0, 1, 2);
    Assertions.assertArrayEquals(expected, actual);
  }

}
