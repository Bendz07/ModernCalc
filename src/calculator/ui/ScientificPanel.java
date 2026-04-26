package calculator.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Scientific calculator mode panel with trig, log, power, and advanced functions.
 */
public class ScientificPanel extends JPanel {

    public interface ButtonHandler {
        void onButton(String action);
    }

    private ButtonHandler handler;
    private boolean isDegrees = true;  // DEG vs RAD mode

    private static final Object[][] BUTTONS = {
        // Row 0: Angle & constants
        { "DEG",  CalcButton.Style.FUNCTION, "DEGRAD" },
        { "π",    CalcButton.Style.FUNCTION, "PI"     },
        { "e",    CalcButton.Style.FUNCTION, "E"      },
        { "EXP",  CalcButton.Style.FUNCTION, "EXP"    },
        { "MC",   CalcButton.Style.MEMORY,   "MC"     },
        { "MR",   CalcButton.Style.MEMORY,   "MR"     },
        { "M+",   CalcButton.Style.MEMORY,   "M+"     },
        { "M-",   CalcButton.Style.MEMORY,   "M-"     },

        // Row 1: Trig
        { "sin",  CalcButton.Style.TRIG,     "SIN"    },
        { "cos",  CalcButton.Style.TRIG,     "COS"    },
        { "tan",  CalcButton.Style.TRIG,     "TAN"    },
        { "sin⁻¹",CalcButton.Style.TRIG,     "ASIN"   },
        { "cos⁻¹",CalcButton.Style.TRIG,     "ACOS"   },
        { "tan⁻¹",CalcButton.Style.TRIG,     "ATAN"   },
        { "C",    CalcButton.Style.CLEAR,    "C"      },
        { "⌫",    CalcButton.Style.FUNCTION, "BSP"    },

        // Row 2: Log & exp
        { "log",  CalcButton.Style.LOG,      "LOG10"  },
        { "ln",   CalcButton.Style.LOG,      "LN"     },
        { "10ˣ",  CalcButton.Style.LOG,      "10X"    },
        { "eˣ",   CalcButton.Style.LOG,      "EX"     },
        { "2ˣ",   CalcButton.Style.LOG,      "2X"     },
        { "log₂", CalcButton.Style.LOG,      "LOG2"   },
        { "÷",    CalcButton.Style.OPERATOR, "÷"      },
        { "×",    CalcButton.Style.OPERATOR, "×"      },

        // Row 3: Powers & roots
        { "x²",   CalcButton.Style.FUNCTION, "SQ"     },
        { "x³",   CalcButton.Style.FUNCTION, "CUBE"   },
        { "xʸ",   CalcButton.Style.FUNCTION, "POW"    },
        { "√x",   CalcButton.Style.FUNCTION, "SQRT"   },
        { "∛x",   CalcButton.Style.FUNCTION, "CBRT"   },
        { "ʸ√x",  CalcButton.Style.FUNCTION, "YROOT"  },
        { "-",    CalcButton.Style.OPERATOR, "-"      },
        { "+",    CalcButton.Style.OPERATOR, "+"      },

        // Row 4: Misc + numbers
        { "n!",   CalcButton.Style.FUNCTION, "FACT"   },
        { "1/x",  CalcButton.Style.FUNCTION, "1/x"    },
        { "|x|",  CalcButton.Style.FUNCTION, "ABS"    },
        { "±",    CalcButton.Style.FUNCTION, "NEG"    },
        { "%",    CalcButton.Style.FUNCTION, "%"      },
        { "CE",   CalcButton.Style.FUNCTION, "CE"     },
        { "7",    CalcButton.Style.NUMBER,   "7"      },
        { "8",    CalcButton.Style.NUMBER,   "8"      },

        // Row 5
        { "(",    CalcButton.Style.FUNCTION, "LPAR"   },
        { ")",    CalcButton.Style.FUNCTION, "RPAR"   },
        { "9",    CalcButton.Style.NUMBER,   "9"      },
        { "4",    CalcButton.Style.NUMBER,   "4"      },
        { "5",    CalcButton.Style.NUMBER,   "5"      },
        { "6",    CalcButton.Style.NUMBER,   "6"      },
        { "1",    CalcButton.Style.NUMBER,   "1"      },
        { "2",    CalcButton.Style.NUMBER,   "2"      },

        // Row 6
        { "3",    CalcButton.Style.NUMBER,   "3"      },
        { "0",    CalcButton.Style.NUMBER,   "0"      },
        { ".",    CalcButton.Style.NUMBER,   "."      },
        { "=",    CalcButton.Style.EQUALS,   "="      },
        { null, null, null }, // spacer
        { null, null, null },
        { null, null, null },
        { null, null, null },
    };

    // Better layout: two distinct areas
    // Left: scientific functions grid (6×6), Right: number pad

    public ScientificPanel() {
        setOpaque(false);
        setLayout(new BorderLayout(8, 0));
        buildLayout();
    }

    public void setButtonHandler(ButtonHandler h) { this.handler = h; }

    private JButton degRadBtn;

