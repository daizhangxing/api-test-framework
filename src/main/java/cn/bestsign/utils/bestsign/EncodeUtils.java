package cn.bestsign.utils.bestsign;

import org.apache.commons.codec.binary.Hex;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;


public class EncodeUtils {
    public static String sha1(byte[] data) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        digest.update(data);
        byte messageDigest[] = digest.digest();
        // Create Hex String
        StringBuffer hexString = new StringBuffer();
        // 字节数组转换为 十六进制 数
        for (int i = 0; i < messageDigest.length; i++) {
            String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
            if (shaHex.length() < 2) {
                hexString.append(0);
            }
            hexString.append(shaHex);
        }
        return hexString.toString();
    }
    
	public static String md5(String data) {
	    try {
            return md5(data.getBytes("UTF-8"));
        }
        catch (UnsupportedEncodingException e) {
            return md5(data.getBytes());
        }
	}
    
	public static String md5(byte[] data) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
        byte[] btInput = data;
        // 获得MD5摘要算法的 MessageDigest 对象
        MessageDigest mdInst;
        try {
            mdInst = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        // 使用指定的字节更新摘要
        mdInst.update(btInput);
        // 获得密文
        byte[] md = mdInst.digest();
        // 把密文转换成十六进制的字符串形式
        int j = md.length;
        char str[] = new char[j * 2];
        int k = 0;
        for (int i = 0; i < j; i++) {
            byte byte0 = md[i];
            str[k++] = hexDigits[byte0 >>> 4 & 0xf];
            str[k++] = hexDigits[byte0 & 0xf];
        }
        return new String(str);
    }

    public static String sha256(byte[] data) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        byte[] hash = digest.digest(data);
        return Hex.encodeHexString(hash);
    }

    public static byte[] base64encode(byte[] data) {
        return Base64.getEncoder().encode(data);
    }
    
    public static byte[] base64decode(byte[] data) {
        return Base64.getDecoder().decode(data);
    }

    public static String simpleEncode(final String src) {
        return new String(simpleEncode(src.getBytes()));
    }

    // 加salt是为了让编码的数据发生一点变化
    public static String simpleEncode(final String src, final int saltLen) {
        return new String(simpleEncode(src.getBytes(), saltLen));
    }

    public static byte[] simpleEncode(final byte[] src) {
        byte[] dst = base64encode(src); // 先base64
        for (int i = 0; i < dst.length; i++) {
            // 只处理字母
            if (dst[i] >= 65 && dst[i] <= 90) {
                dst[i] += 32; // 字母颠倒大小写
                if (dst[i] % 2 != 0) {
                    dst[i]++;
                }
                else {
                    dst[i]--;
                }
            }
            else if (dst[i] >= 97 && dst[i] <= 122) {
                dst[i] -= 32; // 字母颠倒大小写
                if (dst[i] % 2 != 0) {
                    dst[i]++;
                }
                else {
                    dst[i]--;
                }
            }
        }
        return dst;
    }

    // 加salt是为了让编码的数据发生一点变化
    public static byte[] simpleEncode(final byte[] src, final int saltLen) {
        if (saltLen < 1) {
            return simpleEncode(src);
        }

        byte[] saltLenBuffer = new byte[4];
        saltLenBuffer[0] = (byte) (saltLen >> 24 & 0x000000ff);
        saltLenBuffer[1] = (byte) (saltLen >> 16 & 0x000000ff);
        saltLenBuffer[2] = (byte) (saltLen >> 8 & 0x000000ff);
        saltLenBuffer[3] = (byte) (saltLen & 0x000000ff);

        String salt1 = Utils.randString(saltLen);
        String salt2 = Utils.randString(saltLen);

        ByteArrayOutputStream srcDataStream = new ByteArrayOutputStream();
        try {
            srcDataStream.write(saltLenBuffer);
            srcDataStream.write(salt1.getBytes());
            srcDataStream.write(src);
            srcDataStream.write(salt2.getBytes());
        }
        catch (IOException e) {
            return null;
        }
        finally {
            try {
                srcDataStream.close();
            }
            catch (IOException e) {

            }
        }

        byte[] srcData = srcDataStream.toByteArray();
        byte[] encodeData = simpleEncode(srcData);

        byte[] data = new byte[encodeData.length + 4];
        System.arraycopy("tlAs".getBytes(), 0, data, 0, 4); // salt颠倒过来
        System.arraycopy(encodeData, 0, data, 4, encodeData.length);
        return data;
    }

    public static String simpleDecode(final String src) {
        return new String(simpleDecode(src.getBytes()));
    }

    public static byte[] simpleDecode(final byte[] src) {
        // 检测是否带了salt
        boolean hasSalt = false;
        byte[] saltHeaderBuffer = { src[0], src[1], src[2], src[3] };
        if (new String(saltHeaderBuffer).equals("tlAs")) {
            hasSalt = true;
        }

        byte[] srcData = null;
        if (hasSalt) {
            srcData = new byte[src.length - 4];
            System.arraycopy(src, 4, srcData, 0, srcData.length);
        }
        else {
            srcData = src;
        }

        // 先把srcData解码出来
        byte[] decodeData = new byte[srcData.length];
        for (int i = 0; i < srcData.length; i++) {
            decodeData[i] = srcData[i];
            // 只处理字母
            if (decodeData[i] >= 65 && decodeData[i] <= 90) {
                if (decodeData[i] % 2 != 0) {
                    decodeData[i]++;
                }
                else {
                    decodeData[i]--;
                }
                decodeData[i] += 32; // 字母颠倒大小写
            }
            else if (decodeData[i] >= 97 && decodeData[i] <= 122) {
                if (decodeData[i] % 2 != 0) {
                    decodeData[i]++;
                }
                else {
                    decodeData[i]--;
                }
                decodeData[i] -= 32; // 字母颠倒大小写
            }
        }
        // 再base64 decode
        decodeData = base64decode(decodeData);

        if (!hasSalt) {
            return decodeData;
        }

        // 去掉salt
        byte[] saltLenBuffer = { decodeData[0], decodeData[1], decodeData[2], decodeData[3] };
        int saltLen = saltLenBuffer[0] * 256 * 256 * 256 + saltLenBuffer[1] * 256 * 256 + saltLenBuffer[2] * 256 + saltLenBuffer[3];

        byte[] data = new byte[decodeData.length - 4 - saltLen * 2]; // 数据是一个int的salt长度+salt+真正的数据+salt
        System.arraycopy(decodeData, 4 + saltLen, data, 0, data.length);

        return data;
    }
	
	public static byte[] rsaSignWithSHA1(byte[] data, String privateKey)  {
		// 解密由base64编码的私钥
        byte[] privateKeyBytes = base64decode(privateKey.getBytes());
        
        // 构造PKCS8EncodedKeySpec对象  
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);  
  
        // KEY_ALGORITHM 指定的加密算法  
        KeyFactory keyFactory;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage(), e);
        }  
  
        // 取私钥匙对象  
        PrivateKey priKey;
        try {
            priKey = keyFactory.generatePrivate(pkcs8KeySpec);
        }
        catch (InvalidKeySpecException e) {
            throw new RuntimeException(e.getMessage(), e);
        }  
  
        // 用私钥对信息生成数字签名  
        Signature signature;
        try {
            signature = Signature.getInstance("SHA1withRSA");
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage(), e);
        }  
        try {
            signature.initSign(priKey);
        }
        catch (InvalidKeyException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        
        try {
            signature.update(data);
            return signature.sign();
        }
        catch (SignatureException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    private static String calcRsaSignWithSHA256(String privateKey, final String signData) {
        byte[] data;
        try {
            data = signData.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        byte[] sign = null;
        // 解密由base64编码的私钥
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKey.getBytes());

        // 构造PKCS8EncodedKeySpec对象
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);

        // KEY_ALGORITHM 指定的加密算法
        KeyFactory keyFactory;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        // 取私钥匙对象
        PrivateKey priKey;
        try {
            priKey = keyFactory.generatePrivate(pkcs8KeySpec);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        // 用私钥对信息生成数字签名
        Signature signature;
        try {
            signature = Signature.getInstance("SHA256withRSA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        try {
            signature.initSign(priKey);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        try {
            signature.update(data);
            sign = signature.sign();
        } catch (SignatureException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
//        return new String(Base64.getUrlEncoder().encode(sign));
        try {
            return URLEncoder.encode(Base64.getEncoder().encodeToString(sign), "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        return "";
    }

}
