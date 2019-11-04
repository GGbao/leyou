package com.leyou.cart.service;

import com.leyou.auth.entity.UserInfo;
import com.leyou.cart.interceptor.UserInterceptor;
import com.leyou.cart.pojo.Cart;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {
    public static final String KEY_PREFIX = "cart:uid:";
    @Autowired
    private StringRedisTemplate redisTemplate;

    public void addCart(Cart cart) {
        //获取登陆用户
        UserInfo user = UserInterceptor.getUser();
        String key = KEY_PREFIX + user.getId();
        Integer num = cart.getNum();
        //hashkey
        String hashKey = cart.getSkuId().toString();
        BoundHashOperations<String, Object, Object> operation = redisTemplate.boundHashOps(key);
        //判断当前购物车商品，是否存在
        if (operation.hasKey(hashKey)) {
            //是，修改数量
            String json = operation.get(hashKey).toString();
            cart = JsonUtils.parse(json, Cart.class);
            cart.setNum(cart.getNum() + num);
        }
        //写回redis
        operation.put(hashKey, JsonUtils.serialize(cart));

    }

    public List<Cart> queryCartList() {
        //获取登陆用户
        UserInfo user = UserInterceptor.getUser();
        String key = KEY_PREFIX + user.getId();
        if (!redisTemplate.hasKey(key)) {
            //key不存在，返回404
            throw new LyException(ExceptionEnum.CART_NOT_FOUND);
        }
        //获取登陆用户的所有购物车
        BoundHashOperations<String, Object, Object> operation = redisTemplate.boundHashOps(key);
        List<Cart> carts = operation.values()
                .stream().map(o -> JsonUtils.parse(o.toString(), Cart.class)).collect(Collectors.toList());
        return carts;
    }

    public void updateCartNum(Long skuId, Integer num) {
        //获取登陆用户
        UserInfo user = UserInterceptor.getUser();
        String key = KEY_PREFIX + user.getId();
        //hashkey
        String hashkey = skuId.toString();
        //获取操作
        BoundHashOperations<String, Object, Object> operation = redisTemplate.boundHashOps(key);
        //查询
        if (!operation.hasKey(skuId.toString())) {
            //key不存在，返回404
            throw new LyException(ExceptionEnum.CART_NOT_FOUND);
        }
        Cart cart = JsonUtils.parse(operation.get(skuId.toString()).toString(), Cart.class);
        cart.setNum(num);
        //写回redis
        operation.put(hashkey, JsonUtils.serialize(cart));

    }

    public void deleteCart(Long skuId) {
        //获取登陆用户
        UserInfo user = UserInterceptor.getUser();
        String key = KEY_PREFIX + user.getId();
        //删除
        redisTemplate.opsForHash().delete(key, skuId.toString());
    }

    public void addCartLists(List<Cart> carts) {

        UserInfo user = UserInterceptor.getUser();
        //准备key
        String key = KEY_PREFIX + user.getId();

        //查询购物车
        //判断是否存在
        for (Cart cart : carts) {
            Integer num = cart.getNum();
            String hashKey = cart.getSkuId().toString();
            //获取operation对象,并且绑定了key,剩下的就是操作map了
            BoundHashOperations<String, String, String> operation = redisTemplate.boundHashOps(key);
            if (operation.hasKey(hashKey)) {
                //存在则修改数量
                String json = operation.get(hashKey).toString();
                cart = JsonUtils.parse(json, Cart.class);
                cart.setNum(cart.getNum() + num);
            }
            //不存在则直接添加
            operation.put(hashKey, JsonUtils.serialize(cart));
        }

    }
}
