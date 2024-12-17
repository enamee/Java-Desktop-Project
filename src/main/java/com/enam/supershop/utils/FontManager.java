package com.enam.supershop.utils;

import javafx.scene.text.Font;
import java.util.HashMap;
import java.util.Map;

public class FontManager {
    private static final Map<String, Font> fonts = new HashMap<>();

    static {
        // Load fonts during initialization
        loadFont("MaterialSymbols", "/fonts/MaterialSymbolsRounded/MaterialSymbolsRounded-Regular.ttf");
        loadFont("NotoEmoji", "/fonts/NotoEmoji/NotoEmoji-Regular.ttf");
    }

    private static void loadFont(String fontName, String fontPath) {
        Font font = Font.loadFont(FontManager.class.getResourceAsStream(fontPath), 16);
        if (font != null) {
            fonts.put(fontName, font);
        } else {
            System.err.println("Failed to load font: " + fontPath);
        }
    }

    // Get font by name and size
    public static Font getFont(String fontName, double size) {
        Font baseFont = fonts.get(fontName);
        if (baseFont != null) {
            return Font.font(baseFont.getFamily(), size);
        } else {
            throw new IllegalArgumentException("Font not found: " + fontName);
        }
    }
}
