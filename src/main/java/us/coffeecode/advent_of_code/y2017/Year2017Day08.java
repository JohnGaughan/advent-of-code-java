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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2017, day = 8, title = "I Heard You Like Registers")
@Component
public final class Year2017Day08 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return calculate(pc);
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return calculate(pc);
  }

  private static final Long DEFAULT_REGISTER_VALUE = Long.valueOf(0);

  public long calculate(final PuzzleContext pc) {
    final Map<String, Long> registers = new HashMap<>();
    long max = Long.MIN_VALUE;
    for (final Instruction inst : il.linesAsObjects(pc, Instruction::make)) {
      registers.putIfAbsent(inst.compLeft, DEFAULT_REGISTER_VALUE);
      long left = registers.get(inst.compLeft).intValue();
      if (inst.comp.eval(left, inst.compRight)) {
        registers.putIfAbsent(inst.reg, DEFAULT_REGISTER_VALUE);
        long value = registers.get(inst.reg).intValue();
        value += inst.amt;
        max = Math.max(value, max);
        registers.put(inst.reg, Long.valueOf(value));
      }
    }
    if (pc.getBoolean("ReturnMaxFinalValue")) {
      return registers.values().stream().mapToLong(Long::longValue).max().getAsLong();
    }
    return max;
  }

  private static enum Comparison {

    EQ("==") {

      @Override
      public boolean eval(final long arg1, final long arg2) {
        return arg1 == arg2;
      }
    },
    NE("!=") {

      @Override
      public boolean eval(final long arg1, final long arg2) {
        return arg1 != arg2;
      }
    },
    GT(">") {

      @Override
      public boolean eval(final long arg1, final long arg2) {
        return arg1 > arg2;
      }
    },
    LT("<") {

      @Override
      public boolean eval(final long arg1, final long arg2) {
        return arg1 < arg2;
      }
    },
    GTE(">=") {

      @Override
      public boolean eval(final long arg1, final long arg2) {
        return arg1 >= arg2;
      }
    },
    LTE("<=") {

      @Override
      public boolean eval(final long arg1, final long arg2) {
        return arg1 <= arg2;
      }
    };

    static Comparison forSymbol(final String symbol) {
      return Arrays.stream(values()).filter(c -> c.symbol.equals(symbol)).findFirst().get();
    }

    private final String symbol;

    private Comparison(final String _symbol) {
      symbol = _symbol;
    }

    public abstract boolean eval(long arg1, long arg2);
  }

  private static record Instruction(String reg, long amt, String compLeft, Comparison comp, long compRight) {

    private static final Pattern SEPARATOR = Pattern.compile(" ");

    static Instruction make(final String input) {
      final String[] tokens = SEPARATOR.split(input);
      long amt = ("inc".equals(tokens[1]) ? 1 : -1) * Integer.parseInt(tokens[2]);
      return new Instruction(tokens[0], amt, tokens[4], Comparison.forSymbol(tokens[5]), Long.parseLong(tokens[6]));
    }

  }

}
