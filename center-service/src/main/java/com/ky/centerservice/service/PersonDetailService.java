package com.ky.centerservice.service;

import com.ky.centerservice.entity.PersonDetailEntity;
import com.ky.centerservice.mapper.PersonDetailMapper;
import com.ky.centerservice.mybatis.PagerResult;
import com.ky.centerservice.mybatis.RestResult;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class PersonDetailService {

    @Autowired
    PersonDetailMapper personDetailMapper;

    /**
     * 查询全部
     *
     * @param params
     * @return
     */
    @SuppressWarnings("rawtypes")
    public Object queryAll(Map params) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, personDetailMapper._queryAll(params));
    }
    /**
     * currentPage : 当前第几页，默认1 pageSize : 每页多少条，默认10
     *
     * @param params
     * @return
     */
    public RestResult queryPage(Map params) {
        List<PersonDetailEntity> list = personDetailMapper._queryPage(params);
        long count = personDetailMapper._queryCount(params);
        PagerResult pagerResult = new PagerResult(list, count, MapUtils.getLongValue(params, "page"),
                MapUtils.getLongValue(params, "rows"));
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, pagerResult);
    }
    /**
     * 按id查询 参数 要查询的记录的id
     */
    public Object get(Map<String, String> params) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, personDetailMapper._get(params.get("id")));
    }
    /**
     * 新增 参数 map里的key为属性名（字段首字母小写） value为要插入的key的value
     */
    public Object add(PersonDetailEntity entity) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, personDetailMapper._addEntity(entity));
    }

    /**
     * 更新 参数 map里的key为属性名（字段首字母小写） value为要插入的key的value
     */
    public Object update(PersonDetailEntity personDetailEntity) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, personDetailMapper._updateEntity(personDetailEntity));
    }

    /**
     * 逻辑删除
     */
    public Object delete(String id) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, personDetailMapper._delete(id));
    }

    /**
     * 物理删除
     */
    public Object _deleteForce(String id) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, personDetailMapper._deleteForce(id));
    }

}
