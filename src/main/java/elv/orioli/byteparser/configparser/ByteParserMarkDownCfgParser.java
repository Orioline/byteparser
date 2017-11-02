package elv.orioli.byteparser.configparser;

import elv.orioli.byteparser.config.ByteParserConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Orioline on 2017/11/1.
 */
public class ByteParserMarkDownCfgParser {
    static Logger logger = LoggerFactory.getLogger(ByteParserMarkDownCfgParser.class);

    public static ByteParserConfig parse(String strConfig) {

        // 1. split config by ## delimiter;
        String[] lines = strConfig.split("\n");
        List<Integer> tableDelimLineMarks = new ArrayList<>();
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            // new table
            if (line.startsWith("## ")) {
                // create previous table by config;
                tableDelimLineMarks.add(i);
            }
        }
        tableDelimLineMarks.add(lines.length);
        logger.debug("PARSING CFG FILE --- config file contains {} lines, {} blocks.", lines.length, tableDelimLineMarks.size() - 1);
        if (tableDelimLineMarks.size() <= 1) {
            logger.info("Config file does not contain valid configuration.");
            return null;
        }

        // 2. create cfg for each table
        ByteParserSymbolTable symbolTable = new ByteParserSymbolTable();
        List<ByteParserStructDefine> cfgTableList = new LinkedList<>();
        for (int j = 0; j < tableDelimLineMarks.size() - 1; j++) {
            ByteParserStructDefine cfgTable = ByteParserStructDefine.createTable(lines,
                    tableDelimLineMarks.get(j),
                    tableDelimLineMarks.get(j + 1));
            if (null != cfgTable) {
                symbolTable.symbolTable.put(cfgTable.getName(), cfgTable);
                if (null != cfgTable.getProtocolId()) {
                    cfgTableList.add(cfgTable);
                }
            }
        }
        logger.debug("PARSING CFG FILE --- created {} config table(s).", symbolTable.symbolTable.size());
        if (0 == symbolTable.symbolTable.size()) {
            logger.info("Config file does not contain valid configuration.");
            return null;
        }

        // 3. init byteParserConfig
        ByteParserConfig byteParserConfig = new ByteParserConfig();
        for (ByteParserStructDefine structDefineTable : cfgTableList) {
            byteParserConfig.addRule(structDefineTable.getProtocolId(), structDefineTable.getRule(symbolTable));
        }
        logger.debug("PARSING CFG FILE --- created {} parser rule(s), with {} index(s).", byteParserConfig.configTable.size(), byteParserConfig.ruleIndexTable.size());

        return byteParserConfig;
    }
}
