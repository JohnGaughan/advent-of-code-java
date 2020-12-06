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
package net.johngaughan.advent.y2020.day5;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.NavigableSet;
import java.util.TreeSet;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * <p>
 * File parser for day five.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@johngaughan.net&gt;
 */
final class Parser {

  private static final Pattern INPUT = Pattern.compile("(F|B){7}(L|R){3}");

  public NavigableSet<Long> parse(final Path t) {
    try {
      return Files.readAllLines(t).stream().filter(s -> (s != null) && !s.isBlank()).map(s -> parseLine(s)).collect(
        Collectors.toCollection(TreeSet::new));
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private Long parseLine(final String line) {
    if (!INPUT.matcher(line).matches()) {
      throw new IllegalArgumentException("Input [" + line + "] is invalid");
    }

    // Seat ID is a binary number but using FBLR instead of 0 and 1.
    String s = line.replace('F', '0').replace('B', '1').replace('L', '0').replace('R', '1');
    return Long.valueOf(s, 2);
  }

}
