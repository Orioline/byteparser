package elv.orioli.byteparser.config;

import elv.orioli.byteparser.rule.ByteParserRule;

import java.util.HashMap;

/**
 * Created by Orioline on 2017/10/31.
 */
public class ByteParserConfig {
    public HashMap<Long, ByteParserRule> ruleTable = new HashMap<>();   // (protocolId, ruleName)

    /**
     * Add a byte parser rule into byte parser config;
     * @param protocolId id of given byte parser rule.
     * @param rule byte parser rule to add to ruleTable;
     */
    public void addRule(Long protocolId, ByteParserRule rule) {
        if(null != protocolId) {
            this.ruleTable.put(protocolId, rule);
        }
    }
}
