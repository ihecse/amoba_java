import javax.swing.*;
import java.awt.*;

public class MyLabel extends JLabel {
    MyLabel() {
        super();
        setBackground(Color.BLACK);
        setForeground(Color.white);
    }

    MyLabel(String s) {
        super(s);
        setBackground(Color.BLACK);
        setForeground(Color.white);
    }
}
