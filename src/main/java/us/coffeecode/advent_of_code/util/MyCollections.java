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

import java.util.*;
import java.util.stream.IntStream;

/**
 * Utility class with additional collection-oriented methods not included in the JRE.
 */
public class MyCollections {

  /**
   * Convert an array of booleans to a list of boolean wrappers.
   *
   * @param array the source array.
   * @return a list of wrapped booleans.
   */
  public static final List<Boolean> asList(final boolean[] array) {
    // JRE does not have the plumbing to use streams here.
    final List<Boolean> list = new ArrayList<>(array.length);
    for (final boolean b : array) {
      list.add(Boolean.valueOf(b));
    }
    return list;
  }

  /**
   * Convert a Properties to a <code>Map&lt;String, String&gt;</code> This will be a copy of the data in the properties
   * object.
   *
   * @param p the source Properties object.
   * @return a new map containing the properties.
   */
  public static final Map<String, String> mapFromProperties(final Properties p) {
    final Map<String, String> map = new HashMap<>(p.size() << 2);
    /*
     * Properties specify Object but actually contain String in any context used in AOC. This is not true in the broader
     * sense because it is possible to insert non-String keys and values into a Properties, so please do not copy this
     * code for general-purpose use. Within this project I only use Properties as a means to load properties files so
     * this is safe.
     */
    p.entrySet()
     .stream()
     .forEach(e -> map.put((String) e.getKey(), (String) e.getValue()));
    return map;
  }

  /**
   * Construct a set containing the provided elements. This set will be mutable.
   *
   * @param <T> the generic type of the set, which will match the type of the provided element.
   * @param elements the elements to add to the new set.
   * @return a new set that is mutable.
   */
  @SafeVarargs
  public static final <T> Set<T> mutableSetOf(final T... elements) {
    final Set<T> set = new HashSet<>();
    for (final T element : elements) {
      set.add(element);
    }
    return set;
  }

  /**
   * Get the intersection of two sets. This will be a new set independent of the two input sets.
   *
   * @param <T> the generic type of the sets.
   * @param a the first set.
   * @param b the second set.
   * @return the intersection of the two sets.
   */
  public static final <T> Set<T> intersection(final Set<T> a, final Set<T> b) {
    final Set<T> intersection = new HashSet<>(Math.min(a.size(), b.size()));
    for (final Iterator<T> iter = a.iterator(); iter.hasNext();) {
      final T element = iter.next();
      if (b.contains(element)) {
        intersection.add(element);
      }
    }
    return intersection;
  }

  /**
   * Get an object that can iterate over all permutations of elements in the provided collection. This will not be an
   * iterable itself, but rather an iterable which allows it to be used in enhanced for loops.
   *
   * @param <T> the generic type of the elements in the collection
   * @param c the source elements for which to calculate permutations.
   * @return an object that can be used to iterate over permutations.
   */
  public static final <T> Collection<List<T>> permutations(final Collection<T> c) {
    return new PermutationCollection<>(c);
  }

  private static final class PermutationCollection<T>
  extends AbstractCollection<List<T>> {

    private final List<T> data;

    private final int size;

    PermutationCollection(final Collection<T> source) {
      data = Collections.unmodifiableList(new ArrayList<>(source));
      // 0! is 1, so set the size to zero if needed. Factorial throws exception if more than 12 elements.
      size = (data.isEmpty() ? 0 : MyIntMath.factorial(data.size()));
    }

    @Override
    public Iterator<List<T>> iterator() {
      return new PermutationIterator<>(data, size);
    }

    @Override
    public int size() {
      return size;
    }

  }

  private static class PermutationIterator<T>
  implements Iterator<List<T>> {

    private final int order[];

    private final List<T> data;

    private int permutation = 0;

    private final int max;

    PermutationIterator(final List<T> _data, final int _max) {
      data = _data;
      order = IntStream.range(0, data.size())
                       .toArray();
      max = _max;
    }

    @Override
    public boolean hasNext() {
      return (permutation < max) && (order.length != 0);
    }

    @Override
    public List<T> next() {
      final List<T> result = new ArrayList<>();
      for (int i : order) {
        result.add(data.get(i));
      }
      ++permutation;
      if (hasNext()) {
        int k = order.length - 2;
        while ((k >= 0) && (order[k] > order[k + 1])) {
          --k;
        }
        if (k < 0) {
          throw new NoSuchElementException();
        }

        int l = order.length - 1;
        while ((l >= 0) && (l > k) && (order[k] > order[l])) {
          --l;
        }

        final int temp = order[k];
        order[k] = order[l];
        order[l] = temp;

        MyArrays.reverseInPlace(order, k + 1, order.length);
      }
      return result;
    }

  }
}
