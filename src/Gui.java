import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Schreibe deine Lösung nur in diese Datei. Nur diese Datei wird bei der Abgabe hochgeladen.
 * Benenne diese Datei nicht um und verschiebe sie nicht.
 * Du kannst deine Lösung mit der TestMain.java testen bevor du sie abgibst.
 */

record GameOfLifeGui(boolean[][] field) {
  
  static String ex1 = """
            ..........................
            .......0..................
            ........0.................
            ......000.................
            ..........................
            ..........................
            ..........................
            ..........................
            ..........................
            ..........................
            ..........................""";
  
  String show() {
    StringBuilder gameFieldBuilder = new StringBuilder();
    for (boolean[] f1 : field) {
      for (boolean f2 : f1) {
        if (f2) {
          gameFieldBuilder.append("█");
        } else {
          gameFieldBuilder.append(" ");
        }
      }
      gameFieldBuilder.append("\n");
    }
    return gameFieldBuilder.toString();
  }
  
  int population() {
    int sum = 0;
    for (boolean[] f1 : field) {
      for (boolean f2 : f1) {
        if (f2) {
          sum++;
        }
      }
    }
    return sum;
  }
  
  boolean extinct() {
    return population() == 0;
  }
  
  static GameOfLifeGui fromString(String m) {
    String[] lines = m.split("\n");
    int height = lines.length;
    int width = (height > 0) ? lines[0].length() : 0;
    
    boolean[][] field = new boolean[height][width];
    
    for (int i = 0; i < height; i++) {
      String line = lines[i].strip();
      for (int j = 0; j < width; j++) {
        if (j < line.length()) {
          field[i][j] = line.charAt(j) == '0';
        }
      }
    }
    return new GameOfLifeGui(field);
  }
  
  int anzahlBelegterNachbarn(int x, int y) {
    int amount = 0;
    
    for (int i = x - 1; i <= x + 1; i++) {
      for (int j = y - 1; j <= y + 1; j++) {
        if (i == x && j == y) {
          continue;
        }
        
        if (i >= 0 && i < field.length && j >= 0 && j < field[0].length) {
          if (field[i][j]) {
            amount++;
          }
        }
      }
    }
    return amount;
  }
  
