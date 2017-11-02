package elv.orioli.byteparser.util;

import java.math.BigDecimal;
import java.nio.ByteBuffer;

/**
 * Tool to manipulate ByteBuffer and byte arrays.
 * Created by Orioline on 2017/10/31.
 */
public class ByteBufferOperator {
    private static int DECIMAL_ROUND_SCALE = 6;

    // ----
    // Char
    // ----

    /**
     * get unsigned Byte from ByteBuffer, msgBuffer pos += 1
     */
    public static int getUChar(ByteBuffer msgBuffer) {
        return (int) msgBuffer.get() & 0xFF;
    }

    /**
     * get signed Byte from ByteBuffer, msgBuffer pos += 1
     */
    public static int getSignedChar(ByteBuffer msgBuffer) {
        return (int) msgBuffer.get();
    }

    /**
     * get unsigned Byte from ByteBuffer, multiply it by ratio and return, msgBuffer pos +=1
     */
    public static double calcUChar(ByteBuffer msgBuffer, String ratio) {
        return preciseCalculate(getUChar(msgBuffer), ratio);
    }

    public static double calcSignedChar(ByteBuffer msgBuffer, String ratio) {
        return preciseCalculate(msgBuffer.get(), ratio);
    }

    // calcSignedChar

    // ----
    // Short
    // ----

    /**
     * get unsigned Short from ByteBuffer, msgBuffer pos += 2
     */
    public static int getUShort(ByteBuffer msgBuffer) {
        return (int) msgBuffer.getShort() & 0xFFFF;
    }

    public static int getSignedShort(ByteBuffer msgBuffer) {
        return msgBuffer.getShort();
    }

    public static double calcUShort(ByteBuffer msgBuffer, String ratio) {
        return preciseCalculate(getUShort(msgBuffer), ratio);
    }

    public static double calcSignedShort(ByteBuffer msgBuffer, String ratio) {
        return preciseCalculate(msgBuffer.getShort(), ratio);
    }

    public static double calcComplexShort(ByteBuffer msgBuffer, String ratio, String offset) {
        return preciseCalculate(getUShort(msgBuffer), ratio, offset);
    }

    // ----
    // Int
    // ----

    /**
     * get signed integer from ByteBuffer, msgBuffer pos+=4
     */
    public static int getInt(ByteBuffer msgBuffer) {
        return msgBuffer.getInt();
    }

    public static long getUInt(ByteBuffer msgBuffer) {
        return msgBuffer.getInt() & 0xFFFFFFFFL;
    }

    public static double calcInt(ByteBuffer msgBuffer, String ratio) {
        return preciseCalculate(msgBuffer.getInt(), ratio);
    }

    public static long getLong(ByteBuffer msgBuffer) {
        return msgBuffer.getLong();
    }


    // ----
    // ASCII, String & BCD
    // ----
    public static String getStrFromBuffer(ByteBuffer msgBuffer, int len) {
        byte[] strBytes = new byte[len];
        msgBuffer.get(strBytes, 0, strBytes.length);
        StringBuilder sb = new StringBuilder();
        for (byte b : strBytes) {
            if (0 == b) {
                break;
            }
            sb.append((char) b);
        }

        return sb.toString();
    }

    public static String getStrFromBuffer(ByteBuffer msgBuffer) {
        StringBuilder sb = new StringBuilder();
        while (msgBuffer.hasRemaining()) {
            byte b = msgBuffer.get();
            if(0 == b) {
                break;
            }
            sb.append((char) b);
        }
        return sb.toString();
    }

    public static String getHexBCDFromBuffer(ByteBuffer msgBuffer, int len) {
        if (0 == len) {
            return "";
        }

        byte[] bcdBytes = new byte[len];
        msgBuffer.get(bcdBytes, 0, bcdBytes.length);
        return bytesToHex(bcdBytes);
    }

    private static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        if (null == bytes || 0 == bytes.length) {
            return "";
        }

        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static byte[] hexStringToBytes(String str) {
        if (null == str) {
            return null;
        }

        String strData = str.replaceAll("[\\s\n]", "");
        if (0 != strData.length() % 2) {
            throw new RuntimeException("stringToBytes: data '" + str + "' length incorrect!");
        }

        int len = strData.length();
        byte[] b = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            b[i / 2] = (byte) ((Character.digit(strData.charAt(i), 16) << 4) + Character
                    .digit(strData.charAt(i + 1), 16));
        }
        return b;
    }

    // ----
    // Common
    // ----
    private static double preciseCalculate(int base, String ratio) {
        BigDecimal bd = new BigDecimal(base);
        BigDecimal mul = new BigDecimal(ratio);
        int roundScale = DECIMAL_ROUND_SCALE;

        // if need more precise
        if (ratio.length() > DECIMAL_ROUND_SCALE + 2) {
            roundScale = ratio.length() - 2;
        }

        return bd.multiply(mul)
                .setScale(roundScale, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
    }

    private static double preciseCalculate(int base, String ratio, String offset) {
        BigDecimal bd = new BigDecimal(base);
        BigDecimal mul = new BigDecimal(ratio);
        BigDecimal delta = new BigDecimal(offset);

        int roundScale = DECIMAL_ROUND_SCALE;
        if (ratio.length() > DECIMAL_ROUND_SCALE + 2) {
            roundScale = ratio.length() - 2;
        }

        return bd.multiply(mul)
                .setScale(roundScale, BigDecimal.ROUND_HALF_UP)
                .add(delta)
                .doubleValue();
    }
}
