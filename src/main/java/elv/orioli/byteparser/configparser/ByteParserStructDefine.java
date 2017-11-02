package elv.orioli.byteparser.configparser;

import elv.orioli.byteparser.config.E_DataFieldType;
import elv.orioli.byteparser.rule.ByteParserArrayRule;
import elv.orioli.byteparser.rule.ByteParserNodeRule;
import elv.orioli.byteparser.rule.ByteParserObjectRule;
import elv.orioli.byteparser.rule.ByteParserRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * parse the configurations for current struct, for example:
 * <p>
 * ## struct name
 * - attrKey:attrValue
 * name | type | length | ....
 * ---- | ---- | ------ | ----
 * n1   | INT  | 4      | ....
 * <p>
 * this format should
 * Created by Orioline on 2017/11/1.
 */
public class ByteParserStructDefine {

    Logger logger = LoggerFactory.getLogger(ByteParserStructDefine.class);

    public String name;
    public Long protocolId = null;
    public Map<String, String> attributes = new LinkedHashMap<>();
    public String[] titles;
    public List<String[]> columns = new ArrayList<>();

    /**
     * create a ByteParserStructDefine by config;
     * if config cannot form a table, then this method will return null.
     *
     * @param structCfgLines the lines of config files;
     * @param startLine      inclusive
     * @param endLine        exclusive
     * @return
     */
    public static ByteParserStructDefine createTable(String[] structCfgLines, int startLine, int endLine) {
        ByteParserStructDefine byteParserStructDefine = new ByteParserStructDefine(structCfgLines, startLine, endLine);
        if (null == byteParserStructDefine.name) {
            return null;
        } else {
            return byteParserStructDefine;
        }
    }

    public ByteParserStructDefine(String[] structCfgLines, int startLine, int endLine) {
        int table_row_index = 0;
        for (int lineNo = startLine; lineNo < endLine; lineNo++) {
            String currLine = structCfgLines[lineNo].trim();
            // skip comments and blank lines;
            if ("".equals(currLine) || currLine.startsWith("//")) {
                continue;
            }

            // init name
            if (currLine.startsWith(MarkdownConstants.TITLE)) {
                if (null == this.name) {
                    this.name = currLine.substring(MarkdownConstants.TITLE.length()).trim();
                } else {
                    logger.warn("WARNING (Line {}): multiple name definitions in {} block, skip this line '{}'.", lineNo, this.name, currLine);
                }
                continue;
            }

            // init attributes
            if (currLine.startsWith(MarkdownConstants.LIST)) {
                String strAttrLine = currLine.substring(MarkdownConstants.LIST.length()).trim();
                if (strAttrLine.contains(":")) {
                    String[] kvPair = strAttrLine.split(":");
                    if (2 == kvPair.length) {
                        String key = kvPair[0].trim();
                        String value = kvPair[1].trim();
                        this.attributes.put(key, value);
                    } else {
                        logger.warn("WARNING (Line {}): attribute line format should like '- key:value'! skip this line '{}'.", lineNo, currLine);
                        continue;
                    }
                } else {
                    try {
                        if (strAttrLine.startsWith("0x")) {
                            String strNum = strAttrLine.substring("0x".length());
                            this.protocolId = Long.parseLong(strNum, 16);
                        } else {
                            this.protocolId = Long.parseLong(strAttrLine, 10);
                        }
                        logger.debug("DEBUG (Line {}): add protocolId {} for block {}", lineNo, this.protocolId, this.name);
                    } catch (Exception e) {
                        logger.warn("WARNING (Line {}): unknown protocolId attribute '{}'!", lineNo, strAttrLine);
                    } finally {
                        continue;
                    }
                }
            }

            // init struct define table
            // TODO: does not support escape string \| yet;
            if (currLine.contains(" | ")) {
                table_row_index++;
                String[] currColumns = currLine.split(" \\| ");
                if (1 == table_row_index) {
                    // title
                    titles = new String[currColumns.length];
                    for (int i = 0; i < currColumns.length; i++) {
                        titles[i] = currColumns[i].trim();
                    }
                } else if (2 == table_row_index
                        && (currLine.startsWith("--")
                        || currLine.startsWith(":-")
                        || currLine.startsWith("-:"))
                        ) {
                    // if table format looks like
                    // name | type | desc  // (line 1)
                    // ---- | ---- | ----  // (line 2)
                    // id   | INT  | ID    // (line 3)
                    // then skip the second line.
                    continue;
                } else {
                    // table column
                    Objects.requireNonNull(titles, "Table title must be initialized first!");
                    int currRowSize = Math.min(titles.length, currColumns.length);
                    String[] currRowArr = new String[titles.length];
                    for (int i = 0; i < currRowSize; i++) {
                        // TODO: what if rowArr length is shorter than title?
                        currRowArr[i] = currColumns[i].trim();
                    }
                    this.columns.add(currRowArr);
                }
            } else {
                if (table_row_index > 0) {
                    logger.warn("WARNING (Line {}): struct define table parse failed! skip this line '{}'.", lineNo, currLine);
                    continue;
                }
            }

            // TODO: other elements in block
        }
    }

