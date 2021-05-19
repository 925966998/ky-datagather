package com.ky.hyks.mapper;


import com.ky.hyks.mybatis.BaseProvider;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class OrderListSql extends BaseProvider {
    @Override
    protected String getTableName() {
        return "KY_HYKS_orderList";
    }

    // 涉及到插入和更新的字段，不在该定义中的字段不会被操作
    @Override
    protected String[] getColumns() {
        return new String[]{"orderNum","name","totalAmount","askAmount","haveAmount","state","endTime",
                "unit","specs","supplierId","orderType","orderOrg","oddNum","orderTime","needTime" };
    }

    @Override
    protected String _query(Map map) {
        StringBuilder builder = new StringBuilder("select * from KY_HYKS_orderList  where 1=1");
        if (StringUtils.isNotBlank(MapUtils.getString(map, "orderNum"))) {
            builder.append(" and orderNum = #{orderNum}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "name"))) {
            builder.append(" and name like concat('%',#{name},'%')");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "state"))) {
            builder.append(" and state like {state}");
        }
//        if (StringUtils.isNotBlank(MapUtils.getString(map, "companyId"))) {
//            builder.append(" and u.companyId = #{companyId}");
//        }
//        if (StringUtils.isNotBlank(MapUtils.getString(map, "userNote"))) {
//            builder.append(" and u.userNote = #{userNote}");
//        }
//        if (StringUtils.isNotBlank(MapUtils.getString(map, "roleId"))) {
//            builder.append(" and u.roleId = #{roleId}");
//        }
        builder.append(" order by createTime desc");
        return builder.toString();
    }

    @Override
    public String _queryPage(Map map) {
        StringBuilder builder = new StringBuilder("select * from KY_HYKS_orderList  where 1=1");
        if (StringUtils.isNotBlank(MapUtils.getString(map, "orderNum"))) {
            builder.append(" and orderNum = #{orderNum}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "name"))) {
            builder.append(" and name like concat('%',#{name},'%')");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "state"))) {
            builder.append(" and state like {state}");
        }
//        if (StringUtils.isNotBlank(MapUtils.getString(map, "status"))) {
//            builder.append(" and u.status = #{status}");
//        }
//        if (StringUtils.isNotBlank(MapUtils.getString(map, "fullName"))) {
//            builder.append(" and u.fullName like concat('%',#{fullName},'%')");
//        }
//        if (StringUtils.isNotBlank(MapUtils.getString(map, "idCardNo"))) {
//            builder.append(" and u.idCardNo = #{idCardNo}");
//        }
//        if (StringUtils.isNotBlank(MapUtils.getString(map, "companyId"))) {
//            builder.append(" and u.companyId = #{companyId}");
//        }
//        if (StringUtils.isNotBlank(MapUtils.getString(map, "userNote"))) {
//            builder.append(" and u.userNote = #{userNote}");
//        }
//        if (StringUtils.isNotBlank(MapUtils.getString(map, "roleId"))) {
//            builder.append(" and u.roleId = #{roleId}");
//        }
        builder.append(" order by updateTime desc");
//        builder.append(this.pageHelp(MapUtils.getLongValue(map, "page"), MapUtils.getLongValue(map, "rows")));
        return builder.toString();
    }


}