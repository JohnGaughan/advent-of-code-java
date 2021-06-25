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

import java.nio.file.Files;
import java.util.Arrays;
import java.util.regex.Pattern;

import us.coffeecode.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2018/day/8">Year 2018, day 8</a>. Today's puzzle involves parsing a tree and
 * extracting values from it. Each node has metadata: part one asks for the sum of all of the metadata. Part two asks
 * for a slightly more complicated value that varies based on the number of children of each node.
 * </p>
 * <p>
 * This is a fairly simple solution. First, parse the input. The bulk of this work is done in the Node constructor.
 * Start by using the header data to initialize the metadata and child node arrays, then construct each child. Once this
 * is done, we have the offset to the metadata, so copy that in.
 * </p>
 * <p>
 * Part one is implemented in the sumAllMetadata() method. For each node, add its metadata to the sum of its children's
 * metadata, recursively. Part two calls the value() method, which returns results based on whether or not it has
 * children. Since nodes can be referenced multiple times, it also caches the results of its calculation for a minor
 * speed improvement.
 * </p>
 * <p>
 * Copyright (c) 2021 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2018Day08 {

  public long calculatePart1() {
    return getInput().sumAllMetadata();
  }

  public long calculatePart2() {
    return getInput().value();
  }

  private static final Pattern SEPARATOR = Pattern.compile(" ");

  /** Get the input data for this solution. */
  private Node getInput() {
    try {
      return new Node(Arrays.stream(SEPARATOR.split(Files.readString(Utils.getInput(2018, 8)).trim())).mapToInt(
        Integer::parseInt).toArray());
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private static final class Node {

    final int[] metadata;

    final Node[] children;

    private Integer size;

    private Integer value;

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

    int size() {
      if (size == null) {
        int s = 2 + metadata.length;
        for (final Node child : children) {
          s += child.size();
        }
        size = Integer.valueOf(s);
      }
      return size.intValue();
    }

    int sumAllMetadata() {
      int sum = 0;
      for (final int meta : metadata) {
        sum += meta;
      }
      for (final Node child : children) {
        sum += child.sumAllMetadata();
      }
      return sum;
    }

    int value() {
      if (value == null) {
        if (children.length == 0) {
          value = Integer.valueOf(sumAllMetadata());
        }
        else {
          int v = 0;
          for (final int meta : metadata) {
            int idx = meta - 1;
            if (0 <= idx && idx < children.length) {
              v += children[idx].value();
            }
          }
          value = Integer.valueOf(v);
        }
      }
      return value.intValue();
    }
  }

}
