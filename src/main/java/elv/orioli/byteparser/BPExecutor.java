package elv.orioli.byteparser;

import java.nio.ByteBuffer;
import java.util.LinkedHashMap;

/**
 * Created by Orioline on 2017/5/24.
 */
public class BPExecutor {
    public BPDecodeResult decode(byte[] data, BPConfig bpConfig) {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        BPDecodeContext context = new BPDecodeContext();
        bpConfig.bpRule.handleDecode(buffer, context);
        return context.getBpDecodeResult();
    }

    public BPEncodeResult encode(LinkedHashMap<String, Object> map, BPConfig bpConfig) {
        BPEncodeContext context = new BPEncodeContext();
        bpConfig.bpRule.handleEncode(map, context);
        return context.getBpEncodeResult();
    }
}
