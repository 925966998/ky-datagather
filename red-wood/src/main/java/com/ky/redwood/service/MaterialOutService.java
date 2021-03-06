package com.ky.redwood.service;

import com.ky.redwood.entity.*;
import com.ky.redwood.mapper.GoodsMapper;
import com.ky.redwood.mapper.MaterialMapper;
import com.ky.redwood.mapper.MaterialOutMapper;
import com.ky.redwood.mapper.ProcessMapper;
import com.ky.redwood.mybatis.PagerResult;
import com.ky.redwood.mybatis.RestResult;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author linan
 * Create By Generator
 */
@Service
public class MaterialOutService {

    @Autowired
    MaterialOutMapper materialOutMapper;

    @Autowired
    MaterialMapper materialMapper;
    @Autowired
    ProcessMapper processMapper;

    @Autowired
    GoodsMapper goodsMapper;
    /**
     * 查询全部
     *
     * @param params
     * @return
     */
    @SuppressWarnings("rawtypes")
    public Object queryAll(Map params) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, materialOutMapper._queryAll(params));
    }

    /**
     * currentPage : 当前第几页，默认1 pageSize : 每页多少条，默认10
     *
     * @param params
     * @return
     */
    public Object queryPage(Map params) {
        List<MaterialOutEntity> list = materialOutMapper._queryPage(params);
        long count = materialOutMapper._queryCount(params);
        PagerResult pagerResult = new PagerResult(list, count, MapUtils.getLongValue(params, "currentPage"),
                MapUtils.getLongValue(params, "pageSize"));
        return pagerResult.getItems();
    }

    /**
     * 按id查询 参数 要查询的记录的id
     */
    public Object get(Map<String, String> params) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, materialOutMapper._get(params.get("id")));
    }

    public MaterialOutEntity get(String id) {
        return materialOutMapper._get(id);
    }

    public MaterialOutEntity getAll(Map<String, String> params) {
        return materialOutMapper._getAll(params.get("id"));
    }

    /**
     * 新增 参数 map里的key为属性名（字段首字母小写） value为要插入的key的value
     */
    public Object add(Map<String, String> params) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, materialOutMapper._add(params));
    }

    /**
     * 新增 参数 map里的key为属性名（字段首字母小写） value为要插入的key的value
     */
    public Object add(MaterialOutEntity entity) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, materialOutMapper._addEntity(entity));
    }


    /**
     * 更新 参数 map里的key为属性名（字段首字母小写） value为要插入的key的value
     */
    public Object update(Map<String, String> params) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, materialOutMapper._update(params));
    }

    /**
     * 更新 参数 map里的key为属性名（字段首字母小写） value为要插入的key的value
     */
    public Object update(MaterialOutEntity MaterialOutEntity) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, materialOutMapper._updateEntity(MaterialOutEntity));
    }

    /**
     * 逻辑删除
     */
    public Object delete(String id) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, materialOutMapper._delete(id));
    }

    /**
     * 物理删除
     */
    public Object _deleteForce(String id) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, materialOutMapper._deleteForce(id));
    }


    public List editById(String id) {
        List<MaterialOutEntity> list = materialOutMapper._editById(id);
        return list;
    }

    @Transactional
    public Object subMaterial(MaterialEntity materialEntity ,MaterialOutEntity materialOutEntity,MaterialOutEntity materialOutEntity1) {
        materialEntity.setAmount(materialEntity.getAmount().subtract(materialOutEntity.getAmount()));
        materialMapper._updateEntity(materialEntity);
        materialOutMapper._addEntity(materialOutEntity1);
        return new RestResult();
    }

    public int getByProcessId(String processParentId) {
        return  materialOutMapper.queryByProcessId(processParentId);
    }


    @Transactional
    public Object addMaterial(MaterialOutEntity materialOutEntity, HttpServletRequest request) {
        MaterialEntity materialEntity = materialMapper._get(materialOutEntity.getMaterialName());
        BigDecimal amount = materialOutEntity.getAmount();
        if (materialEntity.getAmount().compareTo(amount) == -1 ) {
            return new RestResult(RestResult.ERROR_CODE, RestResult.ERROR_MSG, "数量不足");
        }
        materialEntity.setAmount(materialEntity.getAmount().subtract(amount));
        materialMapper._updateEntity(materialEntity);
        String id = UUID.randomUUID().toString();
        materialOutEntity.setId(id);
        materialOutEntity.setMaterialId(materialEntity.getId());
        materialOutEntity.setMaterialName(materialEntity.getMaterialName());
        materialOutEntity.setProcessStatus(0);
        SysUserEntity user = (SysUserEntity) request.getSession().getAttribute("user");
        materialOutEntity.setUserId(user.getId());
        materialOutEntity.setGoodsId(materialOutEntity.getProductName());
        GoodsEntity goodsEntity = goodsMapper._get(materialOutEntity.getProductName());
        materialOutEntity.setProductName(goodsEntity.getAllName());
        materialOutEntity.setStatus(0);
        materialOutEntity.setUseAmount(BigDecimal.ZERO);
        materialOutEntity.setConsumablesIs(materialEntity.getConsumablesIs());
        ProcessEntity processEntity = new ProcessEntity();
        processEntity.setMaterialOutId(id);
        processEntity.setProductName(goodsEntity.getAllName());
        processEntity.setType(1);
        processEntity.setFlowStatus(0);
        processEntity.setAmount(amount.intValue());
        processEntity.setProcessingPersonnel("");
        processEntity.setFee(BigDecimal.ZERO);
        processEntity.setAdd_fee(BigDecimal.ZERO);
        processEntity.setUserId(user.getId());
        processEntity.setEndTime(new Date());
        processEntity.setIsQuality(0);
        processEntity.setIsStandard(0);
        processMapper._addEntity(processEntity);
        materialOutMapper._addEntity(materialOutEntity);
        return new RestResult();
    }
}
