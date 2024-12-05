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
package us.coffeecode.advent_of_code.y2020;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2020, day = 16, title = "Ticket Translation")
@Component
public final class Year2020Day16 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final Input input = getInput(il.groups(pc));
    long errorRate = 0;
    for (int[] ticket : input.otherTickets) {
      errorRate += Arrays.stream(ticket)
                         .map(i -> Arrays.stream(input.fields)
                                         .anyMatch(f -> f.validFor(i)) ? 0 : i)
                         .sum();
    }
    return errorRate;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final Input input = getInput(il.groups(pc));
    final Field[] fields = input.fields;

    // First, remove all tickets that are invalid
    final Collection<int[]> validTickets = new ArrayList<>(input.otherTickets.stream()
                                                                             .filter(vt -> Arrays.stream(vt)
                                                                                                 .allMatch(
                                                                                                   t -> Arrays.stream(fields)
                                                                                                              .anyMatch(
                                                                                                                f -> f.validFor(
                                                                                                                  t))))
                                                                             .toList());
    validTickets.add(input.myTicket);

    // Remove field mappings that cannot be valid.
    for (final Field field : fields) {
      validTickets.stream()
                  .forEach(ticket -> field.removeImpossibleIndices(ticket));
    }

    /*
     * Each field can have multiple valid indices. This algorithm isn't completely robust, but it works for the input
     * data. Find a field that has only one valid index. Remove that index from all the other fields. Repeat until every
     * field has only a single valid index: the solution.
     */
    while (Arrays.stream(fields)
                 .anyMatch(f -> f.validIndices.size() != 1)) {
      for (final Field f1 : fields) {
        if (f1.validIndices.size() == 1) {
          final Integer removed = f1.validIndices.iterator()
                                                 .next();
          for (final Field f2 : fields) {
            // Yes, use identity equality here.
            if (f1 != f2) {
              f2.validIndices.remove(removed);
            }
          }
        }
      }
    }

    // We should now have a complete mapping. Compute the field product on my ticket.
    long product = 1;
    for (Field field : fields) {
      if (field.name.startsWith("departure")) {
        product *= input.myTicket[field.validIndices.iterator()
                                                    .next()
                                                    .intValue()];
      }
    }
    return product;
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
      if (group.getFirst()
               .startsWith("your")) {
        myTicket = Arrays.stream(LIST.split(group.get(1)))
                         .mapToInt(Integer::parseInt)
                         .toArray();
      }
      else if (group.getFirst()
                    .startsWith("nearby")) {
                      for (int i = 1; i < group.size(); ++i) {
                        otherTickets.add(Arrays.stream(LIST.split(group.get(i)))
                                               .mapToInt(Integer::parseInt)
                                               .toArray());
                      }
                    }
      else {
        for (final String field : group) {
          final String[] kv = FIELD_KV.split(field);
          final int[] value = Arrays.stream(FIELD_V.split(kv[1]))
                                    .mapToInt(Integer::parseInt)
                                    .toArray();
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
      return (low <= value) && (value <= high);
    }

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

    final Set<Integer> validIndices;

    Field(final String n, final Range r1, final Range r2) {
      name = n;
      range1 = r1;
      range2 = r2;
      validIndices = IntStream.range(0, 20)
                              .boxed()
                              .collect(Collectors.toCollection(HashSet::new));
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

    @Override
    public String toString() {
      return "[" + name + "=" + range1 + "," + range2 + "::validFields=" + validIndices + "]";
    }
  }

}
