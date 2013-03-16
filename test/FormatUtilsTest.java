package org.ruleant.ariadne;

import java.util.Locale;
import junit.framework.TestCase;
import org.ruleant.ariadne.FormatUtils;

public class FormatUtilsTest extends TestCase {
    public void testFormatDist() {
        // Set English (US) locale
        Locale.setDefault(Locale.en_US);

        assertEquals("9m", FormatUtils.formatDist(9.0));
        assertEquals("10m", FormatUtils.formatDist(10.0));
        assertEquals("9.0km", FormatUtils.formatDist(9000.0));
        assertEquals("9.9km", FormatUtils.formatDist(9900.0));
        assertEquals("11km", FormatUtils.formatDist(11000.0));
        assertEquals("12,345km", FormatUtils.formatDist(12345000.0));
    }
    public void testFormatDistBelgianFormat() {
        // Set Dutch (Belgium) locale
        Locale.setDefault(Locale.nl_BE);

        assertEquals("9,0km", FormatUtils.formatDist(9000.0));
        assertEquals("9,9km", FormatUtils.formatDist(9900.0));
        assertEquals("12.345km", FormatUtils.formatDist(12345000.0));
    }
    public void testFormatDistRoundUp() {
        assertEquals("10m", FormatUtils.formatDist(9.9));
    }
    public void testFormatDistNeg() {
        // TODO write test for negative distance
        // FormatUtils.formatDist(-1.0);
        fail();
    }
}
