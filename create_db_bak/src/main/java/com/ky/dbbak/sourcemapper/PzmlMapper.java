package com.ky.dbbak.sourcemapper;

import com.ky.dbbak.mybatis.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Map;

@Mapper
public interface PzmlMapper extends BaseMapper {

    @SelectProvider(type = PzmlSql.class, method = "_queryselect")
    List<Map<String, Object>> _queryPzml(Map pagerParam);
    @SelectProvider(type = PzmlSql.class, method = "_queryselectG")
    List<Map<String, Object>> _queryPzmlG(Map<String, Object> pageData);
}
