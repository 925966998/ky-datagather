package com.ky.redwood.sql;

import com.ky.redwood.mybatis.BaseProvider;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author linan
 * Create By Generator
 */
public class ProcessSql extends BaseProvider {
    @Override
    protected String getTableName() {
        return "process";
    }

    // 涉及到插入和更新的字段，不在该定义中的字段不会被操作
    @Override
    protected String[] getColumns() {
        return new String[]{
                "productName",
                "materialId",
                "flowStatus",
                "type",
                "amount",
                "add_fee",
                "fee",
                "userId",
                "startTime",
                "endTime",
                "processingPersonnel",
                "isQuality",
                "isStandard",
                "materialOutId",
        };
    }

    @Override
    protected String getDialect() {
        return "mysql";
    }

    @Override
    protected String _query(Map map) {
        StringBuilder builder = new StringBuilder();
        if (map.get("typePage").toString().equals("queryPageType")) {
            builder.append("SELECT t.* FROM process t where 1=1 and t.type='1' and t.flowStatus='0'  ");
        } else if (map.get("typePage").toString().equals("queryProcessPage")) {
            builder.append("SELECT  t.*  FROM process t  where 1=1 ");
        } else if (map.get("typePage").toString().equals("queryPage")) {
            builder.append("SELECT t.* FROM ");
            builder.append("(SELECT productName,max(createTime) as createTime FROM process GROUP BY productName) a ");
            builder.append("LEFT JOIN process t  ON t.productName=a.productName and t.createTime = a.createTime  where t.flowStatus !=8 ");
        } else if (map.get("typePage").toString().equals("queryFinancial")) {
            builder.append("SELECT b.flowStatus, pf.processFlowName AS processFlowName ,COUNT(b.flowStatus) as num FROM (SELECT t.* FROM (SELECT productName,max(createTime) as createTime FROM process GROUP BY productName) a LEFT JOIN process t ON t.productName=a.productName and t.createTime = a.createTime where t.flowStatus !=8) b LEFT JOIN process_flow pf ON b.flowStatus = pf.id  GROUP BY b.flowStatus");
        }else {
            builder.append("SELECT t.* FROM process t  where 1=1  and t.type='0' and t.flowStatus='0'  ");
        }
        if (StringUtils.isNotEmpty(MapUtils.getString(map, "materialOutId"))) {
            builder.append(" and t.materialOutId=#{materialOutId}");
        }
        if (StringUtils.isNotEmpty(MapUtils.getString(map, "productName"))) {
            builder.append(" and t.productName like concat('%',#{productName},'%')");
        }
        if (StringUtils.isNotEmpty(MapUtils.getString(map, "flowStatus"))) {
            builder.append(" and t.flowStatus=#{flowStatus}");
        }
        if (StringUtils.isNotEmpty(MapUtils.getString(map, "type"))) {
            builder.append(" and t.type=#{type}");
        }
        if (StringUtils.isNotEmpty(MapUtils.getString(map, "amount"))) {
            builder.append(" and t.amount=#{amount}");
        }
        if (StringUtils.isNotEmpty(MapUtils.getString(map, "add_fee"))) {
            builder.append(" and t.add_fee=#{add_fee}");
        }
        if (StringUtils.isNotEmpty(MapUtils.getString(map, "fee"))) {
            builder.append(" and t.fee=#{fee}");
        }
        if (StringUtils.isNotEmpty(MapUtils.getString(map, "userId"))) {
            builder.append(" and t.userId=#{userId}");
        }
        if (StringUtils.isNotEmpty(MapUtils.getString(map, "isQuality"))) {
            builder.append(" and t.isQuality=#{isQuality}");
        }
        if (StringUtils.isNotEmpty(MapUtils.getString(map, "isStandard"))) {
            builder.append(" and t.isStandard=#{isStandard}");
        }
        if (StringUtils.isNotEmpty(MapUtils.getString(map, "startTime"))) {
            builder.append(" and t.createTime >='" + map.get("startTime") + "'");
        }
        if (StringUtils.isNotEmpty(MapUtils.getString(map, "endTime"))) {
            builder.append(" and t.createTime <='" + map.get("endTime") + "'");
        }
        if (StringUtils.isNotEmpty(MapUtils.getString(map, "processingPersonnel"))) {
            builder.append(" and t.processingPersonnel like concat('%',#{processingPersonnel},'%')");
        }
        return builder.toString();
    }
    public String _queryPByName(Map map){
        StringBuilder builder = new StringBuilder("select * from " + getTableName() + " where 1=1");
        if (StringUtils.isNotEmpty(MapUtils.getString(map, "materialOutId"))) {
            builder.append(" and materialOutId=#{materialOutId}");
        } if (StringUtils.isNotEmpty(MapUtils.getString(map, "productName"))) {
            builder.append(" and productName like concat('%',#{productName},'%')");
        }
        return builder.toString();
    }
}
