import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnemyLogicTest {

    @Test
    void buildEasy() {
        GameController ctrl=new GameController(5,1,false);
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                assertEquals(' ', ctrl.getBoard()[x][y]);
                BoardField bf = new BoardField(ctrl, x, y, 10);
            }
        }
        ctrl.makeStep();
        boolean found=false;
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                if(ctrl.getElementAt(x,y)!=' '){
                    found=true;
                }
            }
        }
        assertTrue(found);
    }
}