    private void buildLayout() {
        removeAll();

        // ─── Left scientific functions ─────────────────────────────────────
        JPanel sciGrid = new JPanel(new GridLayout(7, 4, 4, 4));
        sciGrid.setOpaque(false);

        Object[][] sciButtons = {
            {"DEG",   CalcButton.Style.FUNCTION, "DEGRAD"},
            {"π",     CalcButton.Style.FUNCTION, "PI"    },
            {"e",     CalcButton.Style.FUNCTION, "E"     },
            {"EXP",   CalcButton.Style.FUNCTION, "EXP"   },

            {"sin",   CalcButton.Style.TRIG,     "SIN"   },
            {"cos",   CalcButton.Style.TRIG,     "COS"   },
            {"tan",   CalcButton.Style.TRIG,     "TAN"   },
            {"n!",    CalcButton.Style.FUNCTION, "FACT"  },

            {"sin⁻¹", CalcButton.Style.TRIG,     "ASIN"  },
            {"cos⁻¹", CalcButton.Style.TRIG,     "ACOS"  },
            {"tan⁻¹", CalcButton.Style.TRIG,     "ATAN"  },
            {"|x|",   CalcButton.Style.FUNCTION, "ABS"   },

            {"log",   CalcButton.Style.LOG,      "LOG10" },
            {"ln",    CalcButton.Style.LOG,      "LN"    },
            {"10ˣ",   CalcButton.Style.LOG,      "10X"   },
            {"eˣ",    CalcButton.Style.LOG,      "EX"    },

            {"log₂",  CalcButton.Style.LOG,      "LOG2"  },
            {"2ˣ",    CalcButton.Style.LOG,      "2X"    },
            {"x²",    CalcButton.Style.FUNCTION, "SQ"    },
            {"x³",    CalcButton.Style.FUNCTION, "CUBE"  },

            {"xʸ",    CalcButton.Style.FUNCTION, "POW"   },
            {"ʸ√x",   CalcButton.Style.FUNCTION, "YROOT" },
            {"√x",    CalcButton.Style.FUNCTION, "SQRT"  },
            {"∛x",    CalcButton.Style.FUNCTION, "CBRT"  },

            {"1/x",   CalcButton.Style.FUNCTION, "1/x"   },
            {"±",     CalcButton.Style.FUNCTION, "NEG"   },
            {"(",     CalcButton.Style.FUNCTION, "LPAR"  },
            {")",     CalcButton.Style.FUNCTION, "RPAR"  },
        };

        for (Object[] spec : sciButtons) {
            String label  = (String) spec[0];
            CalcButton.Style style = (CalcButton.Style) spec[1];
            String action = (String) spec[2];
            CalcButton btn = new CalcButton(label, style);
            if ("DEGRAD".equals(action)) degRadBtn = btn;
            btn.addActionListener(e -> {
                if (handler != null) handler.onButton(action);
                if ("DEGRAD".equals(action)) updateDegRadLabel();
            });
            sciGrid.add(btn);
        }

        // ─── Right number pad ─────────────────────────────────────────────
        JPanel numPad = new JPanel(new GridLayout(6, 4, 4, 4));
        numPad.setOpaque(false);

        Object[][] numButtons = {
            {"%",  CalcButton.Style.FUNCTION, "%"  },
            {"CE", CalcButton.Style.FUNCTION, "CE" },
            {"C",  CalcButton.Style.CLEAR,   "C"  },
            {"⌫",  CalcButton.Style.FUNCTION, "BSP"},

            {"7",  CalcButton.Style.NUMBER,  "7"  },
            {"8",  CalcButton.Style.NUMBER,  "8"  },
            {"9",  CalcButton.Style.NUMBER,  "9"  },
            {"÷",  CalcButton.Style.OPERATOR,"÷"  },

            {"4",  CalcButton.Style.NUMBER,  "4"  },
            {"5",  CalcButton.Style.NUMBER,  "5"  },
            {"6",  CalcButton.Style.NUMBER,  "6"  },
            {"×",  CalcButton.Style.OPERATOR,"×"  },

            {"1",  CalcButton.Style.NUMBER,  "1"  },
            {"2",  CalcButton.Style.NUMBER,  "2"  },
            {"3",  CalcButton.Style.NUMBER,  "3"  },
            {"-",  CalcButton.Style.OPERATOR,"-"  },

            {"0",  CalcButton.Style.NUMBER,  "0"  },
            {"00", CalcButton.Style.NUMBER,  "00" },
            {".",  CalcButton.Style.NUMBER,  "."  },
            {"+",  CalcButton.Style.OPERATOR,"+"  },

            // Equals spans full width conceptually but in grid just one cell
            {"MS", CalcButton.Style.MEMORY,  "MS" },
            {"MR", CalcButton.Style.MEMORY,  "MR" },
            {"M+", CalcButton.Style.MEMORY,  "M+" },
            {"=",  CalcButton.Style.EQUALS,  "="  },
        };

        for (Object[] spec : numButtons) {
            String label  = (String) spec[0];
            CalcButton.Style style = (CalcButton.Style) spec[1];
            String action = (String) spec[2];
            CalcButton btn = new CalcButton(label, style);
            btn.addActionListener(e -> { if (handler != null) handler.onButton(action); });
            numPad.add(btn);
        }

        add(sciGrid, BorderLayout.CENTER);
        add(numPad, BorderLayout.EAST);
        revalidate(); repaint();
    }

    public boolean isDegrees() { return isDegrees; }

    public void toggleDegRad() {
        isDegrees = !isDegrees;
        updateDegRadLabel();
    }

    private void updateDegRadLabel() {
        if (degRadBtn != null) {
            degRadBtn.setText(isDegrees ? "DEG" : "RAD");
            degRadBtn.repaint();
        }
    }
}
