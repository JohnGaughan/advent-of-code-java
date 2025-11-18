/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2021  John Gaughan
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
package us.coffeecode.advent_of_code.y2021;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.Point2D;

@AdventOfCodeSolution(year = 2021, day = 9)
@Component
public final class Year2021Day09 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final int[][] map = il.linesAs2dIntArrayFromDigits(pc);
    long answer = 0;
    for (int y = 0; y < map.length; ++y) {
      x: for (int x = 0; x < map[y].length; ++x) {
        // Skip this location unless all adjacent locations are greater than it.
        final Point2D here = new Point2D(x, y);
        for (final Point2D neighbor : here.getCardinalNeighbors()) {
          if (neighbor.isIn(map) && (neighbor.get(map) <= here.get(map))) {
            continue x;
          }
        }
        answer += here.get(map) + 1;
      }
    }
    return answer;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final int[][] map = il.linesAs2dIntArrayFromDigits(pc);
    final List<Integer> sizes = new ArrayList<>(230);

    // Iterate in reading order and find the next cell that is not a border. Then process the basin which fills it in,
    // guaranteeing that this code does not double-count it.
    for (int y = 0; y < map.length; ++y) {
      for (int x = 0; x < map[y].length; ++x) {
        if (map[y][x] != 9) {
          sizes.add(processBasinAt(map, new Point2D(x, y)));
        }
      }
    }

    sizes.sort(null);
    return sizes.reversed()
                .stream()
                .limit(3)
                .mapToLong(Integer::intValue)
                .reduce(1, (a, b) -> (a * b));
  }

  private Integer processBasinAt(final int[][] map, final Point2D point) {
    int size = 0;
    final Set<Point2D> processed = new HashSet<>(256);
    final Set<Point2D> processing = new HashSet<>(32);
    processing.add(point);
    while (!processing.isEmpty()) {
      final Point2D next = processing.iterator()
                                     .next();
      processed.add(next);
      processing.remove(next);
      if (next.get(map) != 9) {
        next.set(map, 9);
        ++size;
        for (final Point2D neighbor : next.getCardinalNeighbors()) {
          int value = neighbor.isIn(map) ? neighbor.get(map) : 9;
          if (!processed.contains(neighbor) && value != 9) {
            processing.add(neighbor);
          }
        }
      }
    }
    return Integer.valueOf(size);
  }

}
