/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2023  John Gaughan
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
package us.coffeecode.advent_of_code.util;

import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import us.coffeecode.advent_of_code.AbstractTests;

public class TestMyIntMath
extends AbstractTests {

  @Test
  public void test_Constant_Zero() {
    Assertions.assertEquals(0, MyIntMath.ZERO.intValue());
  }

  @Test
  public void test_Constant_One() {
    Assertions.assertEquals(1, MyIntMath.ONE.intValue());
  }

  // gcd(int, int)

  @Test
  public void testGCD_zero_1() {
    Assertions.assertEquals(3, MyIntMath.gcd(0, 3));
  }

  @Test
  public void testGCD_zero_2() {
    Assertions.assertEquals(5, MyIntMath.gcd(5, 0));
  }

  @Test
  public void testGCD_zero_3() {
    Assertions.assertEquals(0, MyIntMath.gcd(0, 0));
  }

  @Test
  public void testGCD_int_1() {
    Assertions.assertEquals(7, MyIntMath.gcd(14, 21));
  }

  @Test
  public void testGCD_int_2() {
    Assertions.assertEquals(2, MyIntMath.gcd(2, 2));
  }

  @Test
  public void testGCD_int_3() {
    Assertions.assertEquals(2, MyIntMath.gcd(4, 2));
  }

  @Test
  public void testGCD_int_4() {
    Assertions.assertEquals(2, MyIntMath.gcd(2, 4));
  }

  // lcm(int, int)

  @Test
  public void testLCM_int_Negative_1() {
    Assertions.assertThrows(RuntimeException.class, () -> MyIntMath.lcm(-4, 2));
  }

  @Test
  public void testLCM_int_Negative_2() {
    Assertions.assertThrows(RuntimeException.class, () -> MyIntMath.lcm(1, -1));
  }

  @Test
  public void testLCM_int_Zero_1() {
    Assertions.assertEquals(0, MyIntMath.lcm(0, 1));
  }

  @Test
  public void testLCM_int_Zero_2() {
    Assertions.assertEquals(0, MyIntMath.lcm(1, 0));
  }

  @Test
  public void testLCM_int_Zero_3() {
    Assertions.assertThrows(RuntimeException.class, () -> MyIntMath.lcm(0, 0));
  }

  @Test
  public void testLCM_int_1() {
    Assertions.assertEquals(20, MyIntMath.lcm(20, 1));
  }

  @Test
  public void testLCM_int_2() {
    Assertions.assertEquals(15, MyIntMath.lcm(3, 5));
  }

  @Test
  public void testLCM_int_3() {
    Assertions.assertEquals(8, MyIntMath.lcm(8, 2));
  }

  // lcm(int[])

  @Test
  public void testLCM_Array_of_int_Negative_1() {
    Assertions.assertThrows(RuntimeException.class, () -> MyIntMath.lcm(IntStream.of(-12, 5, 7)
                                                                                 .toArray()));
  }

  @Test
  public void testLCM_Array_of_int_Negative_2() {
    Assertions.assertThrows(RuntimeException.class, () -> MyIntMath.lcm(IntStream.of(3, 2, -1)
                                                                                 .toArray()));
  }

  @Test
  public void testLCM_Array_of_int_Zero_1() {
    Assertions.assertEquals(0, MyIntMath.lcm(IntStream.of(0, 5, 7)
                                                      .toArray()));
  }

  @Test
  public void testLCM_Array_of_int_Zero_2() {
    Assertions.assertEquals(0, MyIntMath.lcm(IntStream.of(5, 0, 7)
                                                      .toArray()));
  }

  @Test
  public void testLCM_Array_of_int_Zero_3() {
    Assertions.assertThrows(RuntimeException.class, () -> MyIntMath.lcm(IntStream.of(0, 11, 0)
                                                                                 .toArray()));
  }

  @Test
  public void testLCM_Array_of_int_1() {
    Assertions.assertEquals(20, MyIntMath.lcm(IntStream.of(2, 5, 4)
                                                       .toArray()));
  }

  @Test
  public void testLCM_Array_of_int_2() {
    Assertions.assertEquals(70, MyIntMath.lcm(IntStream.of(5, 7, 10)
                                                       .toArray()));
  }

  @Test
  public void testLCM_Array_of_int_3() {
    Assertions.assertEquals(8, MyIntMath.lcm(IntStream.of(2, 8, 4)
                                                      .toArray()));
  }

  // lcm(Collection<Integer>)

  @Test
  public void testLCM_Collection_of_Integer_Null_1() {
    Assertions.assertThrows(RuntimeException.class, () -> MyIntMath.lcm((Collection<Integer>) null));
  }

  @Test
  public void testLCM_Collection_of_Integer_Null_2() {
    Assertions.assertThrows(RuntimeException.class, () -> MyIntMath.lcm(List.of((Integer) null)));
  }

  @Test
  public void testLCM_Collection_of_Integer_Negative_1() {
    Assertions.assertThrows(RuntimeException.class, () -> MyIntMath.lcm(IntStream.of(12, -2, 7)
                                                                                 .boxed()
                                                                                 .toList()));
  }

  @Test
  public void testLCM_Collection_of_Integer_Negative_2() {
    Assertions.assertThrows(RuntimeException.class, () -> MyIntMath.lcm(IntStream.of(2, 9, -8)
                                                                                 .boxed()
                                                                                 .toList()));
  }

  @Test
  public void testLCM_Collection_of_Integer_Zero_1() {
    Assertions.assertEquals(0, MyIntMath.lcm(IntStream.of(0, 8, 5)
                                                      .boxed()
                                                      .toList()));
  }

  @Test
  public void testLCM_Collection_of_Integer_Zero_2() {
    Assertions.assertEquals(0, MyIntMath.lcm(IntStream.of(3, 0, 1)
                                                      .boxed()
                                                      .toList()));
  }

  @Test
  public void testLCM_Collection_of_Integer_Zero_3() {
    Assertions.assertThrows(RuntimeException.class, () -> MyIntMath.lcm(IntStream.of(0, 0, 0)
                                                                                 .boxed()
                                                                                 .toList()));
  }

  @Test
  public void testLCM_Collection_of_Integer_1() {
    Assertions.assertEquals(18, MyIntMath.lcm(IntStream.of(3, 9, 2)
                                                       .boxed()
                                                       .toList()));
  }

  @Test
  public void testLCM_Collection_of_Integer_2() {
    Assertions.assertEquals(12, MyIntMath.lcm(IntStream.of(4, 3, 12)
                                                       .boxed()
                                                       .toList()));
  }

  @Test
  public void testLCM_Collection_of_Integer_3() {
    Assertions.assertEquals(120, MyIntMath.lcm(IntStream.of(3, 40, 20, 1)
                                                        .boxed()
                                                        .toList()));
  }

  // signum(int)

  @Test
  public void testSignum_Zero() {
    Assertions.assertEquals(0, MyIntMath.signum(0));
  }

  @Test
  public void testSignum_Negative() {
    Assertions.assertEquals(-1, MyIntMath.signum(-4));
  }

  @Test
  public void testSignum_MinValue() {
    Assertions.assertEquals(-1, MyIntMath.signum(Integer.MIN_VALUE));
  }

  @Test
  public void testSignum_Positive() {
    Assertions.assertEquals(1, MyIntMath.signum(12));
  }

  @Test
  public void testSignum_MaxValue() {
    Assertions.assertEquals(1, MyIntMath.signum(Integer.MAX_VALUE));
  }
}
