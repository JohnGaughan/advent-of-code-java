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
package net.johngaughan.advent.y2015.day6;

/**
 * <p>
 * Represents one discrete action to perform on the lights.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@johngaughan.net&gt;
 */
final class Action {

  final Command command;

  final int x1;

  final int x2;

  final int y1;

  final int y2;

  /** Constructs a <code>Action</code>. */
  Action(final Command _command, final int _x1, final int _x2, final int _y1, final int _y2) {
    this.command = _command;
    this.x1 = _x1;
    this.x2 = _x2;
    this.y1 = _y1;
    this.y2 = _y2;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return "[" + this.command + ",(" + this.x1 + "," + this.x2 + "),(" + this.y1 + "," + this.y2 + ")]";
  }

}
