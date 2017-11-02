package elv.orioli.byteparser.configparser;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by Orioline on 2017/11/1.
 */
public class ByteParserSymbolTable {
    public HashMap<String, ByteParserStructDefine> symbolTable = new LinkedHashMap();

    public ByteParserStructDefine getSymbol(String symbolName) {
        return symbolTable.get(symbolName);
    }

}
