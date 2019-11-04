package com.leyou.cart.web;

import com.leyou.cart.pojo.Cart;
import com.leyou.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CartController {
    @Autowired
    private CartService cartService;
    @PostMapping
    public ResponseEntity<Void> addCart(@RequestBody Cart cart) {
        cartService.addCart(cart);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }
    @PostMapping("lists")
    public ResponseEntity<Void> addCartLists(@RequestBody List<Cart> carts) {
        cartService.addCartLists(carts);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @GetMapping("list")
    public ResponseEntity<List<Cart>> queryCartList() {
        return ResponseEntity.ok(cartService.queryCartList());
    }

    /**
     * 修改购物车的商品数量
     * @param skuId
     * @param num
     * @return
     */
    @PutMapping
    public ResponseEntity<Void> updateCartNum(@RequestParam("skuId") Long skuId, @RequestParam("num") Integer num) {
        cartService.updateCartNum(skuId, num);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 删除购物城数据
     * @param skuId
     * @return
     */
    @DeleteMapping("{skuId}")
    public ResponseEntity<Void> deleteCart(@PathVariable("skuId")Long skuId) {
        cartService.deleteCart(skuId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
