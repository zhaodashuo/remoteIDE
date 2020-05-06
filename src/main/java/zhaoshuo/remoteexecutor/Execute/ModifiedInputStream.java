package zhaoshuo.remoteexecutor.Execute;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Description
 * @Author zhaoshuo
 * @Date 2020-05-03 18:01
 */
public class ModifiedInputStream extends InputStream{
  private static final ThreadLocal<InputStream> threadLocal=new ThreadLocal<>();

    @Override
    public int read() {
        return 0;
    }

    public InputStream get(){
      return  threadLocal.get();
    }
    public void set(String systemIn){
        threadLocal.set(new ByteArrayInputStream(systemIn.getBytes()));
    }

    @Override
    public void close()  {
        threadLocal.remove();
    }
}
