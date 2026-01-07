package com.example.check.config;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Base64;

public class JwtEdDsaUtil {
    // 和风天气专用私钥
    private static final String PRIVATE_KEY = "-----BEGIN PRIVATE KEY-----\nMC4CAQAwBQYDK2VwBCIEIISBvg7a0YbN/tq0Ds9hW4jkQnxP3ysJ2vmXjUTOXr2J\n-----END PRIVATE KEY-----";
    private static final String KID = "T7B7YX3P78";
    private static final String SUB = "442J3PYWUM";

    /**
     * 生成和风天气API JWT（EdDSA签名，unix时间iat为当前时间-30秒，exp为15分钟后，可调整参数，输出标准JWT字符串）
     */
    public static String generateHeWeatherJwt() throws Exception {
        // 处理私钥内容（去头尾和换行空格）
        String keyPem = PRIVATE_KEY
                .replace("-----BEGIN PRIVATE KEY-----","")
                .replace("-----END PRIVATE KEY-----","")
                .replaceAll("\\s", "");
        byte[] privateKeyBytes = Base64.getDecoder().decode(keyPem);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("EdDSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

        // 构造Header与Payload
        String headerJson = String.format("{\"alg\": \"EdDSA\", \"kid\": \"%s\"}", KID);
        long iat = ZonedDateTime.now(ZoneOffset.UTC).toEpochSecond() - 30;
        long exp = iat + 86400; // 15分钟有效，可改为 86400 (24小时内)
        String payloadJson = String.format("{\"sub\": \"%s\", \"iat\": %d, \"exp\": %d}", SUB, iat, exp);

        // Base64url编码
        String headerEncoded = Base64.getUrlEncoder().withoutPadding().encodeToString(headerJson.getBytes(StandardCharsets.UTF_8));
        String payloadEncoded = Base64.getUrlEncoder().withoutPadding().encodeToString(payloadJson.getBytes(StandardCharsets.UTF_8));
        String data = headerEncoded + "." + payloadEncoded;

        // 用EdDSA签名
        Signature signer = Signature.getInstance("EdDSA");
        signer.initSign(privateKey);
        signer.update(data.getBytes(StandardCharsets.UTF_8));
        byte[] signature = signer.sign();
        String signatureEncoded = Base64.getUrlEncoder().withoutPadding().encodeToString(signature);

        return data + "." + signatureEncoded;
    }
} 