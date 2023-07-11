import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/***
 * gép logikájának kezeléséhez szükséges függvények
 */
public class EnemyLogic implements Serializable {

    /**
     * A gép logikájának nehézsége
     */
    private final int difficulty;
    private Random rand;
    GameController gc;

    public EnemyLogic(GameController gc, int difficulty) {
        this.difficulty = difficulty;
        this.gc = gc;
        rand = new Random();
    }

    /***
     * Egy lépés elvégzése a gép által, mivel az első kör elején ez fixen meghívódik, ezért ellenőrizzük azt is, hogy a gép jön-e.
     * Először ellenőrzi, hogy megtelt-e már a tábla. Ha nem, akkor a játék beállításainak megfelelő nehézség case-e fut le,
     * aztán újrarajzolja a pályát (elképzelhető, hogy inkozisztens lenne), majd engedi, hogy a felhasználó lépjen (átállítja a booleant)
     */
    public void makeStep() {
        switch (difficulty) {
            case 1:
                buildEasy(); //epito
                break;

            case 2:
                if (rand.nextInt(2) % 2 == 0) {
                    protectHard();
                } else {
                    buildEasy();
                }
                break;
            case 3:
                protectHard(); //akadalyoz
                break;

        }
    }


    /**
     * A gép legnehezebb stratégiája, amikor elsődlegesen a felhasználó győzelmét akarja megakadályozni, és csak akkor építi saját magát,
     * ha a dangerousFields üres. Ha a dangerousFields nem üres, de az attackerFields igen, akkor az előbbi elemei közül választ random egyet,
     * ha pedig az attackFields nem üres, akkor ennek elemei közül rak le egyet. (ezzel biztosítva, hogyha a gép egy lépésre van a győzelemtől,
     * akkor nyerjen, és ne a felhasználót próbálja meg akadályozni)
     */
    protected void protectHard() {
        ArrayList<BoardField> dangerousFields = new ArrayList<>();
        protect(dangerousFields);
        ArrayList<BoardField> attackerFields = new ArrayList<>();
        int window = 5;
        attacking(attackerFields, window);

        if (dangerousFields.size() != 0) {
            if (attackerFields.size() == 0) {
                int numberOfDF = dangerousFields.size();
                int ran = rand.nextInt(numberOfDF);
                BoardField putHere = dangerousFields.get(ran);
                gc.setElementAt(putHere.x, putHere.y, 'x');
            }

            if (attackerFields.size() != 0) {
                int numberOfAF = attackerFields.size();
                int ran = rand.nextInt(numberOfAF);
                BoardField putHere = attackerFields.get(ran);
                gc.setElementAt(putHere.x, putHere.y, 'x');
            }
        } else //ha nincs potencialisan veszelyes mezo, akkor magat epiti
        {
            buildEasy();
        }
    }

    /***
     * A gép "legbutább" stratégiája. A felhasználó lépéseit figyelmen kívül hagyva csak magát építgeti.
     * Ezt úgy teszi, hogy ha nem talál saját bábut, ami mellé rakhat még, akkor random rak, ha pedig van olyan saját bábuja, ami mellett vanszabad mező,
     * akkor oda rakja, lehetőleg egy sorba/oszlopba/átlóba.
     */
    protected void buildEasy() {
        int window = 5;
        ArrayList<BoardField> attackerFields = new ArrayList<>();
        while (attackerFields.size() == 0) {
            attacking(attackerFields, window);
            window--;
        }
        if (attackerFields.size() == 0) {
            int bX = rand.nextInt(gc.getGameSize());
            int bY = rand.nextInt(gc.getGameSize());
            while (gc.getBoard()[bX][bY] != ' ') {
                bX = rand.nextInt(gc.getGameSize());
                bY = rand.nextInt(gc.getGameSize());
            }
            gc.setElementAt(bX, bY, 'x');
        } else {
            int numberOfAF = attackerFields.size();
            int ran = rand.nextInt(numberOfAF);
            BoardField putHere = attackerFields.get(ran);
            gc.setElementAt(putHere.x, putHere.y, 'x');
        }
    }

