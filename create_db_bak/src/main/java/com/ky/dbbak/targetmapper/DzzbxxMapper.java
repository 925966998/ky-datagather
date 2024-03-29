package com.ky.dbbak.targetmapper;

import com.ky.dbbak.entity.DzzbxxEntity;
import com.ky.dbbak.mybatis.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;

import java.util.List;
import java.util.Map;

@Mapper
public interface DzzbxxMapper extends BaseMapper {

    @SelectProvider(type = DzzbxxSql.class, method = "_queryselect")
    List<Map<String, Object>> _queryDzzbxx(Map pagerParam);

    /**
     * 根据条件查询分页 必要参数： currentPage : 当前第几页，默认1 pageSize : 每页多少条，默认10条 其他参数： map里的key为属性名（字段首字母小写） value为查询的条件，默认为等于
     * 要改动sql请修改 *Mapper 类里的 _query() 方法
     */
    @SelectProvider(type = DzzbxxSql.class, method = "_queryPage")
    List<DzzbxxEntity> _queryPage(Map pagerParam);

    /**
     * 根据条件查询全部 参数： map里的key为属性名（字段首字母小写） value为查询的条件，默认为等于 要改动sql请修改 *Mapper 类里的 _query() 方法
     */
    @SuppressWarnings("rawtypes")
    @SelectProvider(type = DzzbxxSql.class, method = "_queryAll")
    List<DzzbxxEntity> _queryAll(Map pagerParam);

    /**
     * 根据条件查询总条数 map里的key为属性名（字段首字母小写） value为查询的条件，默认为等于 要改动sql请修改 *Mapper 类里的 _query() 方法
     */
    @SelectProvider(type = DzzbxxSql.class, method = "_queryCount")
    long _queryCount(Map params);

    /**
     * 按id查询 参数： id ： 要查询的记录的id
     */
    @SelectProvider(type = DzzbxxSql.class, method = "_get")
    DzzbxxEntity _get(String id);

    /**
     * 删除（逻辑） 参数： id ： 要删除的记录的id
     */
    @DeleteProvider(type = DzzbxxSql.class, method = "_delete")
    int _delete(String id);

    /**
     * 删除（物理） 参数： id ： 要删除的记录的id
     */
    @DeleteProvider(type = DzzbxxSql.class, method = "_deleteForce")
    int _deleteForce(String id);

    /**
     * 新增 参数： map里的key为属性名（字段首字母小写） value为要插入的key值
     */
    @SuppressWarnings("rawtypes")
    @InsertProvider(type = DzzbxxSql.class, method = "_add")
    int _add(Map params);

    /**
     * 按实体类新增 参数： 实体类对象，必须有id属性
     */
    @InsertProvider(type = DzzbxxSql.class, method = "_addEntity")
    int _addEntity(DzzbxxEntity bean);

    /**
     * 更新 参数： id ： 要更新的记录的id 其他map里的参数，key为属性名（字段首字母小写） value为要更新成的值
     */
    @InsertProvider(type = DzzbxxSql.class, method = "_update")
    int _update(Map params);

    /**
     * 按实体类更新 参数： 实体类对象，必须有id属性
     */
    @UpdateProvider(type = DzzbxxSql.class, method = "_updateEntity")
    int _updateEntity(DzzbxxEntity bean);

    @UpdateProvider(type = DzzbxxSql.class, method = "_updateEntity_pk")
    int _updateEntity_pk(DzzbxxEntity bean, String pk, String pkValue);


    @Select("select * from dbo.DZZBXX where dwdm = #{dwdm}")
    DzzbxxEntity _queryByCode(String dwdm);

    @Delete("delete from dbo.DZZBXX where KJDZZBBH = #{KJDZZBBH}")
    int _deleteByCode(String KJDZZBBH);
}
