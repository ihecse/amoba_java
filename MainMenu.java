import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

/*
ha nem lambda lenne, ez is kellene
class Ac implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}*/
/***
 * A menü megjelenítéséért felelős osztály.
 */
public class MainMenu {
    private final JFrame frame;
    private JPanel downPanel;
    private GameController gc = null;
    private JPanel newGamePanel;
    private JPanel loadGamePanel;
    private JPanel bottomPanels;
    private boolean userStarts = true;

    /***
     * A menü grafikus felépítését végzi. Egymásba ágyazott layoutok sokasága.
     */
    MainMenu() {

        //fő keret
        GridLayout gl_main = new GridLayout(2, 1);
        GridLayout gl_upper = new GridLayout(1, 2);
        bottomPanels = new MyPanel();
        frame = new MyFrame();
        frame.setTitle("Amőba játék");
        frame.setSize(500, 300);
        frame.getContentPane().setLayout(gl_main);

        //menü felső része
        JPanel upperPanel = new MyPanel();
        Font buttonFont = new Font("Impact", Font.PLAIN, 15);

        MyButton newGameButton = new MyButton("Új játék kezdése");
        MyButton loadGameButton = new MyButton("Megkezdett játék betöltése");
        newGameButton.setFont(buttonFont);
        loadGameButton.setFont(buttonFont);

        upperPanel.add(newGameButton);
        upperPanel.add(loadGameButton);
        upperPanel.setLayout(gl_upper);

        frame.add(upperPanel);
        upperPanel.setSize(500, 120);

        //menü alja
        CardLayout cl = new CardLayout();

        bottomPanels.setLayout(cl);

        newGamePanel = new MyPanel(); //ha új játékot akarok, ezt használjam
        loadGamePanel = new MyPanel(); //ha korábbi játékot akarok betölteni, ezt használjam
        bottomPanels.add("empty", new MyPanel()); //ahhoz h alapjáraton üres lehessen
        bottomPanels.add("new", newGamePanel);
        bottomPanels.add("load", loadGamePanel);

        //új játéknál
        JPanel newGameLeftPanel = new MyPanel(); //alja bal oldala (beállítások lesznek itt)
        JButton startButton = new MyButton("Játék indítása");


        BorderLayout bl = new BorderLayout();

        newGamePanel.setLayout(bl);
        newGamePanel.add(newGameLeftPanel, BorderLayout.CENTER); //játékbeállítások
        newGamePanel.add(startButton, BorderLayout.EAST); //start gomb elhelyezése jobbra

        frame.add(bottomPanels);

        JLabel errorLabel = new MyLabel();
        errorLabel.setFont(new Font("Impact", Font.PLAIN, 21));
        errorLabel.setMaximumSize(new Dimension(200,400));
        errorLabel.setForeground(Color.red);
        loadGamePanel.add(errorLabel);
        //bal alja -> játék beállítások
        GridLayout newGameSettingsLayout = new GridLayout(3, 1); //3 sor, 1 oszlo
        newGameLeftPanel.setLayout(newGameSettingsLayout);

        JPanel startingPlayer = new MyPanel();
        JPanel boardSize = new MyPanel();
        JPanel difficulty = new MyPanel();

        //pályaméret csúszka parametrei
        JSlider boardSizeSlider = new MySlider();
        boardSizeSlider.setMinimum(5);
        boardSizeSlider.setMaximum(20);
        JLabel boardSizeFeedbackLabel = new MyLabel("5"); //mutatja, h mennyi lett beállítva
        JLabel sizeSetting = new MyLabel("Pálya mérete");
        boardSizeSlider.setValue(5);

        //pályaméret csuszka figyelése
        boardSizeSlider.addChangeListener(e -> {
            JSlider slider = (JSlider) e.getSource(); //visszaad egy csuszkát, ami ahhoz kell hogy le tudja kérdezni majd az értékét
            int value = slider.getValue(); //aktuális érték
            boardSizeFeedbackLabel.setText(String.valueOf(value)); //beállítjuk a visszajelző feliratot a csúszka pillanatnyi értékére (és stringet csinalunk belole, mert intet nem fogad el)

            sizeSetting.setText("Pálya mérete"); //?
        });
        boardSize.add(boardSizeSlider, BorderLayout.CENTER);
        boardSize.add(boardSizeFeedbackLabel, BorderLayout.EAST);

        boardSize.add(sizeSetting,BorderLayout.WEST); //?


        //nehézség csuszka
        JSlider difficultySlider = new MySlider();
        difficultySlider.setMinimum(1);
        difficultySlider.setMaximum(3);
        JLabel difficultyFeedbackLabel = new MyLabel("1");
        JLabel difficultySetting = new MyLabel("Nehézségi szint");
        difficultySlider.setValue(1);

        //nehézség csuszka figyelése
        difficultySlider.addChangeListener(e -> {
            JSlider slider = (JSlider) e.getSource();
            int value = slider.getValue(); //aktualis érték
            difficultyFeedbackLabel.setText(String.valueOf(value));
            difficultySetting.setText("Nehézségi szint");
        });
        difficulty.add(difficultySlider, BorderLayout.NORTH);
        difficulty.add(difficultyFeedbackLabel, BorderLayout.EAST);
        difficulty.add(difficultySetting, BorderLayout.EAST);

        //ki kezd
        JButton userIsFirstButton = new MyButton("Felhasználó");

        JButton pcIsFirstButton = new MyButton("Gép");
        startingPlayer.add(userIsFirstButton, BorderLayout.EAST);
        startingPlayer.add(pcIsFirstButton, BorderLayout.WEST);

        //aki alapból kezd, automatikusan kiszínezi
        userIsFirstButton.setForeground(userStarts ? Color.red : Color.white);
        pcIsFirstButton.setForeground(userStarts ? Color.white : Color.red);

        //beszínezi annak a feliratát, amelyiket kiválasztom
        //közös actionListener
        ActionListener playerSelectorColoring = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userIsFirstButton.setForeground(userStarts ? Color.red : Color.white);
                pcIsFirstButton.setForeground(userStarts ? Color.white : Color.red);
            }
        };
        userIsFirstButton.addActionListener(playerSelectorColoring);
        pcIsFirstButton.addActionListener(playerSelectorColoring);

        //beállítja a boolean-t hogy ki kezd
        userIsFirstButton.addActionListener(e -> {
            userStarts = true;
        });
        pcIsFirstButton.addActionListener(e -> {
            userStarts = false;
        });

        newGameLeftPanel.add(startingPlayer);
        newGameLeftPanel.add(boardSize);
        newGameLeftPanel.add(difficulty);

        //gombok figyelése, alsó lap váltás
        newGameButton.addActionListener(e -> {
            cl.show(bottomPanels, "new");
        });
        loadGameButton.addActionListener(e -> {
            cl.show(bottomPanels, "load");
        });


        startButton.addActionListener(e -> {
            gc = new GameController(boardSizeSlider.getValue(), difficultySlider.getValue(), userStarts);
        });

        loadGameButton.addActionListener(e -> {

            try {
                FileInputStream fis = new FileInputStream("jatekAllas.txt");
                ObjectInputStream iis = new ObjectInputStream(fis);
                gc = (GameController) iis.readObject();
            } catch (Exception ex) {
                errorLabel.setText("Hiba a játékállás betöltése közben.");
            }
        });
    }


    GameController startGame() {
        frame.setVisible(true);
        while (gc == null) {

            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
            }
        }
        frame.setVisible(false);
        return gc;
    }


}
