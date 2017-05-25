package elv.orioli.byteparser.rule;

import elv.orioli.byteparser.BPContext;

import java.nio.ByteBuffer;
import java.util.Map;

/**
 * Created by Orioline on 2017/5/24.
 */
public class BPSwitchRule implements BPRule {
    Map<Object, BPRule> patterns;
    private String ruleName;
    private BPRule defaultRule;
    private String switchValue;

    @Override
    public void handle(ByteBuffer buffer, BPContext ctx) {
        // TODO: condition may require calculation
        Object condition = ctx.getAttr(switchValue);
        BPRule rule = patterns.getOrDefault(condition, defaultRule);
        if (null != rule) {
            rule.handle(buffer, ctx);
        } else {
            throw new RuntimeException("ByteParser: switch rule pattern match fail! currRule = " + this.getName() + ", condition = " + condition.toString());
        }
    }

    @Override
    public String getName() {
        return this.ruleName;
    }
}
