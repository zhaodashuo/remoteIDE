package zhaoshuo.remoteexecutor.Execute;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Description
 * @Author zhaoshuo
 * @Date 2020-05-03 17:47
 */
public class JavaClassExecutor {
    public static String execute(byte [] classBytes,String systemIn){

        //字节码文件的修改
         byte[] modifyBytes = ClassModifier.modifyUTF8Constant(classBytes, "java/lang/System", "zhaoshuo/remoteexecutor/Execute/FollowSystem");
       //     modifyBytes = ClassModifier.modifyUTF8Constant(modifyBytes, "java/util/Scanner", "zhaoshuo/remoteexecutor/Execute/FollowSystem");
        //main方法的执行
        HostClassLoader classLoader = new HostClassLoader();
        Class aClass = classLoader.loadByte(modifyBytes);
        try {
            Method method = aClass.getMethod("main", new Class[]{String[].class});
            method.invoke(null,new String[]{null});
            } catch (NoSuchMethodException e) {
                e.printStackTrace(FollowSystem.err);
            } catch (IllegalAccessException e) {
                e.printStackTrace(FollowSystem.err);
            } catch (InvocationTargetException e) {
                e.printStackTrace(FollowSystem.err);
            }
        //结果的收集
        String res = FollowSystem.getBufferString();
        FollowSystem.close();
        return res;
    }


}
