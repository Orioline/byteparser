package elv.orioli.byteparser;

import elv.orioli.byteparser.config.ByteParserConfig;
import elv.orioli.byteparser.configparser.ByteParserMarkDownCfgParser;
import elv.orioli.byteparser.context.ByteParserDecodeResult;
import elv.orioli.byteparser.executor.ByteParserExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.Map;

/**
 * Created by Orioline on 2017/10/31.
 */
public class ByteParser implements IByteParser {

    Logger logger = LoggerFactory.getLogger(ByteParser.class);
    ByteParserConfig byteParserConfig;

    /**
     * User must init the byteParser before using it.
     * @param config config
     * @throws Exception
     */
    @Override
    public void init(Object config) throws Exception {

        if(config instanceof ByteParserConfig) {
            this.byteParserConfig = (ByteParserConfig) config;
        } else {
            this.byteParserConfig = ByteParserMarkDownCfgParser.parse(config.toString());
        }
    }

    @Override
    public Map<String, Object> parse(long msgId, ByteBuffer input) throws Exception {
        // 1.
        ByteParserDecodeResult byteParserDecodeResult = ByteParserExecutor.decode(msgId, input, byteParserConfig);
        if(byteParserDecodeResult.isDecodeSuccess) {
            logger.debug("ByteParser.parse: message {} parse success.", msgId);
            return byteParserDecodeResult.getResultMap();
        } else {
            logger.warn("ByteParser.parse: message {} parse failed!", msgId);
            return null;
        }
    }

    @Override
    public int getRuleNum() {
        return 0;
    }
}
