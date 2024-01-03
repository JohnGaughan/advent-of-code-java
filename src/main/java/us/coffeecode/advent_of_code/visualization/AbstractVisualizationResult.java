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

import java.awt.Font;
import java.util.List;
import java.util.Objects;

/**
 * Base class for visualization results.
 */
public abstract class AbstractVisualizationResult
implements IVisualizationResult {

  private static final List<String> MONOSPACE_FONT_NAMES = List.of("Courier New", "FreeMono Regular");

  private Font monospaceFont = null;

  private final String description;

  protected AbstractVisualizationResult(final String _description) {
    description = Objects.requireNonNull(_description);
    for (final String fontName : MONOSPACE_FONT_NAMES) {
      monospaceFont = new Font(fontName, Font.PLAIN, 12);
      if (fontName.equals(monospaceFont.getFontName())) {
        // This is true only if the font exists on the system. Effectively, this sets up a font priority so multiple
        // operating systems with their own default fonts should work well enough.
        break;
      }
    }
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    else if ((obj != null) && getClass().equals(obj.getClass())) {
      final AbstractVisualizationResult o = (AbstractVisualizationResult) obj;
      return description.equals(o.description);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(getClass().getName(), description);
  }

  @Override
  public String getDescription() {
    return description;
  }

  /** Get a font that is monospaced. */
  protected Font getMonospaceFont() {
    return monospaceFont;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "[description=" + description + "]";
  }

}
