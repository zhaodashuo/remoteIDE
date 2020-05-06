package zhaoshuo.remoteexecutor.Execute;

import com.sun.org.apache.xpath.internal.operations.Mod;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Optional;

/**
 * @Description
 * @Author zhaoshuo
 * @Date 2020-05-03 17:49
 */
public class FollowSystem {
    public final   static  InputStream in;
    public final  static  PrintStream out;
    public final  static  PrintStream err;
    static {
        in=new ModifiedInputStream();
        out=new ModifiedOutputStream();
        err=out;
    }




    public static String getBufferString(){
       return  out.toString();
    }
    public static void  close(){
        ((ModifiedInputStream)in).close();
        out.close();


    }




}
