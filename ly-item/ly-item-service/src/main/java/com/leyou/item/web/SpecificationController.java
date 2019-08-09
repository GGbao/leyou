package com.leyou.item.web;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("spec")
public class SpecificationController {
    @Autowired
    private SpecificationService specService;


    /**
     * 根据分类id查询规格组
     *
     * @param cid
     * @return
     */
    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> queryGroupByCid(@PathVariable("cid") Long cid) {
        return ResponseEntity.ok(specService.queryGroupByCid(cid));

    }

    /**
     * 查询参数集合
     * @param gid 组id
     * @param cid 分类id
     * @param searching 是否搜索
     * @return
     */
    @GetMapping("params")
    public ResponseEntity<List<SpecParam>> queryParamList(
            @RequestParam(value = "gid", required = false) Long gid,
            @RequestParam(value = "cid", required = false) Long cid,
            @RequestParam(value = "searching",required = false) Boolean searching

    ) {
        return ResponseEntity.ok(specService.queryParamList(gid,cid,searching));
    }

    /**
     * 根据分类查询规格组及组内参数
     * @param cid
     * @return
     */
    @GetMapping("group")
    public ResponseEntity<List<SpecGroup>> queryListByCid(@RequestParam("cid") Long cid) {
        return ResponseEntity.ok(specService.queryListByCid(cid));
    }

    /**
     * 修改规格组参数
     *
     * @param group
     * @return
     */
    @PutMapping("group")
    public ResponseEntity<Void> saveEditedSpecGroup(@RequestBody SpecGroup group) {
        specService.saveEditedSpecGroup(group.getId(), group.getName());
        return ResponseEntity.ok().build();
    }

    /**
     * 新增规格组
     *
     * @param group
     * @return
     */
    @PostMapping("group")
    public ResponseEntity<Void> saveSpecGroup(@RequestBody SpecGroup group) {
        specService.addSpecGroup(group.getCid(), group.getName());
        return ResponseEntity.ok().build();
    }

    /**
     * 删除规格组
     *
     * @param id
     * @return
     */
    @DeleteMapping("group/{id}")
    public ResponseEntity<Void> deleteSpecGroup(@PathVariable("id") Long id) {
        specService.deleteSpecGroup(id);
        return ResponseEntity.ok().build();
    }

    /**
     * 编辑参数
     *
     * @param param
     * @return
     */
    @PutMapping("param")
    public ResponseEntity<Void> saveEditedSpecParam(@RequestBody SpecParam param) {
        specService.saveEditedSpecParam(param);
        return ResponseEntity.ok().build();
    }

    /**
     * 新增参数
     *
     * @param param
     * @return
     */
    @PostMapping("param")
    public ResponseEntity<Void> addSpecParam(@RequestBody SpecParam param) {
        specService.addSpecParam(param);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("param/{id}")
    public ResponseEntity<Void> deleteSpecParam(@PathVariable("id") Long id) {
        specService.deleteSpecParam(id);
        return ResponseEntity.ok().build();
    }


}
