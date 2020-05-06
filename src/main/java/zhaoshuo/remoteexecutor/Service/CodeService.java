package zhaoshuo.remoteexecutor.Service;

import org.springframework.stereotype.Service;
import zhaoshuo.remoteexecutor.Compile.SourceCompile;
import zhaoshuo.remoteexecutor.Execute.FollowSystem;
import zhaoshuo.remoteexecutor.Execute.JavaClassExecutor;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;
import java.util.List;
import java.util.concurrent.*;

/**
 * @Description
 * @Author zhaoshuo
 * @Date 2020-05-03 17:29
 */
@Service
public class CodeService {
    private final int RUN_TIME_LIMITED=15;
    private final int NUM_THREAD=3;
    private static final String WAIT_WARNING = "服务器忙，请稍后提交";
    private static final String NO_OUTPUT = "Nothing.";

    private ThreadPoolExecutor threadPoolExecutor=new ThreadPoolExecutor(NUM_THREAD,NUM_THREAD,0L,
            TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(NUM_THREAD)); //必须是Runnable
    //获取编译结果
    public String execute(String source,String systemIn){
        DiagnosticCollector<JavaFileObject> compileCollector = new DiagnosticCollector<>(); // 编译信息收集器

        byte[] classBytes = SourceCompile.compile(source, compileCollector);
        //编译出错
        if(classBytes==null){
            List<Diagnostic<? extends JavaFileObject>> diagnostics = compileCollector.getDiagnostics();
            StringBuilder compileErroeRes=new StringBuilder();
            for(Diagnostic diagnostic:diagnostics){
                compileErroeRes.append("错误位于第:");
                compileErroeRes.append(diagnostic.getLineNumber());
                compileErroeRes.append(".");
                compileErroeRes.append(System.lineSeparator());//?
            }
            return compileErroeRes.toString();
        }
        Future<String> future = null;
        try {
            future = threadPoolExecutor.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    return JavaClassExecutor.execute(classBytes, systemIn);
                }
            });
        } catch (RejectedExecutionException e) {
            return WAIT_WARNING;
        }
        String runResult;
        try {
             runResult = future.get(RUN_TIME_LIMITED, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
           runResult="Program interrupted.";
        } catch (ExecutionException e) {
            runResult = e.getCause().getMessage();
        } catch (TimeoutException e) {
            runResult = "Time Limit Exceeded.";
        }finally {
            future.cancel(true);
        }
       return runResult==null?NO_OUTPUT:runResult;


    }

    //线程池执行任务


}
