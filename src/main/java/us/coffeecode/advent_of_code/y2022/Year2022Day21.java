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
import java.util.HashMap;
import java.util.Map;
import java.util.function.LongBinaryOperator;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2022, day = 21)
@Component
public class Year2022Day21 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return getRoot(pc).eval();
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final ParentNode root = (ParentNode) getRoot(pc);
    simplify(root);
    // This is an equation of the form N = (...) or (...) = N. Get both sides.
    long value;
    Node humanSide;
    if (root.left.canEval()) {
      value = root.left.eval();
      humanSide = root.right;
    }
    else {
      humanSide = root.left;
      value = root.right.eval();
    }

    // Repeatedly take the inverse operation of the human side on both sides until "humn" remains.
    while (humanSide instanceof ParentNode en) {
      final Operation inverse = Operation.opposite(en.op);
      // If the constant is on the right, we can always simply apply the inverse.
      if (en.right.canEval()) {
        final long value2 = en.right.eval();
        value = inverse.eval(value, value2);

        humanSide = en.left;
      }
      // If the constant is on the left, we need to be careful about applying the inverse operation because the right
      // side contains the variable which cannot be evaluated.
      else {
        final long value2 = en.left.eval();

        // If associative, order doesn't matter: if we add a constant and a variable, we can always subtract the
        // constant. Ex: "A = B + x" means we can evaluate "A = A - B" to get the new A.
        if (en.op.isAssociative()) {
          value = inverse.eval(value, value2);
        }
        // If not associative and the constant is on the left, we instead apply the original operation but to the
        // constant. Given "A = B - x" we subtract A from both sides and get "0 = (B - A) - x" and rearrange to get "B -
        // A = x" so the new A is (B - A), which is what we calculate here.
        else {
          value = en.op.eval(value2, value);
        }
        humanSide = en.right;
      }
    }

    return value;
  }

  /**
   * Evaluate everything that can be evaluated for the node's children. Any given parent node will have two children.
   * One will be a leaf node, the other will be another parent node or the human node. If it has a parent node as a
   * child, that parent node will have the human node somewhere in its subtree. This is true for all parent nodes.
   */
  private void simplify(final ParentNode root) {
    if (root.left instanceof ParentNode) {
      if (root.left.canEval()) {
        root.left = new LeafNode(root.left.getId(), root.left.eval());
      }
      else {
        simplify((ParentNode) root.left);
      }
    }
    if (root.right instanceof ParentNode) {
      if (root.right.canEval()) {
        root.right = new LeafNode(root.right.getId(), root.right.eval());
      }
      else {
        simplify((ParentNode) root.right);
      }
    }
  }

  private Node getRoot(final PuzzleContext pc) {
    final Map<String, Node> monkeys = new HashMap<>(4096);
    final Map<String, String[]> rawMonkeys = getRawInput(pc);

    // Build the tree from the leaves up to the root. This ensures that children are constructed before parents and we
    // can always build a node including child references.

    // Get constants first
    final boolean humanIsVariable = pc.getBoolean("HumanIsVariable");
    for (final var iter = rawMonkeys.entrySet()
                                    .iterator(); iter.hasNext();) {
      var entry = iter.next();
      final String[] value = entry.getValue();
      if (value.length == 1) {
        final String key = entry.getKey();
        if (humanIsVariable && HUMAN.equals(key)) {
          monkeys.put(key, new HumanNode());
        }
        else {
          monkeys.put(key, new LeafNode(key, Long.parseLong(value[0])));
        }
        iter.remove();
      }
    }

    // Loop over the map and construct whatever nodes we can each pass, until it is empty.
    while (!rawMonkeys.isEmpty()) {
      for (final var iter = rawMonkeys.entrySet()
                                      .iterator(); iter.hasNext();) {
        var entry = iter.next();
        final String[] value = entry.getValue();
        if (monkeys.containsKey(value[0]) && monkeys.containsKey(value[2])) {
          final String key = entry.getKey();
          final Node left = monkeys.get(value[0]);
          final Operation op = Operation.forString((humanIsVariable && ROOT.equals(key)) ? "=" : value[1]);
          final Node right = monkeys.get(value[2]);
          monkeys.put(key, new ParentNode(key, left, right, op));
          iter.remove();
        }
      }
    }

    return monkeys.get(ROOT);
  }

  private static final String HUMAN = "humn";

  private static final String ROOT = "root";

  private final Map<String, String[]> getRawInput(final PuzzleContext pc) {
    final Map<String, String[]> nameToTokens = new HashMap<>(4096);
    for (final String line : il.lines(pc)) {
      final String[] tokens = SPLIT.split(line);
      final String key = tokens[0].substring(0, 4);
      final String[] value = new String[tokens.length - 1];
      System.arraycopy(tokens, 1, value, 0, value.length);
      nameToTokens.put(key, value);
    }
    return nameToTokens;
  }

  private static final Pattern SPLIT = Pattern.compile(" ");

  private static interface Node {

    String getId();

    long eval();

    boolean canEval();
  }

  private static final class ParentNode
  implements Node {

    Node left;

    Node right;

    private final String id;

    private final Operation op;

    ParentNode(final String _id, final Node _left, final Node _right, final Operation o) {
      id = _id;
      left = _left;
      right = _right;
      op = o;
    }

    @Override
    public String getId() {
      return id;
    }

    @Override
    public boolean canEval() {
      return left.canEval() && right.canEval();
    }

    @Override
    public long eval() {
      return op.eval(left.eval(), right.eval());
    }
  }

  private static final class LeafNode
  implements Node {

    final String id;

    private final long value;

    LeafNode(final String _id, final long v) {
      id = _id;
      value = v;
    }

    @Override
    public String getId() {
      return id;
    }

    @Override
    public boolean canEval() {
      return true;
    }

    @Override
    public long eval() {
      return value;
    }
  }

  private static final class HumanNode
  implements Node {

    @Override
    public String getId() {
      return HUMAN;
    }

    @Override
    public long eval() {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean canEval() {
      return false;
    }
  }

  private enum Operation {

    ADD("+", true, (a, b) -> a + b),
    SUB("-", false, (a, b) -> a - b),
    MUL("*", true, (a, b) -> a * b),
    DIV("/", false, (a, b) -> a / b),
    EQ("=", true, (a, b) -> {
      throw new UnsupportedOperationException();
    });

    static Operation forString(final String str) {
      return Arrays.stream(values())
                   .filter(o -> o.id.equals(str))
                   .findFirst()
                   .get();
    }

    private static final Map<Operation, Operation> opposites = Map.of(ADD, SUB, SUB, ADD, MUL, DIV, DIV, MUL);

    static Operation opposite(final Operation op) {
      return opposites.get(op);
    }

    private final String id;

    private final LongBinaryOperator op;

    private final boolean associative;

    private Operation(final String _id, final boolean _associative, final LongBinaryOperator _op) {
      id = _id;
      associative = _associative;
      op = _op;
    }

    public long eval(final long a, final long b) {
      return op.applyAsLong(a, b);
    }

    public boolean isAssociative() {
      return associative;
    }

  }
}
