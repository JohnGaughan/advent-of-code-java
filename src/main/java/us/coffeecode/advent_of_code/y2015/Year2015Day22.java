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
package us.coffeecode.advent_of_code.y2015;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2015, day = 22)
@Component
public final class Year2015Day22 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return runSimulation(new GameState(il.lines(pc)), Long.MAX_VALUE);
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return runSimulation(new GameState(il.lines(pc)).hardmode(), Long.MAX_VALUE);
  }

  private long runSimulation(final GameState state, final long best) {
    ++state.turn;

    /*
     * If this is hardmode, reduce player health prior to calculating start of turn effects but only on the player turn.
     * The timing matters here because the boss can die when calculating effects, which would end the game in a win.
     */
    if (state.hardmode && (state.turn % 2 != 0)) {
      --state.playerHp;
      if (state.playerHp < 1) {
        // Player loses
        return best;
      }
    }

    // Calculate start of turn effects.
    if (state.poisonTurns > 0) {
      --state.poisonTurns;
      state.bossHp -= Spell.POISON.damage;
      if (state.bossHp < 1) {
        // Player wins
        return Math.min(state.manaSpent, best);
      }
    }
    if (state.rechargeTurns > 0) {
      --state.rechargeTurns;
      state.playerMana += Spell.RECHARGE.manaRestore;
    }
    if (state.shieldTurns > 0) {
      --state.shieldTurns;
      if (state.shieldTurns == 0) {
        state.playerArmor = 0;
      }
    }

    // Do the boss's turn if applicable.
    if (state.turn % 2 == 0) {
      // Boss attacks player. Works the same no matter what.
      final int damage = Math.max(1, state.bossDamage - state.playerArmor);
      state.playerHp -= damage;
      if (state.playerHp < 1) {
        // Player loses
        return best;
      }
      return runSimulation(state, best);
    }

    // Otherwise, do the player's turn.
    else {
      /*
       * Player can cast one of five spells, but the player may not be able to cast certain ones, and different spells
       * have different effects. This is basically an unrolled loop that checks each spell as an option.
       */
      // Cannot afford to cast a spell: game over.
      if (state.playerMana < Spell.minimumManaCost()) {
        // Player loses
        return best;
      }

      long newBest = best;

      if (state.playerMana >= Spell.MAGIC_MISSLE.mana) {
        final GameState newState = new GameState(state);
        newState.bossHp -= Spell.MAGIC_MISSLE.damage;
        newState.playerMana -= Spell.MAGIC_MISSLE.mana;
        newState.manaSpent += Spell.MAGIC_MISSLE.mana;
        if (state.bossHp < 1) {
          // Player wins
          newBest = Math.min(state.manaSpent, newBest);
        }
        else if (newState.manaSpent < newBest) {
          newBest = Math.min(runSimulation(newState, newBest), newBest);
        }
      }

      if (state.playerMana >= Spell.DRAIN.mana) {
        final GameState newState = new GameState(state);
        newState.bossHp -= Spell.DRAIN.damage;
        newState.playerMana -= Spell.DRAIN.mana;
        newState.manaSpent += Spell.DRAIN.mana;
        newState.playerHp += Spell.DRAIN.healing;
        if (state.bossHp < 1) {
          // Player wins
          newBest = Math.min(state.manaSpent, newBest);
        }
        else if (newState.manaSpent < newBest) {
          newBest = Math.min(runSimulation(newState, newBest), newBest);
        }
      }

      if (state.playerMana >= Spell.SHIELD.mana && state.shieldTurns == 0) {
        final GameState newState = new GameState(state);
        newState.playerMana -= Spell.SHIELD.mana;
        newState.manaSpent += Spell.SHIELD.mana;
        newState.playerArmor = Spell.SHIELD.armor;
        newState.shieldTurns = Spell.SHIELD.turns;
        if (newState.manaSpent < newBest) {
          newBest = Math.min(runSimulation(newState, newBest), newBest);
        }
      }

      if (state.playerMana >= Spell.POISON.mana && state.poisonTurns == 0) {
        final GameState newState = new GameState(state);
        newState.playerMana -= Spell.POISON.mana;
        newState.manaSpent += Spell.POISON.mana;
        newState.poisonTurns = Spell.POISON.turns;
        if (newState.manaSpent < newBest) {
          newBest = Math.min(runSimulation(newState, newBest), newBest);
        }
      }

      if (state.playerMana >= Spell.RECHARGE.mana && state.rechargeTurns == 0) {
        final GameState newState = new GameState(state);
        newState.playerMana -= Spell.RECHARGE.mana;
        newState.manaSpent += Spell.RECHARGE.mana;
        newState.rechargeTurns = Spell.RECHARGE.turns;
        if (newState.manaSpent < newBest) {
          newBest = Math.min(runSimulation(newState, newBest), newBest);
        }
      }

      return newBest;
    }
  }

  private static final class GameState {

    private static final Pattern SPLIT = Pattern.compile(": ");

    int bossHp;

    int bossDamage;

    int playerHp = 50;

    int playerMana = 500;

    int playerArmor = 0;

    int shieldTurns = 0;

    int poisonTurns = 0;

    int rechargeTurns = 0;

    int manaSpent = 0;

    int turn = 0;

    boolean hardmode = false;

    /** Construct from program input, using default values where not specified. */
    GameState(final Iterable<String> lines) {
      for (final String line : lines) {
        final String[] tokens = SPLIT.split(line);
        if (tokens[0].startsWith("H")) {
          bossHp = Integer.parseInt(tokens[1]);
        }
        else if (tokens[0].startsWith("D")) {
          bossDamage = Integer.parseInt(tokens[1]);
        }
      }
    }

    /** Copy constructor. */
    GameState(final GameState other) {
      bossHp = other.bossHp;
      bossDamage = other.bossDamage;
      playerHp = other.playerHp;
      playerMana = other.playerMana;
      playerArmor = other.playerArmor;
      shieldTurns = other.shieldTurns;
      poisonTurns = other.poisonTurns;
      rechargeTurns = other.rechargeTurns;
      manaSpent = other.manaSpent;
      turn = other.turn;
      hardmode = other.hardmode;
    }

    /** Turn on hard mode */
    GameState hardmode() {
      hardmode = true;
      return this;
    }
  }

  private static enum Spell {

    MAGIC_MISSLE(53, 4, 0, 0, 0, 0),
    DRAIN(73, 2, 0, 2, 0, 0),
    SHIELD(113, 0, 6, 0, 0, 7),
    POISON(173, 3, 6, 0, 0, 0),
    RECHARGE(229, 0, 5, 0, 101, 0);

    private static Integer minimumManaCost = null;

    /** Get the minimum mana cost of any spell. */
    static final int minimumManaCost() {
      if (minimumManaCost == null) {
        int min = Integer.MAX_VALUE;
        for (final Spell spell : values()) {
          min = Math.min(min, spell.mana);
        }
        minimumManaCost = Integer.valueOf(min);
      }
      return minimumManaCost.intValue();
    }

    final int mana;

    final int damage;

    final int turns;

    final int healing;

    final int manaRestore;

    final int armor;

    private Spell(final int _mana, final int _damage, final int _turns, final int _healing, final int _manaRestore, final int _armor) {
      mana = _mana;
      damage = _damage;
      turns = _turns;
      healing = _healing;
      manaRestore = _manaRestore;
      armor = _armor;
    }
  }
}
