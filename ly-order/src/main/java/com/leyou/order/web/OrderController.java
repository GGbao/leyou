package com.leyou.order.web;

import com.leyou.order.dto.OrderDto;
import com.leyou.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 创建订单
     *
     * @param orderDto
     * @return
     */
    @PostMapping
    public ResponseEntity<Long> creatOrder(@RequestBody OrderDto orderDto) {
        //创建订单

        return ResponseEntity.ok(orderService.creatOrder(orderDto));
    }

}
