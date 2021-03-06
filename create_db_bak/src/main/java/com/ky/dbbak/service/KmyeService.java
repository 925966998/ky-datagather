package com.ky.dbbak.service;

import com.ky.dbbak.entity.OrgEntity;
import com.ky.dbbak.mapper.OrgMapper;
import com.ky.dbbak.mybatis.PagerResult;
import com.ky.dbbak.mybatis.RestResult;
import com.ky.dbbak.sourcemapper.*;
import com.ky.dbbak.targetmapper.DzzbxxMapper;
import com.ky.dbbak.targetmapper.TragetMapper;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName TargetService
 * @Description: TODO
 * @Author czw
 * @Date 2020/3/24
 **/
@Service
public class KmyeService {
    @Autowired
    TragetMapper tragetMapper;
    @Autowired
    SourceMapper sourceMapper;
    @Autowired
    KmxzlxMapper kmxzlxMapper;
    @Autowired
    KmxxMapper kmxxMapper;


    public List<Map<String, Object>> kjkmResult(List<Map<String, Object>> resultList, Map<String, Object> pageDataGL_Ztcs) {
        List<Map<String, Object>> resultListNew = new ArrayList<Map<String, Object>>();
        if (resultList != null && resultList.size() > 0) {
            for (Map<String, Object> map : resultList
            ) {
                resultListNew.add(map);
                Integer legth = map.get("KJKMBM").toString().length();
                String kmbmfa = pageDataGL_Ztcs.get("kmbmfa").toString();
                String[] lbfjStr = kmbmfa.split("-");
                int num = 0;//8  4 2 2 2 2
                String kmqc = "";
                Map<String, Object> quM = new HashMap<String, Object>();
                List kmdms = new ArrayList();
                for (int w = 0; w < lbfjStr.length; w++) {
                    Map<String, Object> dataPullBase = new HashMap<String, Object>(map);
                    num = num + Integer.valueOf(lbfjStr[w].trim());
                    if (num < legth) {
                        quM.put("kmdm", map.get("KJKMBM").toString().substring(0, num));
                        List<Map<String, Object>> pageDataGL_KMXX = kmxxMapper._queryKmxxmx(quM);
                        dataPullBase.put("KJKMBM", map.get("KJKMBM").toString().substring(0, num));
                        dataPullBase.put("KJKMJB", w + 1);
                        dataPullBase.put("KJKMMC", pageDataGL_KMXX.get(0).get("kmmc"));

                        kmdms.add(map.get("KJKMBM").toString().substring(0, num));
                        Map<String, Object> queryPd = new HashMap<String, Object>();
                        queryPd.put("kmdms", kmdms);
                        List<String> pageDataGL_KMXX1 = sourceMapper._queryGL_KMXX1(queryPd);
                        kmqc = String.join("/", pageDataGL_KMXX1);
                        dataPullBase.put("KJKMQC", kmqc.trim());
                        if (w != 0) {
                            dataPullBase.put("SJKMBM", map.get("KJKMBM").toString().substring(0, num - Integer.valueOf(lbfjStr[w].trim())));
                        } else {
                            dataPullBase.put("SJKMBM", " ");

                        }
                        dataPullBase.put("KJTX", pageDataGL_KMXX.get(0).get("KJTXDM"));
                        dataPullBase.put("SFZDJKM", pageDataGL_KMXX.get(0).get("kmmx"));
                        resultListNew.add(dataPullBase);
                    } else {
                        break;
                    }
                }
            }
        }
        return resultListNew;
    }



}
