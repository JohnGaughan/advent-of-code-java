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
package us.coffeecode.advent_of_code.y2019;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.LongStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2019, day = 22, title = "Slam Shuffle")
@Component
public final class Year2019Day22 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    long[] deck = LongStream.rangeClosed(0, 10006).toArray();
    for (final Input move : il.linesAsObjects(pc, this::parse)) {
      deck = move.action.applyPartOne(deck, move.arg.intValue());
    }
    for (int i = 0; i < deck.length; ++i) {
      if (deck[i] == 2019) {
        return i;
      }
    }
    return 0;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final BigInteger m = BigInteger.valueOf(119_315_717_514_047L);
    final BigInteger rounds = BigInteger.valueOf(101_741_582_076_661L);
    final BigInteger position = BigInteger.valueOf(2_020);
    BigInteger a = ONE;
    BigInteger b = ZERO;
    for (final Input move : il.linesAsObjects(pc, this::parse).reversed()) {
      final BigInteger x = BigInteger.valueOf(move.arg.longValue());
      Result result = move.action.applyPartTwo(a, b, x, m);
      a = result.a;
      b = result.b;
    }
    final BigInteger spin = a.modPow(rounds, m);
    final BigInteger t1 = spin.add(m).subtract(ONE);
    final BigInteger t2 = a.subtract(ONE);
    BigInteger answer = spin.multiply(position);
    answer = answer.add(b.multiply(t1).multiply(t2.modPow(m.subtract(TWO), m)));
    answer = answer.mod(m);
    return answer.longValue();
  }

  private static final BigInteger TWO = BigInteger.valueOf(2);

  private static Pattern DIGITS = Pattern.compile("-?\\d+");

  private Input parse(final String line) {
    if (line.startsWith("cut")) {
      final Matcher m = DIGITS.matcher(line);
      m.find();
      return new Input(Action.CUT, Integer.valueOf(m.group()));
    }
    else if (line.startsWith("deal with")) {
      final Matcher m = DIGITS.matcher(line);
      m.find();
      return new Input(Action.DEAL_WITH_INCREMENT, Integer.valueOf(m.group()));
    }
    else if (line.startsWith("deal into")) {
      return new Input(Action.DEAL_INTO_NEW_STACK, Integer.valueOf(0));
    }
    else {
      throw new IllegalArgumentException(line);
    }
  }

  private static record Input(Action action, Integer arg) {}

  private static record Result(BigInteger a, BigInteger b) {}

  private static enum Action {

    DEAL_INTO_NEW_STACK {

      @Override
      long[] applyPartOne(final long[] deck, final long arg) {
        final long[] newDeck = new long[deck.length];
        for (int i = 0; i < deck.length; ++i) {
          newDeck[i] = deck[deck.length - i - 1];
        }
        return newDeck;
      }

      @Override
      Result applyPartTwo(final BigInteger a, final BigInteger b, final BigInteger x, final BigInteger m) {
        return new Result(a.negate().mod(m), b.add(BigInteger.ONE).negate().mod(m));
      }
    },
    CUT {

      @Override
      long[] applyPartOne(final long[] deck, final long arg) {
        if (arg == 0) {
          return deck;
        }
        final long[] newDeck = new long[deck.length];

        for (int i = 0; i < deck.length; ++i) {
          newDeck[Math.floorMod(i - arg, deck.length)] = deck[i];
        }

        return newDeck;
      }

      @Override
      Result applyPartTwo(final BigInteger a, final BigInteger b, final BigInteger x, final BigInteger m) {
        return new Result(a, b.add(x).mod(m));
      }
    },
    DEAL_WITH_INCREMENT {

      @Override
      long[] applyPartOne(final long[] deck, final long arg) {
        final long[] newDeck = new long[deck.length];
        for (int i = 0; i < deck.length; ++i) {
          newDeck[Math.floorMod(i * arg, deck.length)] = deck[i];
        }
        return newDeck;
      }

      @Override
      Result applyPartTwo(final BigInteger a, final BigInteger b, final BigInteger x, final BigInteger m) {
        final BigInteger temp = x.modPow(m.subtract(TWO), m);
        return new Result(a.multiply(temp).mod(m), b.multiply(temp).mod(m));
      }
    };

    abstract long[] applyPartOne(final long[] deck, final long arg);

    abstract Result applyPartTwo(final BigInteger a, final BigInteger b, final BigInteger x, final BigInteger m);
  }

}
