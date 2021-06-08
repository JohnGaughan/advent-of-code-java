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

import java.util.Arrays;

import us.coffeecode.advent_of_code.Utils;
import us.coffeecode.advent_of_code.y2016.assembunny.Interpreter;
import us.coffeecode.advent_of_code.y2016.assembunny.State;

/**
 * <p>
 * <a href="https://adventofcode.com/2016/day/25">Year 2016, day 25</a>. The final challenge for this year further
 * modifies Assembunny and asks us to find an input that produces a repeating output.
 * </p>
 * <p>
 * This was fairly simple. I added the output logic which just stores the generated outputs in the state. Once there is
 * an arbitrary amount of output, compare it against the expected output. While the problem says the output must repeat
 * infinitely, this is tricky to prove. At first I looked for a cycle where once we got to the second zero, the program
 * state was identical to last time. That did not work as expected. Instead, I moved to capturing ten array elements and
 * seeing if it repeats over that interval. This worked as expected.
 * </p>
 * <p>
 * Copyright (c) 2021 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2016Day25 {

  public long calculate() {
    final int[] expected = new int[State.OUPUT_BUFFER_SIZE];
    for (int i = 0; i < expected.length; ++i) {
      expected[i] = i % 2;
    }
    for (int i = 1; i < Integer.MAX_VALUE; ++i) {
      final State state = State.load(Utils.getInput(2016, 25));
      state.reg[0] = i;
      new Interpreter().execute(state);
      if (Arrays.compare(expected, state.out) == 0) {
        return i;
      }
    }
    return -1;
  }

}
