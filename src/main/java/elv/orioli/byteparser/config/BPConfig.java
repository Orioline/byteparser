package elv.orioli.byteparser.config;

import elv.orioli.byteparser.rule.BPRule;

/**
 * Created by Orioline on 2017/5/24.
 */
public class BPConfig {
    BPRule bpRule;

    BPConfig(BPRule bpRule) {
        this.bpRule = bpRule;
    }

    public BPRule getBpRule() {
        return bpRule;
    }
}
