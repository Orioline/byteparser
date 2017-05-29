package elv.orioli.byteparser.rule;

import elv.orioli.byteparser.context.BPContext;

import java.nio.ByteBuffer;

/**
 * Created by Orioline on 2017/5/24.
 */
public class BPNodeRule implements BPRule {
    public E_DataFieldType dataFieldType;
    public int size;
    public boolean isBitOpr;             // the unit of size, true - bits, false - bytes;
    // data type
    private String ruleName;
    //

    public BPNodeRule(String ruleName, E_DataFieldType dataFieldType, int size, boolean isBitOpr) {
        this.ruleName = ruleName;
        this.dataFieldType = dataFieldType;
        this.size = size;
        this.isBitOpr = isBitOpr;
    }

    @Override
    public void handleDecode(ByteBuffer buffer, BPContext ctx) {
        return;
    }

    @Override
    public String getName() {
        return this.ruleName;
    }

    @Override
    public int getRulesNum() {
        return 1;
    }

    public BPNodeRule setSize(int size, boolean isBitOpr) {
        this.size = size;
        this.isBitOpr = isBitOpr;
        return this;
    }

    public BPNodeRule setDataFieldType(E_DataFieldType dataFieldType) {
        this.dataFieldType = dataFieldType;
        return this;
    }
}
