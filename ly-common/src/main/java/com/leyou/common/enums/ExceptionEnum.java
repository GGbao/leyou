package com.leyou.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ExceptionEnum {
    BRAND_NOT_FOUND(404, "品牌不存在"),//此处定义类别对象
    CATEGORY_NOT_FOUND(404, "商品分类未查询到"),
    SPEC_GROUP_NOT_FOUND(404, "商品规格组不存在"),
    SPEC_GROUP_SAVE_ERROR(404, "新增商品规格组失败"),
    DELETE_SPEC_GROUP_ERROR(404, "商品规格组删除失败"),
    GROUP_EDIT_ERROR(400, "规格组修改失败"),
    SPEC_PARAM_NOT_FOUND(404, "商品参数不存在"),
    GOODS_NOT_FOUND(404, "商品不存在"),
    GOODS_SKU_NOT_FOUND(404, "商品SKU不存在"),
    GOODS_STOCK_NOT_FOUND(404, "商品库存不存在"),
    GOODS_DETAIL_NOT_FOUND(404, "商品详情不存在"),
    GOODS_SAVE_ERROR(500, "新增商品失败"),
    GOODS_UPDATE_ERROR(500, "修改商品失败"),
    GOODS_DELETE_ERROR(404, "删除商品失败"),
    GOODS_SOLDOUT_ERROR(404, "商品下架失败"),
    GOODS_PUTAWAY_ERROR(404, "商品上架失败"),
    GOODS_ID_CANNOT_BE_NULL(400, "商品id不能为空"),
    PARAM_EDIT_ERROR(400, "修改商品参数失败"),
    PARAM_SAVE_ERROR(400, "新增商品参数失败"),
    DELETE_PARAM_ERROR(400, "删除商品参数失败"),
    BRAND_SAVE_ERROR(500, "新增品牌失败"),
    UPLOAD_FILE_ERROR(500, "文件上传失败"),
    INVALID_FILE_TYPE(400, "无效的文件类型"),
    BRAND_EDIT_ERROR(400, "品牌修改失败"),
    DELETE_BRAND_ERROR(400, "删除品牌失败"),
    INVALID_USER_DATA_TYPE(400, "用户数据类型无效"),
    INVALID_VERIFY_CODE(400, "无效的验证码"),
    INVALID_USER_PASSWORD(400, "用户名或密码错误"),
    CREATE_TOKEN_ERROR(500, "用户凭证生成失败"),
    UNAUTHORIZED(403, "用户未授权"),
    CART_NOT_FOUND(404, "购物车为空"),
    ORDER_NOT_FOUND(404, "订单不存在"),
    ORDER_DETAIL_NOT_FOUND(404, "订单详情不存在"),
    ORDER_STATUS_NOT_FOUND(404, "订单状态不存在"),
    CREATE_ORDER_ERROR(500,"创建订单失败"),
    STOCK_NOT_ENOUGH(500,"库存不足"),
    WX_PAY_ORDER_FAIL(500,"微信下单失败"),
    ORDER_STATUS_ERROR(400,"订单状态不正确"),
    INVALID_SIGN_ERROR(400,"无效的签名异常"),
    INVALID_ORDER_PARAM(400,"订单参数有误"),
    UPDATE_ORDER_STATUS_ERROR(500,"更新订单状态失败"),
    ;                                                     //此处封号必须的
    private int code;
    private String msg;
}

