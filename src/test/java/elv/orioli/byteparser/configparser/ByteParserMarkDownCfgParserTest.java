package elv.orioli.byteparser.configparser;

import elv.orioli.byteparser.config.ByteParserConfig;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Knight Leo on 2017/11/1.
 */
public class ByteParserMarkDownCfgParserTest {
    @Test
    public void testMarkDownCfgParse_1() {
        String cfg1 =
                "## Table1 UserInfo\n" +
                "- 0x0011\n" +
                "- MessageId: 17\n" +
                "\n" +
                "name | type   | length | desc\n" +
                "---- | ----   | ------ | ----\n" +
                "id   | SHORT  | 2      | User Identifier\n" +
                "name | STRING | 8      | User Name";

        ByteParserConfig config = ByteParserMarkDownCfgParser.parse(cfg1);
        assertNotNull(config);
        assertEquals(1, config.ruleTable.size());
        assertEquals(2, config.ruleTable.get(0x0011L).getRulesNum());
    }
}