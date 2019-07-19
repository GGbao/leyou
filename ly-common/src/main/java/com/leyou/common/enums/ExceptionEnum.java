package com.leyou.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ExceptionEnum {
    PRICE_CANNOT_BE_NULL(400, "价格不能为空！"),//此处定义类别对象
    CATEGORY_NOT_FOUND(404, "商品分类未查询到"),
    ;                                                     //此处封号必须的
    private int code;
    private String msg;
}
