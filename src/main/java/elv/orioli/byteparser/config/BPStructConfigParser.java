package elv.orioli.byteparser.config;

import elv.orioli.byteparser.rule.BPNodeRule;
import elv.orioli.byteparser.rule.BPRule;
import elv.orioli.byteparser.rule.BPSeqRule;
import elv.orioli.byteparser.rule.BPSwitchRule;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Orioline on 2017/5/26.
 */
public class BPStructConfigParser extends BPConfigParser {

    /**
     * Try to 'compile' a config with following format:
     *
     * <p>
     * id: Int(2);      // comment
     * desc: String(8); // ';' is optional
     * arr: UChar(1)[10];
     * <p>
     * attrs : {
     * attrA : Float(4);
     * attrB : Boolean(1bit);
     * attrC : Int(2Bit);
     * _ : Boolean(5Bit);
     * };
     *
     * @param strCfg config file content
     * @return compiled config
     */
    public static BPConfig compile(String strCfg) {

        // 1-1. pre processing
        String trimmedCfg = trimConfig(strCfg);

        // 1-2. config string to structured tree
        BPStructConfigNode mainNode = new BPStructConfigNode(trimmedCfg);
        mainNode.parse();

        // 1-3. structured tree to BPRule tree;
        BPRule rule = generateBPRule(mainNode);

        return new BPConfig(rule);
    }

    private static String trimConfig(String strCfg) {
        String[] cfgLines = strCfg.split("\\n", 0);
        StringBuilder sb = new StringBuilder();
        for (String cfgLine : cfgLines) {
            String varStrLine;
            // remove comment
            if (cfgLine.contains("//")) {
                varStrLine = cfgLine.substring(0, cfgLine.indexOf("//"));
            } else {
                varStrLine = cfgLine;
            }

            // remove useless whitespaces;
            varStrLine = varStrLine.replaceAll(";", "")
                    .trim()
                    .replaceAll("\\s{2,}", " ");
            if (varStrLine.equals("")) {
                // discard empty lines;
                continue;
            }
            sb.append(varStrLine).append("\n");
        }

        return sb.toString();
    }

    private static BPRule generateBPRule(BPStructConfigNode node) {
        BPRule rule;
        switch (node.nodeType) {
            case LeafNode:
                rule = new BPNodeRule(node.nodeName);
                break;
            case Seq:
                List<BPRule> subBPRules = node.subNodes.stream().map(subNode -> generateBPRule(node)).collect(Collectors.toList());
                rule = new BPSeqRule(node.nodeName, subBPRules);
                break;
            case Switch:
                List<BPRule> optionalBPRules = node.subNodes.stream().map(subNode -> generateBPRule(node)).collect(Collectors.toList());
                // TODO:
                //rule = new BPSwitchRule(node.nodeName, optionalBPRules);
                rule = new BPSwitchRule();
                break;
            default:
                throw new RuntimeException("Error: generateBPRule failed!");
        }
        return rule;
    }
}
