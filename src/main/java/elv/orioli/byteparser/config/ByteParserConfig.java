package elv.orioli.byteparser.config;

import elv.orioli.byteparser.rule.ByteParserRule;

import java.util.LinkedHashMap;

/**
 * Created by Orioline on 2017/10/31.
 */
public class ByteParserConfig {
    public LinkedHashMap<Long, ByteParserRule> configTable = null; // = new LinkedHashMap<>();

    public void init(String config) {
        this.configTable = new LinkedHashMap<>();
    }

    public void init(LinkedHashMap<Long, ByteParserRule> configTable) {
        this.configTable = configTable;
    }
}