    public String getName() {
        return this.name;
    }

    public Long getProtocolId() {
        if (null != protocolId) {
            return protocolId;
        } else {
            return null;
        }
    }

    /**
     * 'compile' current struct into a ByteParserRule.
     * if this rule contains another rule, then lookup in 'symbolTable' and call getRule() recursively,
     * then 'link' this rule with the other one.
     *
     * @param symbolTable
     * @return
     */
    public ByteParserRule getRule(ByteParserSymbolTable symbolTable) {
        int titleLength = titles.length;
        Map<E_ConfigTitle, Integer> titleIndexMap = new HashMap<>();
        for (int titleIndex = 0, titlesLength = titles.length; titleIndex < titlesLength; titleIndex++) {
            String title = titles[titleIndex];
            titleIndexMap.put(E_ConfigTitle.valueOf(title), titleIndex);
        }

        for (String[] column : columns) {
            // pick the attributes of current row;
            // find the type of rule: node/object/array;
            //   if (is array or object): find the jump/loop config, recursively call symbolTable.getSymbol().getRule(symbolTable);
            // TODO: runtime: value from cache;
            // find the length/existence type (for runtime)
            //
            // rules:(mapping, ratio-offset, range, bind/cache, )
            // output rule: (use default)

            if (!titleIndexMap.containsKey(E_ConfigTitle.NAME)
                    || !titleIndexMap.containsKey(E_ConfigTitle.TYPE)) {
                throw new RuntimeException("Error in rule '" + this.name + "', table must contain 'name' and 'type'!");
            }

            // Required
            String name = column[titleIndexMap.get(E_ConfigTitle.NAME)];
            String strType = column[titleIndexMap.get(E_ConfigTitle.TYPE)];
            E_DataFieldType dataFieldType = E_DataFieldType.typeOf(strType);
            if (null == dataFieldType) {
                throw new RuntimeException("Error in rule '" + this.name + "', type '" + strType + "' not support!");
            }

            // Optional
            String length;
            if (titleIndexMap.containsKey(E_ConfigTitle.LENGTH)) {
                length = column[titleIndexMap.get(E_ConfigTitle.LENGTH)];
            } else {
                length = generateDefaultLengthByType(dataFieldType);
                if (null == length) {
                    throw new RuntimeException("Error in rule '" + this.name + "', type '" + strType + "' must specify length!");
                }
            }

            String desc;
            if (titleIndexMap.containsKey(E_ConfigTitle.DESC)) {
                desc = column[titleIndexMap.get(E_ConfigTitle.DESC)];
            } else {
                desc = name + " : " + strType + "(" + length + ")";
            }

            ByteParserRule byteParserRule;
            switch (dataFieldType) {
                case ARRAY:
                    byteParserRule = new ByteParserArrayRule();
                    break;
                case OBJECT:
                    byteParserRule = new ByteParserObjectRule();
                    break;
                default:
                    byteParserRule = new ByteParserNodeRule();
                    break;
            }
        }

        // TODO: return what?
        return null;
    }

    /**
     * generate default length by datafield type.
     *
     * @param type E_DataFieldType type
     * @return
     */
    private String generateDefaultLengthByType(E_DataFieldType type) {
        switch (type) {
            case BOOLEAN:
            case BYTE8:
            case UNSIGNED_BYTE8:
                return "1";
            case SHORT16:
            case UNSIGNED_SHORT16:
                return "2";
            case FLOAT:
            case INT32:
            case UNSIGNED_INT32:
                return "4";
            case LONG64:
            case DOUBLE:
                return "8";
            case OBJECT:
            case ARRAY:
            case STRING:
                return "";
            default:
                return null;
        }
    }
}
