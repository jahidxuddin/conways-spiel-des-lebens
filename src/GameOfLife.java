/**
 * Schreibe deine Lösung nur in diese Datei. Nur diese Datei wird bei der Abgabe hochgeladen.
 * Benenne diese Datei nicht um und verschiebe sie nicht.
 * Du kannst deine Lösung mit der TestMain.java testen bevor du sie abgibst.
 */

record GameOfLife(boolean[][] field) {
    String show() {
        return "";
    }

    int population() {
        return 0;
    }

    boolean extinct() {
        return false;
    }

    static GameOfLife fromString(String m) {
        return new GameOfLife(null);
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
        GameOfLife.fromString(GameOfLife.ex1).play();
    }
}