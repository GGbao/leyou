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
    GOODS_ID_CANNOT_BE_NULL(400, "商品id不能为空"),
    PARAM_EDIT_ERROR(400, "修改商品参数失败"),
    PARAM_SAVE_ERROR(400, "新增商品参数失败"),
    DELETE_PARAM_ERROR(400, "删除商品参数失败"),
    BRAND_SAVE_ERROR(500, "新增品牌失败"),
    UPLOAD_FILE_ERROR(500, "文件上传失败"),
    INVALID_FILE_TYPE(400, "无效的文件类型"),
    BRAND_EDIT_ERROR(400, "品牌修改失败"),
    DELETE_BRAND_ERROR(400,"删除品牌失败")
    ;                                                     //此处封号必须的
    private int code;
    private String msg;
}

