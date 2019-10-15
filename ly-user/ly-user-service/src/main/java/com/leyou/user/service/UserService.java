package com.leyou.user.service;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.NumberUtils;
import com.leyou.user.mapper.UserMapper;
import com.leyou.user.pojo.User;
import com.leyou.user.utils.CodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
@Slf4j
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AmqpTemplate amqpTemplate;
    public static final String KEY_PREFIX = "user:verify:phone:";
    @Autowired
    private StringRedisTemplate redisTemplate;
    public static final String TIME_PREFIX = "mail:time:";

    public Boolean checkData(String data, Integer type) {
        //判断数据类型
        User record = new User();
        switch (type) {
            case 1:
                record.setUsername(data);
                break;
            case 2:
                record.setMail(data);
                break;
            default:
                throw new LyException(ExceptionEnum.INVALID_USER_DATA_TYPE);
        }

        int count = userMapper.selectCount(record);
        return count == 0;
    }
    public void sandCode(String mail) {
        String time = TIME_PREFIX + mail;
        // 2019/8/14 0014  按照邮箱限流
        //读取时间
        String lastTime = redisTemplate.opsForValue().get(time);
        if (StringUtils.isNotBlank(lastTime)) {
            Long last = Long.valueOf(lastTime);
            if (System.currentTimeMillis() - last < 60000) {
                log.info("[邮件服务] 发送邮件频率过高，被拦截");
                return;
            }
        }

        //生成key
        String key = KEY_PREFIX + mail;
        //生成验证码
        String code = NumberUtils.generateCode(6);
        Map<String, String> msg = new HashMap<>();
        msg.put("mail", mail);
        msg.put("code", code);
        //发送验证码
        amqpTemplate.convertAndSend("ly.mail.exchange", "mail.verify.code", msg);
        //保存验证码
        redisTemplate.opsForValue().set(key, code, 5, TimeUnit.MINUTES);
        //保存时间
        redisTemplate.opsForValue().set(time, String.valueOf(System.currentTimeMillis()), 1, TimeUnit.MINUTES);
    }

    public void register(User user, String code) {
        //校验验证码
        String cacheCode = redisTemplate.opsForValue().get(KEY_PREFIX + user.getMail());
        if (!StringUtils.equals(code, cacheCode)) {
            throw new LyException(ExceptionEnum.INVALID_VERIFY_CODE);
        }
        //生成盐
        String salt = CodeUtils.generateSalt();
        user.setSalt(salt);
        //对密码加密
        user.setPassword(CodeUtils.md5Hex(user.getPassword(), salt));
        //写入数据库
        user.setCreated(new Date());
        userMapper.insert(user);
    }

    public User queryUserByUsernameAndPassword(String username, String password) {
        //查询用户
        User record = new User();
        record.setUsername(username);
        User user = userMapper.selectOne(record);

        //校验
        if (user == null) {
            throw new LyException(ExceptionEnum.INVALID_USER_PASSWORD);
        }
        //校验密码
        if (!StringUtils.equals(user.getPassword(), CodeUtils.md5Hex(password, user.getSalt()))) {
            throw new LyException(ExceptionEnum.INVALID_USER_PASSWORD);
        }
        //用户名和密码正确
        return user;

    }
}
