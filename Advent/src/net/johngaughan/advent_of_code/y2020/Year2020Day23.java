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
package net.johngaughan.advent_of_code.y2020;

import java.util.Arrays;

/**
 * <p>
 * <a href="https://adventofcode.com/2020/day/23">Year 2020, day 23</a>. This is a puzzle where we move cups around in a
 * circle per some rules, and need to prove we have the final layout of the cups using some math. Parts one and two vary
 * in the number of cups and the number of rounds in the puzzle.
 * </p>
 * <p>
 * The rules are fairly simple, and there are several ways to go about this. At first glance this appears like a good
 * solution for a circular linked list, and this did work well for part one. However, part two taxes that solution
 * brutally. Navigating around in circles takes a lot of time. For this reason I moved to a simple array of integers in
 * a singly-linked list that references itself circularly. I also removed all list iterations, since with the array we
 * know precisely where each element is stored. This means all accesses are direct, making this program complete in a
 * timely manner. The linked list implementation I had previously tried ran for over a minute on part two until I killed
 * the program.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@johngaughan.net&gt;
 */
public class Year2020Day23 {

  private static final int ROUNDS_PART1 = 100;

  private static final int ROUNDS_PART2 = 10_000_000;

  private static final int CUPS_PART2 = 1_000_000;

  private static final int CUPS_MOVED = 3;

  public long calculatePart1() {
    State state = getInput(false);
    for (int i = 0; i < ROUNDS_PART1; ++i) {
      playOneRound(state);
    }
    return state.getPartOneAnswer();
  }

  public long calculatePart2() {
    State state = getInput(true);
    for (int i = 0; i < ROUNDS_PART2; ++i) {
      playOneRound(state);
    }
    return state.getPartTwoAnswer();
  }

  private void playOneRound(final State state) {
    // Remove the cups.
    int[] removed = new int[CUPS_MOVED];
    for (int i = 0; i < removed.length; ++i) {
      removed[i] = state.removeNextCup();
    }

    // Get the value of the destination cup, adjusting if necessary.
    int destination = state.currentValue();
    do {
      --destination;
      if (destination < 0) {
        destination = state.maxCups() - 1;
      }
    } while (contains(removed, destination));

    // Place the new cups so they are immediately after the destination.
    state.insert(destination, removed);

    // Advance the current cup to be one after what it is now.
    state.incrementCurrent();
  }

  private boolean contains(final int[] haystack, final int needle) {
    for (int i : haystack) {
      if (i == needle) {
        return true;
      }
    }
    return false;
  }

  /** Get the input data for this solution. */
  private State getInput(final boolean partTwo) {
    return new State(new int[] { 6, 2, 4, 3, 9, 7, 1, 5, 8 }, partTwo);
  }

  private static final class State {

    private int current = -1;

    /** Cup number is one greater than the array index. Value is next node. */
    private final int[] nodes;

    State(final int[] input, final boolean partTwo) {
      // Allocate space for the nodes
      nodes = new int[partTwo ? CUPS_PART2 : input.length];

      // Set the current node first
      current = input[0] - 1;

      // Work forwards, adding each node.
      for (int i = 0; i < input.length - 1; ++i) {
        nodes[input[i] - 1] = input[i + 1] - 1;
      }
      if (partTwo) {
        nodes[input[input.length - 1] - 1] = input.length;
        for (int i = input.length; i < nodes.length - 1; ++i) {
          nodes[i] = i + 1;
        }
        nodes[nodes.length - 1] = current;
      }
      else {
        nodes[input[input.length - 1] - 1] = current;
      }
    }

    int maxCups() {
      return nodes.length;
    }

    /** Remove the cup after the current cup, returning its value. */
    int removeNextCup() {
      final int removed = nodes[current];
      nodes[current] = nodes[removed];
      return removed;
    }

    /** Get the value of the current cup. */
    int currentValue() {
      return current;
    }

    /** Increment the current cup to be one after its existing value. */
    void incrementCurrent() {
      current = nodes[current];
    }

    /** Insert the provided values after the node with the given value. */
    void insert(final int destination, final int... values) {
      int insertionNode = destination;

      // For each value, add a node after the insertion node and increment the insertion node.
      for (int value : values) {
        nodes[value] = nodes[insertionNode];
        nodes[insertionNode] = value;
        insertionNode = value;
      }
    }

    /** Get the state of the cups in the format required to answer the puzzle for part one. */
    long getPartOneAnswer() {
      int n = nodes[0];
      long answer = 0;
      do {
        answer *= 10;
        answer += n + 1;
        n = nodes[n];
      } while (n != 0);
      return answer;
    }

    /** Get the state of the cups in the format required to answer the puzzle for part two. */
    long getPartTwoAnswer() {
      long v1 = nodes[0] + 1;
      long v2 = nodes[nodes[0]] + 1;
      return v1 * v2;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
      return current + ": " + Arrays.toString(nodes);
    }
  }

}
