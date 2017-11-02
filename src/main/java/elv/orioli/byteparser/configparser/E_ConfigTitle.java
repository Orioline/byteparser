package elv.orioli.byteparser.configparser;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Orioline on 2017/11/1.
 */
public enum E_ConfigTitle {

    NAME("NAME"),
    TYPE("TYPE"),
    LENGTH("LENGTH"),
    DESC("DESC"),
    OTHER("OTHER");

    private static final Map<String, E_ConfigTitle> typeMap = new ConcurrentHashMap<>();

    static {
        synchronized (E_ConfigTitle.class) {
            initMessageMap();
        }
    }

    public String[] fieldNames;

    E_ConfigTitle(String... fieldNames) {
        this.fieldNames = fieldNames;
    }

    private static void initMessageMap() {
        for (E_ConfigTitle type : E_ConfigTitle.values()) {
            for (String fieldName : type.fieldNames) {
                addType(fieldName, type);
            }
        }
    }

    private static void addType(String strType, E_ConfigTitle dataFieldType) {
        typeMap.put(strType.toUpperCase(), dataFieldType);
    }

    public static E_ConfigTitle typeOf(String strType) {
        return typeMap.getOrDefault(strType.toUpperCase(), null);
    }
}
