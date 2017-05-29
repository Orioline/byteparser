package elv.orioli.byteparser.config;


import elv.orioli.byteparser.rule.E_DataFieldType;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Orioline on 2017/5/26.
 */
public class BPStructConfigNode {
    static final String nodeFieldMatchPattern = "(\\w*)\\s*\\(([0-9]*)([a-zA-Z]{3,4})?\\)\\s*(\\[([0-9]*)])?";

    public final String strNodeCfg;
    public String nodeName;
    public String body;
    E_NODE_TYPE nodeType;
    List<BPStructConfigNode> subNodes = null;
    E_DataFieldType dataFieldType;
    int size;
    boolean isBitOpr;             // the unit of size, true - bits, false - bytes;
    boolean isArray;
    String arraySize;

    public BPStructConfigNode(String strNodeCfg) {
        this.strNodeCfg = strNodeCfg;
        parseNameAndBody();
    }

    void parseNameAndBody() {
        int firstLineIndex = this.strNodeCfg.indexOf("\n");
        String strCfgFirstLine;
        if (-1 == firstLineIndex) {
            strCfgFirstLine = this.strNodeCfg;
        } else {
            strCfgFirstLine = this.strNodeCfg.substring(0, firstLineIndex);
        }

        if (strCfgFirstLine.contains(":")) {
            int divIndex = strCfgFirstLine.indexOf(":");
            this.nodeName = strCfgFirstLine.substring(0, divIndex).trim();
            this.body = flat(strCfgFirstLine.substring(divIndex + 1).trim());
        } else {
            this.nodeName = null;
            this.body = flat(this.strNodeCfg);
        }
    }

    private String flat(String strBody) {
        String strData = strBody.trim();
        if (strData.startsWith("{") && strData.endsWith("}")) {
            strData = strData.substring(1, strBody.length() - 1).trim();
        }

        return strData;
    }

    void parse() {
        if (isLeafNode()) {
            this.nodeType = E_NODE_TYPE.LeafNode;
            parseNodeAttributes();
        } else {
            List<String> strings = splitCurrLayerCfg(this.body);
            subNodes = new LinkedList<>();
            strings.forEach(strSubNodeCfg -> subNodes.add(new BPStructConfigNode(strSubNodeCfg)));
            subNodes.forEach(BPStructConfigNode::parse);
            this.nodeType = E_NODE_TYPE.Seq;
        }
    }

    private List<String> splitCurrLayerCfg(String strCfg) {
        int level = 0;
        List<String> strCfgList = new LinkedList<>();
        List<Integer> splitPosList = new LinkedList<>();

        splitPosList.add(-1);
        for (int i = 0; i < strCfg.length(); i++) {
            char c = strCfg.charAt(i);
            if ('\n' == c) {
                if (0 == level) {
                    splitPosList.add(i);
                }
            } else if ('{' == c) {
                level++;
            } else if ('}' == c) {
                level--;
            }
        }
        if (0 != level) {
            throw new RuntimeException("Config parse error, braces nesting error!");
        }
        splitPosList.add(strCfg.length());

        for (int t = 0; t < splitPosList.size() - 1; t++) {
            int startPos = splitPosList.get(t) + 1;
            int endPos = splitPosList.get(t + 1);
            String strCurrCfg = strCfg.substring(startPos, endPos);
            strCfgList.add(strCurrCfg);
        }

        return strCfgList;
    }

    private boolean isLeafNode() {
        // one config must be written in one line;
        return !strNodeCfg.contains("\n");
    }

    private void parseNodeAttributes() {
        // the body should have the format like "BOOLEAN(1Bit)[4]";
        if (this.nodeType.equals(E_NODE_TYPE.LeafNode)) {
            Pattern p = Pattern.compile(nodeFieldMatchPattern);
            Matcher m = p.matcher(this.body);
            if (m.matches() && 5 == m.groupCount()) {
                this.dataFieldType = E_DataFieldType.typeOf(m.group(1));
                this.size = Integer.parseInt(m.group(2));
                this.isBitOpr = (null != m.group(3)) && ("BIT".equals(m.group(3).toUpperCase()));
                if (null != m.group(4) && null != m.group(5)) {
                    this.isArray = true;
                    this.arraySize = m.group(5);
                }
            } else {
                throw new RuntimeException("Config parse error, data type declaration '" + this.body + "' format incorrect!");
            }
        } else {
            throw new RuntimeException("Framework error: non leaf node should have field type!");
        }
    }

    enum E_NODE_TYPE {
        LeafNode,
        Seq,
        Switch
    }
}
