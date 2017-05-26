package elv.orioli.byteparser.config;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

/**
 * Created by Orioline on 2017/5/26.
 */
public class BPStructConfigNodeTest {

    @Test
    public void testFieldMatchPatternRegexMatch() {
        Pattern p = Pattern.compile(BPStructConfigNode.nodeFieldMatchPattern);

        Matcher m;
        m = p.matcher("BOOLEAN(1Bit)[4]");
        if (m.matches()) {
            //System.out.println("====1====");
            //System.out.println("groupCount: " + m.groupCount());
            //System.out.println("group1: " + m.group(1));
            //System.out.println("group2: " + m.group(2));
            //System.out.println("group3: " + m.group(3));
            //System.out.println("group4: " + m.group(4));
            //System.out.println("group5: " + m.group(5));
            assertEquals(5, m.groupCount());
            assertEquals("BOOLEAN", m.group(1));
            assertEquals("1", m.group(2));
            assertEquals("Bit", m.group(3));
            assertEquals("4", m.group(5));
        } else {
            fail("pattern incorrect!");
        }

        m = p.matcher("STRING(16)");
        if (m.matches()) {
            assertEquals(5, m.groupCount());
            assertEquals("STRING", m.group(1));
            assertEquals("16", m.group(2));
            assertNull(m.group(3));
            assertNull(m.group(4));
            assertNull(m.group(5));
        } else {
            fail("pattern incorrect!");
        }
    }
}