import java.io.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * A játék belső logikájáért felelős osztály
 */
public class GameController implements Serializable {

    /**
     * A játékos kezd-e
     */
    private final boolean userStart;
    /**
     * A játékos következik-e
     */
    private boolean userComesNext;
    /**
     * A tábla mérete (oldalhossz mezőkben mérve)
     */
    private int gameSize;
    /**
     * A belső játékállást tároló tábla, mérete n*n ahol n az aktuális játéktábla hossza(minimum 5, maximum 20)
     */
    char[][] board;
    /**
     * A játékmezőn lévő mezőket tároló lista, a mezők újrarajzoláshoz használjuk
     */
    private ArrayList<BoardField> fields;

    private EnemyLogic enemy;

    /** konstruktor
     * @param gameSize A tábla hossza, illetve szélessége
     * @param difficulty gép startégiája
     * @param userStart Játékos kezd-e
     */
    public GameController(int gameSize, int difficulty, boolean userStart) {
        this.gameSize = gameSize;
        this.userStart = userStart;
        this.enemy=new EnemyLogic(this,difficulty);
        //pályát tároló 2D-s tömb
        board = new char[gameSize][gameSize];
        for (int x = 0; x < gameSize; x++) {
            for (int y = 0; y < gameSize; y++) {
                board[x][y] = ' ';
            }
        }
        fields = new ArrayList<>(); //ebbe lesz tárolva az osszes mező a tömbön kívül, hogy minden kattintás után ujrarajzolhassa önmagát

        userComesNext = userStart;
    }


    public int getGameSize() {
        return gameSize;
    }

    /**
     * Kattintás kezeli.
     * Ellenőrzi hogy a felhasználó jön-e a lépéssel, ha igen, és amennyiben az általa kijelölt mezőn még nincs elem,
     * akkor oda tesz egy karikát, majd a gépnek ad egy lépési lehetőséget, ezután pedig újrarajzoljuk a pályát
     * @param boardField A felhesználó által lenyomott mező
     */
    public void clicked(BoardField boardField) {
        if (board[boardField.x][boardField.y] == ' ' && userComesNext) {
            board[boardField.x][boardField.y] = 'o';
            userComesNext = false;
            makeStep();
        }
        reRenderMap();
    }

    /**
     * Összes mező kényszerített újrarajzolása
     */
    private void reRenderMap() {
        for (BoardField bf : fields) {
            bf.repaint();
        }
    }

    /***
     * Megadja, hogy az adott mezőn X vagy kör van vagy semmi sincs
     * @param boardField adott mező, amit vizsgálunk
     * @return visszaadja, hogy az adott koordinátákkal jelzett mezőben milyen bábu van (vagy éppen nincs)
     */
    public char getFieldState(BoardField boardField) {

        return board[boardField.x][boardField.y];
    }
    /***
     * Megadja a keresett koordinátákkal rendelkező mezőt.
     * @param x kresett mező x koordinátája
     * @param y kresett mező y koordinátája
     * @return visszatér a megadott koordinátákkal rendelkező mezővel
     */
    BoardField getBoardFieldXY(int x, int y) {
        for (BoardField bf : fields) {
            if (bf.x == x && bf.y == y) {
                return bf;
            }
        }
        return null;
    }
    /***
     * Mezőt tud hozzáadni a struktúrához kívülről (másik osztályban)
     * @param boardField
     */
    public void registerField(BoardField boardField) {
        fields.add(boardField); //azért kell, hogy kívülről hozzá tudjunk adni egy mezőt
    }

    /***
     * Annak ellenőrzése, hogy van-e még üres mező
     * @return logikai érték, igaz ha van még szabad mező
     */
    boolean areStepsStillPossible() {
        for (int x = 0; x < gameSize; x++) {
            for (int y = 0; y < gameSize; y++) {
                if (board[x][y] == ' ')
                    return true;
            }
        }
        return false;
    }

    public void makeStep() {
        if (!userComesNext) {
            //Annak ellenőrzése h ténylegesen van-e lehetőség lépni, vagy esetleg már megtelt-e a tábla, de még nem hirdettünk győztest
            if (areStepsStillPossible()) {
                enemy.makeStep();
            }
            userComesNext = true;
        }
    }

    //ki nyer??

    ///vizszintesen

    /***
     * Megvizsgálja vízszintesen, hogy valamelyik bábuból van-e 5 egy sorban.
     * @return Ha valamelyik bábuból van, akkor annak a karakterét visszaadja, ha nincs, akkor egy szóközt ad vissza.
     */
    char checkWinner_hor() {
        for (int x = 0; x <= gameSize - 5; ++x) {
            for (int y = 0; y < gameSize; ++y) {
                int odb = 0;
                int xdb = 0;
                for (int k = 0; k < 5; ++k) {
                    if (board[x + k][y] == 'o') {
                        odb++;
                    }
                    if (board[x + k][y] == 'x') {
                        xdb++;
                    }
                }
                if (odb == 5) {
                    return 'o';
                }
                if (xdb == 5) {
                    return 'x';
                }
            }
        }
        return ' ';
    }

