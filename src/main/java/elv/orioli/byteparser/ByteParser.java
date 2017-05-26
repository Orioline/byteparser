package elv.orioli.byteparser;

import elv.orioli.byteparser.config.BPConfig;
import elv.orioli.byteparser.config.BPStructConfigParser;

import java.util.LinkedHashMap;

/**
 * This is the main class of ByteParser tool.
 * Created by Orioline on 2017/5/24.
 */
public class ByteParser {

    public final BPConfig bpConfig;

    private ByteParser(BPConfig bpConfig) {
        this.bpConfig = bpConfig;
    }

    public static ByteParser getInstance(String strCfg) {
        return new ByteParser(BPStructConfigParser.compile(strCfg));
    }

    public static ByteParser getInstance(BPConfig bpConfig) {
        return new ByteParser(bpConfig);
    }

    /**
     * 'Deserialize' the specified bytes into its equivalent representation as a tree of Maps.
     * The structure of bytes should be represented by the format of {@link BPConfig}.
     */
    public LinkedHashMap<String, Object> decode(byte[] bytes) {
        return null;
    }

    /**
     * 'Serialize' the specified map to bytes.
     * The structure of bytes should be represented by the format of {@link BPConfig}.
     */
    public byte[] encode(LinkedHashMap<String, Object> map) {
        return null;
    }
}
