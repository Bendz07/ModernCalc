package calculator.ui;

import calculator.util.BaseConverter;
import calculator.util.BaseConverter.Base;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Programmer calculator mode with binary/octal/decimal/hex displays,
 * bitwise operations, and hex digit buttons.
 */
public class ProgrammerPanel extends JPanel {

    public interface ButtonHandler {
        void onButton(String action);
    }

    private ButtonHandler handler;
    private Base currentBase = Base.DEC;

    // Multi-base display labels
    private JLabel binLabel, octLabel, decLabel, hexLabel;
    private JLabel binVal, octVal, decVal, hexVal;

    // Hex digit buttons (A-F)
    private CalcButton[] hexDigitBtns = new CalcButton[6];

    // Base selector buttons
    private CalcButton btnBin, btnOct, btnDec, btnHex;

    public ProgrammerPanel() {
        setOpaque(false);
        setLayout(new BorderLayout(0, 8));
        buildLayout();
    }

    public void setButtonHandler(ButtonHandler h) { this.handler = h; }

    private void buildLayout() {
        removeAll();

        // ─── Top: Multi-base value display ────────────────────────────────
        JPanel baseDisplay = new JPanel(new GridLayout(4, 2, 6, 4));
        baseDisplay.setOpaque(false);
        baseDisplay.setBorder(new EmptyBorder(0, 0, 4, 0));

        String[] labels = {"BIN", "OCT", "DEC", "HEX"};
        Color[] colors  = {Theme.ACCENT_GREEN, Theme.ACCENT_AMBER, Theme.ACCENT_BLUE, Theme.ACCENT_PURPLE};

        binLabel = makeBaseLabel("BIN", colors[0]); binVal = makeBaseValue();
        octLabel = makeBaseLabel("OCT", colors[1]); octVal = makeBaseValue();
        decLabel = makeBaseLabel("DEC", colors[2]); decVal = makeBaseValue();
        hexLabel = makeBaseLabel("HEX", colors[3]); hexVal = makeBaseValue();

        baseDisplay.add(binLabel); baseDisplay.add(binVal);
        baseDisplay.add(octLabel); baseDisplay.add(octVal);
        baseDisplay.add(decLabel); baseDisplay.add(decVal);
        baseDisplay.add(hexLabel); baseDisplay.add(hexVal);

        // ─── Button grid ──────────────────────────────────────────────────
        JPanel buttonArea = new JPanel(new BorderLayout(4, 4));
        buttonArea.setOpaque(false);

        // Base selectors row
        JPanel baseRow = new JPanel(new GridLayout(1, 4, 4, 0));
        baseRow.setOpaque(false);
        btnBin = makeSelectorBtn("BIN", "BASE_BIN");
        btnOct = makeSelectorBtn("OCT", "BASE_OCT");
        btnDec = makeSelectorBtn("DEC", "BASE_DEC");
        btnHex = makeSelectorBtn("HEX", "BASE_HEX");
        baseRow.add(btnBin); baseRow.add(btnOct); baseRow.add(btnDec); baseRow.add(btnHex);
        highlightBase(currentBase);

        // Main button grid
        JPanel mainGrid = new JPanel(new GridLayout(5, 8, 4, 4));
        mainGrid.setOpaque(false);

        // Row 1: Bit-width + bitwise
        Object[][] row1 = {
            {"AND",  CalcButton.Style.BITWISE, "AND" },
            {"OR",   CalcButton.Style.BITWISE, "OR"  },
            {"XOR",  CalcButton.Style.BITWISE, "XOR" },
            {"NOT",  CalcButton.Style.BITWISE, "NOT" },
            {"LSH",  CalcButton.Style.BITWISE, "LSH" },
            {"RSH",  CalcButton.Style.BITWISE, "RSH" },
            {"CE",   CalcButton.Style.FUNCTION,"CE"  },
            {"C",    CalcButton.Style.CLEAR,   "C"   },
        };
        // Row 2: Hex digits + utility
        Object[][] row2 = {
            {"A",    CalcButton.Style.HEX_DIGIT, "A" },
            {"B",    CalcButton.Style.HEX_DIGIT, "B" },
            {"C",    CalcButton.Style.HEX_DIGIT, "C" },  // hex C, not clear
            {"D",    CalcButton.Style.HEX_DIGIT, "D" },
            {"E",    CalcButton.Style.HEX_DIGIT, "HE" },
            {"F",    CalcButton.Style.HEX_DIGIT, "F" },
            {"⌫",    CalcButton.Style.FUNCTION,  "BSP"},
            {"÷",    CalcButton.Style.OPERATOR,  "÷" },
        };
        // Row 3
        Object[][] row3 = {
            {"7",    CalcButton.Style.NUMBER,    "7"  },
            {"8",    CalcButton.Style.NUMBER,    "8"  },
            {"9",    CalcButton.Style.NUMBER,    "9"  },
            {"4",    CalcButton.Style.NUMBER,    "4"  },
            {"5",    CalcButton.Style.NUMBER,    "5"  },
            {"6",    CalcButton.Style.NUMBER,    "6"  },
            {"±",    CalcButton.Style.FUNCTION,  "NEG"},
            {"×",    CalcButton.Style.OPERATOR,  "×" },
        };
        // Row 4
        Object[][] row4 = {
            {"1",    CalcButton.Style.NUMBER,    "1"  },
            {"2",    CalcButton.Style.NUMBER,    "2"  },
            {"3",    CalcButton.Style.NUMBER,    "3"  },
            {"0",    CalcButton.Style.NUMBER,    "0"  },
            {"00",   CalcButton.Style.NUMBER,    "00" },
            {"MOD",  CalcButton.Style.FUNCTION,  "%"  },
            {"-",    CalcButton.Style.OPERATOR,  "-"  },
            {"+",    CalcButton.Style.OPERATOR,  "+"  },
        };
        // Row 5: Equals + memory
        Object[][] row5 = {
            {"MC",   CalcButton.Style.MEMORY,    "MC" },
            {"MR",   CalcButton.Style.MEMORY,    "MR" },
            {"MS",   CalcButton.Style.MEMORY,    "MS" },
            {"M+",   CalcButton.Style.MEMORY,    "M+" },
            {"M-",   CalcButton.Style.MEMORY,    "M-" },
            {"",     CalcButton.Style.FUNCTION,  ""   },  // spacer
            {"",     CalcButton.Style.FUNCTION,  ""   },  // spacer
            {"=",    CalcButton.Style.EQUALS,    "="  },
        };

        int hexIdx = 0;
        for (Object[][] row : new Object[][][]{row1, row2, row3, row4, row5}) {
            for (Object[] spec : row) {
                String label  = (String) spec[0];
                CalcButton.Style style = (CalcButton.Style) spec[1];
                String action = (String) spec[2];

                if (label.isEmpty()) {
                    // Spacer — invisible panel
                    JPanel spacer = new JPanel();
                    spacer.setOpaque(false);
                    mainGrid.add(spacer);
                    continue;
                }

                // Override hex C button action — use "HC" to not conflict with Clear
                if ("C".equals(action) && CalcButton.Style.HEX_DIGIT == style) action = "HC";

                CalcButton btn = new CalcButton(label, style);
                final String finalAction = action;
                btn.addActionListener(e -> { if (handler != null) handler.onButton(finalAction); });

                // Track hex letter buttons for enabling/disabling
                if (style == CalcButton.Style.HEX_DIGIT && hexIdx < 6) {
                    hexDigitBtns[hexIdx++] = btn;
                }
                mainGrid.add(btn);
            }
        }

        buttonArea.add(baseRow, BorderLayout.NORTH);
        buttonArea.add(mainGrid, BorderLayout.CENTER);

        add(baseDisplay, BorderLayout.NORTH);
        add(buttonArea, BorderLayout.CENTER);

        updateBaseButtons();
        revalidate(); repaint();
    }

