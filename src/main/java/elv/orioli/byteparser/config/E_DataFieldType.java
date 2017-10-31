package elv.orioli.byteparser.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Orioline on 2017/5/26.
 */
public enum E_DataFieldType {
    BOOLEAN("BOOL", "BOOLEAN"),
    BYTE8("BYTE", "CHAR"),
    UNSIGNED_BYTE8("UBYTE", "UCHAR"),
    SHORT16("SHORT"),
    UNSIGNED_SHORT16("USHORT"),
    INT32("INT"),
    UNSIGNED_INT32("UINT"),
    LONG64("LONG"),
    NUMBER("NUMBER"),
    FLOAT("FLOAT"),
    DOUBLE("DOUBLE"),
    BYTES("BYTES", "BLOCK"),
    STRING("STRING"),
    CUSTOM("CUSTOM");

    private static final Map<String, E_DataFieldType> typeMap = new ConcurrentHashMap<>();

    static {
        synchronized (E_DataFieldType.class) {
            initMessageMap();
        }
    }

    public String[] fieldNames;

    E_DataFieldType(String... fieldNames) {
        this.fieldNames = fieldNames;
    }

    private static void initMessageMap() {
        for (E_DataFieldType type : E_DataFieldType.values()) {
            for (String fieldName : type.fieldNames) {
                addType(fieldName, type);
            }
        }
    }

    private static void addType(String strType, E_DataFieldType dataFieldType) {
        typeMap.put(strType.toUpperCase(), dataFieldType);
    }

    public static E_DataFieldType typeOf(String strType) {
        return typeMap.getOrDefault(strType.toUpperCase(), null);
    }
}
