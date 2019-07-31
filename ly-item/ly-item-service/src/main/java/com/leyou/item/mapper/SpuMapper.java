package com.leyou.item.mapper;

import com.leyou.item.pojo.Spu;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

public interface SpuMapper extends Mapper<Spu> {

    @Update("UPDATE tb_spu SET saleable = false WHERE id =#{id};")
    int soldOutBySpuId(@Param("id") Long spuId);

    @Update("UPDATE tb_spu SET saleable = true WHERE id =#{id};")
    int putAwayBySpuId(@Param("id") Long spuId);

}
