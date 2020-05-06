package zhaoshuo.remoteexecutor.Execute;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Locale;

/**
 * @Description
 * @Author zhaoshuo
 * @Date 2020-05-03 18:06
 */
public class ModifiedOutputStream extends PrintStream {
    private static final ThreadLocal<ByteArrayOutputStream> out=new ThreadLocal<>();
    private static final ThreadLocal<Boolean> trouble=new ThreadLocal<>();
    public ModifiedOutputStream(){
        super(new ByteArrayOutputStream());
    }

    @Override
    public String toString() {
       return  out.get().toString();
    }

    private void ensureOpen(){
        if(out.get()==null){
            out.set(new ByteArrayOutputStream());
        }
    }

    @Override
    public void flush() {
        ensureOpen();
        try {
            out.get().flush();
        } catch (IOException e) {
            trouble.set(true);
        }
    }

    @Override
    public void write(int b) {
        ensureOpen();
        out.get().write(b);
        if(b=='\n'){
            try {
                out.get().flush();
            } catch (IOException e) {
               trouble.set(true);
            }
        }

    }

    @Override
    public void write(byte[] b) throws IOException {
        write(b,0,b.length);
        //out.get().write(b);
    }

    @Override
    public void write(byte[] buf, int off, int len) {
        ensureOpen();
       out.get().write(buf,off,len);
    }
    private void write(char[] buf){
        ensureOpen();
        try {
            out.get().write(new String(buf).getBytes());
        } catch (IOException e) {
            trouble.set(true);
        }
    }

    private void write(String str){
        ensureOpen();
        try {
            out.get().write(str.getBytes());
        } catch (IOException e) {
            trouble.set(true);
        }
    }


    @Override
    public void print(int i) {
        write(String.valueOf(i));
    }

    @Override
    public void print(char c) {
        write(String.valueOf(c));
    }

    @Override
    public void print(boolean b) {
        write(String.valueOf(b));
    }

    @Override
    public void print(long l) {
        write(String.valueOf(l));
    }

    @Override
    public void print(float f) {
        write(String.valueOf(f));
    }

    @Override
    public void print(double d) {
        write(String.valueOf(d));
    }

    @Override
    public void print(char[] s) {
        write(s);
    }

    @Override
    public void print(String s) {
        if(s=="null"){
            s=null;
        }
        write(s);
    }

    @Override
    public void print(Object obj) {
        write(String.valueOf(obj));
    }

    @Override
    public void println(boolean x) {
        write(String.valueOf(x));
        newLine();
    }

    @Override
    public void println(char x) {
        write(String.valueOf(x));
        newLine();
    }

    @Override
    public void println(int x) {
        write(String.valueOf(x));
        newLine();
    }

    @Override
    public void println(long x) {
        write(String.valueOf(x));
        newLine();
    }

    @Override
    public void println(float x) {
        write(String.valueOf(x));
        newLine();
    }

    @Override
    public void println(double x) {
        write(String.valueOf(x));
        newLine();
    }

    @Override
    public void println(char[] x) {
        write(String.valueOf(x));
        newLine();
    }

    @Override
    public void println(String x) {
        write(String.valueOf(x));
        newLine();
    }

    @Override
    public void println(Object x) {
        write(String.valueOf(x));
        newLine();
    }
    private void newLine(){
        ensureOpen();
        try {
            out.get().write(System.lineSeparator().getBytes());
        } catch (IOException e) {
            trouble.set(true);
        }
    }
    @Override
    public void close() {
        ensureOpen();
        try {
            out.get().close();
        } catch (IOException e) {
            trouble.set(true);
        }
        out.remove();
    }

    @Override
    public boolean checkError() {
        if(out.get()!=null){
            flush();
        }
        return trouble.get()!=null?trouble.get():false;
    }

    @Override
    protected void setError() {
        trouble.set(true);
    }

    @Override
    protected void clearError() {
        trouble.remove();
    }



}
