package com.ky.dbbak.mapper;

import com.ky.dbbak.mybatis.BaseProvider;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class IndustrySql extends BaseProvider {
    @Override
    protected String getTableName() {
        return "dbo.industry";
    }

    // 涉及到插入和更新的字段，不在该定义中的字段不会被操作
    @Override
    protected String[] getColumns() {
        return new String[]{};
    }

    @Override
    protected String getDialect() {
        return "sqlserver";
    }

    @Override
    protected String _query(Map map) {
        StringBuilder builder = new StringBuilder("select * from " + getTableName() + " where 1=1");
        if (StringUtils.isNotEmpty(MapUtils.getString(map, "industryCode"))) {
            builder.append(" and industryCode=#{industryCode}");
        }
        if (StringUtils.isNotEmpty(MapUtils.getString(map, "industryName"))) {
            builder.append(" and industryName=#{industryName}");
        }
        if (StringUtils.isNotEmpty(MapUtils.getString(map, "parentID"))) {
            builder.append(" and parentID=#{parentID}");
        }
        builder.append(" order by industryCode");
        return builder.toString();
    }

}
