package com.ky.redwood.service;

import com.ky.redwood.entity.ProductEntity;
import com.ky.redwood.entity.ProductEntity;
import com.ky.redwood.mapper.MaterialOutMapper;
import com.ky.redwood.mapper.ProductMapper;
import com.ky.redwood.mybatis.PagerResult;
import com.ky.redwood.mybatis.RestResult;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author linan
 * Create By Generator
 */
@Service
public class ProductService {

    @Autowired
    ProductMapper productMapper;
    

    /**
     * 查询全部
     *
     * @param params
     * @return
     */
    @SuppressWarnings("rawtypes")
    public Object queryAll(Map params) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, productMapper._queryAll(params));
    }

    /**
     * currentPage : 当前第几页，默认1 pageSize : 每页多少条，默认10
     *
     * @param params
     * @return
     */
    public Object queryPage(Map params) {
        List<ProductEntity> list = productMapper._queryPage(params);
        long count = productMapper._queryCount(params);
        PagerResult pagerResult = new PagerResult(list, count, MapUtils.getLongValue(params, "currentPage"),
                MapUtils.getLongValue(params, "pageSize"));
        //return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, pagerResult);
        return pagerResult.getItems();
    }

    /**
     * 按id查询 参数 要查询的记录的id
     */
    public Object get(Map<String, String> params) {
        return productMapper._get(params.get("id"));
    }

    public ProductEntity get(String id) {
        return productMapper._get(id);
    }


    /**
     * 新增 参数 map里的key为属性名（字段首字母小写） value为要插入的key的value
     */
    public Object add(Map<String, String> params) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, productMapper._add(params));
    }

    /**
     * 新增 参数 map里的key为属性名（字段首字母小写） value为要插入的key的value
     */
    @Transactional
    public Object add(ProductEntity entity, int amount) {
        productMapper._addEntity(entity);
        return new RestResult();
    }

    public Object add(ProductEntity entity) {
        productMapper._addEntity(entity);
        return new RestResult();
    }


    /**
     * 更新 参数 map里的key为属性名（字段首字母小写） value为要插入的key的value
     */
    public Object update(Map<String, String> params) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, productMapper._update(params));
    }

    /**
     * 更新 参数 map里的key为属性名（字段首字母小写） value为要插入的key的value
     */
    @Transactional
    public Object update(ProductEntity ProductEntity, int amount) {
        productMapper._updateEntity(ProductEntity);
        return new RestResult();
    }

    public Object update(ProductEntity ProductEntity) {
        productMapper._updateEntity(ProductEntity);
        return new RestResult();
    }

    /**
     * 逻辑删除
     */
    public Object delete(String id) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, productMapper._delete(id));
    }

    /**
     * 物理删除
     */
    public Object _deleteForce(String id) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, productMapper._deleteForce(id));
    }

    public Object _updateForce(String productParentId,String id) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, productMapper._updateForce(productParentId,id));
    }
}
