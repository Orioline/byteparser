package elv.orioli.byteparser.rule;

import elv.orioli.byteparser.context.ByteParserContext;
import elv.orioli.byteparser.context.ByteParserDecodeContext;

import java.nio.ByteBuffer;
import java.util.LinkedHashMap;

/**
 * Created by Orioline on 2017/5/24.
 */
public interface ByteParserRule {
    Object handleDecode(ByteBuffer buffer, ByteParserDecodeContext ctx);

    default void handleEncode(LinkedHashMap map, ByteParserContext ctx) {
        throw new RuntimeException("This method not implemented yet!");
    }

    String getName();

    int getRulesNum();

    default int eval(ByteParserDecodeContext ctx, String expr) {
        // TODO:
        return Integer.parseInt(expr);
    }
}
