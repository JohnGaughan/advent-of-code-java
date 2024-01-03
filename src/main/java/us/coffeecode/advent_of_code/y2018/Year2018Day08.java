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
package us.coffeecode.advent_of_code.y2018;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2018, day = 8, title = "Memory Maneuver")
@Component
public final class Year2018Day08 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return new Node(il.fileAsIntsFromSplit(pc, SEPARATOR)).sumAllMetadata();
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return new Node(il.fileAsIntsFromSplit(pc, SEPARATOR)).value();
  }

  private static final Pattern SEPARATOR = Pattern.compile(" ");

  private static final class Node {

    final int[] metadata;

    final Node[] children;

    private Long size;

    private Long value;

    Node(final int[] input) {
      this(input, 0);
    }

    Node(final int[] input, final int offset) {
      children = new Node[input[offset]];
      metadata = new int[input[offset + 1]];
      int childOffset = offset + 2;
      for (int i = 0; i < children.length; ++i) {
        children[i] = new Node(input, childOffset);
        childOffset += children[i].size();
      }
      for (int i = 0; i < metadata.length; ++i) {
        metadata[i] = input[childOffset + i];
      }
    }

    long size() {
      if (size == null) {
        long s = 2 + metadata.length;
        for (final Node child : children) {
          s += child.size();
        }
        size = Long.valueOf(s);
      }
      return size.longValue();
    }

    long sumAllMetadata() {
      long sum = 0;
      for (final int meta : metadata) {
        sum += meta;
      }
      for (final Node child : children) {
        sum += child.sumAllMetadata();
      }
      return sum;
    }

    long value() {
      if (value == null) {
        if (children.length == 0) {
          value = Long.valueOf(sumAllMetadata());
        }
        else {
          long v = 0;
          for (final int meta : metadata) {
            int idx = meta - 1;
            if ((0 <= idx) && (idx < children.length)) {
              v += children[idx].value();
            }
          }
          value = Long.valueOf(v);
        }
      }
      return value.intValue();
    }
  }

}
