import javax.swing.*;
import java.awt.*;

public class MyButton extends JButton {
    MyButton(){
        super();
        setBackground(Color.BLACK);
        setForeground(Color.white);
        //Szürke keret eltüntetése a gomboknál
        setFocusPainted(false);
    }

    public MyButton(String text) {
        super(text);
        setBackground(Color.BLACK);
        setForeground(Color.white);
        //Szürke keret eltüntetése a gomboknál
        setFocusPainted(false);
    }
}
