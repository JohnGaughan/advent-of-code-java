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
import java.util.Arrays;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2021, day = 16)
@Component
public final class Year2021Day16 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return makeRootNode(pc).getVersionSum();
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return makeRootNode(pc).evaluate();
  }

  public Node makeRootNode(final PuzzleContext pc) {
    final int[] ch = il.fileAsIntsFromHexDigits(pc);
    final boolean[] bits = new boolean[ch.length << 2];
    for (int i = 0; i < ch.length; ++i) {
      final int i_b = i << 2;
      bits[i_b + 3] = ((ch[i] & 0b0001) > 0);
      bits[i_b + 2] = ((ch[i] & 0b0010) > 0);
      bits[i_b + 1] = ((ch[i] & 0b0100) > 0);
      bits[i_b] = ((ch[i] & 0b1000) > 0);
    }
    // There is only ever one root node.
    return makeNode(bits, 0).node;
  }

  private NodeResult makeNode(final boolean[] bits, final int start) {
    final int version = toInt(bits, start, 3);
    final Operator op = Operator.valueOf(toInt(bits, start + 3, 3));
    if (op == null) {
      return makeLeafNode(bits, start, version);
    }
    return makeInternalNode(bits, start, version, op);
  }

  private NodeResult makeLeafNode(final boolean[] bits, final int start, final int version) {
    int offset = 6;
    long number = 0;

    boolean keepGoing = true;
    while (keepGoing) {
      number <<= 4;
      keepGoing = bits[start + offset];
      number += toInt(bits, start + offset + 1, 4);
      offset += 5;
    }

    return new NodeResult(new LeafNode(version, number), offset);
  }

  private NodeResult makeInternalNode(final boolean[] bits, final int start, final int version, final Operator op) {
    if (bits[start + 6]) {
      return makeInternalNodeByCount(bits, start, version, op);
    }
    return makeInternalNodeByLength(bits, start, version, op);
  }

  private NodeResult makeInternalNodeByCount(final boolean[] bits, final int start, final int version, final Operator op) {
    final int innerNodeCount = toInt(bits, start + 7, 11);
    int innerStart = 18;
    final Collection<Node> nodes = new ArrayList<>(innerNodeCount);
    for (int i = 0; i < innerNodeCount; ++i) {
      final NodeResult result = makeNode(bits, start + innerStart);
      nodes.add(result.node);
      innerStart += result.bitsUsed;
    }
    return new NodeResult(new InternalNode(version, op, nodes), innerStart);
  }

  private NodeResult makeInternalNodeByLength(final boolean[] bits, final int start, final int version, final Operator op) {
    final int innerLength = toInt(bits, start + 7, 15);
    final int innerStart = start + 22;
    final Collection<Node> nodes = new ArrayList<>();
    for (int i = 0; i < innerLength;) {
      final NodeResult result = makeNode(bits, innerStart + i);
      nodes.add(result.node);
      i += result.bitsUsed;
    }
    return new NodeResult(new InternalNode(version, op, nodes), 22 + innerLength);
  }

  /** Convert a range of bits to an integer consisting of those bits. */
  private int toInt(final boolean[] bits, final int start, final int length) {
    int result = 0;
    for (int i = 0; i < length; ++i) {
      result <<= 1;
      if (bits[start + i]) {
        ++result;
      }
    }
    return result;
  }

  /** Used to combine a node with its bit usage temporarily while building the tree. */
  private record NodeResult(Node node, int bitsUsed) {}

  private static enum Operator {

    SUM(0) {

      @Override
      long evaluate(final Collection<Node> nodes) {
        return nodes.stream()
                    .mapToLong(Node::evaluate)
                    .sum();
      }
    },
    PRODUCT(1) {

      @Override
      long evaluate(final Collection<Node> nodes) {
        return nodes.stream()
                    .mapToLong(Node::evaluate)
                    .reduce(1, (a, b) -> a * b);
      }
    },
    MINIMUM(2) {

      @Override
      long evaluate(final Collection<Node> nodes) {
        return nodes.stream()
                    .mapToLong(Node::evaluate)
                    .min()
                    .getAsLong();
      }
    },
    MAXIMUM(3) {

      @Override
      long evaluate(final Collection<Node> nodes) {
        return nodes.stream()
                    .mapToLong(Node::evaluate)
                    .max()
                    .getAsLong();
      }
    },
    GREATER_THAN(5) {

      @Override
      long evaluate(final Collection<Node> nodes) {
        final long[] values = nodes.stream()
                                   .mapToLong(Node::evaluate)
                                   .toArray();
        return (values[0] > values[1]) ? 1 : 0;
      }
    },
    LESS_THAN(6) {

      @Override
      long evaluate(final Collection<Node> nodes) {
        final long[] values = nodes.stream()
                                   .mapToLong(Node::evaluate)
                                   .toArray();
        return (values[0] < values[1]) ? 1 : 0;
      }
    },
    EQUAL(7) {

      @Override
      long evaluate(final Collection<Node> nodes) {
        final long[] values = nodes.stream()
                                   .mapToLong(Node::evaluate)
                                   .toArray();
        return (values[0] == values[1]) ? 1 : 0;
      }
    };

    public static Operator valueOf(final int id) {
      return Arrays.stream(values())
                   .filter(o -> o.id == id)
                   .findFirst()
                   .orElse(null);
    }

    private final int id;

    private Operator(final int i) {
      id = i;
    }

    abstract long evaluate(final Collection<Node> nodes);
  }

  private static interface Node {

    long getVersionSum();

    long evaluate();
  }

  private static final class LeafNode
  implements Node {

    private final long version;

    private final long number;

    LeafNode(final long ver, final long n) {
      version = ver;
      number = n;
    }

    @Override
    public long getVersionSum() {
      return version;
    }

    @Override
    public long evaluate() {
      return number;
    }

  }

  private static final class InternalNode
  implements Node {

    private final long version;

    protected final Operator type;

    final Collection<Node> operands;

    InternalNode(final long ver, final Operator t, final Collection<Node> ops) {
      version = ver;
      type = t;
      operands = ops;
    }

    @Override
    public long getVersionSum() {
      return version + operands.stream()
                               .mapToLong(Node::getVersionSum)
                               .sum();
    }

    @Override
    public long evaluate() {
      return type.evaluate(operands);
    }

  }

}
