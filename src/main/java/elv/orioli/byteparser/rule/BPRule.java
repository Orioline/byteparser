package elv.orioli.byteparser.rule;

import elv.orioli.byteparser.BPContext;

import java.nio.ByteBuffer;

/**
 * Created by Orioline on 2017/5/24.
 */
public interface BPRule {
    void handle(ByteBuffer buffer, BPContext ctx);

    String getName();
}
