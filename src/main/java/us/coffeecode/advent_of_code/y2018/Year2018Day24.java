/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2021  John Gaughan
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

import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2018, day = 24, title = "Immune System Simulator 20XX")
@Component
public final class Year2018Day24 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final Map<Force, Army> armies = getInput(pc);
    while (armies.get(Force.IMMUNE).hasUnits() && armies.get(Force.INFECTION).hasUnits()) {
      fight(armies);
    }
    for (final Army army : armies.values()) {
      if (army.hasUnits()) {
        return army.getTotalUnits();
      }
    }
    return 0;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final Map<Force, Army> armies = getInput(pc);
    long answer = 0;
    for (int boost = 1; (boost < 1_000_000) && (answer == 0); ++boost) {
      final Map<Force, Army> boostedArmies = new HashMap<>();
      boostedArmies.put(Force.IMMUNE, new Army(armies.get(Force.IMMUNE), boost));
      boostedArmies.put(Force.INFECTION, new Army(armies.get(Force.INFECTION)));

      int startingUnits = boostedArmies.values().stream().mapToInt(Army::getTotalUnits).sum();

      // Fight until one side loses or there is a stalemate
      while (boostedArmies.get(Force.IMMUNE).hasUnits() && boostedArmies.get(Force.INFECTION).hasUnits()) {
        fight(boostedArmies);
        final int endingUnits = boostedArmies.values().stream().mapToInt(Army::getTotalUnits).sum();
        if (startingUnits == endingUnits) {
          // Abort: this is a stalemate.
          break;
        }
        startingUnits = endingUnits;
      }

      // Success: immune won and infection lost. Both conditions are needed to reject stalemates.
      if (boostedArmies.get(Force.IMMUNE).hasUnits() && !boostedArmies.get(Force.INFECTION).hasUnits()) {
        answer = boostedArmies.get(Force.IMMUNE).getTotalUnits();
      }
    }
    return answer;
  }

  private void fight(final Map<Force, Army> armies) {
    final SortedMap<Group, Group> targets = selectTargets(armies);
    attack(targets);
  }

  private SortedMap<Group, Group> selectTargets(final Map<Force, Army> armies) {
    final List<Group> attackers = new ArrayList<>(20);
    attackers.addAll(armies.get(Force.IMMUNE).getActiveGroups());
    attackers.addAll(armies.get(Force.INFECTION).getActiveGroups());
    Collections.sort(attackers, (o1, o2) -> {
      int result = o2.getEffectivePower() - o1.getEffectivePower();
      if (result != 0) {
        return result;
      }
      return o2.initiative - o1.initiative;
    });

    final SortedMap<Group, Group> targets = new TreeMap<>((o1, o2) -> o2.initiative - o1.initiative);
    for (final Group attacker : attackers) {

      // Get the candidates for receiving the attack.
      final List<Group> candidates = new ArrayList<>();
      final Army enemy = armies.get(Force.enemy(attacker.force));

      // Candidates are all enemy groups that are not already a target.
      candidates.addAll(enemy.getActiveGroups());
      candidates.removeAll(targets.values());

      // Filter out targets this attacker cannot damage.
      for (final var iter = candidates.iterator(); iter.hasNext();) {
        final Group candidate = iter.next();
        if (candidate.getEffectiveDamage(attacker.getEffectivePower(), attacker.damageType) == 0) {
          iter.remove();
        }
      }

      // Only bother targeting if there is a group to target.
      if (!candidates.isEmpty()) {

        // Sort the candidates based on the selection criteria in the problem.
        Collections.sort(candidates, (o1, o2) -> {
          int result = o2.getEffectiveDamage(attacker.getEffectivePower(), attacker.damageType)
            - o1.getEffectiveDamage(attacker.getEffectivePower(), attacker.damageType);
          if (result != 0) {
            return result;
          }
          result = o2.getEffectivePower() - o1.getEffectivePower();
          if (result != 0) {
            return result;
          }
          return o2.initiative - o1.initiative;
        });

        // The first target is the highest priority per the problem.
        targets.put(attacker, candidates.getFirst());
      }
    }
    return targets;
  }

  private void attack(final Map<Group, Group> targets) {
    for (final var pair : targets.entrySet()) {
      final Group attacker = pair.getKey();

      // Only attack if the attacker is alive: someone else may have killed it already.
      if (attacker.hasUnits()) {
        final Group receiver = pair.getValue();
        receiver.dealDamage(attacker.getEffectivePower(), attacker.damageType);
      }
    }
  }

  /** Get the input data for this solution. */
  private Map<Force, Army> getInput(final PuzzleContext pc) {
    return il.groups(pc).stream().map(Army::new).collect(Collectors.toMap(e -> e.force, Function.identity()));
  }

  private static enum Force {

    IMMUNE,
    INFECTION;

    static final Force enemy(final Force ally) {
      if (ally == IMMUNE) {
        return INFECTION;
      }
      else if (ally == INFECTION) {
        return IMMUNE;
      }
      throw new IllegalArgumentException();
    }
  }

  private static enum DamageType {
    bludgeoning,
    cold,
    fire,
    radiation,
    slashing,
  }

  private static final class Army {

    private final Force force;

    private final List<Group> groups = new ArrayList<>();

    Army(final List<String> input) {
      force = input.getFirst().indexOf('m') >= 0 ? Force.IMMUNE : Force.INFECTION;
      input.stream().skip(1).forEach(s -> groups.add(new Group(s, force)));
    }

    Army(final Army other) {
      this(other, 0);
    }

    Army(final Army other, final int boost) {
      force = other.force;
      other.groups.stream().forEach(g -> groups.add(new Group(g, boost)));
    }

    int getTotalUnits() {
      return groups.stream().mapToInt(g -> g.units).sum();
    }

    Collection<Group> getActiveGroups() {
      return groups.stream().filter(Group::hasUnits).toList();
    }

    boolean hasUnits() {
      return groups.stream().anyMatch(Group::hasUnits);
    }

    @Override
    public String toString() {
      return "Army[force=" + force + ",groups=" + groups + "]";
    }
  }

  private static final class Group {

    private static final Pattern SPLIT = Pattern.compile(" ");

    private static final Pattern INNER_SPLIT = Pattern.compile("; ");

    private final Force force;

    private int units;

    private final int hp;

    private final Set<DamageType> immunities;

    private final Set<DamageType> weaknesses;

    private final int damage;

    private final DamageType damageType;

    private final int initiative;

    Group(final String input, final Force _force) {
      immunities = new HashSet<>();
      weaknesses = new HashSet<>();
      force = _force;
      final String[] tokens = SPLIT.split(input);
      String[] weakImmune = new String[0];
      {
        int parenStart = input.indexOf('(');
        int parenEnd = input.indexOf(')');
        if (parenStart > -1 && parenEnd > -1) {
          final String str = input.substring(parenStart + 1, parenEnd).replace("to ", "").replace(",", "");
          weakImmune = INNER_SPLIT.split(str);
        }
      }
      units = Integer.parseInt(tokens[0]);
      hp = Integer.parseInt(tokens[4]);
      damage = Integer.parseInt(tokens[tokens.length - 6]);
      damageType = DamageType.valueOf(tokens[tokens.length - 5]);
      initiative = Integer.parseInt(tokens[tokens.length - 1]);
      for (final String part : weakImmune) {
        final Set<DamageType> mod = part.startsWith("i") ? immunities : weaknesses;
        for (final String type : SPLIT.split(part.substring(part.indexOf(' ') + 1))) {
          mod.add(DamageType.valueOf(type));
        }
      }
    }

    Group(final Group original, final int boost) {
      force = original.force;
      units = original.units;
      hp = original.hp;
      immunities = original.immunities;
      weaknesses = original.weaknesses;
      damage = original.damage + boost;
      damageType = original.damageType;
      initiative = original.initiative;
    }

    int getEffectiveDamage(final int incomingDamage, final DamageType incomingDamageType) {
      if (immunities.contains(incomingDamageType)) {
        return 0;
      }
      else if (weaknesses.contains(incomingDamageType)) {
        return incomingDamage << 1;
      }
      return incomingDamage;
    }

    void dealDamage(final int incomingDamage, final DamageType incomingDamageType) {
      int effectiveDamage = getEffectiveDamage(incomingDamage, incomingDamageType);
      int unitsDestroyed = effectiveDamage / hp;
      units = Math.max(units - unitsDestroyed, 0);
    }

    int getEffectivePower() {
      return units * damage;
    }

    boolean hasUnits() {
      return units > 0;
    }

    @Override
    public String toString() {
      return "Group[force=" + force + ",units=" + units + ",hp=" + hp + ",damage=" + damage + ",damageType=" + damageType
        + ",initiative=" + initiative + ",immunities=" + immunities + ",weaknesses=" + weaknesses + "]";
    }

  }

}
