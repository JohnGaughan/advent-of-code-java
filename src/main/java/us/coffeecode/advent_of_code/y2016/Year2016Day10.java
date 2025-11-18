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
package us.coffeecode.advent_of_code.y2016;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2016, day = 10)
@Component
public final class Year2016Day10 {

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

  public long calculate(final PuzzleContext pc) {
    final int[] values = Arrays.stream(CHIPS_SPLIT.split(pc.getString("values")))
                               .mapToInt(Integer::parseInt)
                               .toArray();
    final boolean findBot = pc.getBoolean("findBot");
    final State state = getInput(pc);
    while (!state.rules.isEmpty() && ((state.outputs[0] == 0) || (state.outputs[1] == 0) || (state.outputs[2] == 0))) {
      final Iterator<Rule> iter = state.rules.iterator();
      while (iter.hasNext()) {
        final Rule rule = iter.next();
        if (state.bots[rule.who].hasTwoChips()) {
          if (findBot) {
            final int[] chips = state.bots[rule.who].peek();
            if (equal(chips, values)) {
              return rule.who;
            }
          }
          final int lowChip = state.bots[rule.who].take();
          final int highChip = state.bots[rule.who].take();
          if (rule.lowType == Target.bot) {
            state.bots[rule.lowId].offer(lowChip);
          }
          else {
            state.outputs[rule.lowId] = lowChip;
          }
          if (rule.highType == Target.bot) {
            state.bots[rule.highId].offer(highChip);
          }
          else {
            state.outputs[rule.highId] = highChip;
          }
          // No cycles in the graph, so each rule only applies once.
          iter.remove();
        }
      }
    }
    return state.outputs[0] * state.outputs[1] * state.outputs[2];
  }

  private boolean equal(final int[] a, final int[] b) {
    return ((a[0] == b[0]) || (a[0] == b[1])) && ((a[1] == b[0]) || (a[1] == b[1]));
  }

  private static final Pattern CHIPS_SPLIT = Pattern.compile(",");

  private static final Pattern LINE_SPLIT = Pattern.compile(" ");

  /** Get the input data for this solution. */
  private State getInput(final PuzzleContext pc) {
    final Collection<Rule> rules = new ArrayList<>(210);
    final Collection<int[]> initialState = new ArrayList<>(21);
    // Get the input and divide it into the initial state, used to give stuff to bots, and rules to apply as the
    // simulation goes on.
    int maxBotId = -1;
    int maxOutputId = -1;
    for (final String line : il.lines(pc)) {
      final String[] tokens = LINE_SPLIT.split(line);
      // If a chip is given to a bot, this is the initial state.
      if ("value".equals(tokens[0])) {
        final int chip = Integer.parseInt(tokens[1]);
        final int bot = Integer.parseInt(tokens[5]);
        initialState.add(new int[] { chip, bot });
        maxBotId = Math.max(maxBotId, bot);
      }
      // Otherwise, this is a rule that a bot evaluates during simulation.
      else {
        final int who = Integer.parseInt(tokens[1]);
        final int lowId = Integer.parseInt(tokens[6]);
        final int highId = Integer.parseInt(tokens[11]);

        final Target lowType = Target.valueOf(tokens[5]);
        final Target highType = Target.valueOf(tokens[10]);

        rules.add(new Rule(who, lowType, lowId, highType, highId));

        if (Target.bot == lowType) {
          maxBotId = Math.max(maxBotId, lowId);
        }
        else {
          maxOutputId = Math.max(maxOutputId, lowId);
        }
        if (Target.bot == highType) {
          maxBotId = Math.max(maxBotId, highId);
        }
        else {
          maxOutputId = Math.max(maxOutputId, highId);
        }
      }
    }

    // Initialize the bot state. Note: bots are numbered starting with zero. In order to fit maxBotId we need an extra
    // array element here. This also means no need to subtract one when indexing later.
    final Bot[] bots = new Bot[maxBotId + 1];
    for (int i = 0; i < bots.length; ++i) {
      bots[i] = new Bot(i);
    }
    for (final int[] state : initialState) {
      bots[state[1]].offer(state[0]);
    }

    return new State(rules, bots, new int[maxOutputId + 1]);
  }

  private static final class State {

    final Collection<Rule> rules;

    final Bot[] bots;

    final int[] outputs;

    State(final Collection<Rule> _rules, final Bot[] _bots, final int[] _outputs) {
      rules = _rules;
      bots = _bots;
      outputs = _outputs;
    }
  }

  private static final class Bot {

    private int[] chips = new int[0];

    private final int id;

    Bot(final int _id) {
      id = _id;
    }

    void offer(final int chip) {
      if (chips.length == 0) {
        chips = new int[] { chip };
      }
      else if (chips.length == 1) {
        if (chips[0] < chip) {
          chips = new int[] { chips[0], chip };
        }
        else {
          chips = new int[] { chip, chips[0] };
        }
      }
      else {
        throw new IllegalStateException("Bot " + id + ": too many chips " + (chips.length + 1));
      }
    }

    boolean hasTwoChips() {
      return chips.length == 2;
    }

    int[] peek() {
      return chips;
    }

    int take() {
      if (chips.length == 1) {
        final int chip = chips[0];
        chips = new int[0];
        return chip;
      }
      else if (chips.length == 2) {
        final int chip = chips[0];
        chips = new int[] { chips[1] };
        return chip;
      }
      throw new IllegalStateException("Bot " + id + ": illegal array length " + chips.length);
    }

    @Override
    public String toString() {
      return id + Arrays.toString(chips);
    }

  }

  private static final record Rule(int who, Target lowType, int lowId, Target highType, int highId) {}

  private static enum Target {
    bot,
    output;
  }

}
