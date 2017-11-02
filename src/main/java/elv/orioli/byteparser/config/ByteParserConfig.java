package elv.orioli.byteparser.config;

import elv.orioli.byteparser.rule.ByteParserRule;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by Orioline on 2017/10/31.
 */
public class ByteParserConfig {
    public LinkedHashMap<String, ByteParserRule> configTable = new LinkedHashMap<>(); // (ruleName, rule)
    public HashMap<Long, String> ruleIndexTable = new LinkedHashMap<>();                   // (protocolId, ruleName)

    public HashMap<Long, ByteParserRule> ruleTable = new HashMap<>();   // (protocolId, ruleName)

    /**
     * Init byte parser config by config table.
     * @param configTable
     */
    public void init(LinkedHashMap<String, ByteParserRule> configTable, HashMap<Long, String> ruleIndex) {
        this.configTable = configTable;
    }

    /**
     * Add a byte parser rule into byte parser config;
     * @param protocolId
     * @param rule
     */
    public void addRule(Long protocolId, ByteParserRule rule) {
        //this.configTable.put(key, rule);
        if(null != protocolId) {
            //this.ruleIndexTable.put(protocolId, key);
            this.ruleTable.put(protocolId, rule);
        }
    }
}
