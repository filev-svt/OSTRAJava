package cz.cvut.fit.ostrajava.Interpreter.Natives;

import cz.cvut.fit.ostrajava.Interpreter.*;
import cz.cvut.fit.ostrajava.Interpreter.Object;
import cz.cvut.fit.ostrajava.Type.Types;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tomaskohout on 11/24/15.
 */
public class Natives {

    private Map<String, Native> nativesMap;

    public Natives(){
        init();
    }

    private void init(){
        if (nativesMap == null) {
            nativesMap = new HashMap<>();
            nativesMap.put("print:" + Types.Char(), new PrintChar());
            nativesMap.put("print:" + Types.CharArray(), new PrintChars());
            nativesMap.put("print:" + Types.Number(), new PrintInt());
            nativesMap.put("print:" + Types.NumberArray(), new PrintInts());
            nativesMap.put("print:" + Types.Boolean(), new PrintBool());
            nativesMap.put("print:" + Types.BooleanArray(), new PrintBools());
            nativesMap.put("print:" + Types.Float(), new PrintFloat());
            nativesMap.put("print:" + Types.FloatArray(), new PrintFloats());

        }
    }

    public boolean nativeExist(String descriptor){
        return (nativesMap.containsKey(descriptor));
    }


    public void invoke(String descriptor, NativeArgument[] args) throws InterpreterException {

        if (nativeExist(descriptor)){
            nativesMap.get(descriptor).invoke(args);
        }else{
            throw new InterpreterException("Trying to call non-existent method '" + descriptor + "'");
        }
    }

}
