package elv.orioli.byteparser.config;

import elv.orioli.byteparser.rule.BPNodeRule;
import elv.orioli.byteparser.rule.BPRule;
import elv.orioli.byteparser.rule.BPSeqRule;
import elv.orioli.byteparser.rule.E_DataFieldType;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Orioline on 2017/5/29.
 */
public class BPStructConfigParserTest {
    @Test
    public void compileTest_1() throws Exception {
        String strConfig1 = "{" +
                "  v1: Int(1);" +
                "  v2: Int(2);" +
                "}";
        BPConfig bpConfig = BPStructConfigParser.compile(strConfig1);

        BPRule bpRule = bpConfig.getBpRule();
        assertEquals(2, bpRule.getRulesNum());
        if(bpRule instanceof BPSeqRule) {
            BPSeqRule bpSeqRule = (BPSeqRule) bpRule;
            assertEquals(null, bpSeqRule.getName());

            assertTrue(bpSeqRule.getSubRules().get(0) instanceof BPNodeRule);
            BPNodeRule nodeRule1 = (BPNodeRule) bpSeqRule.getSubRules().get(0);

            assertEquals("v1", nodeRule1.getName());
            assertEquals(E_DataFieldType.INT32, nodeRule1.dataFieldType);
            assertEquals(1, nodeRule1.size);
        } else {
            fail();
        }
    }

}