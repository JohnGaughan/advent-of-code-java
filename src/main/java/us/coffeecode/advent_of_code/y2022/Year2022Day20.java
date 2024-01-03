/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2022  John Gaughan
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
package us.coffeecode.advent_of_code.y2022;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2022, day = 20, title = "Grove Positioning System")
@Component
public class Year2022Day20 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return calculate(pc, 1, 1);
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return calculate(pc, 811_589_153, 10);
  }

  private long calculate(final PuzzleContext pc, final long multiplier, final int iterations) {
    final long[] array = Arrays.stream(il.linesAsLongs(pc)).map(i -> i * multiplier).toArray();
    final Node[] nodes = new Node[array.length];
    Node zero = null;
    for (int i = 0; i < array.length; ++i) {
      nodes[i] = new Node(array[i]);
      if (array[i] == 0) {
        zero = nodes[i];
      }
      if (i > 0) {
        nodes[i].previous = nodes[i - 1];
        nodes[i - 1].next = nodes[i];
      }
    }
    if (zero == null) {
      throw new IllegalStateException("No element zero");
    }
    nodes[0].previous = nodes[nodes.length - 1];
    nodes[nodes.length - 1].next = nodes[0];

    final long modulo = nodes.length - 1;
    for (int i = 0; i < iterations; ++i) {
      for (final Node moving : nodes) {
        if (moving.value != 0) {
          long distance = moving.value % modulo;
          if (distance < 0) {
            distance += modulo;
          }
          Node movingAfter = moving;
          for (int j = 0; j < distance; ++j) {
            movingAfter = movingAfter.next;
          }
          // Exit the previous location.
          moving.previous.next = moving.next;
          moving.next.previous = moving.previous;

          // Add to the new location.
          moving.previous = movingAfter;
          moving.next = movingAfter.next;

          // Update the new previous and next nodes.
          movingAfter.next.previous = moving;
          movingAfter.next = moving;
        }
      }
    }
    long answer = 0;
    Node iter = zero;
    for (int i = 1; i <= 3_000; ++i) {
      iter = iter.next;
      if (i % 1_000 == 0) {
        answer += iter.value;
      }
    }
    return answer;
  }

  private class Node {

    final long value;

    Node next;

    Node previous;

    Node(final long v) {
      value = v;
    }
  }
}
