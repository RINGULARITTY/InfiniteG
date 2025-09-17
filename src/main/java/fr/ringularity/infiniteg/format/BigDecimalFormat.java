package fr.ringularity.infiniteg.format;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class BigDecimalFormat {
    private static final String[] LARGE = { "K", "M", "G", "T", "P", "E", "Z", "Y", "R", "Q" };
    private static final String[] SMALL = { "m", "u", "n", "p", "f", "a", "z", "y", "r", "q" };

    public static String format(BigDecimal x) {
        return format_n(x, 3);
    }

    public static String format_n(BigDecimal x, int significantDigits) {
        if (significantDigits < 1) {
            throw new IllegalArgumentException("significantDigits must be >= 1");
        }
        if (x.signum() == 0) {
            return "0";
        }

        boolean neg = x.signum() < 0;
        BigDecimal abs = x.abs();

        if (abs.compareTo(BigDecimal.valueOf(1000)) >= 0) {
            int intDigits = integerDigits(abs);
            int group = (intDigits - 1) / 3;

            BigDecimal val = abs
                    .movePointLeft(group * 3)
                    .round(new MathContext(significantDigits, RoundingMode.DOWN));

            if (val.compareTo(BigDecimal.valueOf(1000)) >= 0) {
                val = val.movePointLeft(3);
                group += 1;
            }

            String suffix = tierSuffix(LARGE, group);
            return (neg ? "-" : "") + toPlain(val) + suffix;
        }

        if (abs.compareTo(BigDecimal.ONE) >= 0) {
            BigDecimal val = abs.round(new MathContext(significantDigits, RoundingMode.DOWN));
            return (neg ? "-" : "") + toPlain(val);
        }

        if (abs.compareTo(new BigDecimal("0.001")) >= 0) {
            int sig = Math.max(1, significantDigits - 1);
            BigDecimal val = abs.round(new MathContext(sig, RoundingMode.DOWN));
            return (neg ? "-" : "") + toPlain(val);
        }

        int smallGroup = 0;
        BigDecimal scaled = abs;
        while (scaled.compareTo(BigDecimal.ONE) < 0) {
            scaled = scaled.movePointRight(3);
            smallGroup++;
        }

        BigDecimal val = scaled.round(new MathContext(significantDigits, RoundingMode.UP));

        if (val.compareTo(BigDecimal.valueOf(1000)) >= 0) {
            val = val.movePointLeft(3);
            smallGroup -= 1;
        }

        String suffix = tierSuffix(SMALL, smallGroup);
        return (neg ? "-" : "") + toPlain(val) + suffix;
    }

    private static String tierSuffix(String[] base, int group) {
        int baseIndex = group - 1;
        int cycle = baseIndex / base.length;
        int idx = baseIndex % base.length;
        return base[idx] + (cycle == 0 ? "" : String.valueOf(cycle + 1));
    }

    private static int integerDigits(BigDecimal bd) {
        String s = bd.stripTrailingZeros().toPlainString();
        int dot = s.indexOf('.');
        return (dot >= 0 ? dot : s.length());
    }

    private static String toPlain(BigDecimal bd) {
        return bd.stripTrailingZeros().toPlainString();
    }
}