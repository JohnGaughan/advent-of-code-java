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
import java.util.List;
import java.util.regex.Pattern;

import us.coffeecode.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2016/day/11">Year 2016, day 11</a>. This problem requires moving objects between
 * floors in a way that incompatible items are not left together. Parts one and two use the same algorithm, but part two
 * has more data.
 * </p>
 * <p>
 * At first, this seems like a good candidate for A*. The search space is potentially massive, so a true search
 * algorithm would need to use caching to avoid redundancy as well as heuristics to avoid searching parts of the search
 * space that cannot ever produce the correct result. A* lends itself well to this.
 * </p>
 * <p>
 * However, the problem statement and input data have some constraints and patterns which make this even simpler. The
 * user has to travel between one floor at a time and always must have an item to allow the elevator to work. The best
 * solution candidates will always move two items up and one item down if possible. Nothing else can be more efficient
 * than that. The initial layout of items must be a legal one, as well. Some inspection of the data indicates that we
 * really don't even need to consider the RTG and microchip constraints, either. This problem can be simplified.
 * Starting with floor one: move two items up. Then if there are still items on the floor below: move one item down, two
 * up. Repeat until the lower floor is empty. Repeat that until all but the top floors are empty. As it turns out if
 * there are <code>n</code> items on a floor, it takes <code>2n - 3</code> moves to move them up a floor. Then repeat,
 * using the newly embiggened number of items, snowballing until the problem is complete. In other words, this is an
 * algebra problem, not an A* problem.
 * </p>
 * <p>
 * Copyright (c) 2021 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2016Day11 {

  public long calculatePart1() {
    return solve(getInput());
  }

  public long calculatePart2() {
    final List<List<Item>> input = getInput();
    final Collection<Item> first = input.get(0);
    first.add(new Item(Element.elerium, Device.generator));
    first.add(new Item(Element.elerium, Device.microchip));
    first.add(new Item(Element.dilithium, Device.generator));
    first.add(new Item(Element.dilithium, Device.microchip));
    return solve(input);
  }

  private int solve(final List<List<Item>> input) {
    final int[] items = new int[input.size()];
    for (int i = 0; i < items.length; ++i) {
      items[i] = input.get(i).size();
    }
    int moves = 0;
    for (int i = 1; i < items.length; ++i) {
      items[i] += items[i - 1];
      moves += 2 * items[i - 1] - 3;
    }
    return moves;
  }

  private static final Pattern SPACE = Pattern.compile(" ");

  /** Get the input data for this solution. */
  private List<List<Item>> getInput() {
    try {
      final List<List<Item>> input = new ArrayList<>(4);
      for (int i = 0; i < 4; ++i) {
        input.add(new ArrayList<>());
      }
      for (final String line : Files.readAllLines(Utils.getInput(2016, 11))) {
        if (line.contains("nothing")) {
          continue;
        }
        // Remove text that makes parsing more complex.
        final String[] tokens =
          SPACE.split(line.replace(",", "").replace(".", "").replace("-compatible", "").replace("and ", ""));
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
          Element e = Element.valueOf(tokens[i]);
          Device d = Device.valueOf(tokens[i + 1]);
          input.get(floor).add(new Item(e, d));
        }
      }
      return input;
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private static class Item {

    Element e;

    Device d;

    Item(final Element _e, final Device _d) {
      e = _e;
      d = _d;
    }

    @Override
    public String toString() {
      return e.name() + " " + d.name();
    }
  }

  private static enum Element {
    dilithium,
    elerium,
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
