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
package net.johngaughan.advent_of_code.y2015;

import java.nio.file.Files;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

import net.johngaughan.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2015/day/21">Year 2015, day 21</a>. This problem describes a very simple RPG, but
 * is really a problem about determining which combinations of items have better attributes. Part one wants to minimize
 * gold spent, while part two wants to maximize it.
 * </p>
 * <p>
 * Brute force is not the ideal solution, but with the tiny input sizes is very well within the realm of feasibility.
 * This is a brute force algorithm. Two notes: the simulation works the same but uses functions to determine which
 * outcome is better. First, a function that compares gold spent to the current best gold spent and determines which is
 * better: second, a function that translates the player winning into a successful or unsuccessful outcome. This way the
 * same algorithm works for both parts, by delegating key decisions to the calling method.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@johngaughan.net&gt;
 */
public final class Year2015Day21 {

  public int calculatePart1() {
    return runSimulation((a, b) -> Math.min(a, b), s -> s);
  }

  public int calculatePart2() {
    return runSimulation((a, b) -> Math.max(a, b), s -> !s);
  }

  /**
   * Run the simulation, using the provided functions to determine which gold value is more desirable and whether the
   * fight was successful.
   */
  private int runSimulation(final IntegerBiFunction f, final BooleanBiFunction success) {
    // Keep the original boss around so we don't need to keep parsing strings.
    final Actor bossOriginal = getInput();
    int bestGoldAmount = Integer.MIN_VALUE;
    // I don't like four nested loops here, but the inputs are small.
    for (final Item weapon : WEAPONS) {
      for (final Item armor : ARMOR) {
        for (final Item ring1 : RINGS) {
          for (final Item ring2 : RINGS) {
            // Cannot duplicate rings, but no rings is fine.
            if (!"None".equals(ring1.name) && ring1.equals(ring2)) {
              continue;
            }
            final Actor boss = new Actor(bossOriginal);
            final Actor player = new Actor(weapon, armor, ring1, ring2);
            if (success.apply(fight(boss, player))) {
              final int goldSpent = weapon.cost + armor.cost + ring1.cost + ring2.cost;
              if (bestGoldAmount < 0) {
                bestGoldAmount = goldSpent;
              }
              bestGoldAmount = f.apply(bestGoldAmount, goldSpent);
            }
          }
        }
      }
    }
    return bestGoldAmount;
  }

  /** Let the two actors fight, and return true if the player wins. */
  private boolean fight(final Actor boss, final Actor player) {
    final int bossDamage = Math.max(1, boss.damage - player.armor);
    final int playerDamage = Math.max(1, player.damage - boss.armor);

    // Don't actually need to fight, just figure out how many turns each actor will live.
    int turnsUntilBossDies = boss.hp / playerDamage;
    if (boss.hp % playerDamage > 0) {
      ++turnsUntilBossDies;
    }
    int turnsUntilPlayerDies = player.hp / bossDamage;
    if (player.hp % bossDamage > 0) {
      ++turnsUntilPlayerDies;
    }

    // Player has the tie breaker.
    return turnsUntilPlayerDies >= turnsUntilBossDies;
  }

  /** Get the input data for this solution. */
  private Actor getInput() {
    try {
      return new Actor(Files.readString(Utils.getInput(2015, 21)));
    }
    catch (final RuntimeException ex) {
      throw ex;
    }
    catch (final Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  /** Represents one actor, either the player or a boss. */
  private static final class Actor {

    private static final Pattern SPLIT_STRING = Pattern.compile("\n");

    private static final Pattern SPLIT_LINE = Pattern.compile(": ");

    int hp = 0;

    int damage = 0;

    int armor = 0;

    Actor(final Item... items) {
      hp = 100;
      for (final Item item : items) {
        damage += item.damage;
        armor += item.armor;
      }
    }

    Actor(final String input) {
      for (final String line : SPLIT_STRING.split(input)) {
        final String[] tokens = SPLIT_LINE.split(line);
        if ("Hit Points".equals(tokens[0])) {
          hp = Integer.parseInt(tokens[1]);
        }
        if ("Damage".equals(tokens[0])) {
          damage = Integer.parseInt(tokens[1]);
        }
        if ("Armor".equals(tokens[0])) {
          armor = Integer.parseInt(tokens[1]);
        }
      }
    }

    Actor(final Actor original) {
      // Object.clone() is garbage
      hp = original.hp;
      damage = original.damage;
      armor = original.armor;
    }

  }

  private static final Set<Item> WEAPONS = new HashSet<>();

  private static final Set<Item> ARMOR = new HashSet<>();

  private static final Set<Item> RINGS = new HashSet<>();

  static {
    WEAPONS.add(new Item("Dagger", 8, 4, 0));
    WEAPONS.add(new Item("Shortsword", 10, 5, 0));
    WEAPONS.add(new Item("Warhammer", 25, 6, 0));
    WEAPONS.add(new Item("Longsword", 40, 7, 0));
    WEAPONS.add(new Item("Greataxe", 74, 8, 0));

    ARMOR.add(new Item("None", 0, 0, 0));
    ARMOR.add(new Item("Leather", 13, 0, 1));
    ARMOR.add(new Item("Chainmail", 31, 0, 2));
    ARMOR.add(new Item("Splintmail", 53, 0, 3));
    ARMOR.add(new Item("Bandedmail", 75, 0, 4));
    ARMOR.add(new Item("Platemail", 102, 0, 5));

    RINGS.add(new Item("None", 0, 0, 0));
    RINGS.add(new Item("Damage +1", 25, 1, 0));
    RINGS.add(new Item("Damage +2", 50, 2, 0));
    RINGS.add(new Item("Damage +3", 100, 3, 0));
    RINGS.add(new Item("Defense +1", 20, 0, 1));
    RINGS.add(new Item("Defense +2", 40, 0, 2));
    RINGS.add(new Item("Defense +3", 80, 0, 3));
  }

  /** Represents one item the player can equip. */
  private static final class Item {

    final String name;

    final int cost;

    final int damage;

    final int armor;

    private final int hash;

    Item(final String n, final int c, final int d, final int a) {
      name = n;
      cost = c;
      damage = d;
      armor = a;
      hash = Objects.hash(name, cost, damage, armor);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object obj) {
      if (this == obj) {
        return true;
      }
      else if (!(obj instanceof Item)) {
        return false;
      }
      else {
        // Name is the identity state here, and the only duplicate is "None" - but those would be identical anyway even
        // if this compared all object variables, and are stored in different collections.
        return Objects.equals(name, ((Item) obj).name);
      }
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
      return hash;
    }

    @Override
    public String toString() {
      return "[" + name + ",cost=" + cost + ",damage=" + damage + ",armor=" + armor + "]";
    }
  }

  /** Functional interface that transforms two primitive integers into a single primitive integer. */
  @FunctionalInterface
  private static interface IntegerBiFunction {

    int apply(int a, int b);
  }

  /** Functional interface that transforms one primitive boolean into another. */
  @FunctionalInterface
  private static interface BooleanBiFunction {

    boolean apply(boolean b);
  }
}