    /***
     * Megvizsgálja függőlegesen, hogy valamelyik bábuból van-e 5 egy sorban.
     * @return Ha valamelyik bábuból van, akkor annak a karakterét visszaadja, ha nincs, akkor egy szóközt ad vissza.
     */
    char checkWinner_vert() {
        for (int x = 0; x < gameSize; ++x) {
            for (int y = 0; y <= gameSize - 5; ++y) {
                int odb = 0;
                int xdb = 0;
                for (int k = 0; k < 5; ++k) {
                    if (board[x][y + k] == 'o') {
                        odb++;
                    }
                    if (board[x][y + k] == 'x') {
                        xdb++;
                    }
                }
                if (odb == 5) {
                    return 'o';
                }
                if (xdb == 5) {
                    return 'x';
                }
            }
        }
        return ' ';
    }

    /***
     * Megvizsgálja \ átlóban, hogy valamelyik bábuból van-e 5 egy sorban.
     * @return Ha valamelyik bábuból van, akkor annak a karakterét visszaadja, ha nincs, akkor egy szóközt ad vissza.
     */
    char checkWinner_diag1() {
        for (int x = 0; x <= gameSize - 5; ++x) {
            for (int y = 0; y <= gameSize - 5; ++y) {
                int odb = 0;
                int xdb = 0;
                for (int k = 0; k < 5; ++k) {
                    if (board[x + k][y + k] == 'o') {
                        odb++;
                    }
                    if (board[x + k][y + k] == 'x') {
                        xdb++;
                    }
                }
                if (odb == 5) {
                    return 'o';
                }
                if (xdb == 5) {
                    return 'x';
                }
            }
        }
        return ' ';
    }

    /***
     * Megvizsgálja / átlóban, hogy valamelyik bábuból van-e 5 egy sorban.
     * @return Ha valamelyik bábuból van, akkor annak a karakterét visszaadja, ha nincs, akkor egy szóközt ad vissza.
     */
    char checkWinner_diag2() {
        for (int x = 4; x < gameSize; ++x) {
            for (int y = 0; y <= gameSize - 5; ++y) {
                int odb = 0;
                int xdb = 0;
                for (int k = 0; k < 5; ++k) {
                    if (board[x - k][y + k] == 'o') {
                        odb++;
                    }
                    if (board[x - k][y + k] == 'x') {
                        xdb++;
                    }
                }
                if (odb == 5) {
                    return 'o';
                }
                if (xdb == 5) {
                    return 'x';
                }
            }
        }
        return ' ';
    }

    /***
     * Visszaadja a játék győztesét, vagy egy döntetlen állást jelző karakert
     * @return
     *      <ul>
     *          <li> <b>' '</b> abban az esetben ha még nincs győztes</li>
     *          <li> <b>'x'</b> abban az esetben <b>X<b/> nyert</li>
     *          <li> <b>'o'</b> abban az esetben <b>◯<b/> nyert</li>
     *          <li> <b>'w'</b> abban az esetben ha döntetlen a játék</li>
     *      </ul>
     */
    char gameResult() {
        if (checkWinner_diag2() != ' ')
            return checkWinner_diag2();
        if (checkWinner_diag1() != ' ')
            return checkWinner_diag1();
        if (checkWinner_hor() != ' ')
            return checkWinner_hor();
        if (checkWinner_vert() != ' ')
            return checkWinner_vert();

        if (!areStepsStillPossible()) {
            return 'w';
        }
        return ' ';
    }

    /***
     * A játékállást szerializálással egy .txt fájlba menti
     */
    public void saveGame() {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream("jatekAllas.txt");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
            oos.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***
     * Teszteléshez segdfüggvény, bármi van eredetileg a játékállásban, ezt a mesterségesen létrehozott játékállást teszem be a helyére.
     * @param newBoard
     * @return sikeres volt-e a csere
     */
    protected boolean setBoard(char newBoard[][]){
        if(newBoard.length==gameSize){
            for (int i = 0; i < gameSize; i++) {
                if(newBoard[i].length!=gameSize){
                    return false;
                }
            }
        }
        else{
            return false;
        }
        board=newBoard;
        return true;
    }

    /**
     * A játékállást tároló tábla, mérete n*n ahol n minimum 5 és maximum 20
     * ' '= üres
     * 'x'= X
     * 'o'= kör
     *     * @return A játéktáblca
     */
    public char[][] getBoard() {
        return board;
    }

    char getElementAt(int x,int y){
        return board[x][y];
    }

    void setElementAt( int x, int y,char c){
        if((c == ' ' || c=='x' || c=='o')&&x>=0&&x<gameSize&&y>=0&&y<gameSize)
            board[x][y]=c;

    }

}
