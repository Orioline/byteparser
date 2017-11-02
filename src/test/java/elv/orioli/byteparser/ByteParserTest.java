package elv.orioli.byteparser;

import elv.orioli.byteparser.config.ByteParserConfig;
import elv.orioli.byteparser.config.E_DataFieldType;
import elv.orioli.byteparser.rule.ByteParserNodeRule;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by Knight Leo on 2017/5/24.
 */
public class ByteParserTest {

    @Test
    public void decodeTest1() throws Exception {
        ByteParser byteParser = new ByteParser();

        ByteParserNodeRule rule1 = new ByteParserNodeRule();
        rule1.name = "signal1";
        rule1.desc = "Description for signal1";

        rule1.valueLength = "2";
        rule1.valueType = E_DataFieldType.NUMBER;

        ByteParserConfig byteParserConfig = new ByteParserConfig();
        byteParserConfig.addRule(1L, rule1);

        byteParser.init(byteParserConfig);

        Map<String, Object> result = byteParser.parse(1L, ByteBuffer.wrap(new byte[]{0x01, 0x02}));

        assertEquals(1, result.size());
        assertEquals(0x0102L, result.get("result"));

        System.out.println(result.toString());
    }
}