package elv.orioli.byteparser.rule;

import elv.orioli.byteparser.BPContext;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * Created by Orioline on 2017/5/24.
 */
public class BPSeqRule implements BPRule {
    List<BPRule> subRules;
    private String ruleName;

    @Override
    public void handle(ByteBuffer buffer, BPContext ctx) {
        subRules.forEach(bpRule -> bpRule.handle(buffer, ctx));
    }

    @Override
    public String getName() {
        return this.ruleName;
    }
}
