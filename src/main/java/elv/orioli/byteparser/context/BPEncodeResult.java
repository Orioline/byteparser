package elv.orioli.byteparser.context;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Orioline on 2017/5/26.
 */
public class BPEncodeResult {
    protected ByteArrayOutputStream bufferOutput = new ByteArrayOutputStream();

    public BPEncodeResult append(byte[] bytes) {
        try {
            bufferOutput.write(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public byte[] getResult() {
        return bufferOutput.toByteArray();
    }
}
