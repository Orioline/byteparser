package elv.orioli.byteparser.context;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Orioline on 2017/5/24.
 */
public class ByteParserContext {
    protected Map<String, Object> attrMap = null;

    public void addAttr(String key, Object value) {
        if (null == attrMap) {
            attrMap = new LinkedHashMap<>();
        }
        attrMap.put(key, value);
    }

    public Object getAttr(String key) throws RuntimeException {
        if (null == attrMap) {
            throw new RuntimeException("ByteParserContext: attrMap not init while getting key '" + key + "'!");
        }
        Object result = attrMap.get(key);
        if (null == result) {
            throw new RuntimeException("ByteParserContext: Attr '" + key + "' not found!");
        }
        return result;
    }

    public Object getAttrOrDefault(String key, Object defaultValue) {
        if (null == attrMap) {
            return defaultValue;
        }

        return attrMap.getOrDefault(key, defaultValue);
    }

    public int eval(String expr) {
        // TODO:
        return Integer.parseInt(expr);
    }
}
