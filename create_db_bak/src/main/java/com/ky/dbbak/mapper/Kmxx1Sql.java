package com.ky.dbbak.mapper;

import com.ky.dbbak.mybatis.BaseProvider;

import java.util.Map;

public class Kmxx1Sql extends BaseProvider {
    @Override
    protected String getTableName() {
        return "dbo.GL_KMXX";
    }

    // 涉及到插入和更新的字段，不在该定义中的字段不会被操作
    @Override
    protected String[] getColumns() {
        return new String[]{
        };
    }

    @Override
    protected String getDialect() {
        return "sqlserver";
    }

    @Override
    protected String _query(Map map) {
        StringBuilder builder = new StringBuilder("SELECT\n" +
                "\t*\n" +
                "FROM\n" +
                "\tGL_KMXZLX\n" +
                "WHERE\n" +
                "\tgsdm < '99999999999999999999'; ");
        return builder.toString();
    }
    public String _queryselect(Map map) {
        StringBuilder builder = new StringBuilder(" select\n" +
                "        *\n" +
                "        from GL_KMXX where kmdm=#{kmdm}");
        return builder.toString();
    }
}
