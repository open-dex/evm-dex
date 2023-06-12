package conflux.dex.tool;

import javassist.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ClassPatcher {
    public static void changeCode() {
        ClassPool pool = ClassPool.getDefault();
        Class clz = null;
        try {
            CtClass cc = pool.get("conflux.web3j.types.Address");
            CtMethod m = cc.getDeclaredMethod("addressType", new CtClass[]{pool.get("java.lang.String")});
            String catchCode = "return conflux.web3j.types.AddressType.User;";

//            m.insertBefore("java.lang.System.out.println(\"---hit my code---\");");
            m.addCatch(catchCode, pool.get("conflux.web3j.types.AddressException"));
            cc.writeFile();
            clz = cc.toClass();
//            String modifiedClz = cc.toString();
//            System.out.println(modifiedClz);
        } catch (NotFoundException | CannotCompileException | IOException e) {
            System.out.println("class not found");
            System.out.println(e);
        }
        try {
//            Class<?> clz = Class.forName("conflux.web3j.types.Address");
            Constructor<?> constructor = clz.getConstructor(String.class, int.class);
            Object address = constructor.newInstance("0x423e897749c3b6f9e6e80ef8e60718ce670672d2", 1);
//        Address address = new Address("0x423e897749c3b6f9e6e80ef8e60718ce670672d2", 1);
            System.out.println("it works, "+address);
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException |
                 IllegalAccessException e) {
            System.out.println(e);
        }
    }
}
