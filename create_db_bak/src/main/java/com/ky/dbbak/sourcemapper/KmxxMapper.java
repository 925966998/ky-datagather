package com.ky.dbbak.sourcemapper;

import com.ky.dbbak.mybatis.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Map;

@Mapper
public interface KmxxMapper extends BaseMapper {

    @SelectProvider(type = KmxxSql.class, method = "_queryselect")
    List<Map<String, Object>> _queryGL_KMXX(Map pagerParam);
    @Select("select kmmc,yefx from GL_KMXX  where cHARINDEX('2020',kjnd)=1 AND kmdm=#{kmdm} ")
    List<Map<String, Object>> _queryKmdm(String kmdm);
    @Select("select kmmc,yefx,kmmx from GL_KMXX  where cHARINDEX('2020',kjnd)=1 AND kmdm=#{kmdm} ")
    List<Map<String, Object>> _querykmxx(Map pagerParam);

    @SelectProvider(type = KmxxSql.class, method = "_queryKjnd")
    List<Map<String, Object>> _queryKmxxList(Map pagerParam);

    @SelectProvider(type = KmxxSql.class, method = "_queryKmxz")
    List<Map<String, Object>> _queryKmxxmx(Map pagerParam);
}
