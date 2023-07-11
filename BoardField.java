import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/***
 * A játékpályának kezeléséért felelős.
 */
public class BoardField extends JPanel implements MouseListener {

    int x;
    int y;
    int width;
    boolean hovering;
    boolean clicked;
    GameController gc;

    public BoardField(GameController gc1, int x, int y, int width) //konstr.
    {
        this.width = width;
        this.x = x;
        this.y = y;
        this.gc = gc1;
        gc.registerField(this);
        this.setBorder(new EmptyBorder(0, 0, 0, 0));
        this.setMinimumSize(new Dimension(width, width));
        this.addMouseListener(this);
    }

    /***
     * Kirajzolja az elhelyezett X vagy kör bábut, valamint a játékpályának egy mezőjét.
     * @param _g
     */

    public void paintComponent(Graphics _g)
    {
        super.paintComponent(_g);
        Graphics2D g = (Graphics2D) _g;
        g.setColor(Color.black);
        g.fillRect(0, 0, width, width);
        if (clicked) {
            g.setColor(Color.lightGray);
        } else if (hovering) {
            g.setColor(Color.gray);
        }
        int gap = (int) (width * 0.1); //arányszám h milyen vastag legyen pl az X
        g.fillRect(0, 0, width, width);
        g.setColor(Color.red);
        g.setStroke(new BasicStroke(gap / 2));
        g.setColor(Color.white);
        g.drawRect(0, 0, width, width);
        g.setStroke(new BasicStroke(gap));
        if (gc.getFieldState(this) == 'x') {
            //X rajzolása
            g.setColor(Color.red);
            g.drawLine(gap * 2, gap * 2, width - gap * 2, width - gap * 2);
            g.drawLine(gap * 2, width - gap * 2, width - gap * 2, gap * 2);
        }
        if (gc.getFieldState(this) == 'o') {
            //kor rajzolása
            g.setStroke(new BasicStroke(gap/2));
            g.setColor(Color.blue);
            g.drawOval(gap,gap,width-gap*2,width-gap*2);
        }

    }

    /***
     * Ha rákattintok az adott objektumra, meghívódik a gc (GameController) clicked függvénye.
     * @param e
     */
    public void mouseClicked(MouseEvent e)
    {
        gc.clicked(this);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        clicked = true;
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        clicked = false;
        gc.clicked(this);
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        hovering = true;
        repaint();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        hovering = false;
        repaint();
    }

}

