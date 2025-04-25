package com.easterfg.mae2a.util;

import static net.minecraft.ChatFormatting.*;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextColor;

/**
 * @author EasterFG on 2024/10/12
 */
public final class TooltipHelper {

    private TooltipHelper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    private static final List<FormattingCode> CODES = new ArrayList<>();

    /**
     * Array of TextFormatting codes that are used to create a rainbow effect
     */
    private static final ChatFormatting[] ALL_COLORS = new ChatFormatting[] {
            RED, GOLD, YELLOW, GREEN, AQUA, DARK_AQUA, DARK_BLUE, BLUE, DARK_PURPLE, LIGHT_PURPLE
    };

    public static final FormattingCode RAINBOW = new FormattingCode(160, ALL_COLORS);

    public static final RGB GRADIENT = new RGB(160, new int[] {
            0x0ebeff, 0x1cb7fb, 0x2aaff6, 0x39a8f2, 0x47a1ed, 0x559ae9, 0x6392e4, 0x718be0, 0x7f84db,
            0x8e7cd7, 0x9c75d2, 0xaa6ece, 0xb866c9, 0xc65fc5, 0xd458c0, 0xe351bc, 0xf149b7, 0xff42b3
    });

    public static final RGB PREVIEW_GRADIENT = new RGB(160, new int[] {
            0xFF0ebeff, 0xFF17b9fc, 0xFF21b4fa, 0xFF2ab0f7, 0xFF33abf4, 0xFF3ca6f2, 0xFF46a1ef, 0xFF4f9dec, 0xFF5898ea,
            0xFF6193e7, 0xFF6b8ee4, 0xFF748ae2, 0xFF7d85df, 0xFF8780dd, 0xFF907bda, 0xFF9976d7, 0xFFa272d5, 0xFFac6dd2,
            0xFFb568cf, 0xFFbe63cd, 0xFFc75fca, 0xFFd15ac7, 0xFFda55c5, 0xFFe350c2, 0xFFec4cbf, 0xFFf647bd, 0xFFff42ba
    });

    public static class FormattingCode {
        private final double rate;
        private final ChatFormatting[] codes;

        public FormattingCode(double rate, ChatFormatting[] codes) {
            this.rate = rate;
            this.codes = codes;
        }

        public ChatFormatting getCurrent() {
            int offset = (int) Math.floor((System.currentTimeMillis() & 0x3FFFL) / rate) % codes.length;
            return codes[offset];
        }
    }

    public static class RGB {
        private final double rate;
        private final int[] colors;

        public RGB(double rate, int[] color) {
            this.rate = rate;
            this.colors = color;
        }

        public int getOffset() {
            return (int) Math.floor((System.currentTimeMillis() & 0x3FFFL) / rate) % colors.length;
        }

        public TextColor getCurrent() {
            return TextColor.fromRgb(colors[getOffset()]);
        }

        public int getARGB() {
            return 0xFF000000 | colors[getOffset()];
        }
    }

}