    /***
     * A támadó (1-es nehézségű) függvényeket csoportosítja.
     * @param attackerFields azon mezők listája, amiken támadó (önmagát építő) stratégiát alkalmazhat
     * @param window ekkora "ablakot" csúsztatgatva vizsgáljuk a pályát
     */
    protected void attacking(ArrayList<BoardField> attackerFields, int window) {
        horizontal_attack(attackerFields, window);
        vertical_attack(attackerFields, window);
        diagonal1_attack(attackerFields, window);
        diagonal2_attack(attackerFields, window);
    }

    /***
     * Window-nyi darab szomszédos mezőt vizsgálunk / átlóban, hogy ha window-1 darab X van benne, akkor, ha az a
     * maradék egy üres, akkor az kerüljön a attackerFields listára, mint egy olyan mező
     * amire potenciálisan bábut rakhat, ha 1-es nehézségű stratégiát alkalmazunk. Ezt úgy
     * tudjuk elképzelni, mintha mindig window-nyi méretű ablakot csúsztatgatnánk végig a
     * pályán és abban számolnánk meg az X-eket.
     * @param attackerFields azon mezők listája, amiken támadó stratégiát alkalmazhat
     * @param window ekkora "ablakot" csúsztatgatva vizsgáljuk a pályát
     */
    protected void diagonal2_attack(ArrayList<BoardField> attackerFields, int window) {
        for (int x = window; x < gc.getGameSize(); ++x) {
            for (int y = 0; y <= gc.getGameSize() - window; ++y) {
                int db = 0;
                for (int k = 0; k < window; ++k) {
                    if (gc.getElementAt(x - k,y + k) == 'x') {
                        db++;
                    }
                }
                if (db == window - 1) {
                    for (int k = 0; k < window; ++k) {
                        if (gc.getElementAt(x - k,y + k) == ' ') {
                            BoardField bf = gc.getBoardFieldXY(x - k, y + k);
                            attackerFields.add(bf);
                        }
                    }
                }
            }
        }
    }

    /***
     * Window-nyi darab szomszédos mezőt vizsgálunk \ átlóban, hogy ha window-1 darab X van benne, akkor, ha az a
     * maradék egy üres, akkor az kerüljön a attackerFields listára, mint egy olyan mező
     * amire potenciálisan bábut rakhat, ha 1-es nehézségű stratégiát alkalmazunk. Ezt úgy
     * tudjuk elképzelni, mintha mindig window-nyi méretű ablakot csúsztatgatnánk végig a
     * pályán és abban számolnánk meg az X-eket.
     * @param attackerFields azon mezők listája, amiken támadó stratégiát alkalmazhat
     * @param window ekkora "ablakot" csúsztatgatva vizsgáljuk a pályát
     */

    protected void diagonal1_attack(ArrayList<BoardField> attackerFields, int window) {
        for (int x = 0; x <= gc.getGameSize() - window; ++x) {
            for (int y = 0; y <= gc.getGameSize() - window; ++y) {
                int db = 0;
                for (int k = 0; k < window; ++k) {
                    if (gc.getElementAt(x + k, y + k) == 'x') {
                        db++;
                    }
                }
                if (db == window - 1) {
                    for (int k = 0; k < window; ++k) {
                        if (gc.getElementAt(x + k, y + k) == ' ') {
                            BoardField bf = gc.getBoardFieldXY(x + k, y + k);
                            attackerFields.add(bf);
                        }
                    }
                }
            }
        }
    }

