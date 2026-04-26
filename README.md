# Multi-Mode Calculator — Java Swing
### Developed for NetBeans IDE | Java 11+

---

## Overview

A modern, visually striking desktop calculator built with Java Swing featuring three operational 
modes, a dark "obsidian" theme, and fully custom-painted components — no plain JButtons in sight.

---

## Features

### Three Modes (switchable via tab bar — no restart needed)

| Mode         | Functions                                                            |
|--------------|----------------------------------------------------------------------|
| **Standard** | Arithmetic (+, −, ×, ÷, %), memory (MC/MR/MS/M+/M−), √x, x², 1/x  |
| **Scientific**| Trig (sin/cos/tan + inverses), log/ln, powers, roots, factorial,    |
|              | constants (π, e), DEG/RAD toggle, exp notation, log₂, 2ˣ, eˣ, 10ˣ  |
| **Programmer**| BIN/OCT/DEC/HEX base switching, bitwise (AND/OR/XOR/NOT/LSH/RSH),  |
|              | hex digit buttons A–F, live 4-base display panel                     |

### UI Design
- **Dark "obsidian" theme** — #12121 background, per-function color coding  
- **Color-coded buttons**: numbers (blue-gray), operators (deep blue), trig (green),  
  log (purple), memory (amber), bitwise (teal), hex digits (light blue), clear (red)  
- **Custom-painted CalcButton** — rounded corners, hover glow, press feedback  
- **Adaptive display** — font size shrinks automatically for long numbers  
- **Multi-base display panel** — BIN/OCT/DEC/HEX all shown simultaneously  
- **Smooth mode switching** — CardLayout, window auto-resizes per mode  

### Engineering
- Clean separation: `CalculatorEngine` (logic) ↔ UI panels  
- Functional interface `DoubleFunction` for concise unary ops  
- Full keyboard support (digits, operators, Enter, Backspace, Escape)  
- Robust error handling — divide by zero, domain errors, overflow  
- Memory (MC/MR/MS/M+/M−) persists across mode switches  

---

## Project Structure

```
Calculator/
├── src/
│   └── calculator/
│       ├── Main.java                    ← Entry point
│       ├── logic/
│       │   └── CalculatorEngine.java   ← All math, state management
│       ├── util/
│       │   └── BaseConverter.java      ← BIN/OCT/DEC/HEX conversions
│       └── ui/
│           ├── Theme.java              ← Color palette + fonts (single source of truth)
│           ├── CalcButton.java         ← Custom-painted button component
│           ├── DisplayPanel.java       ← Main + history display with adaptive font
│           ├── ModeTabBar.java         ← Custom tab switcher
│           ├── CalculatorFrame.java    ← Top-level JFrame, event routing
│           ├── StandardPanel.java      ← Standard mode button grid
│           ├── ScientificPanel.java    ← Scientific mode (dual-section layout)
│           └── ProgrammerPanel.java    ← Programmer mode with base displays
├── nbproject/
│   ├── project.xml
│   └── project.properties
├── build.xml                           ← Ant build (used by NetBeans)
└── README.md
```

---

## Opening in NetBeans

1. **File → Open Project** → select the `CalculatorNetBeans` folder  
2. NetBeans detects it as an Ant project automatically  
3. Press **F6** (Run Project) or **Shift+F11** (Clean & Build)  
4. The executable JAR is placed in `dist/Calculator.jar`  

## Building from Command Line

```bash
# Compile + jar
ant jar

# Run directly
ant run

# Or run the JAR
java -jar dist/Calculator.jar
```

---

## Keyboard Shortcuts

| Key                  | Action            |
|----------------------|-------------------|
| `0`–`9`              | Digit input       |
| `.`                  | Decimal point     |
| `+` `-` `*` `/`     | Operators         |
| `%`                  | Modulo            |
| `Enter` or `=`       | Equals            |
| `Backspace`          | Delete last digit |
| `Escape`             | Clear (C)         |
| `A`–`F`              | Hex digits (Programmer mode) |

---

## Design Decisions

- **No external dependencies** — pure Java SE (Swing + AWT)  
- **Java 11+** for broad compatibility; no module-info needed  
- `CalcButton` extends `JButton` but overrides `paintComponent` completely  
  so the look is pixel-perfect regardless of platform L&F  
- `CalculatorEngine.format()` smartly removes trailing zeros:  
  `3.0` → `"3"`, `3.14159…` → sensible decimal count  
- Programmer mode converts the current value when switching bases,  
  preserving the numeric value while changing representation  
