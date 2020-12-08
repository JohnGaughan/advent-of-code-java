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
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * <p>
 * <a href="https://adventofcode.com/2020/day/2">Year 2020, day 2</a>. This problem requires simple string parsing and
 * pattern matching. There are two numbers and a character, along with a string. The goal is to count the number lines
 * that meet some criteria. Part one wants to count the lines where the number of times the character appears is between
 * the two provided numbers. Part two wants to count the lines where the given character appears at exactly one of two
 * locations in the string, offsets given by those numbers.
 * </p>
 * <p>
 * My solution was to create the Input class which contains each parsed input line, which makes it simpler to reference
 * values in lambdas. Filter the lines with lambdas that validate the "passwords," and count what is left over.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@johngaughan.net&gt;
 */
public final class Year2020Day02 {

  public long calculatePart1(final Path path) {
    return parse(path).stream().filter(t -> {
      final long count = t.password.chars().filter(c -> c == t.ch).count();
      return (t.n1 <= count) && (count <= t.n2);
    }).count();
  }

  public long calculatePart2(final Path path) {
    return parse(path).stream().filter(
      t -> (t.password.charAt(t.n1 - 1) == t.ch) ^ (t.password.charAt(t.n2 - 1) == t.ch)).count();
  }

  /** Parse the file located at the provided path location. */
  private List<Input> parse(final Path path) {
    try {
      return Files.readAllLines(path).stream().map(s -> new Input(s)).collect(Collectors.toList());
    }
    catch (final RuntimeException ex) {
      throw ex;
    }
    catch (final Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private static final class Input {

    /** This pattern separates the policy and password on an input line. */
    private static final Pattern SPLIT = Pattern.compile(": ");

    int n1;

    int n2;

    char ch;

    String password;

    Input(final String line) {
      // Separate the policy from the password
      final String[] pieces = SPLIT.split(line);

      // Get the character to test and the positions to look at.
      final int split1 = pieces[0].indexOf('-');
      final int split2 = pieces[0].indexOf(' ', split1);
      n1 = Integer.parseInt(pieces[0].substring(0, split1));
      n2 = Integer.parseInt(pieces[0].substring(split1 + 1, split2));
      ch = pieces[0].charAt(split2 + 1);
      password = pieces[1];
    }
  }

}
