
  
  
import javax.crypto.Cipher;  
import javax.crypto.spec.IvParameterSpec;  
import javax.crypto.spec.SecretKeySpec;  
  
import sun.misc.BASE64Decoder;  
import sun.misc.BASE64Encoder;  
  
/**AES 是一种可逆加密算法，对用户的敏感信息加密处理 
* 对原始数据进行AES加密后，在进行Base64编码转化； 
*/  
public class AesCBC {  
/* 
* 加密用的Key 可以用26个字母和数字组成 
* 此处使用AES-128-CBC加密模式，key需要为16位。 
*/  
    private static String sKey="1234567890123456";  
    private static AesCBC instance=null;  
    private AesCBC(){  
  
    }  
    public static AesCBC getInstance(){  
        if (instance==null)  
            instance= new AesCBC();  
        return instance;  
    }   
 
    public static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    public static final byte[] IV = new byte[16];

    public static String encrypt(String content, String secretKey) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(secretKey.getBytes("utf-8"), "AES");
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        IvParameterSpec iv = new IvParameterSpec(IV);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(content.getBytes());
        return new BASE64Encoder().encode(encrypted);
    }
    
    public static String decrypt(String encrypted, String secretKey) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(secretKey.getBytes("utf-8"), "AES");
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        IvParameterSpec iv = new IvParameterSpec(IV);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
        byte[] encrypted1 = new BASE64Decoder().decodeBuffer(encrypted);
        byte[] original = cipher.doFinal(encrypted1);
        return new String(original);
    }
    
    
    public static void main(String[] args) throws Exception {  
        // 需要加密的字串  
        String cSrc = "hello world";  
        System.out.println(cSrc);  
        // 加密  
        long lStart = System.currentTimeMillis();  
        String enString = AesCBC.getInstance().encrypt(cSrc,sKey);
        System.out.println("加密后的字串是："+ enString);  
  
        long lUseTime = System.currentTimeMillis() - lStart;  
        System.out.println("加密耗时：" + lUseTime + "毫秒");  
        // 解密  
        lStart = System.currentTimeMillis();  
        String DeString = AesCBC.getInstance().decrypt(enString,sKey);  
        System.out.println("解密后的字串是：" + DeString);  
        lUseTime = System.currentTimeMillis() - lStart;  
        
        System.out.println("解密耗时：" + lUseTime + "毫秒");  
    }  
}  