import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import java.util.ArrayList;

class GameControllerTest {

    @Test
    void getGameSize() {
        GameController ctrl = new GameController(10, 1, false);
        assertEquals(10, ctrl.getGameSize());

        GameController ctrl2 = new GameController(5, 1, true);
        assertEquals(5, ctrl2.getGameSize());
    }

    @Test
    void clicked() {
        GameController ctrl = new GameController(5, 1, true);
        ArrayList<BoardField> bfs = new ArrayList<>();

        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                assertEquals(' ', ctrl.getBoard()[x][y]);
                BoardField bf = new BoardField(ctrl, x, y, 10);
                bfs.add(bf);
            }
        }
        ctrl.clicked(bfs.get(0));
        assertEquals('o', ctrl.getBoard()[0][0]);
    }


    @Test
    void GameController() {
        GameController ctrl = new GameController(5, 1, true);
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                assertEquals(' ', ctrl.getBoard()[x][y]);
            }
        }
    }

    @Test
    void areStepsStillPossible() {
        GameController ctrl = new GameController(5, 1, true);
        char board[][] = {{'x', 'x', 'o', 'x', 'x'}, {'o', 'o', 'x', 'o', 'o'}, {'x', 'x', 'o', 'x', 'x'}, {'x', 'x', 'o', 'x', 'x'}, {'o', 'o', 'x', 'o', 'o'}};
        assertTrue(ctrl.setBoard(board));
        assertFalse(ctrl.areStepsStillPossible());
        board = new char[][]{{' ', 'x', 'o', 'x', 'x'}, {'o', 'o', 'x', 'o', 'o'}, {'x', 'x', 'o', 'x', 'x'}, {'x', 'x', 'o', 'x', 'x'}, {'o', 'o', 'x', 'o', 'o'}};
        assertTrue(ctrl.setBoard(board));
        assertTrue(ctrl.areStepsStillPossible());
    }

    @Test
    void checkWinner_hor() {
        GameController ctrl = new GameController(5, 1, true);
        char board[][] = {{' ', ' ', ' ', ' ', ' '}, {' ', ' ', ' ', ' ', ' '}, {' ', ' ', ' ', ' ', ' '}, {' ', ' ', ' ', ' ', ' '}, {' ', ' ', ' ', ' ', ' '}};
        assertTrue(ctrl.setBoard(board));
        assertEquals(' ', ctrl.checkWinner_hor());

        board = new char[][]{{'x', 'x', 'x', 'x', 'x'}, {' ', ' ', ' ', ' ', ' '}, {' ', ' ', ' ', ' ', ' '}, {' ', ' ', ' ', ' ', ' '}, {' ', ' ', ' ', ' ', ' '}};
        assertTrue(ctrl.setBoard(board));
        assertEquals(' ', ctrl.checkWinner_hor());
        board = new char[][]{{'x', ' ', ' ', ' ', ' '}, {'x', ' ', ' ', ' ', ' '}, {'x', ' ', ' ', ' ', ' '}, {'x', ' ', ' ', ' ', ' '}, {'x', ' ', ' ', ' ', ' '}};
        assertTrue(ctrl.setBoard(board));
        assertEquals('x', ctrl.checkWinner_hor());

        board = new char[][]{{'o', 'o', 'o', 'o', 'o'}, {' ', ' ', ' ', ' ', ' '}, {' ', ' ', ' ', ' ', ' '}, {' ', ' ', ' ', ' ', ' '}, {' ', ' ', ' ', ' ', ' '}};
        assertTrue(ctrl.setBoard(board));
        assertEquals(' ', ctrl.checkWinner_hor());
        board = new char[][]{{' ', ' ', 'o', ' ', ' '}, {' ', ' ', 'o', ' ', ' '}, {' ', ' ', 'o', ' ', ' '}, {' ', ' ', 'o', ' ', ' '}, {' ', ' ', 'o', ' ', ' '}};
        assertTrue(ctrl.setBoard(board));
        assertEquals('o', ctrl.checkWinner_hor());

    }

    @Test
    void checkWinner_vert() {
        GameController ctrl = new GameController(5, 1, true);
        char board[][] = {{' ', ' ', ' ', ' ', ' '}, {' ', ' ', ' ', ' ', ' '}, {' ', ' ', ' ', ' ', ' '}, {' ', ' ', ' ', ' ', ' '}, {' ', ' ', ' ', ' ', ' '}};
        assertTrue(ctrl.setBoard(board));
        assertEquals(' ', ctrl.checkWinner_vert());

        board = new char[][]{{'x', ' ', ' ', ' ', ' '}, {'x', ' ', ' ', ' ', ' '}, {'x', ' ', ' ', ' ', ' '}, {'x', ' ', ' ', ' ', ' '}, {'x', ' ', ' ', ' ', ' '}};
        assertTrue(ctrl.setBoard(board));
        assertEquals(' ', ctrl.checkWinner_vert());

        board = new char[][]{{'x', 'x', 'x', 'x', 'x'}, {' ', ' ', ' ', ' ', ' '}, {' ', ' ', ' ', ' ', ' '}, {' ', ' ', ' ', ' ', ' '}, {' ', ' ', ' ', ' ', ' '}};
        assertTrue(ctrl.setBoard(board));
        assertEquals('x', ctrl.checkWinner_vert());


        board = new char[][]{{'o', 'o', 'o', 'o', 'o'}, {' ', ' ', ' ', ' ', ' '}, {' ', ' ', ' ', ' ', ' '}, {' ', ' ', ' ', ' ', ' '}, {' ', ' ', ' ', ' ', ' '}};
        assertTrue(ctrl.setBoard(board));
        assertEquals('o', ctrl.checkWinner_vert());

        board = new char[][]{{' ', ' ', 'o', ' ', ' '}, {' ', ' ', 'o', ' ', ' '}, {' ', ' ', 'o', ' ', ' '}, {' ', ' ', 'o', ' ', ' '}, {' ', ' ', 'o', ' ', ' '}};
        assertTrue(ctrl.setBoard(board));
        assertEquals(' ', ctrl.checkWinner_vert());

    }

    @Test
    void checkWinner_diag1() {
        GameController ctrl = new GameController(5, 1, true);
        char board[][] = {{' ', ' ', ' ', ' ', ' '}, {' ', ' ', ' ', ' ', ' '}, {' ', ' ', ' ', ' ', ' '}, {' ', ' ', ' ', ' ', ' '}, {' ', ' ', ' ', ' ', ' '}};
        assertTrue(ctrl.setBoard(board));
        assertEquals(' ', ctrl.checkWinner_diag1());

        board = new char[][]{{'x', 'x', 'x', 'x', 'x'}, {' ', ' ', ' ', ' ', ' '}, {' ', ' ', ' ', ' ', ' '}, {' ', ' ', ' ', ' ', ' '}, {' ', ' ', ' ', ' ', ' '}};
        assertTrue(ctrl.setBoard(board));
        assertEquals(' ', ctrl.checkWinner_diag1());

        board = new char[][]{{'x', ' ', ' ', ' ', ' '}, {' ', 'x', ' ', ' ', ' '}, {' ', ' ', 'x', ' ', ' '}, {' ', ' ', ' ', 'x', ' '}, {' ', ' ', ' ', ' ', 'x'}};
        assertTrue(ctrl.setBoard(board));
        assertEquals('x', ctrl.checkWinner_diag1());

        board = new char[][]{{'o', 'o', 'o', 'o', 'o'}, {' ', ' ', ' ', ' ', ' '}, {' ', ' ', ' ', ' ', ' '}, {' ', ' ', ' ', ' ', ' '}, {' ', ' ', ' ', ' ', ' '}};
        assertTrue(ctrl.setBoard(board));
        assertEquals(' ', ctrl.checkWinner_diag1());

        board = new char[][]{{'o', ' ', ' ', ' ', ' '}, {' ', 'x', ' ', ' ', ' '}, {' ', ' ', 'o', ' ', ' '}, {' ', ' ', ' ', 'o', ' '}, {' ', ' ', ' ', ' ', 'o'}};
        assertTrue(ctrl.setBoard(board));
        assertEquals(' ', ctrl.checkWinner_diag1());

    }

    @Test
    void checkWinner_diag2() {
        GameController ctrl = new GameController(5, 1, true);
        char board[][] = {{' ', ' ', ' ', ' ', ' '}, {' ', ' ', ' ', ' ', ' '}, {' ', ' ', ' ', ' ', ' '}, {' ', ' ', ' ', ' ', ' '}, {' ', ' ', ' ', ' ', ' '}};
        assertTrue(ctrl.setBoard(board));
        assertEquals(' ', ctrl.checkWinner_diag2());

        board = new char[][]{{'x', 'x', 'x', 'x', 'x'}, {' ', ' ', ' ', ' ', ' '}, {' ', ' ', ' ', ' ', ' '}, {' ', ' ', ' ', ' ', ' '}, {' ', ' ', ' ', ' ', ' '}};
        assertTrue(ctrl.setBoard(board));
        assertEquals(' ', ctrl.checkWinner_diag2());

        board = new char[][]{{' ', ' ', ' ', ' ', 'o'}, {' ', ' ', ' ', 'o', ' '}, {' ', ' ', 'o', ' ', ' '}, {' ', 'o', ' ', ' ', ' '}, {'o', ' ', ' ', ' ', ' '}};
        assertTrue(ctrl.setBoard(board));
        assertEquals('o', ctrl.checkWinner_diag2());

        board = new char[][]{{'o', 'o', 'o', 'o', 'o'}, {' ', ' ', ' ', ' ', ' '}, {' ', ' ', ' ', ' ', ' '}, {' ', ' ', ' ', ' ', ' '}, {' ', ' ', ' ', ' ', ' '}};
        assertTrue(ctrl.setBoard(board));
        assertEquals(' ', ctrl.checkWinner_diag2());

        board = new char[][]{{'x', ' ', ' ', ' ', ' '}, {' ', 'o', ' ', ' ', ' '}, {' ', ' ', 'o', ' ', ' '}, {' ', ' ', ' ', 'o', ' '}, {' ', ' ', ' ', ' ', 'o'}};
        assertTrue(ctrl.setBoard(board));
        assertEquals(' ', ctrl.checkWinner_diag2());
    }
}