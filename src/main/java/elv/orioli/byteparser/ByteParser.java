package elv.orioli.byteparser;

import elv.orioli.byteparser.config.ByteParserConfig;
import elv.orioli.byteparser.configparser.ByteParserMarkDownCfgParser;
import elv.orioli.byteparser.context.ByteParserDecodeResult;
import elv.orioli.byteparser.executor.ByteParserExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Objects;

/**
 * The ByteParser Main Class
 * Created by Orioline on 2017/10/31.
 */
public class ByteParser implements IByteParser {
    private Logger logger = LoggerFactory.getLogger(ByteParser.class);
    protected ByteParserConfig byteParserConfig;

    /**
     * User must init the byteParser before using it.
     * @param config config
     * @throws Exception
     */
    @Override
    public void init(ByteParserConfig config) throws Exception {
        Objects.requireNonNull(config, "byteParserConfig should not be null!");
        this.byteParserConfig = config;
    }

    @Override
    public void init(String strConfig, String configType) {
        // if "MarkDown".equals(configType);
        this.byteParserConfig = ByteParserMarkDownCfgParser.parse(strConfig);
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
