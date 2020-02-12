package cn.bestsign.utils.bestsign;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

/**
 * Created by dai on 2020/1/20.
 */
public class CalSign {

    public static String calcSign(String timeStamp, String requestBody, String urlPath, String clientId, String privateKey) {

        StringBuilder signatureString = new StringBuilder();

        signatureString.append("bestsign-client-id=")
                .append(clientId)
                .append("bestsign-sign-timestamp=")
                .append(timeStamp)
                .append("bestsign-signature-type=RSA256")
                .append("request-body=")
                .append(getRequestMd5(requestBody == null ? "" : requestBody))
                .append("uri=")
                .append(urlPath);

        return calcRsaSign(privateKey, signatureString.toString());
    }

    private static String calcRsaSign(String privateKey, final String signData) {
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


    public static String getRequestMd5(String requestBody) {
        byte[] data;

        String newRequestBody = Utils.convertToUtf8(requestBody);
        try {
            data = newRequestBody.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return EncodeUtils.md5(data);
    }

    public static String getTimeStamp() {

        return (System.currentTimeMillis() / 1000) + "";
    }


    public static void main(String[] args) {

        String path = "/api/contracts/2217302723118759939?account=sso_dev_1%40b.cn";
        String timeStamp = "1579588746";
        String clientId = "clientId";
        String privateKey ="MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDJwxJHx1bexFPBFpo2pnxymHcaRwqC1B4DwrfFQuge/soW+xohiACD47gRAA18Gj0ey70of+h69ozxeKWn6+fEzJ5w3wCu89jkDi8LzrxTqSababkKPRKqU5r8qEelAgHI/VW8HXTHzoGDabQFKNoIJ5FvGops/c9jKcNGxn69+kHBsxQ+L3uxMoE4yn69dEY+WX+GAtf3OBbuWJWrJ/LVXa3blEuS114BRg++O3cooUa8FbWQqEbsNWeZ7arURhUpBU/xAIru4sQyn/UfREdTJPGaayg9hOBmiL3VoJdbCI1loSpr0N/Q64N8F1dvj0cF3mOz4vhbmpat9Q7RWXibAgMBAAECggEASHhc9JNRTKrQOqbXtZGPWcoR3cpuqBdwwIEMaTpjDd8Oz7pZ5HlpVfZRgym1rkHuekVMKGPpKWGttn5TYkGF+lZVAs3W/QBxg1jk5STY2GQFjgUnhdnx/mQRY3c+ZLKX2Y3FYdBixfIS6xToqIHH7u3nezTZS9TOzRuPHLjnhWze87uhDiEAi72OPBh/mxHd44BbCTgdST6fojn7Q9ZPo8/alzesUfHbjflXqyD+mWkJPOoeo7t0n0TDTnR+74H/z+PgJ4GWu0nHGlw/YVmzskrWj2845Pezckealicba3Ly/Pm+rL9EBLkgHDMo8MEX+SiM1j8izzK9FJE42iiaYQKBgQDwYfy4pvD8AErN2WsPgkpZZuDRUxn83G3D5qOIkYrlvykDRHHHcxIcq33RKZV6+/rDR6YCbbcdu+I1XsqzyWbRdu1iMSg6VIt8m88T6lF1lbbodVk1056eGVnQdKbzrXtYze6vXY5OHrh0ggJo4b8dsZowBE0omKcTTRMpz7bgBQKBgQDW3r++y9BTnfWkkH+gyGTFVBNiRgP5D8kj6O9TVV2tO3KVuGNWNqvr8hHJKvqH9Wirq+gG2suo4FssGx9W37WkVURXl7f4PnZmYsvm2DGfXdTyzetbxQa/0nqby+F6wn42/GSIH0qFePvkEjBeQ5RMBJyHkVpKn/XFrj6fPQp4HwKBgCELR0NW54+stpu/hIEYRwItK0B7LPpejtQ8M0TFxRM6CPhDA2UIg3ffdyc2kS7bzqfjhIWW8XIr7repdTHrwK+Qqqcx3LbQAnC5tmilcFc6om8oGoBUu9w93ePS9VRQB8G4ZVREBxjkDjUt3gju52e8j1aEcNrgyS7VDIpV3GzFAoGBALiwxogtkGgPQnGtcsa0+UlUN0UlgYy3sOCY+/3ZRqf8jUV8NIaKf8RTUk5kDuyGWO4W2V3q+1uxXSzQM2Ps7qjOUewokQ1kQ0lGapWGB0FA2iXKMh+zf1WqiZEY8jseqnaBA7itjNSlZX5o1AJyfCnLXPB6U+4JCKxeVZ1yTmlpAoGBANYAYBQkq4vYnKg99XbQyHio4NjFZfpiBPizChLUtDWsXWZBocSJHt4nbFpPjvi+2MhI5m1w6dVwt49ru7lfdjRY768AAwQnJJxdmMkSdIGqlnHkdmDiM3dVwN076AfCzCiRGIqRaQQb2+ZMdTgw6IHWzH6McYPjO+veXxE5OOmZ";


        System.out.println(calcSign(timeStamp, null, path, clientId, privateKey));

//        ZEc3nD1DrZ0yYS0j83cM7PMhZ8mnAZhdhNoA%2FXotEQC%2BEm5HVPjL6gXko1JFccBQtOXUo2XJ16P9Eys5hV%2Bi7jZ8gY74gztdSS86z9wfwGBsWA88EcT4ob6d%2BzEsjAfzjyXRQ%2FKeZ1dJPeyvYxjAafBmdxrCMXilhGlA0IJirt%2FoXw29Mra1nOzUfrslxbK4UEQXmfy1UgQkjoDBp7O4NIjOHR3jRDMZHcIRC6UgmI%2Bz46RoKACr7V2VrhhWYhB66D40A4pmVtBXxTM5Cx6hplgQVvkuZISvpjBYvMv%2F%2BYX0W5dA0d12h7bt1%2FnDqzTXJpEMf1Fa4OrQglmX5KAZ7w%3D%3D
//        Fn21malGrk5ObdUs5Vft84tCLQFUv%2BQ51qvCsd6C8vmGIT3CSlrEP9m%2F%2BVUdoIunNRTyan8OJiznS2NYlSVsUapyZfew2mxM58Q1EI22xV81fLJNukEGH5wKM%2BRHtvgoQ9EJIYSP8GxgCDCuU8cPT8tyvEVODihDvJEa3VWTd5Ws57l2P%2BItqe4hjh0TxNLbNUrqOCYbYm8BplUHdsMYh9uOnSr3KJX5eZRi6iqOe6pewUGveC9wRBLan%2FH%2FT8MhzYA%2FEFqTsfg7ITDHGr%2FHDNx%2Fwed1kKm5ePbUp8dvT9GXcRx6RXbMqkuCqjpJt%2BjG3JM4OjFiHaxP5GatfIvkeQ%3D%3D

    }
}
