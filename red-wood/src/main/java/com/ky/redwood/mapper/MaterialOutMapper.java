package com.ky.redwood.mapper;

import com.ky.redwood.entity.MaterialEntity;
import com.ky.redwood.entity.MaterialOutEntity;
import com.ky.redwood.mybatis.BaseMapper;
import com.ky.redwood.sql.MaterialOutSql;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * @author linan
 * Create By Generator
 */
@Mapper
public interface MaterialOutMapper extends BaseMapper {


    /**
     * 根据条件查询分页 必要参数： currentPage : 当前第几页，默认1 pageSize : 每页多少条，默认10条 其他参数： map里的key为属性名（字段首字母小写） value为查询的条件，默认为等于
     * 要改动sql请修改 *Mapper 类里的 _query() 方法
     */
    @SelectProvider(type = MaterialOutSql.class, method = "_queryPage")
    List<MaterialOutEntity> _queryPage(Map pagerParam);

    /**
     * 根据条件查询全部 参数： map里的key为属性名（字段首字母小写） value为查询的条件，默认为等于 要改动sql请修改 *Mapper 类里的 _query() 方法
     */
    @SelectProvider(type = MaterialOutSql.class, method = "_queryAll")
    List<MaterialOutEntity> _queryAll(Map pagerParam);

    /**
     * 根据条件查询总条数 map里的key为属性名（字段首字母小写） value为查询的条件，默认为等于 要改动sql请修改 *Mapper 类里的 _query() 方法
     */
    @SelectProvider(type = MaterialOutSql.class, method = "_queryCount")
    long _queryCount(Map params);

    /**
     * 按id查询 参数： id ： 要查询的记录的id
     */
    @SelectProvider(type = MaterialOutSql.class, method = "_get")
    MaterialOutEntity _get(String id);

    /**
     * 删除（逻辑） 参数： id ： 要删除的记录的id
     */
    @DeleteProvider(type = MaterialOutSql.class, method = "_delete")
    int _delete(String id);

    /**
     * 删除（物理） 参数： id ： 要删除的记录的id
     */
    @DeleteProvider(type = MaterialOutSql.class, method = "_deleteForce")
    int _deleteForce(String id);

    /**
     * 新增 参数： map里的key为属性名（字段首字母小写） value为要插入的key值
     */
    @InsertProvider(type = MaterialOutSql.class, method = "_add")
    int _add(Map params);

    /**
     * 按实体类新增 参数： 实体类对象，必须有id属性
     */
    @InsertProvider(type = MaterialOutSql.class, method = "_addEntity")
    int _addEntity(MaterialOutEntity bean);

    /**
     * 更新 参数： id ： 要更新的记录的id 其他map里的参数，key为属性名（字段首字母小写） value为要更新成的值
     */
    @UpdateProvider(type = MaterialOutSql.class, method = "_update")
    int _update(Map params);

    /**
     * 按实体类更新 参数： 实体类对象，必须有id属性
     */
    @UpdateProvider(type = MaterialOutSql.class, method = "_updateEntity")
    int _updateEntity(MaterialOutEntity bean);


    @Select("SELECT * FROM material_out WHERE id=#{id}")
    List<MaterialOutEntity> _editById(String id);

    @Select("select DISTINCT SUM(amount)-(select sum(useAmount) from material_out where processParentId=#{processParentId}) as amount FROM material_out WHERE processParentId=#{processParentId} ")
    Integer queryByProcessId(String processParentId);

    @Update("update material_out set useAmount = #{amount}+useAmount WHERE processParentId=#{processParentId} and parentId is null ")
    int updateUseAmountByParentProcessId(int amount, String processParentId);


    @Select("select m.*,g.allName as allName from material_out m LEFT JOIN  goods g On g.id=m.productName  where m. id=#{id}")
    MaterialOutEntity _getAll(String id);
}
