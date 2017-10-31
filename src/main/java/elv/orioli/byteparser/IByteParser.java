package elv.orioli.byteparser;

import java.nio.ByteBuffer;
import java.util.Map;

/**
 * Created by Orioline on 2016/11/18.
 */
public interface IByteParser {
    void init(Object byteParserConfig) throws Exception;

    default Map<String, Object> parse(long msgId, ByteBuffer input) throws Exception {
        return parse(msgId, input.array());
    }

    default Map<String, Object> parse(long msgId, byte[] input) throws Exception {
        return parse(msgId, ByteBuffer.wrap(input));
    }

    int getRuleNum();
}
