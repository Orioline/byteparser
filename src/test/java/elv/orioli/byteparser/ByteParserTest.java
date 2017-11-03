package elv.orioli.byteparser;

import elv.orioli.byteparser.config.ByteParserConfig;
import elv.orioli.byteparser.config.E_DataFieldType;
import elv.orioli.byteparser.rule.ByteParserNodeRule;
import elv.orioli.byteparser.util.ByteBufferOperator;
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

    @Test
    public void decodeTest_init_parse_basic() throws Exception {
        String markDownCfg =
                "## Table1 UserInfo\n" +
                        "- 0x0011\n" +
                        "\n" +
                        "name | type   | length | desc\n" +
                        "---- | ----   | ------ | ----\n" +
                        "id   | SHORT  | 2      | User Identifier\n" +
                        "name | STRING | 8      | User Name";
        ByteParser byteParser = new ByteParser();
        byteParser.init(markDownCfg, "MARKDOWN");

        Map<String, Object> result = byteParser.parse(0x0011L,
                ByteBufferOperator.hexStringToBytes("0001 75 73 65 72 30 30 30 31"));

        assertEquals(2, result.size());
        assertEquals(1L, result.get("id"));
        assertEquals("user0001", result.get("name"));
    }

    @Test
    public void decodeTest_objects() throws Exception {
        String markDownCfg =
                "## Table1 UserInfo\n" +
                        "- 0x0011\n" +
                        "\n" +
                        "name   | type   | length | desc\n" +
                        "----   | ----   | ------ | ----\n" +
                        "id     | SHORT  | 2      | User Identifier\n" +
                        "name   | STRING | 8      | User Name\n" +
                        "userAttr | OBJECT |        | Attributes\n" +
                        "\n" +
                        "## userAttr\n" +
                        "\n" +
                        "name   | type   | desc\n" +
                        "------ | ----   | ----\n" +
                        "gender | BYTE   | gender\n" +
                        "fav food   | STRING | favorite food\n" +
                        "fav sports | OBJECT | favorite sports\n" +
                        "\n" +
                        "## fav sports\n" +
                        "\n" +
                        "name    | type\n" +
                        "------  | ----\n" +
                        "indoor  | STRING\n" +
                        "outdoor | STRING";
        ByteParser byteParser = new ByteParser();
        byteParser.init(markDownCfg, "MARKDOWN");

        Map<String, Object> result = byteParser.parse(0x0011L,
                ByteBufferOperator.hexStringToBytes(
                        "0001 75 73 65 72 30 30 30 31  " +
                                "01 63 6F 6C 61 00" +
                                "73 6C 65 65 70 00  73 6B 69 00"));

        System.out.println(result);

        assertEquals(3, result.size());
        assertEquals(1L, result.get("id"));
        assertEquals("user0001", result.get("name"));
        Map<String, Object> userAttr = (Map<String, Object>) result.get("userAttr");
        assertEquals(1L, userAttr.get("gender"));
        assertEquals("cola", userAttr.get("fav food"));
        Map<String, Object> fav_sports = (Map<String, Object>) userAttr.get("fav sports");
        assertEquals("sleep", fav_sports.get("indoor"));
        assertEquals("ski", fav_sports.get("outdoor"));
    }
}