package elv.orioli.byteparser.context;

import java.util.LinkedHashMap;

/**
 * Created by Orioline on 2017/5/24.
 */
public class ByteParserDecodeResult {
    public boolean isDecodeSuccess;
    protected Throwable cause;
    protected LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();

    void setResultFail(Throwable t) {
        this.isDecodeSuccess = false;
        this.cause = t;
        this.resultMap = null;
    }

    void setResultMap(LinkedHashMap<String, Object> resultMap) {
        this.isDecodeSuccess = true;
        this.resultMap = resultMap;
    }

    public LinkedHashMap<String, Object> getResultMap() {
        return resultMap;
    }
}