  GameOfLifeGui nextGeneration() {
    int height = field.length;
    int width = (height > 0) ? field[0].length : 0;
    boolean[][] nextField = new boolean[height][width];
    
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        
        int anzahlBelegterNachbarn = anzahlBelegterNachbarn(i, j);
        boolean istAmLeben = field[i][j];
        
        if (istAmLeben) {
          if (anzahlBelegterNachbarn == 2 || anzahlBelegterNachbarn == 3) {
            nextField[i][j] = true;
          }
        } else {
          if (anzahlBelegterNachbarn == 3) {
            nextField[i][j] = true;
          }
        }
      }
    }
    return new GameOfLifeGui(nextField);
  }
  
  // Optional: alte Konsolen-Version, falls du sie noch brauchst
  void waitAndClear() {
    try {
      Thread.sleep(2000);
    } catch (Exception e) {
      Thread.currentThread().interrupt();
    }
    System.out.print("\033[H\033[2J");
    System.out.flush();
  }
  
  void play() {
    var current = this;
    
    while (true) {
      waitAndClear();
      System.out.println(current.show());
      
      if (current.population() == 0) {
        System.out.println("Alle Zellen sind ausgestorben.");
        break;
      }
      
      current = current.nextGeneration();
    }
  }
  
  // ===================== GUI-Teil =====================
  
  static class GameOfLifeGuiFrame extends JFrame {
    
    private final int rows;
    private final int cols;
    private final JToggleButton[][] cells;
    private Timer timer;
    private GameOfLifeGui currentGame;
    
    // Für Drag-Malen
    private boolean isDragging = false;
    private boolean dragValue = false; // true = anmalen, false = löschen
    
    GameOfLifeGuiFrame() {
      // Spielfeldgröße abfragen
      this.rows = askForInt("Anzahl Zeilen des Spielfelds:", 20);
      this.cols = askForInt("Anzahl Spalten des Spielfelds:", 30);
      
      setTitle("Game of Life");
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setLayout(new BorderLayout());
      
      // Zellen-Panel
      JPanel gridPanel = new JPanel(new GridLayout(rows, cols));
      cells = new JToggleButton[rows][cols];
      
      // Gemeinsamer MouseAdapter für alle Zellen
      MouseAdapter cellMouseAdapter = new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
          JToggleButton cell = (JToggleButton) e.getSource();
          if (!cell.isEnabled()) return;
          
          isDragging = true;
          // Wenn Zelle vorher aus war -> anmalen, sonst löschen
          dragValue = !cell.isSelected();
          applyCellState(cell, dragValue);
        }
        
        @Override
        public void mouseEntered(MouseEvent e) {
          if (!isDragging) return;
          JToggleButton cell = (JToggleButton) e.getSource();
          if (!cell.isEnabled()) return;
          
          applyCellState(cell, dragValue);
        }
        
        @Override
        public void mouseReleased(MouseEvent e) {
          isDragging = false;
        }
      };
      
      for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
          JToggleButton cell = new JToggleButton();
          cell.setMargin(new Insets(0, 0, 0, 0));
          cell.setFocusPainted(false);
          cell.setPreferredSize(new Dimension(20, 20));
          cell.setBackground(Color.WHITE);
          
          // Drag-/Klick-Handling
          cell.addMouseListener(cellMouseAdapter);
          cell.addMouseMotionListener(cellMouseAdapter);
          
          cells[i][j] = cell;
          gridPanel.add(cell);
        }
      }
      
      // Button-Leiste unten
      JPanel controlPanel = new JPanel();
      JButton startButton = new JButton("Start");
      JButton resetButton = new JButton("Reset");
      
      startButton.addActionListener(e -> startSimulation(startButton, resetButton));
      resetButton.addActionListener(e -> resetField(startButton, resetButton));
      
      controlPanel.add(startButton);
      controlPanel.add(resetButton);
      
      add(gridPanel, BorderLayout.CENTER);
      add(controlPanel, BorderLayout.SOUTH);
      
      pack();
      setLocationRelativeTo(null);
      setVisible(true);
    }
    
    private int askForInt(String message, int defaultValue) {
      while (true) {
        String input = JOptionPane.showInputDialog(
            this,
            message,
            defaultValue
        );
        if (input == null) {
          // Abbrechen -> Programm beenden
          System.exit(0);
        }
        try {
          int value = Integer.parseInt(input.trim());
          if (value > 0 && value <= 200) {
            return value;
          }
        } catch (NumberFormatException ignored) {
        }
        JOptionPane.showMessageDialog(
            this,
            "Bitte eine positive ganze Zahl (1–200) eingeben.",
            "Ungültige Eingabe",
            JOptionPane.ERROR_MESSAGE
        );
      }
    }
    
    private void applyCellState(JToggleButton cell, boolean alive) {
      cell.setSelected(alive);
      cell.setBackground(alive ? Color.BLACK : Color.WHITE);
    }
    
    private void startSimulation(JButton startButton, JButton resetButton) {
      // Ausgangsfeld aus den Buttons lesen
      boolean[][] startField = new boolean[rows][cols];
      for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
          startField[i][j] = cells[i][j].isSelected();
          // während der Simulation nicht mehr anklickbar
          cells[i][j].setEnabled(false);
        }
      }
      
      currentGame = new GameOfLifeGui(startField);
      
      startButton.setEnabled(false);
      resetButton.setEnabled(true);
      
      // Timer für die Generationen
      timer = new Timer(200, e -> stepGeneration(startButton, resetButton));
      timer.start();
    }
    
    private void stepGeneration(JButton startButton, JButton resetButton) {
      if (currentGame.extinct()) {
        timer.stop();
        JOptionPane.showMessageDialog(this, "Alle Zellen sind ausgestorben.");
        resetAfterStop(startButton, resetButton);
        return;
      }
      
      currentGame = currentGame.nextGeneration();
      boolean[][] f = currentGame.field();
      
      for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
          boolean alive = f[i][j];
          applyCellState(cells[i][j], alive);
        }
      }
    }
    
    private void resetAfterStop(JButton startButton, JButton resetButton) {
      // Zellen wieder editierbar und leer machen
      for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
          cells[i][j].setEnabled(true);
          applyCellState(cells[i][j], false);
        }
      }
      startButton.setEnabled(true);
      resetButton.setEnabled(true);
    }
    
    private void resetField(JButton startButton, JButton resetButton) {
      if (timer != null && timer.isRunning()) {
        timer.stop();
      }
      resetAfterStop(startButton, resetButton);
    }
  }
  
  public static void main(String... args) {
    SwingUtilities.invokeLater(GameOfLifeGuiFrame::new);
    // falls du die alte Konsolen-Variante starten willst:
    // GameOfLife.fromString(GameOfLife.ex1).play();
  }
}
