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

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Pattern;

import us.coffeecode.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2016/day/10">Year 2016, day 10</a>. This is a directed graph problem. Bots are
 * internal nodes. Inputs are nodes with edges moving away, into bots. Outputs are the opposite: nodes with a single
 * edge moving into them. Part one asks us to find the internal node with two specific inputs, and part two asks us to
 * multiply the values of three outputs.
 * </p>
 * <p>
 * This was an interesting problem compounded by the fact that an earlier version parsed the input file <i>mostly</i>
 * correct. I used a regular expression approach that did not always produce correct results. Part one got my confidence
 * up with the correct answer, but part two kept throwing exceptions. Tearing out and replacing the input parsing fixed
 * it right away. One pattern I noticed here is the graph has no cycles. Therefore, each rule applies exactly once. This
 * means we can prune rules as we go along, and never look at them again.
 * </p>
 * <p>
 * Copyright (c) 2021 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2016Day10 {

  public long calculatePart1() {
    return calculate(true);
  }

  public long calculatePart2() {
    return calculate(false);
  }

  public long calculate(final boolean part1) {
    State state = getInput();
    while (!state.rules.isEmpty() && (state.outputs[0] == 0 || state.outputs[1] == 0 || state.outputs[2] == 0)) {
      final Iterator<Rule> iter = state.rules.iterator();
      while (iter.hasNext()) {
        final Rule rule = iter.next();
        if (state.bots[rule.who].hasTwoChips()) {
          if (part1) {
            final int[] chips = state.bots[rule.who].peek();
            if (chips[0] == 17 && chips[1] == 61) {
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

  private static final Pattern LINE_SPLIT = Pattern.compile(" ");

  /** Get the input data for this solution. */
  private State getInput() {
    try {
      final Collection<Rule> rules = new ArrayList<>(210);
      final Collection<String> input = Files.readAllLines(Utils.getInput(2016, 10));
      final Collection<int[]> initialState = new ArrayList<>(21);
      // Get the input and divide it into the initial state, used to give stuff to bots, and rules to apply as the
      // simulation goes on.
      int maxBotId = -1;
      int maxOutputId = -1;
      for (final String line : input) {
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
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
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
      if (chips.length == 0) {
        throw new IllegalStateException("Bot " + id + ": no chips to take");
      }
      else if (chips.length == 1) {
        final int chip = chips[0];
        chips = new int[0];
        return chip;
      }
      else if (chips.length == 2) {
        final int chip = chips[0];
        chips = new int[] { chips[1] };
        return chip;
      }
      else {
        throw new IllegalStateException("Bot " + id + ": illegal array length " + chips.length);
      }
    }

    @Override
    public String toString() {
      StringBuilder str = new StringBuilder();
      str.append(id).append(Arrays.toString(chips));
      return str.toString();
    }

  }

  private static final class Rule {

    final int who;

    final Target lowType;

    final int lowId;

    final Target highType;

    final int highId;

    Rule(final int _who, final Target _lowType, final int _lowId, final Target _highType, final int _highId) {
      who = _who;
      lowType = _lowType;
      lowId = _lowId;
      highType = _highType;
      highId = _highId;
    }

    @Override
    public String toString() {
      StringBuilder str = new StringBuilder(32);
      str.append(who).append("[low->").append(lowType.name()).append(lowId).append(",high->").append(
        highType.name()).append(highId).append("]");
      return str.toString();
    }
  }

  private static enum Target {
    bot,
    output;
  }

}
