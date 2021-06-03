package com.ky.hyks.mapper;


import com.ky.hyks.entity.CompanyEntity;
import com.ky.hyks.entity.CompanyOrderEntity;
import com.ky.hyks.entity.OrderListInfoEntity;
import com.ky.hyks.mybatis.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface CompanyOrderMapper extends BaseMapper {

    /**
     * 根据条件查询分页 必要参数： currentPage : 当前第几页，默认1 pageSize : 每页多少条，默认10条 其他参数： map里的key为属性名（字段首字母小写） value为查询的条件，默认为等于
     * 要改动sql请修改 *Mapper 类里的 _query() 方法
     */
    @SelectProvider(type = CompanyOrderSql.class, method = "_queryPage")
    List<CompanyOrderEntity> _queryPage(Map pagerParam);

    /**
     * 根据条件查询全部 参数： map里的key为属性名（字段首字母小写） value为查询的条件，默认为等于 要改动sql请修改 *Mapper 类里的 _query() 方法
     */
    @SuppressWarnings("rawtypes")
    @SelectProvider(type = CompanyOrderSql.class, method = "_queryAll")
    List<CompanyOrderEntity> _queryAll(Map pagerParam);

    /**
     * 根据条件查询总条数 map里的key为属性名（字段首字母小写） value为查询的条件，默认为等于 要改动sql请修改 *Mapper 类里的 _query() 方法
     */
    @SelectProvider(type = CompanyOrderSql.class, method = "_queryCount")
    long _queryCount(Map params);

    /**
     * 按id查询 参数： id ： 要查询的记录的id
     */
    @SelectProvider(type = CompanyOrderSql.class, method = "_get")
    CompanyOrderEntity _get(String id);

    /**
     * 删除（逻辑） 参数： id ： 要删除的记录的id
     */
    @DeleteProvider(type = CompanyOrderSql.class, method = "_delete")
    int _delete(String id);

    /**
     * 删除（物理） 参数： id ： 要删除的记录的id
     */
    @DeleteProvider(type = CompanyOrderSql.class, method = "_deleteForce")
    int _deleteForce(String id);

    /**
     * 新增 参数： map里的key为属性名（字段首字母小写） value为要插入的key值
     */
    @SuppressWarnings("rawtypes")
    @InsertProvider(type = CompanyOrderSql.class, method = "_add")
    int _add(Map params);

    /**
     * 按实体类新增 参数： 实体类对象，必须有id属性
     */
    @InsertProvider(type = CompanyOrderSql.class, method = "_addEntity")
    int _addEntity(CompanyOrderEntity bean);

    /**
     * 更新 参数： id ： 要更新的记录的id 其他map里的参数，key为属性名（字段首字母小写） value为要更新成的值
     */
    @InsertProvider(type = CompanyOrderSql.class, method = "_update")
    int _update(Map params);

    /**
     * 按实体类更新 参数： 实体类对象，必须有id属性
     */
    @InsertProvider(type = CompanyOrderSql.class, method = "_updateEntity")
    int _updateEntity(CompanyOrderEntity bean);

    @Select("select * from KY_HYKS_company_order where companyId=#{companyId} and orderId=#{orderId}")
    List<CompanyOrderEntity> _queryRelation(Map map);

    @Select("select co.*,c.NAME as companyName,o.matterName as orderName,o.nastNum as nastNum from KY_HYKS_company_order co left join bd_supplier c on co.companyId=c.pk_supplier left join KY_HYKS_orderInfo o on co.orderId=o.id where co.id=#{id} ")
    CompanyOrderEntity queryById(String id);

    @SelectProvider(type = CompanyOrderSql.class, method = "_queryCommitPrice")
    List<CompanyOrderEntity> _queryCommitPrice(Map pagerParam);

    @SelectProvider(type = CompanyOrderSql.class, method = "_queryCommitCount")
    long _queryCommitCount(Map pagerParam);

    @Delete("delete from KY_HYKS_company_order where orderId =#{orderInfoId}")
    void deleteByOrderId(String orderInfoId);
}
