/**
 * Schreibe deine Lösung nur in diese Datei. Nur diese Datei wird bei der Abgabe hochgeladen.
 * Benenne diese Datei nicht um und verschiebe sie nicht.
 * Du kannst deine Lösung mit der TestMain.java testen bevor du sie abgibst.
 */

record GameOfLife(boolean[][] field) {
    String show() {
      var sb  = new StringBuilder();
      for (int i = 0; i < field.length; i++){
        for (int j = 0; j < field[0].length; j++){
          sb.append(field[i][j] ? '\u2588' : ' ');
        }
        sb.append('\n');
      }
      return sb.toString(); // TODO
      
    }

    int population() {
      int count = 0;
      for (int i = 0; i < field.length; i++){
        for  (int j = 0; j < field[0].length; j++) {
          if (field[i][j]) count++;
        }
      }
      return count;
      
    }

    boolean extinct() {
      
      return population() == 0;
      
    }

    static GameOfLife fromString(String m) {
      var lines = m.split("\n");
      int rows = lines.length;
      int cols = lines[0].length();
      
      boolean[][] f = new boolean[rows][cols];
      
      for (int i = 0; i < rows; i++){
        for (int j = 0; j < cols; j++){
          char c = lines[i].charAt(j);
          f[i][j] = (c != '.');
        }
      }
      return new GameOfLife(f); // TODO
      
    }

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


    int anzahlBelegterNachbarn(int x, int y) {
        return 0;
    }

    GameOfLife nextGeneration() {
        return new GameOfLife(field);
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

    }

    public static void main(String... args) {
      
      
      //GameOfLife.fromString(GameOfLife.ex1).play();
      System.out.println(GameOfLife.fromString(GameOfLife.ex1).show());
      
      
    }
}