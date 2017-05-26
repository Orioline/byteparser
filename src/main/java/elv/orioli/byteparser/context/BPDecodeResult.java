package elv.orioli.byteparser.context;

import java.util.LinkedHashMap;

/**
 * Created by Orioline on 2017/5/24.
 */
public class BPDecodeResult {
    protected LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();

    private BPDecodeResult append(String key, Object value) {
        this.resultMap.put(key, value);
        return this;
    }
}
