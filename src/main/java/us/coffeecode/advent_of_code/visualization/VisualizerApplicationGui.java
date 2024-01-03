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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.component.DynamicTestFactory;
import us.coffeecode.advent_of_code.component.TestContext;
import us.coffeecode.advent_of_code.component.VisualizerExecutable;

/**
 *
 */
@Component
public class VisualizerApplicationGui {

  @Autowired
  private DynamicTestFactory testFactory;

  /** Build the GUI and show the main window. */
  void initAndRunGui() {
    SwingUtilities.invokeLater(new Runnable() {

      @Override
      public void run() {
        try {
          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (RuntimeException ex) {
          throw ex;
        }
        catch (Exception ex) {
          throw new RuntimeException(ex);
        }
        constructMainWindow().setVisible(true);
      }
    });
  }

  /** Construct the main window of the application. */
  private JFrame constructMainWindow() {
    final JFrame frame = new JFrame("Advent of Code Visualizer");
    frame.setLayout(new GridLayout(1, 1));
    frame.add(constructSelectionPanel());
    frame.pack();
    frame.setMinimumSize(frame.getSize());
    frame.setLocationRelativeTo(null);
    frame.addWindowListener(new WindowAdapter() {

      @Override
      public void windowClosing(final WindowEvent e) {
        frame.dispose();
        VisualizerApplication.close();
        System.exit(0);
      }
    });
    return frame;
  }

  /**
   * Construct the selection panel, which contains UI elements that allow the user to select a test to run and run it.
   */
  private JPanel constructSelectionPanel() {
    final JPanel panel = new JPanel(new GridBagLayout());
    panel.setBorder(new EmptyBorder(10, 10, 10, 10));
    final Map<String, VisualizerExecutable> tests = getTests();
    final GridBagConstraints c = new GridBagConstraints();

    final JList<String> box = new JList<>(tests.keySet().toArray(String[]::new));
    box.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    box.setSelectedIndex(0);

    final JScrollPane scroll = new JScrollPane(box);
    scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    c.gridx = 0;
    c.gridy = 0;
    c.weightx = 1;
    c.weighty = 1;
    c.insets = new Insets(10, 10, 10, 10);
    c.fill = GridBagConstraints.BOTH;
    c.anchor = GridBagConstraints.FIRST_LINE_START;
    panel.add(scroll, c);

    final JButton visualize = new JButton("Visualize");
    visualize.addActionListener(new VisualizeListener(box, tests));
    c.gridy = 1;
    c.weighty = 0.01;
    c.fill = GridBagConstraints.HORIZONTAL;
    panel.add(visualize, c);

    return panel;
  }

  /**
   * Get all of the tests that are available for visualization. The key is a text description of the test, and the value
   * is TBD.
   */
  private Map<String, VisualizerExecutable> getTests() {
    final Map<String, VisualizerExecutable> tests = new TreeMap<>();
    for (final VisualizerExecutable exec : testFactory.getTestsForVisualization(new TestContext(true))) {
      tests.put(exec.getDescription(), exec);
    }
    return tests;
  }

  /**
   * Listens for button clicks. When the user clicks the button, get the combo box's text, run the corresponding test,
   * and display the results in a new window.
   */
  private final class VisualizeListener
  implements ActionListener {

    private final JList<String> component;

    private final Map<String, VisualizerExecutable> tests;

    VisualizeListener(final JList<String> _combo, final Map<String, VisualizerExecutable> _tests) {
      component = _combo;
      tests = _tests;
    }

    /**
     * When the user clicks on the button, get the test from the combo box, run it, and display the results in a new
     * window.
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
      final String key = component.getSelectedValue();
      if (key == null) {
        JOptionPane.showMessageDialog(component, "There are no available tests.");
        return;
      }
      final Collection<IVisualizationResult> results = tests.get(key).call();

      final JFrame frame = new JFrame(key);

      if (results.size() == 1) {
        frame.add(results.iterator().next().buildComponent());
      }
      else {
        final JTabbedPane tabs = new JTabbedPane();
        results.stream().forEach(r -> tabs.addTab(r.getDescription(), r.buildComponent()));
        frame.add(tabs);
      }
      frame.pack();
      frame.setLocationRelativeTo(null);
      SwingUtilities.invokeLater(new Runnable() {

        @Override
        public void run() {
          frame.setVisible(true);
        }
      });
    }

  }

}
