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
        if (strNodeCfg.contains(":")) {
            int divIndex = strNodeCfg.indexOf(":");
            this.nodeName = strNodeCfg.substring(0, divIndex).trim();
            this.body = strNodeCfg.substring(divIndex + 1).trim();
        } else {
            throw new RuntimeException("Node name parse failed! currNode = " + strNodeCfg);
        }
    }

    void parse() {
        if (isLeafNode()) {
            this.nodeType = E_NODE_TYPE.LeafNode;
            parseNodeAttributes();
        } else {
            List<String> strings = splitCurrLayerCfg(strNodeCfg);
            subNodes = new LinkedList<>();
            strings.forEach(strSubNodeCfg -> subNodes.add(new BPStructConfigNode(strSubNodeCfg)));
            subNodes.forEach(bpStructConfigNode -> parse());
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
                this.dataFieldType = E_DataFieldType.valueOf(m.group(1));
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
