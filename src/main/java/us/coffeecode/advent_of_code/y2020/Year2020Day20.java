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
package us.coffeecode.advent_of_code.y2020;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2020, day = 20)
@Component
public class Year2020Day20 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final Element[][] grid = getElements(pc);
    final long a = grid[0][0].item.getId();
    final long b = grid[grid.length - 1][0].item.getId();
    final long c = grid[0][grid[0].length - 1].item.getId();
    final long d = grid[grid.length - 1][grid[0].length - 1].item.getId();
    return a * b * c * d;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final Grid grid = new Grid(getElements(pc));
    final boolean[][] monster = getMonster();

    long monsterHashes = 0;
    for (final boolean[] element : monster) {
      for (int y = 0; y < element.length; ++y) {
        if (element[y]) {
          ++monsterHashes;
        }
      }
    }

    long hashes = 0;
    for (int x = 0; x < grid.size(); ++x) {
      for (int y = 0; y < grid.size(); ++y) {
        if (grid.get(x, y)) {
          ++hashes;
        }
      }
    }

    for (final Transformation tran : Transformation.transformations) {
      final Element e = new Element(grid, tran);
      final long seaMonsters = countSeaMonsters(e, monster);
      if (seaMonsters > 0) {
        return hashes - seaMonsters * monsterHashes;
      }
    }
    return 0;
  }

  private boolean[][] getMonster() {
    final String[] raw = new String[] { "                  # ", "#    ##    ##    ###", " #  #  #  #  #  #   " };
    final boolean[][] monster = new boolean[raw[0].length()][raw.length];
    for (int x = 0; x < monster.length; ++x) {
      for (int y = 0; y < monster[x].length; ++y) {
        monster[x][y] = raw[y].codePointAt(x) == '#';
      }
    }
    return monster;
  }

  private long countSeaMonsters(final Element e, final boolean[][] monster) {
    final int size = e.getItem()
                      .size();
    final int max_x = size - monster.length;
    final int max_y = size - monster[0].length;
    long monsters = 0;
    for (int x = 0; x < max_x; ++x) {
      for (int y = 0; y < max_y; ++y) {
        if (isMonster(e, monster, x, y)) {
          ++monsters;
        }
      }
    }
    return monsters;
  }

  private boolean isMonster(final Element e, final boolean[][] monster, final int x0, final int y0) {
    for (int x = 0; x < monster.length; ++x) {
      for (int y = 0; y < monster[x].length; ++y) {
        if (monster[x][y] && !e.get(x0 + x, y0 + y)) {
          return false;
        }
      }
    }
    return true;
  }

  /** Get the raw element data, compiled into a grid. */
  private Element[][] getElements(final PuzzleContext pc) {
    final List<Tile> tiles = new ArrayList<>(il.groupsAsObjects(pc, Tile::new));

    final int d = (int) Math.sqrt(tiles.size());
    if (d * d != tiles.size()) {
      throw new IllegalArgumentException("Square dimension " + d + " is not valid for number of tiles: " + tiles.size());
    }

    // Grab the first tile and add it to a temporary row. First element is not transformed at all.
    final List<Element> row = new ArrayList<>(d);
    row.add(new Element(tiles.getFirst(), Transformation.none()));
    tiles.remove(0);

    // Add tiles to the right until none fit.
    while (true) {
      boolean found = false;
      final Element left = row.getLast();
      // Iterate over every tile, and try to append it to the right.
      for (final Iterator<Tile> iter = tiles.iterator(); iter.hasNext();) {
        final Tile candidate = iter.next();
        // Try every transformation
        for (final Transformation tran : Transformation.values()) {
          final Element right = new Element(candidate, tran);
          if (left.compatible(right, Direction.RIGHT)) {
            // Found the next tile! Add it and break out.
            row.add(right);
            found = true;
            iter.remove();
            break;
          }
        }
        if (found) {
          break;
        }
      }
      // No tiles found: we have gone as far to the right as possible.
      if (!found) {
        break;
      }
    }

    // Now do the same, to the left.
    while (true) {
      boolean found = false;
      final Element right = row.getFirst();
      // Iterate over every tile, and try to append it to the right.
      for (final Iterator<Tile> iter = tiles.iterator(); iter.hasNext();) {
        final Tile candidate = iter.next();
        // Try every transformation
        for (final Transformation tran : Transformation.values()) {
          final Element left = new Element(candidate, tran);
          if (left.compatible(right, Direction.RIGHT)) {
            // Found the next tile! Add it and break out.
            row.add(0, left);
            found = true;
            iter.remove();
            break;
          }
        }
        if (found) {
          break;
        }
      }
      // No tiles found: we have gone as far to the left as possible.
      if (!found) {
        break;
      }
    }

    // Construct a temporary column
    final List<Element> col = new ArrayList<>(d);
    col.add(row.getFirst());

    // We have the first row. Starting on the left, go up.
    while (true) {
      boolean found = false;
      final Element down = col.getFirst();
      // Iterate over every tile, and try to append it to the right.
      for (final Iterator<Tile> iter = tiles.iterator(); iter.hasNext();) {
        final Tile candidate = iter.next();
        // Try every transformation
        for (final Transformation tran : Transformation.values()) {
          final Element up = new Element(candidate, tran);
          if (down.compatible(up, Direction.UP)) {
            // Found the next tile! Add it and break out.
            col.add(0, up);
            found = true;
            iter.remove();
            break;
          }
        }
        if (found) {
          break;
        }
      }
      // No tiles found: we have gone as far to the right as possible.
      if (!found) {
        break;
      }
    }

    // Now go down.
    while (true) {
      boolean found = false;
      final Element up = col.getLast();
      // Iterate over every tile, and try to append it to the right.
      for (final Iterator<Tile> iter = tiles.iterator(); iter.hasNext();) {
        final Tile candidate = iter.next();
        // Try every transformation
        for (final Transformation tran : Transformation.values()) {
          final Element down = new Element(candidate, tran);
          if (down.compatible(up, Direction.UP)) {
            // Found the next tile! Add it and break out.
            col.add(down);
            found = true;
            iter.remove();
            break;
          }
        }
        if (found) {
          break;
        }
      }
      // No tiles found: we have gone as far to the right as possible.
      if (!found) {
        break;
      }
    }

    // We have enough to construct a grid. The first column is completely filled in, and one row is. Add them all.
    final Element[][] grid = new Element[d][d];
    final HasCoordinates anchor = row.getFirst()
                                     .getItem();
    for (int y = 0; y < grid[0].length; ++y) {
      grid[0][y] = col.get(y);
      if (anchor == grid[0][y].item) {
        for (int x = 1; x < grid.length; ++x) {
          grid[x][y] = row.get(x);
        }
      }
    }

    // Now fill in the rest of the tiles.
    for (int x = 1; x < grid.length; ++x) {
      for (int y = 0; y < grid[x].length; ++y) {
        for (final Iterator<Tile> iter = tiles.iterator(); iter.hasNext() && grid[x][y] == null;) {
          final Tile candidate = iter.next();
          for (final Transformation tran : Transformation.values()) {
            final Element right = new Element(candidate, tran);
            if (grid[x - 1][y].compatible(right, Direction.RIGHT)) {
              grid[x][y] = right;
              iter.remove();
              break;
            }
          }
        }
      }
    }

    return grid;
  }

  private static enum Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT;
  }

  private static final class Element {

    private final HasCoordinates item;

    private final Transformation tran;

    Element(final HasCoordinates i, final Transformation tr) {
      item = i;
      tran = tr;
    }

    HasCoordinates getItem() {
      return item;
    }

    /** Get the value at the specified coordinate, after transforming coordinates if necessary. */
    boolean get(final int x, final int y) {
      return tran.get(item, x, y);
    }

    /** Determines if the given candidate element is compatible with this one in the given direction. */
    boolean compatible(final Element candidate, final Direction dir) {
      if (dir == Direction.DOWN) {
        return candidate.compatible(this, Direction.UP);
      }
      else if (dir == Direction.LEFT) {
        return candidate.compatible(this, Direction.RIGHT);
      }
      else if (dir == Direction.UP) {
        final int size = item.size();
        for (int x = 0; x < size; ++x) {
          final boolean top = get(x, 0);
          final boolean bottom = candidate.get(x, size - 1);
          if (top != bottom) {
            return false;
          }
        }
        return true;
      }
      else if (dir == Direction.RIGHT) {
        final int size = item.size();
        for (int y = 0; y < size; ++y) {
          final boolean left = get(size - 1, y);
          final boolean right = candidate.get(0, y);
          if (left != right) {
            return false;
          }
        }
        return true;
      }
      throw new IllegalArgumentException(String.valueOf(dir));
    }

    @Override
    public String toString() {
      final StringBuilder str = new StringBuilder(9216);
      for (int x = 0; x < item.size(); ++x) {
        for (int y = 0; y < item.size(); ++y) {
          str.append(item.get(x, y) ? '#' : ' ');
        }
        str.append('\n');
      }
      return str.toString();
    }

  }

  /** Represents all of the transformations that can be applied to tiles. */
  private static final class Transformation {

    private static final Set<Transformation> transformations;

    private static Transformation NONE;

    static {
      final Set<Transformation> temp = new HashSet<>();
      for (final Rotation r : Rotation.values()) {
        for (final Flip f : Flip.values()) {
          final Transformation t = new Transformation(f, r);
          temp.add(t);
          if (Rotation.NONE == r && Flip.NONE == f) {
            NONE = t;
          }
        }
      }
      transformations = Collections.unmodifiableSet(temp);
    }

    static Transformation none() {
      return NONE;
    }

    static Collection<Transformation> values() {
      return transformations;
    }

    private final Flip flip;

    private final Rotation rot;

    /** Constructs a <code>Transformation</code>. */
    private Transformation(final Flip f, final Rotation r) {
      flip = f;
      rot = r;
    }

    boolean get(final HasCoordinates grid, final int x, final int y) {
      final int x1 = rot.transformX(x, y, grid.size());
      final int y1 = rot.transformY(x, y, grid.size());
      final int x2 = flip.transformX(x1, y1, grid.size());
      final int y2 = flip.transformY(x1, y1, grid.size());
      return grid.get(x2, y2);
    }

    @Override
    public boolean equals(final Object obj) {
      if (this == obj) {
        return true;
      }
      else if (obj instanceof Transformation o) {
        return flip == o.flip && rot == o.rot;
      }
      return false;
    }

    @Override
    public int hashCode() {
      return Objects.hash(rot, flip);
    }
  }

  /** Represents a single rotation of a tile. */
  private static enum Rotation {

    NONE {

      @Override
      public int transformX(final int x, final int y, final int size) {
        return x;
      }

      @Override
      public int transformY(final int x, final int y, final int size) {
        return y;
      }
    },
    R90 {

      @Override
      public int transformX(final int x, final int y, final int size) {
        return y;
      }

      @Override
      public int transformY(final int x, final int y, final int size) {
        return size - x - 1;
      }
    },
    R180 {

      @Override
      public int transformX(final int x, final int y, final int size) {
        return size - x - 1;
      }

      @Override
      public int transformY(final int x, final int y, final int size) {
        return size - y - 1;
      }
    },
    R270 {

      @Override
      public int transformX(final int x, final int y, final int size) {
        return size - y - 1;
      }

      @Override
      public int transformY(final int x, final int y, final int size) {
        return x;
      }
    };

    public abstract int transformX(int x, int y, int size);

    public abstract int transformY(int x, int y, int size);
  }

  /** Represents a single flip of a tile. */
  private static enum Flip {

    NONE {

      @Override
      public int transformX(final int x, final int y, final int size) {
        return x;
      }

      @Override
      public int transformY(final int x, final int y, final int size) {
        return y;
      }
    },
    X {

      @Override
      public int transformX(final int x, final int y, final int size) {
        return x;
      }

      @Override
      public int transformY(final int x, final int y, final int size) {
        return size - y - 1;
      }
    },
    Y {

      @Override
      public int transformX(final int x, final int y, final int size) {
        return size - x - 1;
      }

      @Override
      public int transformY(final int x, final int y, final int size) {
        return y;
      }
    },
    XY {

      @Override
      public int transformX(final int x, final int y, final int size) {
        return size - x - 1;
      }

      @Override
      public int transformY(final int x, final int y, final int size) {
        return size - y - 1;
      }
    };

    public abstract int transformX(int x, int y, int size);

    public abstract int transformY(int x, int y, int size);
  }

  private interface HasCoordinates {

    /** Get the grid element at the provided coordinates. */
    boolean get(final int x, final int y);

    /** Get the size of any edge of this object. */
    int size();

    long getId();
  }

  private static final class Grid
  implements HasCoordinates {

    private final boolean[][] grid;

    /** Constructs a <code>Grid</code>. */
    Grid(final Element[][] source) {
      final int eleSize = source[0][0].item.size() - 2;
      final int eleCount = source.length;
      // Combine all of the elements, but omit their outermost edges.
      final int d = eleCount * eleSize;
      grid = new boolean[d][d];
      for (int x = 0; x < grid.length; ++x) {
        for (int y = 0; y < grid[x].length; ++y) {
          final int xNumber = x / eleSize;
          final int xOffset = 1 + x % eleSize;
          final int yNumber = y / eleSize;
          final int yOffset = 1 + y % eleSize;
          grid[x][y] = source[xNumber][yNumber].get(xOffset, yOffset);
        }
      }
    }

    @Override
    public boolean get(final int x, final int y) {
      return grid[x][y];
    }

    @Override
    public int size() {
      return grid.length;
    }

    @Override
    public long getId() {
      return 0;
    }

  }

  private static final class Tile
  implements HasCoordinates {

    final long id;

    final boolean[][] grid;

    Tile(final List<String> input) {
      grid = new boolean[input.size() - 1][input.size() - 1];
      final String first = input.getFirst();
      id = Long.parseLong(first.substring(5, first.length() - 1));
      for (int i = 0; i < grid.length; ++i) {
        final String inputRow = input.get(i + 1);
        for (int j = 0; j < grid.length; ++j) {
          final int ch = inputRow.codePointAt(j);
          grid[i][j] = ch == '#';
        }
      }
    }

    @Override
    public boolean get(final int x, final int y) {
      return grid[x][y];
    }

    @Override
    public String toString() {
      final StringBuilder str = new StringBuilder();
      str.append(id)
         .append("=");
      for (final boolean[] row : grid) {
        str.append("\n");
        for (final boolean cell : row) {
          str.append(cell ? '#' : '.');
        }
      }
      return str.toString();
    }

    @Override
    public int size() {
      return grid.length;
    }

    @Override
    public long getId() {
      return id;
    }
  }

}
