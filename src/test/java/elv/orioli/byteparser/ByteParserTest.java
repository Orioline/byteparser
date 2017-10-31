package elv.orioli.byteparser;

import elv.orioli.byteparser.config.ByteParserConfig;
import elv.orioli.byteparser.config.E_DataFieldType;
import elv.orioli.byteparser.rule.ByteParserNodeRule;
import elv.orioli.byteparser.rule.ByteParserRule;
import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by Knight Leo on 2017/5/24.
 */
public class ByteParserTest {

    @Test
    public void decodeTest1() throws Exception {
        ByteParser byteParser = new ByteParser();

        ByteParserNodeRule nodeConfig1 = new ByteParserNodeRule();
        nodeConfig1.name = "signal1";
        nodeConfig1.desc = "Desc for signal 1";

        nodeConfig1.valueLength = "2";
        nodeConfig1.valueType = E_DataFieldType.NUMBER;

        LinkedHashMap<Long, ByteParserRule> configTable = new LinkedHashMap<>();
        configTable.put(1L, nodeConfig1);

        ByteParserConfig byteParserConfig = new ByteParserConfig();
        byteParserConfig.init(configTable);

        byteParser.init(byteParserConfig);

        Map<String, Object> result = byteParser.parse(1L, ByteBuffer.wrap(new byte[]{0x01, 0x02}));

        assertEquals(1, result.size());
        assertEquals(0x0102L, result.get("result"));

        System.out.println(result.toString());
    }
}