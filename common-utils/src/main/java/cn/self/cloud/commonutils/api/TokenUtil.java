package cn.self.cloud.commonutils.api;

import cn.self.cloud.commonutils.validate.ValidateUtils;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by nicholas on 2017/6/23.
 */
public class TokenUtil {
    private final static String TOKEN_PREFIX = "Bearer"; // Token前缀
    private final static Logger logger = LoggerFactory.getLogger(TokenUtil.class);

    /**
     * 生成认证的token
     *
     * @param username   用户名
     * @param secret     秘钥
     * @param data       需要保存的数据
     * @param expireTime 有效期(Millisecond)
     */
    public static String createAuthToken(String username, String secret, Object data, long expireTime) {
        if (ValidateUtils.isEmpty(username)) {
            logger.error("username不能为空");
            throw new NullPointerException("username不能为空");
        }
        if (ValidateUtils.isEmpty(secret)) {
            logger.error("secret密钥不能为空");
            throw new NullPointerException("secret密钥不能为空");
        }
        if (ValidateUtils.isEmpty(data)) {
            logger.error("data不能为空");
            throw new NullPointerException("data不能为空");
        }
        if (expireTime <= 0L) {
            logger.error("有效时间必须大于0");
            throw new RuntimeException("有效时间必须大于0");
        }

        String jwtStr = Jwts.builder()
                //保存传入的数据
                .claim("data", data)
                // 用户名写入标题
                .setSubject(username)
                // 有效期设置
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                // 签名设置
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();

        return jwtStr;
    }

    /**
     * token认证
     *
     * @param token  token字符串
     * @param secret 生成token生成的秘钥
     */
    public static Object tokenAuthentication(String token, String secret) {
        if (ValidateUtils.isNotEmpty(token) && ValidateUtils.isNotEmpty(secret)) {
            try {
                Claims claims = Jwts.parser()
                        // 验签签名
                        .setSigningKey(secret)
                        // 去掉 Bearer
                        .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                        .getBody();

                //获取token携带的数据
                Object object = claims.get("data");

                return object;
            } catch (Exception e) {
                logger.error("token认证失败,{}", e.getMessage());
                throw new RuntimeException("token认证失败," + e.getMessage());
            }
        } else {
            logger.error("token认证失败，参数错误，token或者secret参数为空");
            throw new RuntimeException("token认证失败，参数错误，token或者secret参数为空");
        }
    }
}