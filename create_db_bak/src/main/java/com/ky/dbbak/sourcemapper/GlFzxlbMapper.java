package com.ky.dbbak.sourcemapper;

import com.ky.dbbak.mapper.*;
import com.ky.dbbak.mybatis.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Map;

@Mapper
public interface GlFzxlbMapper extends BaseMapper {

    @SelectProvider(type = FzxlbSql.class, method = "_queryselect")
    List<Map<String, Object>> _queryGL_Fzxlb(Map pagerParam);

}
