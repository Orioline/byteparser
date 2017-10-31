package elv.orioli.byteparser.rule;

import elv.orioli.byteparser.context.ByteParserDecodeContext;

import java.nio.ByteBuffer;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Orioline on 2017/10/31.
 */
public class ByteParserObjectRule implements ByteParserRule {
    public String name;
    public String desc;

    public LinkedHashMap<String, ByteParserRule> subRules;

    @Override
    public Object handleDecode(ByteBuffer buffer, ByteParserDecodeContext ctx) {
        Map<String, Object> resultMap = new LinkedHashMap<>();
        for (Map.Entry<String, ByteParserRule> entry: subRules.entrySet()) {
            ByteParserRule rule = entry.getValue();

            Object result = rule.handleDecode(buffer, ctx);
            resultMap.put(entry.getKey(), result);
        }

        return resultMap;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getRulesNum() {
        return subRules.size();
    }
}
