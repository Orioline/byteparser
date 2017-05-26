package elv.orioli.byteparser;

import elv.orioli.byteparser.config.BPConfig;
import elv.orioli.byteparser.context.BPDecodeContext;
import elv.orioli.byteparser.context.BPDecodeResult;
import elv.orioli.byteparser.context.BPEncodeContext;
import elv.orioli.byteparser.context.BPEncodeResult;

import java.nio.ByteBuffer;
import java.util.LinkedHashMap;

/**
 * Created by Orioline on 2017/5/24.
 */
public class BPExecutor {
    public BPDecodeResult decode(byte[] data, BPConfig bpConfig) {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        BPDecodeContext context = new BPDecodeContext();
        bpConfig.getBpRule().handleDecode(buffer, context);
        return context.getBpDecodeResult();
    }

    public BPEncodeResult encode(LinkedHashMap<String, Object> map, BPConfig bpConfig) {
        BPEncodeContext context = new BPEncodeContext();
        bpConfig.getBpRule().handleEncode(map, context);
        return context.getBpEncodeResult();
    }
}
