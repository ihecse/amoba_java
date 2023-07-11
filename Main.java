import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        boolean shallRestart=true;
        while (shallRestart) {
            MainMenu menu = new MainMenu();
            GameController ctrl = menu.startGame();
            GameWindowGUI gui = new GameWindowGUI(ctrl);
            shallRestart=gui.getNewGame();
        }
    }
}
