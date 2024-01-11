package ui;

import javax.swing.JButton;

public class PositionAwareButton extends JButton {
    
    private static final long serialVersionUID = 1L;
    private int across;
    private int down;

    public PositionAwareButton(final String label, final int across, final int down) {
        super(label);
        this.across = across;
        this.down = down;
    }

    // getters and setters for across and down
    public int getAcross() {
        return across;
    }

    public void setAcross(final int across) {
        this.across = across;
    }

    public int getDown() {
        return down;
    }

    public void setDown(final int down) {
        this.down = down;
    }
}