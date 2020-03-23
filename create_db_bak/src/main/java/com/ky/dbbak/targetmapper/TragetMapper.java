package com.ky.dbbak.targetmapper;

import com.ky.dbbak.mapper.FzncsSql;
import com.ky.dbbak.mapper.PzfzmxSql;
import com.ky.dbbak.mapper.YsdwSql;
import com.ky.dbbak.mybatis.BaseMapper;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Map;

@Mapper
public interface TragetMapper extends BaseMapper {

    @SelectProvider(type = DzzbxxSql.class, method = "_queryselect")
    List<Map<String, Object>> _queryDzzbxx(Map pagerParam);

    @InsertProvider(type = YsdwSql.class, method = "_add")
    int _add(Map params);

    @InsertProvider(type = PzfzmxSql.class, method = "_addBatch")
    int _addPzfzmx(Map params);

    @InsertProvider(type = FzncsSql.class, method = "_addBatch")
    int _addFzncs(Map params);

}