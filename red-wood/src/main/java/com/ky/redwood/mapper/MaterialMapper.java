package com.ky.redwood.mapper;

import com.ky.redwood.entity.MaterialEntity;
import com.ky.redwood.mybatis.BaseMapper;
import com.ky.redwood.sql.MaterialSql;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * @author linan
 * Create By Generator
 */
@Mapper
public interface MaterialMapper extends BaseMapper {


    /**
     * 根据条件查询分页 必要参数： currentPage : 当前第几页，默认1 pageSize : 每页多少条，默认10条 其他参数： map里的key为属性名（字段首字母小写） value为查询的条件，默认为等于
     * 要改动sql请修改 *Mapper 类里的 _query() 方法
     */
    @SelectProvider(type = MaterialSql.class, method = "_queryPage")
    List<MaterialEntity> _queryPage(Map pagerParam);

    /**
     * 根据条件查询全部 参数： map里的key为属性名（字段首字母小写） value为查询的条件，默认为等于 要改动sql请修改 *Mapper 类里的 _query() 方法
     */
    @SelectProvider(type = MaterialSql.class, method = "_queryAll")
    List<MaterialEntity> _queryAll(Map pagerParam);

    /**
     * 根据条件查询总条数 map里的key为属性名（字段首字母小写） value为查询的条件，默认为等于 要改动sql请修改 *Mapper 类里的 _query() 方法
     */
    @SelectProvider(type = MaterialSql.class, method = "_queryCount")
    long _queryCount(Map params);

    /**
     * 按id查询 参数： id ： 要查询的记录的id
     */
    @SelectProvider(type = MaterialSql.class, method = "_get")
    MaterialEntity _get(String id);

    /**
     * 删除（逻辑） 参数： id ： 要删除的记录的id
     */
    @DeleteProvider(type = MaterialSql.class, method = "_delete")
    int _delete(String id);

    /**
     * 删除（物理） 参数： id ： 要删除的记录的id
     */
    @DeleteProvider(type = MaterialSql.class, method = "_deleteForce")
    int _deleteForce(String id);

    /**
     * 新增 参数： map里的key为属性名（字段首字母小写） value为要插入的key值
     */
    @InsertProvider(type = MaterialSql.class, method = "_add")
    int _add(Map params);

    /**
     * 按实体类新增 参数： 实体类对象，必须有id属性
     */
    @InsertProvider(type = MaterialSql.class, method = "_addEntity")
    int _addEntity(MaterialEntity bean);

    /**
     * 更新 参数： id ： 要更新的记录的id 其他map里的参数，key为属性名（字段首字母小写） value为要更新成的值
     */
    @UpdateProvider(type = MaterialSql.class, method = "_update")
    int _update(Map params);

    /**
     * 按实体类更新 参数： 实体类对象，必须有id属性
     */
    @UpdateProvider(type = MaterialSql.class, method = "_updateEntity")
    int _updateEntity(MaterialEntity bean);

    @Select("SELECT * FROM material WHERE id=#{id}")
    List<MaterialEntity> _countById(String id);

    @Select("SELECT * FROM material WHERE consumablesIs='1'")
    List<MaterialEntity> _queryConsumable(Map params);

    @Select("SELECT * FROM material WHERE consumablesIs='0'")
    List<MaterialEntity> _queryMaterialOut(Map params);
}
