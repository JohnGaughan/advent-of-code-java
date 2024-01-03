/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2023  John Gaughan
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
package us.coffeecode.advent_of_code.y2023;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2023, day = 15, title = "Lens Library")
@Component
public class Year2023Day15 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return Arrays.stream(il.fileAsObject(pc, s -> SPLIT.split(s))).mapToLong(this::hash).sum();
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final Map<Integer, List<Lens>> boxes = new HashMap<>(512);
    for (final String s : il.fileAsObject(pc, s -> SPLIT.split(s))) {
      final String[] tokens = split(s);

      final String lensId = tokens[0];
      final Integer boxId = Integer.valueOf(hash(lensId));
      boxes.computeIfAbsent(boxId, ArrayList::new);

      if (tokens[1].codePointAt(0) == '-') {
        for (final Iterator<Lens> iter = boxes.get(boxId).iterator(); iter.hasNext();) {
          if (iter.next().lensId.equals(lensId)) {
            iter.remove();
            break;
          }
        }
      }
      else /* = */ {
        final Lens adding = new Lens(lensId, Integer.parseInt(tokens[1].substring(1)));
        boolean added = false;
        for (final ListIterator<Lens> iter = boxes.get(boxId).listIterator(); iter.hasNext();) {
          if (iter.next().lensId.equals(lensId)) {
            // Replace this lens.
            iter.remove();
            iter.add(adding);
            added = true;
            break;
          }
        }
        if (!added) {
          // Did not replace: add to the end.
          boxes.get(boxId).addLast(adding);
        }
      }
    }
    long answer = 0;
    for (var entry : boxes.entrySet()) {
      final long boxNumber = 1 + entry.getKey().intValue();
      final List<Lens> lenses = entry.getValue();
      for (int i = 0; i < lenses.size(); ++i) {
        final Lens l = lenses.get(i);
        final long focusingPower = boxNumber * (i + 1) * l.focalLength;
        answer += focusingPower;
      }
    }
    return answer;
  }

  /** Given one command in the input, split it into a lens and everything after the lens. */
  private String[] split(final String command) {
    // Example input only has two-character lens IDs, but the real input has several that are longer. In the absence of
    // a delimiter, just find the first non-alphabetic character and split the string in two.
    int end = -1;
    for (int i = 0; i < command.length(); ++i) {
      if (!Character.isAlphabetic(command.codePointAt(i))) {
        end = i;
        break;
      }
    }
    return new String[] { command.substring(0, end), command.substring(end) };
  }

  private record Lens(String lensId, int focalLength) {}

  private int hash(final String s) {
    int hash = 0;
    for (final int ch : s.codePoints().toArray()) {
      hash = ((hash + ch) * 17) % 256;
    }
    return hash;
  }

  private static final Pattern SPLIT = Pattern.compile(",");
}
