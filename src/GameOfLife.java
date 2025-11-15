/**
 * Schreibe deine Lösung nur in diese Datei. Nur diese Datei wird bei der Abgabe hochgeladen.
 * Benenne diese Datei nicht um und verschiebe sie nicht.
 * Du kannst deine Lösung mit der TestMain.java testen bevor du sie abgibst.
 */

record GameOfLife(boolean[][] field) {
  static String ex1 = """
            ..................
            ..................
            ..................
            ..................
            ......0....0......
            ....00.0000.00....
            ......0....0......
            ..................
            ..................
            ..................
            ..................""";
  
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
  
  static GameOfLife fromString(String m) {
    String[] lines = m.split("\n");
    int height = lines.length;
    int width = (height > 0) ? lines[0].length() : 0;
    
    boolean[][] field = new boolean[height][width];
    
    for (int i = 0; i < height; i++) {
      String line = lines[i];
      for (int j = 0; j < width; j++) {
        if (j < line.length()) {
          field[i][j] = line.charAt(j) == '0';
        }
      }
    }
    return new GameOfLife(field);
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
  
  GameOfLife nextGeneration() {
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
    return new GameOfLife(nextField);
  }
  
  void waitAndClear() {
    try {
      Thread.sleep(2000);
    } catch (Exception e) {
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
  
  public static void main(String... args) {
    GameOfLife.fromString(GameOfLife.ex1).play();
  }
}