package calculator.ui;

import java.awt.Color;
import java.awt.Font;

/**
 * Centralized color palette and font definitions.
 * Inspired by a dark "obsidian" theme with vibrant accent colors.
 */
public final class Theme {

    private Theme() {}

    // ─── Backgrounds ──────────────────────────────────────────────────────────
    public static final Color BG_WINDOW      = new Color(0x12, 0x12, 0x17);
    public static final Color BG_DISPLAY     = new Color(0x0A, 0x0A, 0x0F);
    public static final Color BG_PANEL       = new Color(0x1A, 0x1A, 0x22);
    public static final Color BG_TAB_BAR     = new Color(0x10, 0x10, 0x16);
    public static final Color BG_HISTORY     = new Color(0x0E, 0x0E, 0x14);

    // ─── Button Colors (Normal) ───────────────────────────────────────────────
    public static final Color BTN_NUMBER     = new Color(0x22, 0x22, 0x2E);  // dark blue-gray
    public static final Color BTN_OPERATOR   = new Color(0x1C, 0x2A, 0x44);  // deep blue
    public static final Color BTN_FUNCTION   = new Color(0x1E, 0x1E, 0x2A);  // slate
    public static final Color BTN_EQUALS     = new Color(0x00, 0x8B, 0xD8);  // vivid blue
    public static final Color BTN_CLEAR      = new Color(0x8B, 0x1A, 0x1A);  // deep red
    public static final Color BTN_TRIG       = new Color(0x14, 0x2A, 0x20);  // deep green
    public static final Color BTN_LOG        = new Color(0x2A, 0x1A, 0x2E);  // deep purple
    public static final Color BTN_MEMORY     = new Color(0x2A, 0x1E, 0x10);  // deep amber
    public static final Color BTN_BITWISE    = new Color(0x1A, 0x24, 0x2A);  // teal-dark
    public static final Color BTN_HEX_DIGIT  = new Color(0x1C, 0x28, 0x3A);  // hex blue

    // ─── Button Hover Colors ──────────────────────────────────────────────────
    public static final Color BTN_NUMBER_H   = new Color(0x2E, 0x2E, 0x3E);
    public static final Color BTN_OPERATOR_H = new Color(0x26, 0x3A, 0x60);
    public static final Color BTN_FUNCTION_H = new Color(0x2A, 0x2A, 0x3A);
    public static final Color BTN_EQUALS_H   = new Color(0x00, 0xA5, 0xFF);
    public static final Color BTN_CLEAR_H    = new Color(0xC0, 0x22, 0x22);
    public static final Color BTN_TRIG_H     = new Color(0x1C, 0x3A, 0x2C);
    public static final Color BTN_LOG_H      = new Color(0x3A, 0x24, 0x40);
    public static final Color BTN_MEMORY_H   = new Color(0x3A, 0x28, 0x14);
    public static final Color BTN_BITWISE_H  = new Color(0x24, 0x34, 0x3C);
    public static final Color BTN_HEX_DIGIT_H= new Color(0x28, 0x38, 0x50);

    // ─── Text Colors ─────────────────────────────────────────────────────────
    public static final Color TEXT_DISPLAY   = new Color(0xEC, 0xF0, 0xFF);
    public static final Color TEXT_HISTORY   = new Color(0x80, 0x88, 0xAA);
    public static final Color TEXT_NUMBER    = new Color(0xD0, 0xD8, 0xFF);
    public static final Color TEXT_OPERATOR  = new Color(0x64, 0xB5, 0xFF);
    public static final Color TEXT_FUNCTION  = new Color(0xB0, 0xB8, 0xE0);
    public static final Color TEXT_EQUALS    = Color.WHITE;
    public static final Color TEXT_CLEAR     = new Color(0xFF, 0xB0, 0xB0);
    public static final Color TEXT_TRIG      = new Color(0x80, 0xFF, 0xB4);
    public static final Color TEXT_LOG       = new Color(0xD0, 0x90, 0xFF);
    public static final Color TEXT_MEMORY    = new Color(0xFF, 0xCC, 0x80);
    public static final Color TEXT_BITWISE   = new Color(0x80, 0xE0, 0xFF);
    public static final Color TEXT_HEX_DIGIT = new Color(0x90, 0xC8, 0xFF);
    public static final Color TEXT_DISABLED  = new Color(0x40, 0x40, 0x50);

    // ─── Accent / Glow ───────────────────────────────────────────────────────
    public static final Color ACCENT_BLUE    = new Color(0x00, 0x8B, 0xD8);
    public static final Color ACCENT_GREEN   = new Color(0x00, 0xC8, 0x64);
    public static final Color ACCENT_PURPLE  = new Color(0x9C, 0x40, 0xFF);
    public static final Color ACCENT_AMBER   = new Color(0xFF, 0xA0, 0x00);
    public static final Color ACCENT_RED     = new Color(0xFF, 0x44, 0x44);
    public static final Color BORDER_SUBTLE  = new Color(0x30, 0x30, 0x45);
    public static final Color BORDER_ACTIVE  = new Color(0x00, 0x8B, 0xD8, 180);

    // ─── Tab colors ──────────────────────────────────────────────────────────
    public static final Color TAB_ACTIVE_BG  = new Color(0x00, 0x8B, 0xD8);
    public static final Color TAB_HOVER_BG   = new Color(0x22, 0x22, 0x30);
    public static final Color TAB_IDLE_BG    = new Color(0x10, 0x10, 0x16);
    public static final Color TAB_ACTIVE_FG  = Color.WHITE;
    public static final Color TAB_IDLE_FG    = new Color(0x70, 0x78, 0x9A);

    // ─── Fonts ───────────────────────────────────────────────────────────────
    public static final Font FONT_DISPLAY_LARGE = new Font("SansSerif", Font.PLAIN, 42);
    public static final Font FONT_DISPLAY_MED   = new Font("SansSerif", Font.PLAIN, 30);
    public static final Font FONT_DISPLAY_SMALL = new Font("SansSerif", Font.PLAIN, 22);
    public static final Font FONT_HISTORY       = new Font("SansSerif", Font.PLAIN, 13);
    public static final Font FONT_BTN_LARGE     = new Font("SansSerif", Font.BOLD,  18);
    public static final Font FONT_BTN_NORMAL    = new Font("SansSerif", Font.BOLD,  15);
    public static final Font FONT_BTN_SMALL     = new Font("SansSerif", Font.BOLD,  12);
    public static final Font FONT_TAB           = new Font("SansSerif", Font.BOLD,  13);
    public static final Font FONT_LABEL         = new Font("SansSerif", Font.PLAIN, 11);
    public static final Font FONT_BASE_DISPLAY  = new Font("Monospaced",Font.PLAIN, 12);
}
