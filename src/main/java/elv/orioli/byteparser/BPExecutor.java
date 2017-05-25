package elv.orioli.byteparser;

import java.nio.ByteBuffer;

/**
 * Created by Orioline on 2017/5/24.
 */
public class BPExecutor {
    public BPResult decode(byte[] data, BPConfig bpConfig) {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        BPContext context = new BPContext();
        bpConfig.bpRule.handle(buffer, context);
        return context.getBpResult();
    }
}
