package elv.orioli.byteparser.configparser;

import elv.orioli.byteparser.config.ByteParserConfig;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Knight Leo on 2017/11/1.
 */
public class ByteParserMarkDownCfgParserTest {

    @Test
    public void testMarkDownCfgParse_1() {
        String cfg1 =
                "## Table.1 UserInfo\n" +
                "0x0011\n" +
                "- MessageId: 3\n" +
                "\n" +
                "name | type   | length | desc\n" +
                "---- | ----   | ------ | ----\n" +
                "id   | SHORT  | 2      | User Identifier\n" +
                "name | STRING | 8      | User Name";

        ByteParserConfig config = ByteParserMarkDownCfgParser.parse(cfg1);

        assertEquals(1, config.configTable.size());
    }


}