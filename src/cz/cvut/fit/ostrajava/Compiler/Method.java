package cz.cvut.fit.ostrajava.Compiler;

import cz.cvut.fit.ostrajava.Interpreter.ClassPool;
import cz.cvut.fit.ostrajava.Interpreter.LookupException;
import cz.cvut.fit.ostrajava.Type.ReferenceType;
import cz.cvut.fit.ostrajava.Type.Type;
import cz.cvut.fit.ostrajava.Type.Types;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomaskohout on 11/12/15.
 */
public class Method {
    protected String name;
    protected List<Type> args;
    protected Type returnType;
    protected ByteCode byteCode;
    protected int localVariablesCount =  0;

    boolean staticMethod = false;

    public Method() {
        args = new ArrayList<>();
    }

    public Method(String name, List<Type> args, Type returnType) {
        this.name = name;
        this.args = args;
        this.returnType = returnType;
    }

    public Method(String descriptor) {
        String[] parts = descriptor.split(":");
        args = new ArrayList<>();
        //TODO: Put real return type
        returnType = Types.Void();

        for (int i=0; i < parts.length; i++){
             if (i == 0){
                 name = parts[i];
             }else{
                 args.add(Types.fromString(parts[i]));
             }
        }
    }

    public int getSimilarity(Method method, ClassPool classPool) throws LookupException {
        //Best similarity = 0
        int similarity = 0;

        if (!method.getName().equals(this.getName())){
            return -1;
        }

        if (method.getArgs().size() != this.getArgs().size()){
            return -1;
        }

        int i = 0;
        for (Type methodArgType: method.getArgs()){
            Type argType = this.getArgs().get(i);

            //If it's both objects
            if (methodArgType instanceof ReferenceType && argType instanceof ReferenceType){
                String methodArgClassName = ((ReferenceType) methodArgType).getClassName();
                String argClassName = ((ReferenceType) argType).getClassName();

                Class methodArgClass = classPool.lookupClass(methodArgClassName);
                Class argClass = classPool.lookupClass(argClassName);

                //Check whether arguments inherit from each other
                if (argClass.inheritsFrom(methodArgClass)){
                    //Increase similarity score if it's inheriting from class
                    similarity += argClass.getDistanceFrom(methodArgClass);
                }else{
                    return -1;
                }

            }else if (methodArgType != argType){
                return -1;
            }
        }

        return similarity;
    }

    public Type getReturnType() {
        return returnType;
    }

    public void setReturnType(Type returnType) {
        this.returnType = returnType;
    }

    public List<Type> getArgs() {
        return args;
    }

    public void setArgs(List<Type> args) {
        this.args = args;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ByteCode getByteCode() {
        return byteCode;
    }

    public void setByteCode(ByteCode byteCode) {
        this.byteCode = byteCode;
    }

    public int getLocalVariablesCount() {
        return localVariablesCount;
    }

    public void setLocalVariablesCount(int localVariablesCount) {
        this.localVariablesCount = localVariablesCount;
    }

    public boolean isStaticMethod() {
        return staticMethod;
    }

    public void setStaticMethod(boolean staticMethod) {
        this.staticMethod = staticMethod;
    }

    public String getDescriptor(){
        StringBuilder sb = new StringBuilder();
        sb.append(name);

        for (Type arg: args){
            sb.append(":" + arg.toString().toLowerCase());
        }

        return sb.toString();
    }
}
