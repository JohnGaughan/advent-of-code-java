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
package us.coffeecode.advent_of_code.y2019;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.LongStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import us.coffeecode.advent_of_code.AbstractTests;

public class TestLongQueue
extends AbstractTests {

  @Test
  public void testLongQueue_resize() throws Exception {
    // Method is private, but we can figure out how big the internal array is then stuff more values in there than can
    // fit.
    final LongQueue q = context.getBean(LongQueue.class);
    final Field field = q.getClass().getDeclaredField("array");
    field.setAccessible(true);
    final long[] ref = (long[]) field.get(q);
    final long[] array = LongStream.rangeClosed(1, ref.length << 1).toArray();
    Arrays.stream(array).forEach(l -> q.add(l));
    final long[] removed = q.removeAll();
    Assertions.assertArrayEquals(array, removed);
  }

  @Test
  public void testLongQueue_manyAddsAndRemoves() throws Exception {
    // Method is private, but we can figure out how big the internal array is then stuff more values in there than can
    // fit.
    final LongQueue q = context.getBean(LongQueue.class);
    final Field field = q.getClass().getDeclaredField("array");
    field.setAccessible(true);
    final long[] ref = (long[]) field.get(q);
    final long[] added = new long[ref.length << 1];
    final long[] removed = new long[added.length];
    for (int i = 0; i < added.length; ++i) {
      added[i] = i;
      q.add(i);
      removed[i] = q.remove();
    }
    Assertions.assertArrayEquals(added, removed);
  }

}
