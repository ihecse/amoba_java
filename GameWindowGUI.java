import javax.swing.*;
import java.awt.*;
import java.util.concurrent.TimeUnit;

/***
 * A játék megjelenítéséért felelős osztály.
 */
public class GameWindowGUI {
    private final GameController ctrl;
    private final JFrame frame;
    private boolean newGame;

    /***
     * Felépíti a grafikus felületét a játékpályának. A függvény játék futása alatt végig fut, amíg új játékot nem kérünk, vagy ki nem lépünk.
     * @param ctrl
     */
    public GameWindowGUI(GameController ctrl) {
        this.ctrl = ctrl;
        frame = new MyFrame();
        frame.setTitle("Amőba játék");
        JPanel main_p = new MyPanel();
        GridLayout gridLayout = new GridLayout(ctrl.getGameSize(), ctrl.getGameSize(), 0, 0);

        main_p.setLayout(gridLayout);
        frame.setMinimumSize(new Dimension(800, 800)); //azért minimumot adunk meg, hogy ha szükséges, lehessen nagyobb is ( pl menuBar miatt)

        JMenuBar menuBar = new JMenuBar();
        JMenuItem exitButton = new JMenuItem("Kilépés");
        JMenuItem saveButton = new JMenuItem("Mentés");
        JMenuItem newGameButton = new JMenuItem("Új játék");

        exitButton.addActionListener(e -> {
            System.exit(0);
        });
        saveButton.addActionListener(e -> {
            ctrl.saveGame();
        });
        newGameButton.addActionListener(e -> {
            newGame = true;
        });

        menuBar.add(exitButton);
        menuBar.add(saveButton);
        menuBar.add(newGameButton);

        frame.setJMenuBar(menuBar);

        frame.add(main_p);
        for (int x = 0; x < ctrl.getGameSize(); x++) {
            for (int y = 0; y < ctrl.getGameSize(); y++) {
                BoardField b = new BoardField(ctrl, x, y, 800 / ctrl.getGameSize());
                main_p.add(b);
            }
        }
        main_p.revalidate();
        main_p.repaint();
        frame.setVisible(true);
        ctrl.makeStep();
        while (ctrl.gameResult() == ' ' && !newGame) {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("hiba a kirajzolásnál");
            }
        }

        if (!newGame) {
            String winner = " ";
            switch (ctrl.gameResult()) {
                case 'o' -> winner = "A kör nyert!";
                case 'x' -> winner = "Az X nyert!";
                case 'w' -> winner = "Döntetlen lett!";
            }
            JLabel sign = new MyLabel(winner);
            sign.setFont(new Font("Impact", Font.BOLD, 75));
            sign.setHorizontalAlignment(SwingConstants.CENTER);
            frame.getContentPane().removeAll();
            frame.getContentPane().setLayout(new BorderLayout());

            frame.getContentPane().add(sign, BorderLayout.CENTER);

            frame.revalidate();
            while (!newGame) {
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                }
            }
            frame.setVisible(false);

        } else {
            frame.setVisible(false);
        }

    }


    public boolean getNewGame() {
        return newGame;
    }
}
