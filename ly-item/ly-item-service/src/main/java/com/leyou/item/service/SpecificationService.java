package com.leyou.item.service;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.mapper.SpecParamMapper;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SpecificationService {

    @Autowired
    private SpecGroupMapper groupMapper;

    @Autowired
    private SpecParamMapper paramMapper;

    public List<SpecGroup> queryGroupByCid(Long cid) {
        SpecGroup group = new SpecGroup();
        group.setCid(cid);
        List<SpecGroup> list = groupMapper.select(group);
        if (CollectionUtils.isEmpty(list)) {
            //没查到
            throw new LyException(ExceptionEnum.SPEC_GROUP_NOT_FOUND);
        }
        return list;

    }

    public List<SpecParam> queryParamList(Long gid,Long cid,Boolean searching) {
        SpecParam param = new SpecParam();
        param.setGroupId(gid);
        param.setCid(cid);
        param.setSearching(searching);
        List<SpecParam> list = paramMapper.select(param);
        if (CollectionUtils.isEmpty(list)) {
            throw new LyException(ExceptionEnum.SPEC_PARAM_NOT_FOUND);
        }
        return list;
    }

    @Transactional
    public void saveEditedSpecGroup(Long id,String name) {
        //修改规格组
        int count = groupMapper.update(id, name);
        if (count != 1) {
            throw new LyException(ExceptionEnum.GROUP_EDIT_ERROR);
        }

    }

    @Transactional
    public void addSpecGroup(Long cid, String name) {
        //添加规格组
        List<SpecGroup> groups = groupMapper.selectAll();
        SpecGroup group = new SpecGroup();
        group.setCid(cid);
        group.setName(name);
        int count = groupMapper.insert(group);
        if (count != 1) {
            throw new LyException(ExceptionEnum.SPEC_GROUP_SAVE_ERROR);
        }
    }

    @Transactional
    public void deleteSpecGroup(Long id) {
        //删除规格组
        int count = groupMapper.deleteByPrimaryKey(id);
        if (count != 1) {
            throw new LyException(ExceptionEnum.DELETE_SPEC_GROUP_ERROR);
        }
    }

    @Transactional
    public void saveEditedSpecParam(SpecParam param) {
        int count = paramMapper.updateByPrimaryKeySelective(param);
        if (count != 1) {
            throw new LyException(ExceptionEnum.PARAM_EDIT_ERROR);
        }

    }
    @Transactional
    public void addSpecParam(SpecParam param) {
        int count = paramMapper.insert(param);
        if (count != 1) {
            throw new LyException(ExceptionEnum.PARAM_SAVE_ERROR);
        }
    }
    @Transactional
    public void deleteSpecParam(Long id) {
        int count = paramMapper.deleteByPrimaryKey(id);
        if (count != 1) {
            throw new LyException(ExceptionEnum.DELETE_PARAM_ERROR);
        }

    }

    public List<SpecGroup> queryListByCid(Long cid) {
        //查询规格组
        List<SpecGroup> specGroups = queryGroupByCid(cid);
        //查询当前分类下的参数
        List<SpecParam> specParams = queryParamList(null, cid, null);
        //先把规格参数变成map，map的key是规格组id，map的值是组下的所有参数
        Map<Long, List<SpecParam>> map = new HashMap<>();

        for (SpecParam param : specParams) {
            if (!map.containsKey(param.getGroupId())) {
                //这个组id在map中不存在，新增一个list
                map.put(param.getGroupId(), new ArrayList<>());
            }
            map.get(param.getGroupId()).add(param);
        }
        //填充param到group
        for (SpecGroup specGroup : specGroups) {
            specGroup.setParams(map.get(specGroup.getId()));
        }
        return specGroups;
    }
}
