package elv.orioli.byteparser.rule;

import elv.orioli.byteparser.context.BPContext;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * Created by Orioline on 2017/5/24.
 */
public class BPSeqRule implements BPRule {
    List<BPRule> subRules;
    private String ruleName;

    public BPSeqRule(String ruleName, List<BPRule> subRules) {
        this.ruleName = ruleName;
        this.subRules = subRules;
    }

    @Override
    public void handleDecode(ByteBuffer buffer, BPContext ctx) {
        subRules.forEach(bpRule -> bpRule.handleDecode(buffer, ctx));
    }

    @Override
    public String getName() {
        return this.ruleName;
    }

    @Override
    public int getRulesNum() {
        return subRules != null ? subRules.size() : 0;
    }

    public List<BPRule> getSubRules() {
        return subRules;
    }
}
