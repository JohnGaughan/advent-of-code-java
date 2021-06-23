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
package us.coffeecode.advent_of_code.y2017;

import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import us.coffeecode.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2017/day/8">Year 2017, day 8</a>. This problem simulates a simple instruction set
 * as in a CPU. It iterates through all of the instructions, one after another, performing conditional arithmetic. We
 * either need to know the maximum value in any register at the end of execution (part one) or at any point during
 * execution (part two).
 * </p>
 * <p>
 * This is fairly simple. Encode the logic in an enum and some values for register names and integers, then go through
 * and evaluate the conditionals and possibly add a value to a register. Even having an indeterminate number of
 * registers is trivial, since they are stored in a map and we can simply add a new register before using it if the
 * current instruction references one that does not exist.
 * </p>
 * <p>
 * Copyright (c) 2021 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2017Day08 {

  public long calculatePart1() {
    return calculate(false);
  }

  public long calculatePart2() {
    return calculate(true);
  }

  private static final Integer DEFAULT_REGISTER_VALUE = Integer.valueOf(0);

  public int calculate(final boolean partTwo) {
    final Map<String, Integer> registers = new HashMap<>();
    int max = Integer.MIN_VALUE;
    for (final Instruction inst : getInput()) {
      registers.putIfAbsent(inst.compLeft, DEFAULT_REGISTER_VALUE);
      int left = registers.get(inst.compLeft).intValue();
      if (inst.comp.eval(left, inst.compRight)) {
        registers.putIfAbsent(inst.reg, DEFAULT_REGISTER_VALUE);
        int value = registers.get(inst.reg).intValue();
        value += inst.amt;
        max = Math.max(value, max);
        registers.put(inst.reg, Integer.valueOf(value));
      }
    }
    return partTwo ? max : registers.values().stream().mapToInt(i -> i.intValue()).max().getAsInt();
  }

  /** Get the input data for this solution. */
  private List<Instruction> getInput() {
    try {
      return Files.readAllLines(Utils.getInput(2017, 8)).stream().map(Instruction::new).collect(Collectors.toList());
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private static enum Comparison {

    EQ("==") {

      @Override
      public boolean eval(final int arg1, final int arg2) {
        return arg1 == arg2;
      }
    },
    NE("!=") {

      @Override
      public boolean eval(final int arg1, final int arg2) {
        return arg1 != arg2;
      }
    },
    GT(">") {

      @Override
      public boolean eval(final int arg1, final int arg2) {
        return arg1 > arg2;
      }
    },
    LT("<") {

      @Override
      public boolean eval(final int arg1, final int arg2) {
        return arg1 < arg2;
      }
    },
    GTE(">=") {

      @Override
      public boolean eval(final int arg1, final int arg2) {
        return arg1 >= arg2;
      }
    },
    LTE("<=") {

      @Override
      public boolean eval(final int arg1, final int arg2) {
        return arg1 <= arg2;
      }
    };

    static Comparison forSymbol(final String symbol) {
      for (Comparison c : values()) {
        if (c.symbol.equals(symbol)) {
          return c;
        }
      }
      throw new IllegalArgumentException(symbol);
    }

    private final String symbol;

    private Comparison(final String _symbol) {
      symbol = _symbol;
    }

    public abstract boolean eval(int arg1, int arg2);
  }

  private static final class Instruction {

    private static final Pattern SEPARATOR = Pattern.compile(" ");

    final String reg;

    final int amt;

    final String compLeft;

    final Comparison comp;

    final int compRight;

    Instruction(final String input) {
      final String[] tokens = SEPARATOR.split(input);
      reg = tokens[0];
      if ("inc".equals(tokens[1])) {
        amt = Integer.parseInt(tokens[2]);
      }
      else {
        amt = -1 * Integer.parseInt(tokens[2]);
      }
      compLeft = tokens[4];
      comp = Comparison.forSymbol(tokens[5]);
      compRight = Integer.parseInt(tokens[6]);
    }

  }

}
