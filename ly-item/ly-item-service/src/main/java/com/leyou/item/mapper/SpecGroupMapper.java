package com.leyou.item.mapper;

import com.leyou.item.pojo.SpecGroup;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

public interface SpecGroupMapper extends Mapper<SpecGroup> {
    @Update("UPDATE tb_spec_group SET name = #{name} WHERE id =#{id};")
    int update(@Param("id") Long id, @Param("name") String name);
}
