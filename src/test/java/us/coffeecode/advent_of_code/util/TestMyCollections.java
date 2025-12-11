/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2025  John Gaughan
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

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test harness for the MyCollections class.
 */
public class TestMyCollections {

  //
  // List<Boolean> asList(final boolean[] array)
  //

  @Test
  public void test_asList_boolean_1() {
    Assertions.assertEquals(List.of(), MyCollections.asList(new boolean[0]));
  }

  @Test
  public void test_asList_boolean_2() {
    Assertions.assertEquals(List.of(Boolean.TRUE, Boolean.FALSE), MyCollections.asList(new boolean[] { true, false }));
  }

  @Test
  public void test_asList_boolean_3() {
    Assertions.assertNotEquals(List.of(Boolean.TRUE), MyCollections.asList(new boolean[0]));
  }

  @Test
  public void test_asList_boolean_4() {
    Assertions.assertNotEquals(List.of(Boolean.TRUE), MyCollections.asList(new boolean[] { false }));
  }

  //
  // <T> Set<T> mutableSetOf(final T element)
  //

  @Test
  public void test_mutableSetOf_1() {
    final Set<String> set = new HashSet<>();
    set.add("test");
    Assertions.assertEquals(set, MyCollections.mutableSetOf("test"));
  }

  @SuppressWarnings("boxing")
  @Test()
  public void test_mutableSetOf_2() {
    final Set<String> set = MyCollections.mutableSetOf("test");
    Assertions.assertDoesNotThrow(() -> set.add("test2"));
  }

  //
  // Set<T> intersection(final Set<T> a, final Set<T> b)
  //

  @Test
  public void test_intersection_1() {
    final Set<String> set1 = MyCollections.mutableSetOf("1");
    final Set<String> set2 = MyCollections.mutableSetOf("1", "2");
    Assertions.assertEquals(set1, MyCollections.intersection(set1, set2));
  }

  @Test
  public void test_intersection_2() {
    final Set<String> set1 = MyCollections.mutableSetOf("1");
    final Set<String> set2 = MyCollections.mutableSetOf("2");
    Assertions.assertEquals(Collections.emptySet(), MyCollections.intersection(set1, set2));
  }

  @Test
  public void test_intersection_3() {
    final Set<String> set1 = MyCollections.mutableSetOf("1", "2");
    final Set<String> set2 = MyCollections.mutableSetOf("2", "3");
    Assertions.assertEquals(MyCollections.mutableSetOf("2"), MyCollections.intersection(set1, set2));
  }

  @Test
  public void test_intersection_4() {
    final Set<String> set1 = MyCollections.mutableSetOf();
    final Set<String> set2 = MyCollections.mutableSetOf("2");
    Assertions.assertEquals(Collections.emptySet(), MyCollections.intersection(set1, set2));
  }

  //
  // Iterable<List<T>> permutations(final Collection<T> c)
  //

  @Test
  public void test_permutations_1() {
    final Set<List<String>> expected = Set.of(List.of("1", "2", "3"), List.of("1", "3", "2"), List.of("2", "1", "3"),
      List.of("2", "3", "1"), List.of("3", "1", "2"), List.of("3", "2", "1"));
    final Set<List<String>> actual = new HashSet<>();
    for (final List<String> result : MyCollections.permutations(List.of("1", "2", "3"))) {
      actual.add(result);
    }
    Assertions.assertTrue(expected.containsAll(actual));
    Assertions.assertTrue(actual.containsAll(expected));
  }

  @Test
  public void test_permutations_2() {
    Assertions.assertEquals(0, MyCollections.permutations(List.of())
                                            .size());
  }

  @Test
  public void test_permutations_3() {
    final Set<List<String>> expected = Set.of(List.of("1"));
    final Set<List<String>> actual = new HashSet<>();
    for (final List<String> result : MyCollections.permutations(List.of("1"))) {
      actual.add(result);
    }
    Assertions.assertTrue(expected.containsAll(actual));
    Assertions.assertTrue(actual.containsAll(expected));
  }
}
