import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class MyButtonTest {
    @Test
    void test(){
        MyButton b = new MyButton();
        assertEquals(Color.black,b.getBackground());
    }

}