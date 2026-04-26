package calculator.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

/**
 * A custom-painted button with rounded corners, hover/press animations,
 * and per-button color theming. Replaces plain JButton for the calculator.
 */
public class CalcButton extends JButton {

    public enum Style {
        NUMBER, OPERATOR, FUNCTION, EQUALS, CLEAR,
        TRIG, LOG, MEMORY, BITWISE, HEX_DIGIT, DISABLED
    }

    private final Style style;
    private Color normalBg;
    private Color hoverBg;
    private Color textColor;

    private boolean hovered = false;
    private boolean pressed = false;
    private float pressAlpha = 0f;

    // Optional: smaller sub-label (e.g., "sin⁻¹" above "sin")
    private String subLabel = null;

    public CalcButton(String text, Style style) {
        super(text);
        this.style = style;
        applyStyle(style);
        setupBehavior();
    }

    public CalcButton(String text, Style style, String subLabel) {
        this(text, style);
        this.subLabel = subLabel;
    }

    private void applyStyle(Style s) {
        switch (s) {
            case NUMBER:
                normalBg = Theme.BTN_NUMBER;    hoverBg = Theme.BTN_NUMBER_H;
                textColor = Theme.TEXT_NUMBER; break;
            case OPERATOR:
                normalBg = Theme.BTN_OPERATOR;  hoverBg = Theme.BTN_OPERATOR_H;
                textColor = Theme.TEXT_OPERATOR; break;
            case FUNCTION:
                normalBg = Theme.BTN_FUNCTION;  hoverBg = Theme.BTN_FUNCTION_H;
                textColor = Theme.TEXT_FUNCTION; break;
            case EQUALS:
                normalBg = Theme.BTN_EQUALS;    hoverBg = Theme.BTN_EQUALS_H;
                textColor = Theme.TEXT_EQUALS; break;
            case CLEAR:
                normalBg = Theme.BTN_CLEAR;     hoverBg = Theme.BTN_CLEAR_H;
                textColor = Theme.TEXT_CLEAR; break;
            case TRIG:
                normalBg = Theme.BTN_TRIG;      hoverBg = Theme.BTN_TRIG_H;
                textColor = Theme.TEXT_TRIG; break;
            case LOG:
                normalBg = Theme.BTN_LOG;       hoverBg = Theme.BTN_LOG_H;
                textColor = Theme.TEXT_LOG; break;
            case MEMORY:
                normalBg = Theme.BTN_MEMORY;    hoverBg = Theme.BTN_MEMORY_H;
                textColor = Theme.TEXT_MEMORY; break;
            case BITWISE:
                normalBg = Theme.BTN_BITWISE;   hoverBg = Theme.BTN_BITWISE_H;
                textColor = Theme.TEXT_BITWISE; break;
            case HEX_DIGIT:
                normalBg = Theme.BTN_HEX_DIGIT; hoverBg = Theme.BTN_HEX_DIGIT_H;
                textColor = Theme.TEXT_HEX_DIGIT; break;
            case DISABLED:
            default:
                normalBg = Theme.BTN_FUNCTION;  hoverBg = Theme.BTN_FUNCTION;
                textColor = Theme.TEXT_DISABLED; break;
        }

        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setForeground(textColor);

        Font f = (style == Style.FUNCTION || style == Style.TRIG || style == Style.LOG
                  || style == Style.MEMORY || style == Style.BITWISE)
                 ? Theme.FONT_BTN_SMALL : Theme.FONT_BTN_LARGE;
        setFont(f);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void setupBehavior() {
        addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                if (!isEnabled()) return;
                hovered = true; repaint();
            }
            @Override public void mouseExited(MouseEvent e) {
                hovered = false; pressed = false; repaint();
            }
            @Override public void mousePressed(MouseEvent e) {
                if (!isEnabled()) return;
                pressed = true; repaint();
            }
            @Override public void mouseReleased(MouseEvent e) {
                pressed = false; repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        int w = getWidth(), h = getHeight();
        int arc = 10;

        // Background
        Color bg = (hovered && isEnabled()) ? hoverBg : normalBg;
        if (pressed && isEnabled()) {
            bg = bg.darker();
        }
        g2.setColor(bg);
        g2.fill(new RoundRectangle2D.Float(1, 1, w - 2, h - 2, arc, arc));

        // Border
        if (hovered && isEnabled()) {
            g2.setColor(new Color(255, 255, 255, 25));
            g2.setStroke(new BasicStroke(1f));
            g2.draw(new RoundRectangle2D.Float(1, 1, w - 2, h - 2, arc, arc));
        } else {
            g2.setColor(Theme.BORDER_SUBTLE);
            g2.setStroke(new BasicStroke(0.5f));
            g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, w - 1, h - 1, arc, arc));
        }

        // Press effect - light flash
        if (pressed) {
            g2.setColor(new Color(255, 255, 255, 20));
            g2.fill(new RoundRectangle2D.Float(1, 1, w - 2, h - 2, arc, arc));
        }

        // Text
        String label = getText();
        Color fg = isEnabled() ? textColor : Theme.TEXT_DISABLED;
        g2.setColor(fg);

        if (subLabel != null) {
            // Draw sub-label at top in smaller font
            Font smallFont = Theme.FONT_LABEL;
            g2.setFont(smallFont);
            FontMetrics sfm = g2.getFontMetrics();
            int sx = (w - sfm.stringWidth(subLabel)) / 2;
            int sy = h / 2 - 2;
            g2.setColor(new Color(fg.getRed(), fg.getGreen(), fg.getBlue(), 160));
            g2.drawString(subLabel, sx, sy);

            // Main label below
            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            int tx = (w - fm.stringWidth(label)) / 2;
            int ty = h / 2 + fm.getAscent() / 2 + 4;
            g2.setColor(fg);
            g2.drawString(label, tx, ty);
        } else {
            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            int tx = (w - fm.stringWidth(label)) / 2;
            int ty = (h - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(label, tx, ty);
        }

        g2.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(68, 52);
    }
}
