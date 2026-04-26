package calculator.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Standard calculator mode: basic arithmetic with number pad.
 */
public class StandardPanel extends JPanel {

    public interface ButtonHandler {
        void onButton(String action);
    }

    private ButtonHandler handler;

    // Layout: rows × cols of [label, style, action]
    private static final Object[][] BUTTONS = {
        // Row 0: Memory & Utility
        { "MC",  CalcButton.Style.MEMORY,   "MC"   },
        { "MR",  CalcButton.Style.MEMORY,   "MR"   },
        { "M+",  CalcButton.Style.MEMORY,   "M+"   },
        { "M-",  CalcButton.Style.MEMORY,   "M-"   },
        { "MS",  CalcButton.Style.MEMORY,   "MS"   },

        // Row 1: Utility
        { "%",   CalcButton.Style.FUNCTION, "%"    },
        { "CE",  CalcButton.Style.FUNCTION, "CE"   },
        { "C",   CalcButton.Style.CLEAR,    "C"    },
        { "⌫",   CalcButton.Style.FUNCTION, "BSP"  },
        { "÷",   CalcButton.Style.OPERATOR, "÷"    },

        // Row 2
        { "7",   CalcButton.Style.NUMBER,   "7"    },
        { "8",   CalcButton.Style.NUMBER,   "8"    },
        { "9",   CalcButton.Style.NUMBER,   "9"    },
        { "±",   CalcButton.Style.FUNCTION, "NEG"  },
        { "×",   CalcButton.Style.OPERATOR, "×"    },

        // Row 3
        { "4",   CalcButton.Style.NUMBER,   "4"    },
        { "5",   CalcButton.Style.NUMBER,   "5"    },
        { "6",   CalcButton.Style.NUMBER,   "6"    },
        { "1/x", CalcButton.Style.FUNCTION, "1/x"  },
        { "-",   CalcButton.Style.OPERATOR, "-"    },

        // Row 4
        { "1",   CalcButton.Style.NUMBER,   "1"    },
        { "2",   CalcButton.Style.NUMBER,   "2"    },
        { "3",   CalcButton.Style.NUMBER,   "3"    },
        { "x²",  CalcButton.Style.FUNCTION, "SQ"   },
        { "+",   CalcButton.Style.OPERATOR, "+"    },

        // Row 5
        { "0",   CalcButton.Style.NUMBER,   "0"    },
        { "00",  CalcButton.Style.NUMBER,   "00"   },
        { ".",   CalcButton.Style.NUMBER,   "."    },
        { "√x",  CalcButton.Style.FUNCTION, "SQRT" },
        { "=",   CalcButton.Style.EQUALS,   "="    },
    };

    private static final int COLS = 5;

    public StandardPanel() {
        setOpaque(false);
        setLayout(new GridLayout(6, COLS, 5, 5));
        buildButtons();
    }

    public void setButtonHandler(ButtonHandler h) { this.handler = h; }

    private void buildButtons() {
        removeAll();
        for (Object[] spec : BUTTONS) {
            String label  = (String) spec[0];
            CalcButton.Style style = (CalcButton.Style) spec[1];
            String action = (String) spec[2];

            CalcButton btn = new CalcButton(label, style);
            btn.addActionListener(e -> { if (handler != null) handler.onButton(action); });
            add(btn);
        }
        revalidate(); repaint();
    }
}
