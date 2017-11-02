package elv.orioli.byteparser;

import elv.orioli.byteparser.config.ByteParserConfig;

import java.nio.ByteBuffer;
import java.util.Map;

/**
 * Created by Orioline on 2016/11/18.
 */
public interface IByteParser {
    void init(ByteParserConfig byteParserConfig) throws Exception;

    void init(String strConfig, String configType) throws Exception;

    default Map<String, Object> parse(long msgId, ByteBuffer input) throws Exception {
        return parse(msgId, input.array());
    }

    default Map<String, Object> parse(long msgId, byte[] input) throws Exception {
        return parse(msgId, ByteBuffer.wrap(input));
    }

    int getRuleNum();
}