    /***
     * Window-nyi darab szomszédos mezőt vizsgálunk függőlegesen, hogy ha window-1 darab X van benne, akkor, ha az a
     * maradék egy üres, akkor az kerüljön a attackerFields listára, mint egy olyan mező
     * amire potenciálisan bábut rakhat, ha 1-es nehézségű stratégiát alkalmazunk. Ezt úgy
     * tudjuk elképzelni, mintha mindig window-nyi méretű ablakot csúsztatgatnánk végig a
     * pályán és abban számolnánk meg az X-eket.
     * @param attackerFields azon mezők listája, amiken támadó stratégiát alkalmazhat
     * @param window ekkora "ablakot" csúsztatgatva vizsgáljuk a pályát
     */
    protected void vertical_attack(ArrayList<BoardField> attackerFields, int window) {
        for (int x = 0; x < gc.getGameSize(); ++x) {
            for (int y = 0; y <= gc.getGameSize() - window; ++y) {
                int db = 0;
                for (int k = 0; k < window; ++k) {
                    if (gc.getElementAt(x, y + k) == 'x') {
                        db++;
                    }
                }
                if (db == window - 1) {
                    for (int k = 0; k < window; ++k) {
                        if (gc.getElementAt(x, y + k) == ' ') {
                            BoardField bf = gc.getBoardFieldXY(x, y + k);
                            attackerFields.add(bf);
                        }
                    }
                }
            }
        }
    }

    /***
     * Window-nyi darab szomszédos mezőt vizsgálunk vízszintesen, hogy ha window-1 darab X van benne, akkor, ha az a
     * maradék egy üres, akkor az kerüljön a attackerFields listára, mint egy olyan mező
     * amire potenciálisan bábut rakhat, ha 1-es nehézségű stratégiát alkalmazunk. Ezt úgy
     * tudjuk elképzelni, mintha mindig window-nyi méretű ablakot csúsztatgatnánk végig a
     * pályán és abban számolnánk meg az X-eket.
     * @param attackerFields azon mezők listája, amiken támadó stratégiát alkalmazhat
     * @param window ekkora "ablakot" csúsztatgatva vizsgáljuk a pályát
     */
    protected void horizontal_attack(ArrayList<BoardField> attackerFields, int window) {
        for (int x = 0; x <= gc.getGameSize() - window; ++x) {
            for (int y = 0; y < gc.getGameSize(); ++y) {
                int db = 0;
                for (int k = 0; k < window; ++k) {
                    if (gc.getElementAt(x + k,y) == 'x') {
                        db++;
                    }
                }
                if (db == window - 1) {
                    for (int k = 0; k < window; ++k) {
                        if (gc.getElementAt(x + k,y) == ' ') {
                            BoardField boardField = gc.getBoardFieldXY(x + k, y);
                            attackerFields.add(boardField);
                        }
                    }
                }
            }
        }
    }

    /***
     * A gép 3-as stratégiájának (védekező) függvényeit csoportosítja
     * @param dangerousFields azon mezők listája, amiken védekező (felhasználót akadályozó) stratégiát alkalmazhat
     */
    protected void protect(ArrayList<BoardField> dangerousFields) { //ved_akadalyoz
        horizontal_protect(dangerousFields);
        vertical_protect(dangerousFields);
        diagonal2_protect(dangerousFields);
        diagonal1_protect(dangerousFields);
    }

    /***
     * 4 szomszédos mezőt vizsgálunk / átlóban, hogy ha 3 darab kör van benne, akkor, ha az a
     * maradék egy üres, akkor az kerüljön a dangerousFields listára, mint egy olyan mező
     * amire potenciálisan bábut rakhat, ha 3-es nehézségű stratégiát alkalmazunk. Ezt úgy
     * tudjuk elképzelni, mintha mindig 4 mező méretű ablakot csúsztatgatnánk végig a
     * pályán és abban számolnánk meg az X-eket.
     * @param dangerousFields azon mezők listája, amiken védekező stratégiát alkalmazhat
     */
    protected void diagonal1_protect(ArrayList<BoardField> dangerousFields) {
        for (int x = 3; x < gc.getGameSize(); ++x) {
            for (int y = 0; y <= gc.getGameSize() - 4; ++y) {
                int db = 0;
                for (int k = 0; k < 4; ++k) {
                    if (gc.getElementAt(x - k,y + k) == 'o') {
                        db++;
                    }
                }
                if (db == 3) {
                    for (int k = 0; k < 4; ++k) {
                        if (gc.getElementAt(x - k,y + k) == ' ') {
                            BoardField bf = gc.getBoardFieldXY(x - k, y + k);
                            dangerousFields.add(bf);
                        }
                    }
                }
            }
        }
    }

