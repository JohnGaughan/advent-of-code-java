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
package us.coffeecode.advent_of_code.y2020;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2020, day = 18, title = "Operation Order")
@Component
public final class Year2020Day18 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return getInput(pc, s -> recursiveDescent(s)).stream()
                                                 .mapToLong(Expression::evaluate)
                                                 .sum();
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return getInput(pc, s -> shuntingYard(s)).stream()
                                             .mapToLong(Expression::evaluate)
                                             .sum();
  }

  /** Get the input data for this solution. */
  private List<Expression> getInput(final PuzzleContext pc, final Function<String, Expression> algorithm) {
    return il.linesAsObjects(pc, s -> parseLine(s, algorithm));
  }

  private static final Pattern SPACE = Pattern.compile(" ");

  /** Parse a single line from the input file. */
  private Expression parseLine(final String line, final Function<String, Expression> algorithm) {
    // Remove useless spaces then call whatever algorithm is needed.
    String expression = SPACE.matcher(line)
                             .replaceAll("");
    return algorithm.apply(expression);
  }

  /**
   * Parse the input using the shunting yard algorithm. This specific implementation of the algorithm gives precedence
   * to addition over multiplication.
   */
  private Expression shuntingYard(final String exp) {
    final Deque<String> outputQueue = new ArrayDeque<>(64);
    final Deque<String> operatorStack = new ArrayDeque<>(64);
    for (int i = 0; i < exp.length(); ++i) {
      final String token = exp.substring(i, i + 1);
      final int codePoint = exp.codePointAt(i);
      if (Character.isDigit(codePoint)) {
        outputQueue.addLast(token);
      }
      else if (Operator.isValid(token)) {
        while (true) {
          if (operatorStack.isEmpty()) {
            break;
          }
          final String top = operatorStack.peekFirst();
          if ("(".equals(top)) {
            break;
          }
          if ("*".equals(top) && "+".equals(token)) {
            break;
          }
          outputQueue.addLast(operatorStack.removeFirst());
        }
        operatorStack.addFirst(token);
      }
      else if ("(".equals(token)) {
        operatorStack.addFirst(token);
      }
      else if (")".equals(token)) {
        while (!"(".equals(operatorStack.peekFirst())) {
          outputQueue.addLast(operatorStack.removeFirst());
        }
        if ("(".equals(operatorStack.peekFirst())) {
          operatorStack.removeFirst();
        }
      }
    }
    while (!operatorStack.isEmpty()) {
      outputQueue.addLast(operatorStack.removeFirst());
    }

    // Drain the output queue into a stack of tokens, reversing its order (RPN)
    final Deque<String> tokenStack = new ArrayDeque<>(outputQueue.size());
    while (!outputQueue.isEmpty()) {
      tokenStack.addFirst(outputQueue.removeFirst());
    }

    return toExpression(tokenStack);
  }

  /** Convert the tokens to an expression object. */
  private Expression toExpression(final Deque<String> tokens) {
    final Operator op = Operator.forToken(tokens.removeFirst());

    final Expression left;
    if (Operator.isValid(tokens.peekFirst())) {
      left = toExpression(tokens);
    }
    else {
      long value = Long.parseLong(tokens.removeFirst());
      left = new ConstantExpression(value);
    }

    final Expression right;
    if (Operator.isValid(tokens.peekFirst())) {
      right = toExpression(tokens);
    }
    else {
      long value = Long.parseLong(tokens.removeFirst());
      right = new ConstantExpression(value);
    }
    return new CompoundExpression(left, op, right);
  }

  /**
   * Parse the input using a recursive descent left-associative algorithm. This specific implementation does not have
   * any operator precedence.
   */
  private Expression recursiveDescent(final String exp) {
    // To make it left-associative, we need to parse from the right.
    int next = exp.codePointAt(exp.length() - 1);
    // If this is a constant...
    if (Character.isDigit(next)) {
      final int idx_right = exp.length() - 1;
      final int idx_op = idx_right - 1;
      final Expression constant = new ConstantExpression(Integer.parseInt(exp.substring(idx_right)));
      if (exp.length() == 1) {
        // There is nothing left to do and this is actually the final constant on the right.
        return constant;
      }
      final Operator op = Operator.forToken(exp.substring(idx_op, idx_op + 1));
      final Expression left = recursiveDescent(exp.substring(0, idx_op));
      return new CompoundExpression(left, op, constant);
    }
    // If this is a nested expression...
    else if (next == ')') {
      // Find the matching parenthesis which is the last part of the left expression.
      int level = 1;
      int match = -1;
      for (int i = exp.length() - 2; i >= 0; --i) {
        int at_i = exp.codePointAt(i);
        if (at_i == ')') {
          ++level;
        }
        else if (at_i == '(') {
          --level;
        }
        if (level == 0) {
          match = i;
          break;
        }
      }
      // Found the left edge of the right expression. Remove the parenthesis and recurse what's inside.
      final Expression nested = recursiveDescent(exp.substring(match + 1, exp.length() - 1));

      // If there is nothing left to match to the left, return the nested expression.
      if (match == 0) {
        return nested;
      }

      // Find the operator.
      final Operator op = Operator.forToken(exp.substring(match - 1, match));

      // The remainder of the line is the left side.
      final Expression left = recursiveDescent(exp.substring(0, match - 1));

      return new CompoundExpression(left, op, nested);
    }
    else {
      throw new IllegalArgumentException("Invalid expression [" + exp + "]");
    }
  }

  /** Represents one expression */
  @FunctionalInterface
  private static interface Expression {

    /** Evaluate this expression */
    long evaluate();
  }

  /** An expression that evaluates to a constant value. */
  private static final class ConstantExpression
  implements Expression {

    private final long value;

    ConstantExpression(final long v) {
      value = v;
    }

    @Override
    public long evaluate() {
      return value;
    }

    @Override
    public String toString() {
      return Long.toString(value);
    }
  }

  /** An expression that contains other expressions. */
  private static final class CompoundExpression
  implements Expression {

    private final Expression left;

    private final Operator op;

    private final Expression right;

    CompoundExpression(final Expression l, final Operator oper, final Expression r) {
      left = l;
      op = oper;
      right = r;
    }

    @Override
    public long evaluate() {
      final long a = left.evaluate();
      final long b = right.evaluate();
      return op.apply(a, b);
    }

    @Override
    public String toString() {
      return "(" + left + op + right + ")";
    }
  }

  /** Operators that combine integers in the input. */
  private static enum Operator {

    ADD("+") {

      @Override
      long apply(final long a, final long b) {
        return a + b;
      }
    },
    MULTIPLY("*") {

      @Override
      long apply(final long a, final long b) {
        return a * b;
      }
    };

    /** Get whether the provided code point represents a valid operator. */
    public static boolean isValid(final String str) {
      for (Operator op : values()) {
        if (op.symbol.equals(str)) {
          return true;
        }
      }
      return false;
    }

    /** Get the operator that corresponds with the provided code point. */
    public static Operator forToken(final String token) {
      for (Operator op : values()) {
        if (op.symbol.equals(token)) {
          return op;
        }
      }
      throw new IllegalArgumentException("Unknown token [" + token + "]");
    }

    private final String symbol;

    private Operator(final String codePoint) {
      symbol = codePoint;
    }

    /** Applies this operator to the inputs, combining them into a single result. */
    abstract long apply(long a, long b);

    @Override
    public String toString() {
      return symbol;
    }

  }

}
