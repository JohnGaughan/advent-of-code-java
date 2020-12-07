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
package net.johngaughan.advent;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * This utility class contains general-purpose code used by multiple problems.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@johngaughan.net&gt;
 */
public final class Utils {

  /**
   * <p>
   * Given a list of strings, group them up based on spacing. A blank string marks the boundary between string groups.
   * </p>
   * <p>
   * The blank strings are not returned.
   * </p>
   *
   * @param lines the lines to group.
   * @return the lines grouped together.
   */
  public static List<List<String>> getLineGroups(final List<String> lines) {
    // 1. Split lines list into segments based on blank lines separating passports.
    final List<List<String>> lineGroups = new ArrayList<>();
    int start = -1;
    for (int i = 0; i < lines.size(); ++i) {
      final boolean currentLineHasData = !lines.get(i).isBlank();
      // Not currently inside data
      if (start < 0) {
        // Don't combine these if statements!
        if (currentLineHasData) {
          start = i;
        }
      }
      // Current inside passport data
      else if (!currentLineHasData) {
        lineGroups.add(lines.subList(start, i));
        start = -1;
      }
    }
    // See if there is dangling data: if so, add it
    if (start > 0) {
      lineGroups.add(lines.subList(start, lines.size()));
    }

    return lineGroups;
  }

}
