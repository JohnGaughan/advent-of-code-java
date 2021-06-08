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

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import us.coffeecode.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2016/day/7">Year 2016, day 7</a>. This problem requires parsing strings and finding
 * which ones have regions that meet certain criteria (part one) or that match other regions in certain ways (part two).
 * </p>
 * <p>
 * There may be ways to solve this that are more complicated but also more efficient. The code is pretty clear so I will
 * go with the simpler approach. There is a lot of iterating and looking for patterns, but no iterating multiple times
 * so that is good. The only other design decision worth noting is the input parsing. The method I opted for can
 * technically fail in the general case, but not for the specific case of the provided data. Brackets never begin or end
 * a line, and never occur consecutively. This makes a simple regex split practical and makes this implementation very
 * concise.
 * </p>
 * <p>
 * Copyright (c) 2021 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2016Day07 {

  public long calculatePart1() {
    int matches = 0;
    for (Address address : getInput()) {
      if (!hasAbba(address.hypernetSegments) && hasAbba(address.supernetSegments)) {
        ++matches;
      }
    }
    return matches;
  }

  private boolean hasAbba(final Collection<String> segments) {
    for (String segment : segments) {
      for (int i = 0; i < segment.length() - 3; ++i) {
        int a = segment.codePointAt(i);
        int b = segment.codePointAt(i + 1);
        int c = segment.codePointAt(i + 2);
        int d = segment.codePointAt(i + 3);
        if (a != b && a == d && b == c) {
          return true;
        }
      }
    }
    return false;
  }

  public long calculatePart2() {
    int matches = 0;
    for (Address address : getInput()) {
      if (hasSsl(address)) {
        ++matches;
      }
    }
    return matches;
  }

  private boolean hasSsl(final Address address) {
    Set<String> babs = new HashSet<>();
    for (String segment : address.supernetSegments) {
      for (int i = 0; i < segment.length() - 2; ++i) {
        int a = segment.codePointAt(i);
        int b = segment.codePointAt(i + 1);
        int c = segment.codePointAt(i + 2);
        if (a == c && a != b) {
          StringBuilder str = new StringBuilder();
          str.appendCodePoint(b).appendCodePoint(a).appendCodePoint(b);
          babs.add(str.toString());
        }
      }
    }
    for (String segment : address.hypernetSegments) {
      for (String bab : babs) {
        if (segment.contains(bab)) {
          return true;
        }
      }
    }
    return false;
  }

  /** Get the input data for this solution. */
  private List<Address> getInput() {
    try {
      return Files.readAllLines(Utils.getInput(2016, 7)).stream().map(Address::new).collect(Collectors.toList());
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private static final class Address {

    /** Match either a single opening or closing square brace. */
    private static final Pattern SPLIT = Pattern.compile("[\\[\\]]");

    final Collection<String> supernetSegments = new ArrayList<>();

    final Collection<String> hypernetSegments = new ArrayList<>();

    Address(final String input) {
      boolean hypernet = false;
      for (String token : SPLIT.split(input)) {
        if (hypernet) {
          hypernetSegments.add(token);
        }
        else {
          supernetSegments.add(token);
        }
        hypernet = !hypernet;
      }
    }

  }

}
