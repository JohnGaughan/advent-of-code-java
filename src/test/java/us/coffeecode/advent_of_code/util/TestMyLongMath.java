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
import java.util.stream.LongStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import us.coffeecode.advent_of_code.AbstractTests;

public class TestMyLongMath
extends AbstractTests {

  @Test
  public void test_Constant_Zero() {
    Assertions.assertEquals(0, MyLongMath.ZERO.longValue());
  }

  @Test
  public void test_Constant_One() {
    Assertions.assertEquals(1, MyLongMath.ONE.longValue());
  }

  //
  // gcd(long, long)
  //

  @Test
  public void testGCD_zero_1() {
    Assertions.assertEquals(3, MyLongMath.gcd(0, 3));
  }

  @Test
  public void testGCD_zero_2() {
    Assertions.assertEquals(5, MyLongMath.gcd(5, 0));
  }

  @Test
  public void testGCD_zero_3() {
    Assertions.assertEquals(0, MyLongMath.gcd(0, 0));
  }

  @Test
  public void testGCD_int_1() {
    Assertions.assertEquals(7, MyLongMath.gcd(14, 21));
  }

  @Test
  public void testGCD_int_2() {
    Assertions.assertEquals(2, MyLongMath.gcd(2, 2));
  }

  @Test
  public void testGCD_int_3() {
    Assertions.assertEquals(2, MyLongMath.gcd(4, 2));
  }

  @Test
  public void testGCD_int_4() {
    Assertions.assertEquals(2, MyLongMath.gcd(2, 4));
  }

  //
  // lcm(long, long)
  //

  @Test
  public void testLCM_long_Negative_1() {
    Assertions.assertThrows(RuntimeException.class, () -> MyLongMath.lcm(-4, 2));
  }

  @Test
  public void testLCM_long_Negative_2() {
    Assertions.assertThrows(RuntimeException.class, () -> MyLongMath.lcm(1, -1));
  }

  @Test
  public void testLCM_long_Zero_1() {
    Assertions.assertEquals(0, MyLongMath.lcm(0, 1));
  }

  @Test
  public void testLCM_long_Zero_2() {
    Assertions.assertEquals(0, MyLongMath.lcm(1, 0));
  }

  @Test
  public void testLCM_long_Zero_3() {
    Assertions.assertThrows(RuntimeException.class, () -> MyLongMath.lcm(0, 0));
  }

  @Test
  public void testLCM_long_1() {
    Assertions.assertEquals(20, MyLongMath.lcm(20, 1));
  }

  @Test
  public void testLCM_long_2() {
    Assertions.assertEquals(15, MyLongMath.lcm(3, 5));
  }

  @Test
  public void testLCM_long_3() {
    Assertions.assertEquals(8, MyLongMath.lcm(8, 2));
  }

  //
  // lcm(long[])
  //

  @Test
  public void testLCM_Array_of_long_Negative_1() {
    Assertions.assertThrows(RuntimeException.class, () -> MyLongMath.lcm(LongStream.of(-12, 5, 7)
                                                                                   .toArray()));
  }

  @Test
  public void testLCM_Array_of_long_Negative_2() {
    Assertions.assertThrows(RuntimeException.class, () -> MyLongMath.lcm(LongStream.of(3, 2, -1)
                                                                                   .toArray()));
  }

  @Test
  public void testLCM_Array_of_long_Zero_1() {
    Assertions.assertEquals(0, MyLongMath.lcm(LongStream.of(0, 5, 7)
                                                        .toArray()));
  }

  @Test
  public void testLCM_Array_of_long_Zero_2() {
    Assertions.assertEquals(0, MyLongMath.lcm(LongStream.of(5, 0, 7)
                                                        .toArray()));
  }

  @Test
  public void testLCM_Array_of_long_Zero_3() {
    Assertions.assertThrows(RuntimeException.class, () -> MyLongMath.lcm(LongStream.of(0, 11, 0)
                                                                                   .toArray()));
  }

  @Test
  public void testLCM_Array_of_long_1() {
    Assertions.assertEquals(20, MyLongMath.lcm(LongStream.of(2, 5, 4)
                                                         .toArray()));
  }

  @Test
  public void testLCM_Array_of_long_2() {
    Assertions.assertEquals(70, MyLongMath.lcm(LongStream.of(5, 7, 10)
                                                         .toArray()));
  }

  @Test
  public void testLCM_Array_of_long_3() {
    Assertions.assertEquals(8, MyLongMath.lcm(LongStream.of(2, 8, 4)
                                                        .toArray()));
  }

  //
  // lcm(Collection<Long>)
  //

  @Test
  public void testLCM_Collection_of_Long_Null_1() {
    Assertions.assertThrows(RuntimeException.class, () -> MyLongMath.lcm((Collection<Long>) null));
  }

  @Test
  public void testLCM_Collection_of_Long_Null_2() {
    Assertions.assertThrows(RuntimeException.class, () -> MyLongMath.lcm(List.of((Long) null)));
  }

  @Test
  public void testLCM_Collection_of_Long_Negative_1() {
    Assertions.assertThrows(RuntimeException.class, () -> MyLongMath.lcm(LongStream.of(12, -2, 7)
                                                                                   .boxed()
                                                                                   .toList()));
  }

  @Test
  public void testLCM_Collection_of_Long_Negative_2() {
    Assertions.assertThrows(RuntimeException.class, () -> MyLongMath.lcm(LongStream.of(2, 9, -8)
                                                                                   .boxed()
                                                                                   .toList()));
  }

  @Test
  public void testLCM_Collection_of_Long_Zero_1() {
    Assertions.assertEquals(0, MyLongMath.lcm(LongStream.of(0, 8, 5)
                                                        .boxed()
                                                        .toList()));
  }

  @Test
  public void testLCM_Collection_of_Long_Zero_2() {
    Assertions.assertEquals(0, MyLongMath.lcm(LongStream.of(3, 0, 1)
                                                        .boxed()
                                                        .toList()));
  }

  @Test
  public void testLCM_Collection_of_Long_Zero_3() {
    Assertions.assertThrows(RuntimeException.class, () -> MyLongMath.lcm(LongStream.of(0, 0, 0)
                                                                                   .boxed()
                                                                                   .toList()));
  }

  @Test
  public void testLCM_Collection_of_Long_1() {
    Assertions.assertEquals(18, MyLongMath.lcm(LongStream.of(3, 9, 2)
                                                         .boxed()
                                                         .toList()));
  }

  @Test
  public void testLCM_Collection_of_Long_2() {
    Assertions.assertEquals(12, MyLongMath.lcm(LongStream.of(4, 3, 12)
                                                         .boxed()
                                                         .toList()));
  }

  @Test
  public void testLCM_Collection_of_Long_3() {
    Assertions.assertEquals(120, MyLongMath.lcm(LongStream.of(3, 40, 20, 1)
                                                          .boxed()
                                                          .toList()));
  }

  //
  // signum(long)
  //

  @Test
  public void testSignum_Zero() {
    Assertions.assertEquals(0, MyLongMath.signum(0));
  }

  @Test
  public void testSignum_Negative() {
    Assertions.assertEquals(-1, MyLongMath.signum(-7));
  }

  @Test
  public void testSignum_MinValue() {
    Assertions.assertEquals(-1, MyLongMath.signum(Long.MIN_VALUE));
  }

  @Test
  public void testSignum_Positive() {
    Assertions.assertEquals(1, MyLongMath.signum(73));
  }

  @Test
  public void testSignum_MaxValue() {
    Assertions.assertEquals(1, MyLongMath.signum(Long.MAX_VALUE));
  }

  //
  // factorial
  //

  @Test
  void test_factorial_1() {
    Assertions.assertThrows(RuntimeException.class, () -> MyLongMath.factorial(-1));
  }

  @Test
  void test_factorial_2() {
    Assertions.assertThrows(RuntimeException.class, () -> MyLongMath.factorial(100));
  }

  @Test
  void test_factorial_3() {
    Assertions.assertEquals(1, MyLongMath.factorial(0));
  }

  @Test
  void test_factorial_4() {
    Assertions.assertEquals(1, MyLongMath.factorial(1));
  }

  @Test
  void test_factorial_5() {
    Assertions.assertEquals(2, MyLongMath.factorial(2));
  }
}
