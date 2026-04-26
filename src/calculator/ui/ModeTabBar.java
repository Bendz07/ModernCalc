package calculator.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Custom tab bar for switching between Standard, Scientific, and Programmer modes.
 */
public class ModeTabBar extends JPanel {

    public interface ModeChangeListener {
        void onModeChanged(String mode);
    }

    private static final String[] MODES = {"Standard", "Scientific", "Programmer"};
    private int selectedIndex = 0;
    private int hoveredIndex  = -1;
    private ModeChangeListener listener;

    public ModeTabBar() {
        setOpaque(false);
        setPreferredSize(new Dimension(400, 40));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int idx = getTabAt(e.getX());
                if (idx >= 0 && idx != selectedIndex) {
                    selectedIndex = idx;
                    repaint();
                    if (listener != null) listener.onModeChanged(MODES[idx]);
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                hoveredIndex = -1; repaint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int idx = getTabAt(e.getX());
                if (idx != hoveredIndex) {
                    hoveredIndex = idx; repaint();
                }
            }
        });
    }

    public void setModeChangeListener(ModeChangeListener l) { this.listener = l; }

    public String getSelectedMode() { return MODES[selectedIndex]; }

    public void selectMode(String mode) {
        for (int i = 0; i < MODES.length; i++) {
            if (MODES[i].equals(mode)) { selectedIndex = i; repaint(); break; }
        }
    }

    private int getTabAt(int x) {
        int w = getWidth() / MODES.length;
        int idx = x / w;
        return (idx >= 0 && idx < MODES.length) ? idx : -1;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        int totalW = getWidth(), h = getHeight();
        int tabW = totalW / MODES.length;

        // Background strip
        g2.setColor(Theme.BG_TAB_BAR);
        g2.fill(new RoundRectangle2D.Float(0, 0, totalW, h, 10, 10));

        g2.setFont(Theme.FONT_TAB);
        FontMetrics fm = g2.getFontMetrics();

        for (int i = 0; i < MODES.length; i++) {
            int x = i * tabW;
            boolean active = (i == selectedIndex);
            boolean hovered = (i == hoveredIndex);

            if (active) {
                // Active pill
                g2.setColor(Theme.TAB_ACTIVE_BG);
                g2.fill(new RoundRectangle2D.Float(x + 4, 4, tabW - 8, h - 8, 8, 8));
            } else if (hovered) {
                g2.setColor(Theme.TAB_HOVER_BG);
                g2.fill(new RoundRectangle2D.Float(x + 4, 4, tabW - 8, h - 8, 8, 8));
            }

            // Label
            String label = MODES[i];
            int tx = x + (tabW - fm.stringWidth(label)) / 2;
            int ty = (h - fm.getHeight()) / 2 + fm.getAscent();
            g2.setColor(active ? Theme.TAB_ACTIVE_FG : Theme.TAB_IDLE_FG);
            g2.drawString(label, tx, ty);
        }

        // Separator lines
        g2.setColor(Theme.BORDER_SUBTLE);
        for (int i = 1; i < MODES.length; i++) {
            int x = i * tabW;
            g2.drawLine(x, 8, x, h - 8);
        }

        g2.dispose();
    }
}
