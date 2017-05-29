package elv.orioli.byteparser.rule;

import elv.orioli.byteparser.context.BPContext;

import java.nio.ByteBuffer;
import java.util.LinkedHashMap;

/**
 * Created by Orioline on 2017/5/24.
 */
public interface BPRule {
    void handleDecode(ByteBuffer buffer, BPContext ctx);

    default void handleEncode(LinkedHashMap map, BPContext ctx) {
        throw new RuntimeException("This method not implemented yet!");
    }

    String getName();

    int getRulesNum();
}
