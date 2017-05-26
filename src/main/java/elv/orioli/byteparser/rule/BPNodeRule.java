package elv.orioli.byteparser.rule;

import elv.orioli.byteparser.context.BPContext;

import java.nio.ByteBuffer;

/**
 * Created by Orioline on 2017/5/24.
 */
public class BPNodeRule implements BPRule {
    private String ruleName;

    @Override
    public void handleDecode(ByteBuffer buffer, BPContext ctx) {
        return;
    }

    @Override
    public String getName() {
        return this.ruleName;
    }
}
