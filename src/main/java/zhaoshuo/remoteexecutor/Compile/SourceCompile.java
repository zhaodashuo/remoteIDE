package zhaoshuo.remoteexecutor.Compile;

import javax.tools.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description
 * @Author zhaoshuo
 * @Date 2020-05-03 16:47
 */
public class SourceCompile {

    private static ConcurrentHashMap<String,JavaFileObject> fileObjectMap=new ConcurrentHashMap<>();
    //对source解析出className
    private static Pattern CLASS_PATTERN = Pattern.compile("class\\s+([$_a-zA-Z][$_a-zA-Z0-9]*)\\s*");
        //源代码和诊断器
    public static byte[] compile(String source, DiagnosticCollector<JavaFileObject> diagnosticCollector){
        JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
        //获取文件管理器
        JavaFileManager javaFileManager=new REJavaFileManager(javaCompiler.getStandardFileManager(diagnosticCollector,
                null,null));

        Matcher matcher = CLASS_PATTERN.matcher(source);
        String className=null;
        if(matcher.find()){
            className=matcher.group(1);
        }else{
            throw new IllegalArgumentException("Class No Found");
        }


        JavaFileObject javaFileObject=new REJavaFileObject(source,className);
        Boolean res = javaCompiler.getTask(null, javaFileManager, diagnosticCollector, null, null, Arrays.asList(javaFileObject)).call();
        JavaFileObject javaFileObject1 = fileObjectMap.get(className);
        if(res&&javaFileObject1!=null){
            return ((REJavaFileObject)javaFileObject1).getCompileBytes();
        }
        return null;
    }



    private static class REJavaFileManager extends ForwardingJavaFileManager<JavaFileManager>{
        protected REJavaFileManager(JavaFileManager javaFileManager){
            super(javaFileManager);
        }

        @Override
        public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
            REJavaFileObject reJavaFileObject = new REJavaFileObject(className, kind);
            fileObjectMap.put(className,reJavaFileObject);
            return reJavaFileObject;
        }

        @Override
        public JavaFileObject getJavaFileForInput(Location location, String className, JavaFileObject.Kind kind) throws IOException {
            JavaFileObject javaFileObject = fileObjectMap.get(className);
            if(javaFileObject==null){
                return super.getJavaFileForInput(location,className,kind);
            }
            return javaFileObject;
        }
    }

    private static class REJavaFileObject extends SimpleJavaFileObject{
        String source;
        ByteArrayOutputStream outputStream;

        public REJavaFileObject(String source,String className){
            super(URI.create("String:///" + className + Kind.SOURCE.extension), Kind.SOURCE);
            this.source=source;
        }
        REJavaFileObject(String classNane,Kind kind){
            super(URI.create("String:///" + classNane + Kind.SOURCE.extension), Kind.SOURCE);
            this.source=source;
        }
        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
            if(source==null){
                throw new IllegalArgumentException("source not found");
            }
            return source;
        }

        @Override
        public OutputStream openOutputStream() throws IOException {
            outputStream = new ByteArrayOutputStream();
            return outputStream;
        }
        public byte[] getCompileBytes(){
            return outputStream.toByteArray();
        }


    }


}
