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

import java.awt.GridLayout;
import java.util.Objects;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

/**
 * Visualization result that uses a simple string displayed without any interpretation.
 */
public final class StringVisualizationResult
extends AbstractVisualizationResult {

  private final String result;

  public StringVisualizationResult(final String _description, final String _result) {
    super(_description);
    result = Objects.requireNonNull(_result);
  }

  @Override
  public JComponent buildComponent() {
    final JTextArea text = new JTextArea(result);
    text.setFont(getMonospaceFont());
    text.setEditable(false);

    final JScrollPane scroll = new JScrollPane(text);
    scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

    final JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(1, 1));
    panel.add(scroll);

    return panel;
  }

  @Override
  public boolean equals(final Object obj) {
    if (super.equals(obj)) {
      return result.equals(((StringVisualizationResult) obj).result);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(Integer.valueOf(super.hashCode()), result);
  }

}
