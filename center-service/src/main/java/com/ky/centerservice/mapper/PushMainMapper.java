package com.ky.centerservice.mapper;

import com.ky.centerservice.entity.PushMainEntity;
import com.ky.centerservice.mybatis.BaseMapper;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Map;

@Mapper
public interface PushMainMapper extends BaseMapper {

    /**
     * 根据条件查询分页 必要参数： currentPage : 当前第几页，默认1 pageSize : 每页多少条，默认10条 其他参数： map里的key为属性名（字段首字母小写） value为查询的条件，默认为等于
     * 要改动sql请修改 *Mapper 类里的 _query() 方法
     */
    @SelectProvider(type = PushMainSql.class, method = "_queryPage")
    List<PushMainEntity> _queryPage(Map pagerParam);

    /**
     * 根据条件查询全部 参数： map里的key为属性名（字段首字母小写） value为查询的条件，默认为等于 要改动sql请修改 *Mapper 类里的 _query() 方法
     */
    @SuppressWarnings("rawtypes")
    @SelectProvider(type = PushMainSql.class, method = "_queryAll")
    List<PushMainEntity> _queryAll(Map pagerParam);

    /**
     * 根据条件查询总条数 map里的key为属性名（字段首字母小写） value为查询的条件，默认为等于 要改动sql请修改 *Mapper 类里的 _query() 方法
     */
    @SelectProvider(type = PushMainSql.class, method = "_queryCount")
    long _queryCount(Map params);

    /**
     * 按id查询 参数： id ： 要查询的记录的id
     */
    @SelectProvider(type = PushMainSql.class, method = "_get")
    PushMainEntity _get(String id);

    /**
     * 删除（逻辑） 参数： id ： 要删除的记录的id
     */
    @DeleteProvider(type = PushMainSql.class, method = "_delete")
    int _delete(String id);

    /**
     * 删除（物理） 参数： id ： 要删除的记录的id
     */
    @DeleteProvider(type = PushMainSql.class, method = "_deleteForce")
    int _deleteForce(String id);


    /**
     * 按实体类新增 参数： 实体类对象，必须有id属性
     */
    @InsertProvider(type = PushMainSql.class, method = "_addEntity")
    int _addEntity(PushMainEntity bean);


    /**
     * 按实体类更新 参数： 实体类对象，必须有id属性
     */
    @InsertProvider(type = PushMainSql.class, method = "_updateEntity")
    int _updateEntity(PushMainEntity bean);
}
