package com.ky.dbbak.service;

import com.alibaba.fastjson.JSONObject;
import com.ky.dbbak.entity.AreaEntity;
import com.ky.dbbak.mapper.AreaMapper;
import com.ky.dbbak.mybatis.PagerResult;
import com.ky.dbbak.mybatis.RestResult;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AreaService {

    @Autowired
    AreaMapper areaMapper;


    /**
     * 查询全部
     *
     * @param params
     * @return
     */
    @SuppressWarnings("rawtypes")
    public Object queryAll(Map params) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, areaMapper._queryAll(params));
    }

    /**
     * currentPage : 当前第几页，默认1 pageSize : 每页多少条，默认10
     *
     * @param params
     * @return
     */
    public Object queryPage(Map params) {
        List<AreaEntity> list = areaMapper._queryPage(params);
        long count = areaMapper._queryCount(params);
        PagerResult pagerResult = new PagerResult(list, count, MapUtils.getLongValue(params, "currentPage"),
                MapUtils.getLongValue(params, "pageSize"));
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, pagerResult);
    }

    /**
     * 按id查询 参数 要查询的记录的id
     */
    public Object get(Map<String, String> params) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, areaMapper._get(params.get("id")));
    }


    /**
     * 新增 参数 map里的key为属性名（字段首字母小写） value为要插入的key的value
     */
    public Object add(Map<String, String> params) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, areaMapper._add(params));
    }

    /**
     * 新增 参数 map里的key为属性名（字段首字母小写） value为要插入的key的value
     */
    public Object add(AreaEntity entity) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, areaMapper._addEntity(entity));
    }


    /**
     * 更新 参数 map里的key为属性名（字段首字母小写） value为要插入的key的value
     */
    public Object update(Map<String, String> params) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, areaMapper._update(params));
    }

    /**
     * 更新 参数 map里的key为属性名（字段首字母小写） value为要插入的key的value
     */
    public Object update(AreaEntity areaEntity) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, areaMapper._updateEntity(areaEntity));
    }

    /**
     * 逻辑删除
     */
    public Object delete(String id) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, areaMapper._delete(id));
    }

    /**
     * 物理删除
     */
    public Object _deleteForce(String id) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, areaMapper._deleteForce(id));
    }


    public Object queryAreas(Map params) {
        List<AreaEntity> areaEntities = areaMapper._queryAll(params);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("provinces", areaEntities);
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, jsonObject);
    }

    public Object queryProvinces(Map params) {
        List<AreaEntity> areaEntities = areaMapper._queryAll(params);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cities", areaEntities);
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, jsonObject);
    }

    public Object queryCities(Map params) {
        List<AreaEntity> areaEntities = areaMapper._queryAll(params);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("areas", areaEntities);
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, jsonObject);
    }
}