package com.leyou.page.service;

import com.leyou.common.utils.ThreadUtils;
import com.leyou.item.pojo.*;
import com.leyou.page.client.BrandClient;
import com.leyou.page.client.CategoryClient;
import com.leyou.page.client.GoodsClient;
import com.leyou.page.client.SpecificationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class PageService {
    @Autowired
    private BrandClient brandClient;
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private SpecificationClient specClient;
    @Autowired
    private TemplateEngine templateEngine;
    public Map<String, Object> loadModel(Long spuId) {
        Map<String, Object> model = new HashMap<>();

        //查询spu
        Spu spu = goodsClient.querySpuById(spuId);
        //查询skus
        List<Sku> skus = spu.getSkus();
        //查询详情
        SpuDetail detail = spu.getSpuDetail();
        //查询brand
        Brand brand = brandClient.queryBrandById(spu.getBrandId());
        //查询商品分类
        List<Category> categories = categoryClient.queryCategoryByIds(
                Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        //查询规格参数
        List<SpecGroup> specs = specClient.queryListByCid(spu.getCid3());
        model.put("title", spu.getTitle());
        model.put("subTitle", spu.getSubTitle());
        model.put("skus", skus);
        model.put("detail", detail);
        model.put("brand", brand);
        model.put("categories", categories);
        model.put("specs", specs);
        return model;
    }

    public void createHtml(Long spuId) {
        //上下文
        Context context = new Context();
        context.setVariables(loadModel(spuId));

        //输出流
        // TODO: 2019/10/30 0030 暂时存放在本地，之后部署在服务器
        File dest = new File("/Users/Administrator/IdeaProjects/leyou/upload", spuId + ".html");
        PrintWriter writer = null;
        if (dest.exists()) {
            dest.delete();
        }
        try {
            writer = new PrintWriter(dest, "UTF-8");
            //生成HTML
            templateEngine.process("item", context, writer);
        } catch (Exception e) {
            log.error("[静态页服务] 生成静态页异常", e);
        }
    }
    /**
     * 新建线程处理页面静态化
     * @param spuId
     */
    /*public void asyncExcute(Long spuId) {
        ThreadUtils.execute(()->createHtml(spuId));
        *//*ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                createHtml(spuId);
            }
        });*//*
    }*/

    public void deleteHtml(Long spuId) {
        File dest = new File("/Users/Administrator/IdeaProjects/leyou/upload", spuId + ".html");
        if (dest.exists()) {
            dest.delete();
        }
    }
}

