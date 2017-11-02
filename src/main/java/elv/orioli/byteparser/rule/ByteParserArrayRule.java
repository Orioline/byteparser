package elv.orioli.byteparser.rule;

import elv.orioli.byteparser.context.ByteParserDecodeContext;

import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * Created by Orioline on 2017/10/31.
 */
public class ByteParserArrayRule implements ByteParserRule {
    public String name;
    public String desc;

    public boolean isArrayLengthFixed;
    public String arrayLengthCfg;

    public ByteParserNodeRule nodeRule;

    //public long arrayLengthCfg;

    @Override
    public Object handleDecode(ByteBuffer buffer, ByteParserDecodeContext ctx) {
        int arrLength;

        if (isArrayLengthFixed) {
            arrLength = Integer.parseInt(arrayLengthCfg);
        } else {
            arrLength = ctx.eval(arrayLengthCfg);
        }

        ArrayList<Object> result = new ArrayList<>(arrLength);
        for (int i = 0; i < arrLength; i++) {
            result.add(nodeRule.handleDecode(buffer, ctx));
        }

        return result;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getRulesNum() {
        return 0;
    }
}
