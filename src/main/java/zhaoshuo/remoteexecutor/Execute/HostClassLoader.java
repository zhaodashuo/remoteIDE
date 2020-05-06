package zhaoshuo.remoteexecutor.Execute;

/**
 * @Description
 * @Author zhaoshuo
 * @Date 2020-05-03 20:42
 */
public class HostClassLoader extends ClassLoader {
    public HostClassLoader(){
        super(HostClassLoader.class.getClassLoader());
    }

    public Class loadByte(byte[] classBytes){
        return defineClass(null,classBytes,0,classBytes.length);
    }
}
