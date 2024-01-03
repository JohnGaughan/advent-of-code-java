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
package us.coffeecode.advent_of_code.util;

import java.util.Iterator;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import us.coffeecode.advent_of_code.AbstractTests;

public class TestArrayInfinitelyIterable
extends AbstractTests {

  @Test
  public void test() {
    final Integer[] array = IntStream.range(0, 10).boxed().toArray(Integer[]::new);
    final ArrayInfinitelyIterable<Integer> aii = new ArrayInfinitelyIterable<>(array);
    final Iterator<Integer> iter = aii.iterator();
    for (int i = 0; i < 105; ++i) {
      iter.next();
    }
    Assertions.assertEquals(array[5], iter.next());
  }

}
