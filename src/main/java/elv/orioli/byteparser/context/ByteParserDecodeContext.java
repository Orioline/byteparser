package elv.orioli.byteparser.context;

import elv.orioli.byteparser.config.ByteParserConfig;
import elv.orioli.byteparser.rule.ByteParserRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Orioline on 2017/5/26.
 */
public class ByteParserDecodeContext extends ByteParserContext {
    Logger logger = LoggerFactory.getLogger(ByteParserDecodeContext.class);

    public long msgId;
    public ByteBuffer buffer;
    public ByteParserConfig byteParserConfig;
    public ByteParserDecodeResult byteParserDecodeResult;

    public ByteParserDecodeContext(long msgId, ByteBuffer buffer, ByteParserConfig byteParserConfig) {
        this.msgId = msgId;
        this.buffer = buffer;
        this.byteParserConfig = byteParserConfig;
        this.byteParserDecodeResult = new ByteParserDecodeResult();
    }

    public ByteParserDecodeResult doParsing() {
        // handle parsing
        // String ruleName = byteParserConfig.ruleIndexTable.get(msgId);
        // ByteParserRule byteParserRule = byteParserConfig.configTable.get(ruleName);
        ByteParserRule byteParserRule = byteParserConfig.ruleTable.get(msgId);
        try {
            Object result = byteParserRule.handleDecode(buffer, this);
            if(result instanceof Map) {
                this.byteParserDecodeResult.setResultMap((LinkedHashMap)result);
            } else {
                LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();
                resultMap.put("result", result);
                this.byteParserDecodeResult.setResultMap(resultMap);
            }
        } catch (Throwable t) {
            logger.warn("Error in doParsing, cause = ", t);
            this.byteParserDecodeResult.setResultFail(t);
        }

        return byteParserDecodeResult;
    }

    public ByteParserDecodeResult getByteParserDecodeResult() {
        return byteParserDecodeResult;
    }
}
