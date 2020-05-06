package zhaoshuo.remoteexecutor.Execute;

import zhaoshuo.remoteexecutor.utils.ByteUtils;

/**
 * @Description
 * @Author zhaoshuo
 * @Date 2020-05-03 19:37
 */
public class ClassModifier {
    private static final int CONSTANT_POOL_COUNT_INDEX = 8;

    private static final int CONSTANT_UTF8_INFO = 1;

    private static final int[] CONSTANT_ITEM_LENGTH = {-1, -1, -1, 5, 5, 9, 9, 3, 3, 5, 5, 5, 5};
    /**
     * tag用u1个字节表示
     * len用u2个字节表示
     */

    private static final int u1 = 1;
    private static final int u2 = 2;

    private static int getConstantPoolCount(byte[] classBytes){
     return  ByteUtils.byte2Int(classBytes,CONSTANT_POOL_COUNT_INDEX,u2);
    }
    public static byte[] modifyUTF8Constant(byte [] classBytes,String oldStr,String newStr){
        int count = getConstantPoolCount(classBytes);
        int offset=CONSTANT_POOL_COUNT_INDEX+u2;
        for(int i=0;i<count;i++){
          int tag=ByteUtils.byte2Int(classBytes, offset, u1);
            if(tag==CONSTANT_UTF8_INFO){
                //获取len
                int len = ByteUtils.byte2Int(classBytes, offset + u1, u2);
                //解析内容
                offset+=u1+u2;
                String str = ByteUtils.byte2String(classBytes, offset, len);
                //进行判断
                if(str.equals(oldStr)){
                    byte[] newBytes = ByteUtils.string2Byte(newStr);
                    byte[] lenbytes = ByteUtils.int2Byte(newBytes.length, u2);
                    classBytes= ByteUtils.byteReplace(classBytes,offset-u2,u2,lenbytes);//内部返回的是新的byteClass
                    classBytes = ByteUtils.byteReplace(classBytes, offset, len, newBytes);
                    return classBytes;
                }else{
                    offset+=len;
                }
            }else{
                offset+=CONSTANT_ITEM_LENGTH[tag];
            }
        }
        return classBytes;
    }

}
