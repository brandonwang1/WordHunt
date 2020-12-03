import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseHelper extends MouseAdapter {

    LetterGrid lg;
    public MouseHelper(LetterGrid lg) {
        super();
        this.lg = lg;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        lg.setMouseDown(true);
//        System.out.println("Mouse Pressed");
        mouseEntered(e); // So we get the current letter in
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
//        System.out.println("Mouse Up");
        lg.evaluateWord();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        super.mouseEntered(e);
        if (lg.isMouseDown()) {
            Letter l = ((Letter)e.getSource());
            if (!l.isSelected()) {
//                System.out.println(l.letter);
                lg.getCurrentWord().add(l.letter);
                l.setSelected(true);
            }
        }
    }
}
