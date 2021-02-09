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

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import us.coffeecode.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2020/day/19">Year 2020, day 19</a>. This problem requires parsing input using
 * grammar rules that describe a linear grammar. Part one has no loops in the rules, while part two does.
 * </p>
 * <p>
 * The input to the program is a set of production rules for a finite state machine along with strings to test whether
 * or not they are in its language: i.e. whether this particular finite state machine can produce those strings. One
 * important property of these grammar rules is there are no loops: essentially, starting with the nonterminal 0, the
 * rules form an acyclic directed graph. The algorithm for part one reduces the rules to a single regular expression
 * which Java understands, and can use to match the input strings.
 * </p>
 * <p>
 * Part two is a bit trickier. It is no longer a true regular language, instead, it is context-free. Implementing a
 * parser for this would be a lot of work for an AoC challenge: instead, I opted to "unwind" the grammar a bit and
 * construct a set of regular expressions that work for the input provided.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public class Year2020Day19 {

  public long calculatePart1() {
    final Input input = getInput();
    reduce(input.rules, Collections.singleton("0"));
    final Pattern regex = Pattern.compile(input.rules.get("0").replace(",", ""));
    return countValidMessages(input.messages, Collections.singleton(regex));
  }

  public long calculatePart2() {
    final Input input = getInput();
    // Remove these rules and instead manually piece together regexes based on what we know about 8 and 11.
    input.rules.remove("0");
    input.rules.remove("8");
    input.rules.remove("11");
    reduce(input.rules, Arrays.asList(new String[] { "0", "31", "42" }));
    final String r31 = input.rules.get("31").replace(",", "");
    final String r42 = input.rules.get("42").replace(",", "");
    final Collection<Pattern> patterns = new ArrayList<>();
    // match 2+ 42s, at most N-1 31s
    for (int i = 2; i < 7; ++i) {
      for (int j = 1; j < i; ++j) {
        patterns.add(Pattern.compile("(" + r42 + "){" + i + "}(" + r31 + "){" + j + "}"));
      }
    }
    return countValidMessages(input.messages, patterns);
  }

  private void reduce(final Map<String, String> rules, final Collection<String> skip) {
    /*
     * Reduce this regular language to a single rule. Do this iteratively by looking for production rules that only have
     * terminals on the right side: once we find one, substitute its ID with its right side in all the other rules, then
     * remove it from the rule set.
     */
    boolean modified = true;
    while (modified) {
      modified = false;
      // Find the next rule that only has nonterminals.
      for (final Iterator<Map.Entry<String, String>> iter = rules.entrySet().iterator(); iter.hasNext();) {
        final Map.Entry<String, String> entry = iter.next();
        if (!skip.contains(entry.getKey()) && entry.getValue().codePoints().noneMatch(Character::isDigit)) {
          // Substitute this rule in all the other rules.
          for (final Map.Entry<String, String> entry2 : rules.entrySet()) {
            final String value = entry2.getValue();
            String replacement = entry.getValue();
            if (replacement.contains("|")) {
              replacement = "(" + replacement + ")";
            }
            final Pattern regex = Pattern.compile("\\b" + entry.getKey() + "\\b");
            entry2.setValue(regex.matcher(value).replaceAll(replacement));
          }

          // Remove this rule.
          iter.remove();
          modified = true;
        }
      }
    }
  }

  private int countValidMessages(final Iterable<String> messages, final Iterable<Pattern> patterns) {
    int count = 0;
    for (final String message : messages) {
      for (final Pattern pattern : patterns) {
        if (pattern.matcher(message).matches()) {
          ++count;
          break;
        }
      }
    }
    return count;
  }

  /** Get the input data for this solution. */
  private Input getInput() {
    try {
      final List<List<String>> groups = Utils.getLineGroups(Files.readAllLines(Utils.getInput(2020, 19)));
      return new Input(parseRules(groups.get(0)), groups.get(1));
    }
    catch (final RuntimeException ex) {
      throw ex;
    }
    catch (final Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private static final Pattern RULE_KV_SPLIT = Pattern.compile(":");

  private static Map<String, String> parseRules(final List<String> inputRules) {
    final Map<String, String> rules = new HashMap<>(inputRules.size() * 4 / 3);
    for (final String inputRule : inputRules) {
      String[] tokens = RULE_KV_SPLIT.split(inputRule);
      rules.put(tokens[0].trim(), tokens[1].trim().replace(" | ", "|").replace(' ', ',').replace("\"", ""));
    }
    return rules;
  }

  private static final class Input {

    final Map<String, String> rules;

    final Collection<String> messages;

    Input(final Map<String, String> r, final Collection<String> m) {
      rules = r;
      messages = m;
    }
  }

}