    private JLabel makeBaseLabel(String text, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(Theme.FONT_LABEL);
        l.setForeground(color);
        l.setBorder(new EmptyBorder(2, 4, 2, 0));
        return l;
    }

    private JLabel makeBaseValue() {
        JLabel l = new JLabel("0");
        l.setFont(Theme.FONT_BASE_DISPLAY);
        l.setForeground(Theme.TEXT_HISTORY);
        l.setHorizontalAlignment(SwingConstants.RIGHT);
        return l;
    }

    private CalcButton makeSelectorBtn(String label, String action) {
        CalcButton btn = new CalcButton(label, CalcButton.Style.FUNCTION);
        btn.addActionListener(e -> {
            if (handler != null) handler.onButton(action);
        });
        return btn;
    }

    /**
     * Update all four base displays for the given long value.
     */
    public void updateBaseDisplays(long value) {
        binVal.setText(Long.toBinaryString(value));
        octVal.setText(Long.toOctalString(value));
        decVal.setText(Long.toString(value));
        hexVal.setText(Long.toHexString(value).toUpperCase());
    }

    public void setCurrentBase(Base base) {
        this.currentBase = base;
        highlightBase(base);
        updateBaseButtons();
    }

    public Base getCurrentBase() { return currentBase; }

    private void highlightBase(Base base) {
        Color active = Theme.ACCENT_BLUE;
        Color idle   = Theme.TEXT_HISTORY;

        binLabel.setForeground(base == Base.BIN ? Theme.ACCENT_GREEN  : Theme.TEXT_HISTORY);
        octLabel.setForeground(base == Base.OCT ? Theme.ACCENT_AMBER  : Theme.TEXT_HISTORY);
        decLabel.setForeground(base == Base.DEC ? Theme.ACCENT_BLUE   : Theme.TEXT_HISTORY);
        hexLabel.setForeground(base == Base.HEX ? Theme.ACCENT_PURPLE : Theme.TEXT_HISTORY);

        binVal.setForeground(base == Base.BIN ? Theme.TEXT_DISPLAY : Theme.TEXT_HISTORY);
        octVal.setForeground(base == Base.OCT ? Theme.TEXT_DISPLAY : Theme.TEXT_HISTORY);
        decVal.setForeground(base == Base.DEC ? Theme.TEXT_DISPLAY : Theme.TEXT_HISTORY);
        hexVal.setForeground(base == Base.HEX ? Theme.TEXT_DISPLAY : Theme.TEXT_HISTORY);
    }

    /**
     * Enable/disable number buttons and hex letters based on current base.
     */
    private void updateBaseButtons() {
        // Hex digit buttons (A-F): only in HEX
        for (CalcButton b : hexDigitBtns) {
            if (b != null) b.setEnabled(currentBase == Base.HEX);
        }
    }
}
