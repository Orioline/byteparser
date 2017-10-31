package elv.orioli.byteparser.rule;

import elv.orioli.byteparser.config.E_DataFieldType;
import elv.orioli.byteparser.context.ByteParserDecodeContext;
import elv.orioli.byteparser.util.ByteBufferOperator;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

/**
 * Created by Orioline on 2017/10/31.
 */
public class ByteParserNodeRule implements ByteParserRule {

    public String name;
    public String desc;

    // reading rule
    public String valueSource;
    public String valueLength;
    public E_DataFieldType valueType;

    // state transfer rule
    public String stateTransferRule; // default = next rule;

    public String bindingRule;

    public String outputRule;

    @Override
    public Object handleDecode(ByteBuffer buffer, ByteParserDecodeContext ctx) {

        Object rawData;
        switch (valueType) {
            case NUMBER:
                rawData = getValueFromBuffer(buffer, eval(ctx, valueLength));
                break;
            case STRING:
                if (null == valueLength || "".equals(valueLength)) {
                    rawData = getStringFromBuffer(buffer);
                } else {
                    rawData = getStringFromBuffer(buffer, eval(ctx, valueLength));
                }
                break;
            case BYTES:
                rawData = getBytesFromBuffer(buffer, eval(ctx, valueLength));
                break;
            default:
                throw new RuntimeException("ByteParserNodeRule.handleDecode: value type " + valueType.name() + " not support!");
        }

        Object result;
        if (rawData instanceof byte[]) {
            result = ByteBufferOperator.bytesToHex((byte[]) rawData);
        } else if (rawData instanceof Number) {
            result = rawData;
        } else {
            throw new RuntimeException("ByteParserNodeRule.handleDecode: value type not support!");
        }

        // TODO: binding

        return result;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public int getRulesNum() {
        return 0;
    }

    private String getStringFromBuffer(ByteBuffer buffer) {
        return ByteBufferOperator.getStrFromBuffer(buffer);
    }

    private String getStringFromBuffer(ByteBuffer buffer, int size) {
        return ByteBufferOperator.getStrFromBuffer(buffer, size);
    }

    private byte[] getBytesFromBuffer(ByteBuffer buffer, int size) throws BufferUnderflowException {
        byte[] arr = new byte[size];
        buffer.get(arr);
        return arr;
    }

    // TODO: Little-endian
    private Object getSignedValueFromBuffer(ByteBuffer buffer, int size) throws BufferUnderflowException {
        switch (size) {
            case 1:
                return buffer.get();
            case 2:
                return buffer.getShort();
            case 4:
                return buffer.getInt();
            case 8:
                return buffer.getLong();
            case 0:
                throw new RuntimeException("getValueFromBuffer: size (" + size + ") not support now!");
            default:
                byte[] bytes = new byte[size];
                buffer.get(bytes);
                return bytes;
        }
    }

    private Object getValueFromBuffer(ByteBuffer buffer, int size) throws BufferUnderflowException {
        switch (size) {
            case 1:
                return buffer.get() & 0xFFL;
            case 2:
                return buffer.getShort() & 0xFFFFL;
            case 4:
                return buffer.getInt() & 0xFFFFFFFFL;
            case 8:
                return buffer.getLong();
            case 0:
                throw new RuntimeException("getValueFromBuffer: size (" + size + ") not support now!");
            default:
                byte[] bytes = new byte[size];
                buffer.get(bytes);
                return bytes;
        }
    }
}
