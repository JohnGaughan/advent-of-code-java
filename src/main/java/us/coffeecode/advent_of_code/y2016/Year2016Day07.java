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
package us.coffeecode.advent_of_code.y2016;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2016, day = 7)
@Component
public final class Year2016Day07 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return il.linesAsObjects(pc, Address::make)
             .stream()
             .filter(this::filterPart1)
             .count();
  }

  private boolean filterPart1(final Address address) {
    return !hasAbba(address.hypernetSegments) && hasAbba(address.supernetSegments);
  }

  private boolean hasAbba(final Collection<String> segments) {
    for (final String segment : segments) {
      for (int i = 0; i < segment.length() - 3; ++i) {
        int a = segment.codePointAt(i);
        int b = segment.codePointAt(i + 1);
        int c = segment.codePointAt(i + 2);
        int d = segment.codePointAt(i + 3);
        if ((a != b) && (a == d) && (b == c)) {
          return true;
        }
      }
    }
    return false;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return il.linesAsObjects(pc, Address::make)
             .stream()
             .filter(this::hasSsl)
             .count();
  }

  private boolean hasSsl(final Address address) {
    final Set<String> babs = new HashSet<>();
    for (final String segment : address.supernetSegments) {
      for (int i = 0; i < segment.length() - 2; ++i) {
        int a = segment.codePointAt(i);
        int b = segment.codePointAt(i + 1);
        int c = segment.codePointAt(i + 2);
        if ((a == c) && (a != b)) {
          final StringBuilder str = new StringBuilder();
          str.appendCodePoint(b)
             .appendCodePoint(a)
             .appendCodePoint(b);
          babs.add(str.toString());
        }
      }
    }
    for (final String segment : address.hypernetSegments) {
      for (final String bab : babs) {
        if (segment.contains(bab)) {
          return true;
        }
      }
    }
    return false;
  }

  private static final record Address(Collection<String> supernetSegments, Collection<String> hypernetSegments) {

    /** Match either a single opening or closing square brace. */
    private static final Pattern SPLIT = Pattern.compile("[\\[\\]]");

    static Address make(final String input) {
      final Collection<String> supernet = new ArrayList<>();
      final Collection<String> hypernet = new ArrayList<>();
      boolean isHypernet = false;
      for (String token : SPLIT.split(input)) {
        if (isHypernet) {
          hypernet.add(token);
        }
        else {
          supernet.add(token);
        }
        isHypernet = !isHypernet;
      }
      return new Address(supernet, hypernet);
    }

  }

}
