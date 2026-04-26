package calculator;

import calculator.ui.CalculatorFrame;
import calculator.ui.Theme;

import javax.swing.*;

/**
 * Application entry point. Sets Look & Feel and launches the calculator.
 */
public class Main {

    public static void main(String[] args) {
        // Set system properties for better rendering on all platforms
        System.setProperty("awt.useSystemAAFontSettings", "lcd");
        System.setProperty("swing.aatext", "true");
        System.setProperty("sun.java2d.opengl", "true");

        SwingUtilities.invokeLater(() -> {
            try {
                // Use system L&F as base, then override everything with custom painting
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // Fall through to default
            }

            // Global UI defaults
            UIManager.put("Panel.background",      Theme.BG_WINDOW);
            UIManager.put("Frame.background",      Theme.BG_WINDOW);
            UIManager.put("OptionPane.background", Theme.BG_WINDOW);

            CalculatorFrame frame = new CalculatorFrame();
            frame.setVisible(true);
        });
    }
}
