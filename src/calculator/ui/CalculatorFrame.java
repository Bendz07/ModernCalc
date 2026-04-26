package calculator.ui;

import calculator.logic.CalculatorEngine;
import calculator.util.BaseConverter;
import calculator.util.BaseConverter.Base;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Main application window. Manages mode switching and connects
 * UI events to CalculatorEngine logic.
 */
public class CalculatorFrame extends JFrame {

    // ─── Core ────────────────────────────────────────────────────────────────
    private final CalculatorEngine engine = new CalculatorEngine();
    private double memory = 0;
    private String currentInput = "0";
    private boolean justCalculated = false;

    // ─── UI Components ────────────────────────────────────────────────────────
    private DisplayPanel displayPanel;
    private ModeTabBar   tabBar;
    private JPanel       modeContainer;
    private CardLayout   cardLayout;

    private StandardPanel   standardPanel;
    private ScientificPanel scientificPanel;
    private ProgrammerPanel programmerPanel;

    // Current mode string
    private String currentMode = "Standard";

    public CalculatorFrame() {
        setTitle("Calculator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);
        setMinimumSize(new Dimension(420, 580));

        // Transparent/undecorated feel with custom background
        setBackground(Theme.BG_WINDOW);

        buildUI();
        setupKeyboardInput();
        pack();
        setLocationRelativeTo(null);
    }

    // ─── UI Construction ──────────────────────────────────────────────────────

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(0, 0)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Theme.BG_WINDOW);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.dispose();
            }
        };
        root.setOpaque(false);
        root.setBorder(new EmptyBorder(12, 12, 12, 12));
        setContentPane(root);

        // Display
        displayPanel = new DisplayPanel();

        // Tab bar
        tabBar = new ModeTabBar();
        tabBar.setModeChangeListener(mode -> switchMode(mode));

        // Mode panels
        standardPanel   = new StandardPanel();
        scientificPanel = new ScientificPanel();
        programmerPanel = new ProgrammerPanel();

        standardPanel.setButtonHandler(this::handleButton);
        scientificPanel.setButtonHandler(this::handleButton);
        programmerPanel.setButtonHandler(this::handleButton);

        // Card layout for mode switching
        cardLayout = new CardLayout();
        modeContainer = new JPanel(cardLayout);
        modeContainer.setOpaque(false);
        modeContainer.add(standardPanel,   "Standard");
        modeContainer.add(scientificPanel, "Scientific");
        modeContainer.add(programmerPanel, "Programmer");

        JPanel top = new JPanel(new BorderLayout(0, 8));
        top.setOpaque(false);
        top.add(displayPanel, BorderLayout.CENTER);
        top.add(tabBar, BorderLayout.SOUTH);

        root.add(top, BorderLayout.NORTH);
        root.add(modeContainer, BorderLayout.CENTER);

        // Set preferred sizes
        displayPanel.setPreferredSize(new Dimension(420, 120));
        tabBar.setPreferredSize(new Dimension(420, 38));
    }

    private void switchMode(String mode) {
        currentMode = mode;
        cardLayout.show(modeContainer, mode);
        displayPanel.setModeIndicator(mode.substring(0, 3).toUpperCase());

        // Resize frame to fit
        SwingUtilities.invokeLater(() -> {
            pack();
            // Don't go below minimum
            int w = Math.max(getWidth(), getMinimumSize().width);
            int h = Math.max(getHeight(), getMinimumSize().height);
            if ("Scientific".equals(mode)) {
                setSize(Math.max(w, 680), Math.max(h, 580));
            } else if ("Programmer".equals(mode)) {
                setSize(Math.max(w, 640), Math.max(h, 600));
            } else {
                setSize(Math.max(w, 420), Math.max(h, 560));
            }
        });
    }

    // ─── Button Handler ───────────────────────────────────────────────────────

    private void handleButton(String action) {
        if (engine.hasError() && !"C".equals(action) && !"CE".equals(action)) {
            Toolkit.getDefaultToolkit().beep();
            return;
        }

        try {
            switch (action) {
                // Digits
                case "0": case "1": case "2": case "3": case "4":
                case "5": case "6": case "7": case "8": case "9":
                    appendDigit(action); break;
                case "00":
                    if (!"0".equals(currentInput)) { appendDigit("0"); appendDigit("0"); }
                    break;
                case ".": appendDecimal(); break;

                // Hex digits
                case "A": case "B": case "HC": case "D": case "HE": case "F":
                    appendHexDigit(action.equals("HC") ? "C" : action.equals("HE") ? "E" : action); break;

                // Basic operators
                case "+": case "-": case "×": case "÷": case "%":
                case "POW": case "YROOT": case "AND": case "OR":
                case "XOR": case "LSH": case "RSH":
                    handleOperator(action); break;

                case "=": handleEquals(); break;
                case "C":   handleClear(); break;
                case "CE":  handleCE(); break;
                case "BSP": handleBackspace(); break;
                case "NEG": handleNegate(); break;

                // Unary scientific
                case "SIN":   applyUnary(v -> engine.sin(v, scientificPanel.isDegrees())); break;
                case "COS":   applyUnary(v -> engine.cos(v, scientificPanel.isDegrees())); break;
                case "TAN":   applyUnary(v -> engine.tan(v, scientificPanel.isDegrees())); break;
                case "ASIN":  applyUnary(v -> engine.asin(v, scientificPanel.isDegrees())); break;
                case "ACOS":  applyUnary(v -> engine.acos(v, scientificPanel.isDegrees())); break;
                case "ATAN":  applyUnary(v -> engine.atan(v, scientificPanel.isDegrees())); break;
                case "LOG10": applyUnary(v -> engine.log10(v)); break;
                case "LN":    applyUnary(v -> engine.ln(v)); break;
                case "10X":   applyUnary(v -> engine.tenPow(v)); break;
                case "EX":    applyUnary(v -> engine.exp(v)); break;
                case "2X":    applyUnary(v -> engine.twoPow(v)); break;
                case "LOG2":  applyUnary(v -> engine.log10(v) / engine.log10(2)); break;
                case "SQ":    applyUnary(v -> engine.square(v)); break;
                case "CUBE":  applyUnary(v -> engine.cube(v)); break;
                case "SQRT":  applyUnary(v -> engine.sqrt(v)); break;
                case "CBRT":  applyUnary(v -> engine.cbrt(v)); break;
                case "FACT":  applyUnary(v -> engine.factorial(v)); break;
                case "1/x":   applyUnary(v -> engine.reciprocal(v)); break;
                case "ABS":   applyUnary(v -> engine.absVal(v)); break;
                case "EXP":   appendText("e"); break;  // scientific notation

                case "NOT":   applyUnary(v -> (double) engine.bitwiseNot((long) v)); break;

                // Constants
                case "PI":  setInputValue(Math.PI); break;
                case "E":   setInputValue(Math.E);  break;

                // Memory
                case "MC": memory = 0; break;
                case "MR": setInputValue(memory); break;
                case "MS": memory = parseCurrentInput(); break;
                case "M+": memory += parseCurrentInput(); break;
                case "M-": memory -= parseCurrentInput(); break;

                // DEG/RAD toggle
                case "DEGRAD": scientificPanel.toggleDegRad(); break;

                // Programmer base switches
                case "BASE_BIN": switchBase(Base.BIN); break;
                case "BASE_OCT": switchBase(Base.OCT); break;
                case "BASE_DEC": switchBase(Base.DEC); break;
                case "BASE_HEX": switchBase(Base.HEX); break;
            }
        } catch (ArithmeticException ex) {
            displayPanel.setError(ex.getMessage());
            engine.setError(true);
            engine.setNewNumber(true);
        } catch (NumberFormatException ex) {
            displayPanel.setError("Invalid input");
            engine.setError(true);
        }
    }

    // ─── Input Handling ───────────────────────────────────────────────────────

    private void appendDigit(String digit) {
        if (engine.isNewNumber() || justCalculated) {
            currentInput = digit.equals("0") ? "0" : digit;
            engine.setNewNumber(false);
            justCalculated = false;
        } else {
            if ("0".equals(currentInput) && !digit.equals(".")) {
                currentInput = digit;
            } else {
                if (currentInput.length() < 20) {
                    currentInput += digit;
                }
            }
        }
        updateDisplay();
    }

    private void appendDecimal() {
        if (engine.isNewNumber() || justCalculated) {
            currentInput = "0.";
            engine.setNewNumber(false);
            justCalculated = false;
        } else if (!currentInput.contains(".")) {
            currentInput += ".";
        }
        updateDisplay();
    }

    private void appendHexDigit(String digit) {
        if (engine.isNewNumber() || justCalculated) {
            currentInput = digit;
            engine.setNewNumber(false);
            justCalculated = false;
        } else {
            if (currentInput.length() < 16) currentInput += digit;
        }
        updateDisplay();
    }

    private void appendText(String text) {
        currentInput += text;
        updateDisplay();
    }

    private void handleOperator(String op) {
        double current = parseCurrentInput();

        // If there's a pending operation and user enters a new operator, compute first
        if (!engine.getPendingOperator().isEmpty() && !engine.isNewNumber() && !justCalculated) {
            double result = engine.evaluatePending(current);
            engine.setPreviousValue(result);
            engine.setLastExpression(CalculatorEngine.format(engine.getPreviousValue())
                                    + " " + op);
            currentInput = CalculatorEngine.format(result);
        } else {
            engine.setPreviousValue(current);
            engine.setLastExpression(CalculatorEngine.format(current) + " " + op);
        }

        engine.setPendingOperator(op);
        engine.setNewNumber(true);
        justCalculated = false;
        displayPanel.setHistoryText(engine.getLastExpression());
        updateDisplay();
    }

    private void handleEquals() {
        double current = parseCurrentInput();
        if (!engine.getPendingOperator().isEmpty()) {
            String expr = engine.getLastExpression() + " " + CalculatorEngine.format(current) + " =";
            double result = engine.evaluatePending(current);
            currentInput  = CalculatorEngine.format(result);
            engine.setPendingOperator("");
            engine.setNewNumber(true);
            justCalculated = true;
            displayPanel.setHistoryText(expr);

            // Update programmer base displays
            if ("Programmer".equals(currentMode)) {
                programmerPanel.updateBaseDisplays((long) result);
            }
        }
        updateDisplay();
    }

    private void handleClear() {
        engine.reset();
        currentInput    = "0";
        justCalculated  = false;
        memory          = 0;
        displayPanel.setHistoryText("");
        if ("Programmer".equals(currentMode)) {
            programmerPanel.updateBaseDisplays(0);
        }
        updateDisplay();
    }

    private void handleCE() {
        currentInput = "0";
        engine.setNewNumber(true);
        engine.setError(false);
        updateDisplay();
    }

    private void handleBackspace() {
        if (engine.isNewNumber() || justCalculated) return;
        if (currentInput.length() <= 1 || (currentInput.startsWith("-") && currentInput.length() == 2)) {
            currentInput = "0";
            engine.setNewNumber(true);
        } else {
            currentInput = currentInput.substring(0, currentInput.length() - 1);
            if ("-".equals(currentInput)) currentInput = "0";
        }
        updateDisplay();
    }

    private void handleNegate() {
        double v = parseCurrentInput();
        v = -v;
        currentInput = CalculatorEngine.format(v);
        updateDisplay();
    }

    // ─── Unary Operation Helper ───────────────────────────────────────────────

    @FunctionalInterface
    interface DoubleFunction { double apply(double v) throws ArithmeticException; }

    private void applyUnary(DoubleFunction fn) throws ArithmeticException {
        double v = parseCurrentInput();
        double result = fn.apply(v);
        String expr = engine.getLastExpression() + " f(" + CalculatorEngine.format(v) + ") =";
        currentInput = CalculatorEngine.format(result);
        engine.setNewNumber(true);
        justCalculated = true;
        displayPanel.setHistoryText(expr);
        if ("Programmer".equals(currentMode)) {
            programmerPanel.updateBaseDisplays((long) result);
        }
        updateDisplay();
    }

    // ─── Base Switching (Programmer Mode) ────────────────────────────────────

    private void switchBase(Base base) {
        // Convert current display value to new base
        long currentLong;
        try {
            currentLong = BaseConverter.fromBase(currentInput, programmerPanel.getCurrentBase());
        } catch (NumberFormatException ex) {
            currentLong = 0;
        }
        programmerPanel.setCurrentBase(base);
        currentInput = BaseConverter.toBase(currentLong, base).toUpperCase();
        if (currentInput.isEmpty()) currentInput = "0";
        programmerPanel.updateBaseDisplays(currentLong);
        updateDisplay();
    }

    // ─── Parsing / Display ────────────────────────────────────────────────────

    private double parseCurrentInput() {
        if (currentInput == null || currentInput.isEmpty() || "0".equals(currentInput)) return 0;
        if ("Programmer".equals(currentMode)) {
            try {
                return (double) BaseConverter.fromBase(currentInput, programmerPanel.getCurrentBase());
            } catch (NumberFormatException e) { return 0; }
        }
        try {
            return Double.parseDouble(currentInput);
        } catch (NumberFormatException e) { return 0; }
    }

    private void setInputValue(double value) {
        currentInput = CalculatorEngine.format(value);
        engine.setNewNumber(true);
        justCalculated = false;
        updateDisplay();
    }

    private void updateDisplay() {
        displayPanel.setMainText(currentInput);
        if ("Programmer".equals(currentMode)) {
            try {
                long lv = BaseConverter.fromBase(currentInput, programmerPanel.getCurrentBase());
                programmerPanel.updateBaseDisplays(lv);
            } catch (NumberFormatException ignored) {}
        }
    }

    // ─── Keyboard Input ───────────────────────────────────────────────────────

    private void setupKeyboardInput() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
            if (e.getID() != KeyEvent.KEY_PRESSED) return false;
            int  code = e.getKeyCode();
            char ch   = e.getKeyChar();

            if (ch >= '0' && ch <= '9') { handleButton(String.valueOf(ch)); return true; }
            if (ch == '.') { handleButton("."); return true; }
            if (ch == '+') { handleButton("+"); return true; }
            if (ch == '-') { handleButton("-"); return true; }
            if (ch == '*') { handleButton("×"); return true; }
            if (ch == '/') { handleButton("÷"); return true; }
            if (ch == '%') { handleButton("%"); return true; }
            if (ch == '\n' || ch == '=') { handleButton("="); return true; }
            if (ch == '\b' || code == KeyEvent.VK_BACK_SPACE) { handleButton("BSP"); return true; }
            if (code == KeyEvent.VK_DELETE || ch == 'c' || ch == 'C') {
                if (e.isControlDown()) { handleButton("C"); return true; }
            }
            if (code == KeyEvent.VK_ESCAPE) { handleButton("C"); return true; }

            // Hex A-F
            if ("Programmer".equals(currentMode)) {
                if (ch >= 'a' && ch <= 'f') { handleButton(String.valueOf(ch).toUpperCase()); return true; }
                if (ch >= 'A' && ch <= 'F') { handleButton(String.valueOf(ch)); return true; }
            }
            return false;
        });
    }
}
