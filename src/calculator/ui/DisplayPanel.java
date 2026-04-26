package calculator.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * The calculator's display area: shows expression history above
 * and the current input/result below in large text.
 */
public class DisplayPanel extends JPanel {

    private String mainText    = "0";
    private String historyText = "";
    private String modeIndicator = "STD";
    private boolean isError    = false;

    // Dynamic font scaling based on text length
    private static final int MAX_CHARS_LARGE = 10;
    private static final int MAX_CHARS_MED   = 16;

    public DisplayPanel() {
        setOpaque(false);
        setPreferredSize(new Dimension(400, 120));
    }

    public void setMainText(String text)    { this.mainText = text; isError = false; repaint(); }
    public void setHistoryText(String text) { this.historyText = text; repaint(); }
    public void setModeIndicator(String m)  { this.modeIndicator = m; repaint(); }
    public void setError(String msg)        { this.mainText = msg; isError = true; repaint(); }
    public String getMainText()             { return mainText; }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        int w = getWidth(), h = getHeight();
        int pad = 16;

        // Background
        g2.setColor(Theme.BG_DISPLAY);
        g2.fill(new RoundRectangle2D.Float(0, 0, w, h, 14, 14));

        // Mode indicator badge
        String badge = modeIndicator;
        g2.setFont(Theme.FONT_LABEL);
        FontMetrics bfm = g2.getFontMetrics();
        int bw = bfm.stringWidth(badge) + 12;
        int bh = bfm.getHeight() + 4;
        int bx = pad, by = pad;
        g2.setColor(new Color(0x00, 0x8B, 0xD8, 60));
        g2.fill(new RoundRectangle2D.Float(bx, by, bw, bh, 6, 6));
        g2.setColor(Theme.ACCENT_BLUE);
        g2.drawString(badge, bx + 6, by + bfm.getAscent() + 2);

        // History text (right-aligned, dimmed)
        if (historyText != null && !historyText.isEmpty()) {
            g2.setFont(Theme.FONT_HISTORY);
            FontMetrics hfm = g2.getFontMetrics();
            String hist = historyText;
            // Truncate if too long
            while (hfm.stringWidth(hist) > w - 2 * pad && hist.length() > 3) {
                hist = "…" + hist.substring(Math.min(hist.length(), 4));
            }
            g2.setColor(Theme.TEXT_HISTORY);
            g2.drawString(hist, w - pad - hfm.stringWidth(hist), h / 2 - 2);
        }

        // Main display text (right-aligned, adaptive size)
        Font displayFont = pickFont(mainText);
        g2.setFont(displayFont);
        FontMetrics dfm = g2.getFontMetrics();

        String display = mainText;
        // Truncate if still overflows
        while (dfm.stringWidth(display) > w - 2 * pad - 10 && display.length() > 1) {
            display = display.substring(0, display.length() - 1);
            if (!display.endsWith("…")) display += "…";
        }

        int tx = w - pad - dfm.stringWidth(display);
        int ty = h - pad - 4;

        g2.setColor(isError ? Theme.ACCENT_RED : Theme.TEXT_DISPLAY);
        g2.drawString(display, tx, ty);

        // Subtle bottom border glow
        GradientPaint glow = new GradientPaint(
            0, h - 2, new Color(0, 139, 216, 60),
            w, h - 2, new Color(156, 64, 255, 40)
        );
        g2.setPaint(glow);
        g2.fillRect(0, h - 2, w, 2);

        g2.dispose();
    }

    private Font pickFont(String text) {
        int len = text.length();
        if (len <= MAX_CHARS_LARGE) return Theme.FONT_DISPLAY_LARGE;
        if (len <= MAX_CHARS_MED)   return Theme.FONT_DISPLAY_MED;
        return Theme.FONT_DISPLAY_SMALL;
    }
}
