package elv.orioli.byteparser.executor;

import elv.orioli.byteparser.config.ByteParserConfig;
import elv.orioli.byteparser.context.ByteParserDecodeContext;
import elv.orioli.byteparser.context.ByteParserDecodeResult;

import java.nio.ByteBuffer;

/**
 * Created by Orioline on 2017/5/24.
 */
public class ByteParserExecutor {
    public static ByteParserDecodeResult decode(long msgId, ByteBuffer buffer, ByteParserConfig bpConfig) {
        //ByteBuffer buffer = ByteBuffer.wrap(data);
        ByteParserDecodeContext context = new ByteParserDecodeContext(msgId, buffer, bpConfig);
        return context.doParsing();
    }
}
