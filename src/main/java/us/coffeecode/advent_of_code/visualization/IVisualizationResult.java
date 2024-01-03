/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2023  John Gaughan
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
package us.coffeecode.advent_of_code.visualization;

import javax.swing.JComponent;

/**
 * Root interface for objects that contain visualization results. They have a description which goes in the title area
 * of a tab or frame, and results which are rendered in the main area of a panel or frame.
 */
public interface IVisualizationResult {

  /** Get the description of the results. */
  String getDescription();

  /** Create a component that displays the visualization for this result. */
  JComponent buildComponent();
}
