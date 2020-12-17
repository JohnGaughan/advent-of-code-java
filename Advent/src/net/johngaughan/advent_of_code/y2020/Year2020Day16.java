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
package net.johngaughan.advent_of_code.y2020;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import net.johngaughan.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2015/day/16">Year 2020, day 16</a>. This problem has us look at train tickets where
 * we cannot read the field names, figure out what those field names are, then do some math with certain fields to prove
 * it. We do this because we know what the field names are and what valid data is for each one, we just do not know
 * which numbers on a ticket match up with which field definition. Part one starts out by having us remove all invalid
 * tickets, where a ticket cannot meet any of the validation rules. Part two extends this by asking us to map the
 * fields, then do some math with our own ticket to prove that we mapped them correctly.
 * </p>
 * <p>
 * The solution is just a lot of number crunching. Finding invalid tickets is trivial: for each ticket, check against
 * each field definition. If it cannot meet any of them, it is invalid. Each field definition starts out valid for each
 * field index: we then go through each field definition and see if there is any ticket that is not valid for that
 * field. If so, we remove that field index and keep going. Eventually, each field definition is only valid for a single
 * field index. From there, we can check our own ticket against the field definitions and get the necessary values.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@johngaughan.net&gt;
 */
public final class Year2020Day16 {

  public int calculatePart1() {
    final Input input = getInput();
    int errorRate = 0;
    for (int[] ticket : input.otherTickets) {
      errorRate +=
        Arrays.stream(ticket).map(i -> Arrays.stream(input.fields).anyMatch(f -> f.validFor(i)) ? 0 : i).sum();
    }
    return errorRate;
  }

  public long calculatePart2() {
    final Input input = getInput();
    final Field[] fields = input.fields;

    // First, remove all tickets that are invalid
    final Collection<int[]> validTickets = input.otherTickets.stream().filter(
      vt -> Arrays.stream(vt).allMatch(t -> Arrays.stream(fields).anyMatch(f -> f.validFor(t)))).collect(
        Collectors.toList());
    validTickets.add(input.myTicket);

    // Remove field mappings that cannot be valid.
    for (final Field field : fields) {
      validTickets.stream().forEach(ticket -> field.removeImpossibleIndices(ticket));
    }

    /*
     * Each field can have multiple valid indices. This algorithm isn't completely robust, but it works for the input
     * data. Find a field that has only one valid index. Remove that index from all the other fields. Repeat until every
     * field has only a single valid index: the solution.
     */
    while (!isSolved(fields)) {
      for (Field f1 : fields) {
        if (f1.validIndices.size() == 1) {
          final Integer remove = f1.validIndices.iterator().next();
          for (Field f2 : fields) {
            // Yes, use identity equality here.
            if (f1 != f2) {
              f2.validIndices.remove(remove);
            }
          }
        }
      }
    }

    // We should now have a complete mapping. Compute the field product on my ticket.
    long product = 1;
    for (Field field : fields) {
      if (field.name.startsWith("departure")) {
        product *= input.myTicket[field.validIndices.iterator().next().intValue()];
      }
    }
    return product;
  }

  /** Determine if the field definitions are solved: they should have only one valid index each. */
  private boolean isSolved(final Field[] fields) {
    for (Field field : fields) {
      if (field.validIndices.size() != 1) {
        return false;
      }
    }
    return true;
  }

  /** Get the input data for this solution. */
  private Input getInput() {
    try {
      return getInput(Utils.getLineGroups(Files.readAllLines(Utils.getInput(2020, 16))));
    }
    catch (final RuntimeException ex) {
      throw ex;
    }
    catch (final Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  /** Pattern that splits an integer list in the input file. */
  private static final Pattern LIST = Pattern.compile(",");

  /** Pattern that splits a field line in the input file into its name and its values. */
  private static final Pattern FIELD_KV = Pattern.compile(": ");

  /** Pattern that splits a field value into its constituent numbers. */
  private static final Pattern FIELD_V = Pattern.compile("(-| or )");

  /** Given raw file */
  private Input getInput(final Iterable<List<String>> groups) {
    final List<Field> fields = new ArrayList<>(235);
    int[] myTicket = null;
    final Collection<int[]> otherTickets = new ArrayList<>();

    for (final List<String> group : groups) {
      if (group.get(0).startsWith("your")) {
        myTicket = Arrays.stream(LIST.split(group.get(1))).mapToInt(Integer::parseInt).toArray();
      }
      else if (group.get(0).startsWith("nearby")) {
        for (int i = 1; i < group.size(); ++i) {
          otherTickets.add(Arrays.stream(LIST.split(group.get(i))).mapToInt(Integer::parseInt).toArray());
        }
      }
      else {
        for (final String field : group) {
          final String[] kv = FIELD_KV.split(field);
          final int[] value = Arrays.stream(FIELD_V.split(kv[1])).mapToInt(Integer::parseInt).toArray();
          fields.add(new Field(kv[0], new Range(value[0], value[1]), new Range(value[2], value[3])));
        }
      }
    }

    return new Input(fields.toArray(new Field[fields.size()]), myTicket, otherTickets);
  }

  /** The program input. */
  private static final class Input {

    final Field[] fields;

    final int[] myTicket;

    final Collection<int[]> otherTickets;

    Input(final Field[] f, final int[] m, final Collection<int[]> o) {
      fields = f;
      myTicket = m;
      otherTickets = o;
    }
  }

  /** One range of integers for which a field is valid. */
  private static final class Range {

    final int low;

    final int high;

    Range(final int l, final int h) {
      low = l;
      high = h;
    }

    /** Get whether this range contains the provided value. Bounds are both inclusive. */
    boolean contains(final int value) {
      return low <= value && value <= high;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
      return "[" + low + "-" + high + "]";
    }
  }

  /** One field in the input file. */
  private static final class Field {

    final String name;

    final Range range1;

    final Range range2;

    final Set<Integer> validIndices = new HashSet<>();

    Field(final String n, final Range r1, final Range r2) {
      name = n;
      range1 = r1;
      range2 = r2;
      for (int i = 0; i < 20; ++i) {
        validIndices.add(Integer.valueOf(i));
      }
    }

    void removeImpossibleIndices(final int[] ticket) {
      for (int fieldId = 0; fieldId < ticket.length; ++fieldId) {
        if (!validFor(ticket[fieldId])) {
          validIndices.remove(Integer.valueOf(fieldId));
        }
      }
    }

    boolean validFor(final int value) {
      return range1.contains(value) || range2.contains(value);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
      return "[" + name + "=" + range1 + "," + range2 + "::validFields=" + validIndices + "]";
    }
  }

}