    /***
     * 4 szomszédos mezőt vizsgálunk \ átlóban, hogy ha 3 darab kör van benne, akkor, ha az a
     * maradék egy üres, akkor az kerüljön a dangerousFields listára, mint egy olyan mező
     * amire potenciálisan bábut rakhat, ha 3-es nehézségű stratégiát alkalmazunk. Ezt úgy
     * tudjuk elképzelni, mintha mindig 4 mező méretű ablakot csúsztatgatnánk végig a
     * pályán és abban számolnánk meg az X-eket.
     * @param dangerousFields azon mezők listája, amiken védekező stratégiát alkalmazhat
     */
    protected void diagonal2_protect(ArrayList<BoardField> dangerousFields) {
        for (int x = 0; x <= gc.getGameSize() - 4; ++x) {
            for (int y = 0; y <= gc.getGameSize() - 4; ++y) {
                int db = 0;
                for (int k = 0; k < 4; ++k) {
                    if (gc.getElementAt(x + k,y + k) == 'o') {
                        db++;
                    }
                }

                if (db == 3) {
                    for (int k = 0; k < 4; ++k) {
                        if (gc.getElementAt(x + k,y + k) == ' ') {
                            BoardField bf = gc.getBoardFieldXY(x + k, y + k);
                            dangerousFields.add(bf);
                        }
                    }
                }
            }
        }
    }

    /***
     * 4 szomszédos mezőt vizsgálunk függőlegesen, hogy ha 3 darab kör van benne, akkor, ha az a
     * maradék egy üres, akkor az kerüljön a dangerousFields listára, mint egy olyan mező
     * amire potenciálisan bábut rakhat, ha 3-es nehézségű stratégiát alkalmazunk. Ezt úgy
     * tudjuk elképzelni, mintha mindig 4 mező méretű ablakot csúsztatgatnánk végig a
     * pályán és abban számolnánk meg az X-eket.
     * @param dangerousFields azon mezők listája, amiken védekező stratégiát alkalmazhat
     */
    protected void vertical_protect(ArrayList<BoardField> dangerousFields) {
        for (int x = 0; x < gc.getGameSize(); ++x) {
            for (int y = 0; y <= gc.getGameSize() - 4; ++y) {
                int db = 0;
                for (int k = 0; k < 4; ++k) {
                    if (gc.getElementAt(x,y + k) == 'o') {
                        db++;
                    }
                }
                if (db == 3) {
                    for (int k = 0; k < 4; ++k) {
                        if (gc.getElementAt(x,y + k) == ' ') {
                            BoardField bf = gc.getBoardFieldXY(x, y + k);
                            dangerousFields.add(bf);
                        }
                    }

                }
            }
        }
    }

    /***
     * 4 szomszédos mezőt vizsgálunk vízszintesen, hogy ha 3 darab kör van benne, akkor, ha az a
     * maradék egy üres, akkor az kerüljön a dangerousFields listára, mint egy olyan mező
     * amire potenciálisan bábut rakhat, ha 3-es nehézségű stratégiát alkalmazunk. Ezt úgy
     * tudjuk elképzelni, mintha mindig 4 mező méretű ablakot csúsztatgatnánk végig a
     * pályán és abban számolnánk meg az X-eket.
     * @param dangerousFields azon mezők listája, amiken védekező stratégiát alkalmazhat
     */
    protected void horizontal_protect(ArrayList<BoardField> dangerousFields) {
        for (int x = 0; x <= gc.getGameSize() - 4; ++x) {
            for (int y = 0; y < gc.getGameSize(); ++y) {
                int db = 0;

                for (int k = 0; k < 4; ++k) {
                    if (gc.getElementAt(x + k,y) == 'o') {
                        db++;
                    }

                }
                if (db == 3) {
                    for (int k = 0; k < 4; ++k) {
                        if (gc.getElementAt(x + k,y) == ' ') {
                            BoardField bf = gc.getBoardFieldXY(x + k, y);
                            dangerousFields.add(bf);
                        }
                    }
                }
            }
        }
    }


}