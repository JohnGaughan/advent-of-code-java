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
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2016, day = 11, title = "Radioisotope Thermoelectric Generators")
@Component
public final class Year2016Day11 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return solve(getInput(pc));
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final List<Collection<Item>> input = getInput(pc);
    final Collection<Item> first = input.getFirst();
    first.add(new Item(Element.elerium, Device.generator));
    first.add(new Item(Element.elerium, Device.microchip));
    first.add(new Item(Element.dilithium, Device.generator));
    first.add(new Item(Element.dilithium, Device.microchip));
    return solve(input);
  }

  private long solve(final List<Collection<Item>> input) {
    final int[] items = new int[input.size()];
    for (int i = 0; i < items.length; ++i) {
      items[i] = input.get(i)
                      .size();
    }
    long moves = 0;
    for (int i = 1; i < items.length; ++i) {
      items[i] += items[i - 1];
      moves += 2 * items[i - 1] - 3;
    }
    return moves;
  }

  private static final Pattern SPACE = Pattern.compile(" ");

  /** Get the input data for this solution. */
  private List<Collection<Item>> getInput(final PuzzleContext pc) {
    final List<Collection<Item>> input = new ArrayList<>(4);
    for (int i = 0; i < 4; ++i) {
      input.add(new ArrayList<>());
    }
    for (final String line : il.lines(pc)) {
      if (line.contains("nothing")) {
        continue;
      }
      // Remove text that makes parsing more complex.
      final String[] tokens = SPACE.split(line.replace(",", "")
                                              .replace(".", "")
                                              .replace("-compatible", "")
                                              .replace("and ", ""));
      final int floor;
      if ("first".equals(tokens[1])) {
        floor = 0;
      }
      else if ("second".equals(tokens[1])) {
        floor = 1;
      }
      else if ("third".equals(tokens[1])) {
        floor = 2;
      }
      else if ("fourth".equals(tokens[1])) {
        floor = 3;
      }
      else {
        throw new IllegalArgumentException("[" + line + "]");
      }
      for (int i = 5; i + 1 < tokens.length; i += 3) {
        input.get(floor)
             .add(new Item(Element.valueOf(tokens[i]), Device.valueOf(tokens[i + 1])));
      }
    }
    return input;
  }

  private static record Item(Element e, Device d) {}

  private static enum Element {
    dilithium,
    elerium,
    hydrogen,
    lithium,
    plutonium,
    promethium,
    ruthenium,
    strontium,
    thulium;
  }

  private static enum Device {
    generator,
    microchip;
  }

}
