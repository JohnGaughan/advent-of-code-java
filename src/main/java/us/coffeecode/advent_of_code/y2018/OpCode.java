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
package us.coffeecode.advent_of_code.y2018;

/**
 * OpCodes used in multiple days' puzzles. These model instructions in a fictional CPU.
 */
enum OpCode {

  addr() {

    @Override
    public void apply(final long[] registers, final long a, final long b, final long c) {
      registers[(int) c] = registers[(int) a] + registers[(int) b];
    }
  },

  addi() {

    @Override
    public void apply(final long[] registers, final long a, final long b, final long c) {
      registers[(int) c] = registers[(int) a] + b;
    }

  },

  mulr() {

    @Override
    public void apply(final long[] registers, final long a, final long b, final long c) {
      registers[(int) c] = registers[(int) a] * registers[(int) b];
    }
  },

  muli() {

    @Override
    public void apply(final long[] registers, final long a, final long b, final long c) {
      registers[(int) c] = registers[(int) a] * b;
    }
  },

  banr() {

    @Override
    public void apply(final long[] registers, final long a, final long b, final long c) {
      registers[(int) c] = registers[(int) a] & registers[(int) b];
    }
  },

  bani() {

    @Override
    public void apply(final long[] registers, final long a, final long b, final long c) {
      registers[(int) c] = registers[(int) a] & b;
    }
  },

  borr() {

    @Override
    public void apply(final long[] registers, final long a, final long b, final long c) {
      registers[(int) c] = registers[(int) a] | registers[(int) b];
    }
  },

  bori() {

    @Override
    public void apply(final long[] registers, final long a, final long b, final long c) {
      registers[(int) c] = registers[(int) a] | b;
    }
  },

  setr() {

    @Override
    public void apply(final long[] registers, final long a, final long b, final long c) {
      registers[(int) c] = registers[(int) a];
    }
  },

  seti() {

    @Override
    public void apply(final long[] registers, final long a, final long b, final long c) {
      registers[(int) c] = a;
    }
  },

  gtir() {

    @Override
    public void apply(final long[] registers, final long a, final long b, final long c) {
      registers[(int) c] = a > registers[(int) b] ? 1 : 0;
    }
  },

  gtri() {

    @Override
    public void apply(final long[] registers, final long a, final long b, final long c) {
      registers[(int) c] = registers[(int) a] > b ? 1 : 0;
    }
  },

  gtrr() {

    @Override
    public void apply(final long[] registers, final long a, final long b, final long c) {
      registers[(int) c] = registers[(int) a] > registers[(int) b] ? 1 : 0;
    }
  },

  eqir() {

    @Override
    public void apply(final long[] registers, final long a, final long b, final long c) {
      registers[(int) c] = a == registers[(int) b] ? 1 : 0;
    }
  },

  eqri() {

    @Override
    public void apply(final long[] registers, final long a, final long b, final long c) {
      registers[(int) c] = registers[(int) a] == b ? 1 : 0;
    }
  },

  eqrr() {

    @Override
    public void apply(final long[] registers, final long a, final long b, final long c) {
      registers[(int) c] = registers[(int) a] == registers[(int) b] ? 1 : 0;
    }
  };

  /** Apply the action of this opcode against the provided registers using the provided parameters. */
  public abstract void apply(final long[] registers, final long a, final long b, final long c);
}
