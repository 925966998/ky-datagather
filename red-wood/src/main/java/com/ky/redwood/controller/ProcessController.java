package com.ky.redwood.controller;

import com.alibaba.fastjson.JSONObject;
import com.ky.redwood.entity.*;
import com.ky.redwood.entity.ProcessEntity;
import com.ky.redwood.logUtil.Log;
import com.ky.redwood.mapper.ProcessFlowMapper;
import com.ky.redwood.mapper.ProcessMapper;
import com.ky.redwood.mybatis.RestResult;
import com.ky.redwood.service.MaterialOutService;
import com.ky.redwood.service.ProcessParentService;
import com.ky.redwood.service.ProcessService;
import com.ky.redwood.service.ProductService;
import com.ky.redwood.utils.HttpUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/ky-redwood/process")
public class ProcessController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    ProcessService processService;
    @Autowired
    ProcessFlowMapper processFlowMapper;

    @Autowired
    MaterialOutService materialOutService;

    @Autowired
    ProcessMapper processMapper;

    @Autowired
    ProcessParentService processParentService;

    @Autowired
    ProductService productService;

    /**
     * 根据条件查询数据（不分页）
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/queryByParams", method = RequestMethod.GET)
    public Object queryByParams(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The ProcessController queryByParams method params are {}", params);
        return processService.queryAll(params);
    }

    /**
     * 根据Id查询数据
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/queryById", method = RequestMethod.GET)
    public Object queryById(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The ProcessController queryById method params are {}", params);
        return processService.get(params);
    }


    /**
     * 根据Id查询数据
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/getById", method = RequestMethod.GET)
    public Object getById(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The ProcessController getById method params are {}", params);
        System.out.println(params.get("id").toString());
        return processMapper._getById(params.get("id").toString());
    }


    /**
     * 新增OR更新数据
     */
    @Log(description = "材料定制新增,修改操作", module = "材料定制")
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST, consumes = "application/json")
    public Object saveOrUpdate(@RequestBody String body, HttpServletRequest request) {
        logger.info("The ProcessController saveOrUpdate method params are {}", body);
        ProcessEntity processEntity = JSONObject.parseObject(body, ProcessEntity.class);
        int amount = materialOutService.getByProcessId(processEntity.getMaterialOutId());
        if (amount < processEntity.getAmount()) {
            return new RestResult(RestResult.ERROR_CODE, RestResult.ERROR_MSG, "数量不足");
        }
        if (StringUtils.isNotEmpty(processEntity.getId())) {
            processEntity.setUpdateTime(new Date());
            return processService.update(processEntity, processEntity.getAmount());
        } else {
            processEntity.setId(UUID.randomUUID().toString());
            // 获取当前登录用户
            SysUserEntity user = (SysUserEntity) request.getSession().getAttribute("user");
            processEntity.setUserId(user.getId());
            processEntity.setEndTime(new Date());
            processEntity.setFlowStatus(0);
            processEntity.setType(1);
            processEntity.setIsStandard(0);
            processEntity.setIsQuality(0);
            return processService.add(processEntity, processEntity.getAmount());
        }
    }


  /*  @Log(description = "半成品入库,修改操作", module = "半成品入库")
    @RequestMapping(value = "/halfSaveOrUpdate", method = RequestMethod.POST, consumes = "application/json")
    public Object halfSaveOrUpdate(@RequestBody String body, HttpServletRequest request) {
        logger.info("The ProcessController halfSaveOrUpdate method params are {}", body);
        ProcessEntity processEntity = JSONObject.parseObject(body, ProcessEntity.class);
        if (StringUtils.isNotEmpty(processEntity.getId())) {
            processEntity.setUpdateTime(new Date());
            ProcessParentEntity processParentEntity = new ProcessParentEntity();
            processParentEntity.setProcessName(processEntity.getProcessName());
            processParentService.update(processParentEntity);
            return processService.update(processEntity);
        } else {
            processEntity.setId(UUID.randomUUID().toString());
            // 获取当前登录用户
            SysUserEntity user = (SysUserEntity) request.getSession().getAttribute("user");
            processEntity.setUserId(user.getId());
            processEntity.setEndTime(new Date());
            processEntity.setFlowStatus(0);
            processEntity.setType(0);
            String ProcessParentId = UUID.randomUUID().toString();
            processEntity.setProcessParentId(ProcessParentId);
            ProcessParentEntity processParentEntity = new ProcessParentEntity();
            processParentEntity.setProcessName(processEntity.getProcessName());
            processParentEntity.setType(0);
            processParentEntity.setId(ProcessParentId);
            processParentService.add(processParentEntity);
            return processService.add(processEntity);
        }
    }*/

    /**
     * 逻辑删除
     */
    @SuppressWarnings("rawtypes")
    @Log(description = "用户管理逻辑删除操作", module = "物料管理")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public Object delete(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The ProcessController delete method params is {}", params);
        return processService.delete(params.get("id").toString());
    }

    /**
     * 物理删除
     */
    @Log(description = "用户管理物理删除操作", module = "物料管理")
    @RequestMapping(value = "/deleteForce", method = RequestMethod.GET)
    public Object deleteForce(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The ProcessController deleteForce method params is {}", params);
        String id = params.get("id").toString();
        if (id.contains(",")) {
            String[] split = id.split(",");
            for (int i = 0; i < split.length; i++) {
                processService._deleteForce(split[i]);
            }
        } else {
            processService._deleteForce(params.get("id").toString());
        }
        return new RestResult();
    }

    /**
     * 根据条件分页查询
     */
    @RequestMapping(value = "/queryPage", method = RequestMethod.GET)
    public Object queryPage(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        params.put("currentPage", params.get("page"));
        params.put("pageSize", params.get("rows"));
        params.put("typePage", "queryPage");
        logger.info("The ProcessController queryPage method params are {}", params);
        return processService.queryPage(dealTimeFormat(params));
    }

    @RequestMapping(value = "/queryPageType", method = RequestMethod.GET)
    public Object queryPageType(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        params.put("currentPage", params.get("page"));
        params.put("pageSize", params.get("rows"));
        params.put("isQuality", 0);
        params.put("isStandard",0);

        params.put("typePage", "queryPageType");
        logger.info("The ProcessController queryPageType method params are {}", params);
        return processService.queryPage(dealTimeFormat(params));
    }

    private Map dealTimeFormat(Map params) {
        if (StringUtils.isNotEmpty(MapUtils.getString(params, "startTime"))) {
            params.put("startTime", params.get("startTime") + " 00:00:00");
        }
        if (StringUtils.isNotEmpty(MapUtils.getString(params, "endTime"))) {
            params.put("endTime", params.get("endTime") + " 23:59:59");
        }
        return params;
    }

    @RequestMapping(value = "/queryPageHalf", method = RequestMethod.GET)
    public Object queryPageHalf(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        params.put("currentPage", params.get("page"));
        params.put("pageSize", params.get("rows"));
        params.put("typePage", "queryPageHalf");
        logger.info("The ProcessController queryPageHalf method params are {}", params);
        return processService.queryPage(params);
    }

    /**
     * 继续加工
     */
    @Log(description = "继续加工", module = "材料加工管理")
    @RequestMapping(value = "/doSubmitAudit", method = RequestMethod.POST, consumes = "application/json")
    public Object doSubmitAudit(@RequestBody String body, HttpServletRequest request) throws ParseException {
        logger.info("The ProcessController saveOrUpdate method params are {}", body);
        ProcessEntity processEntity = JSONObject.parseObject(body, ProcessEntity.class);
        Map processMap = new HashMap();
        processMap.put("id", processEntity.getId());
        ProcessEntity processEntity1 = processService.queryProcess(processMap);
        processEntity.setId(UUID.randomUUID().toString());
        processEntity.setProductName(processEntity1.getProductName());
        processEntity.setMaterialOutId(processEntity1.getMaterialOutId());
        processEntity.setType(processEntity1.getType());
        processEntity.setIsQuality(0);
        processEntity.setIsStandard(0);
        processEntity.setAmount(processEntity1.getAmount());
        processEntity.setAdd_fee(BigDecimal.ZERO);
        // 获取当前登录用户
        SysUserEntity user = (SysUserEntity) request.getSession().getAttribute("user");
        processEntity.setUserId(user.getId());
        if (processEntity.getProcessingPersonnel().isEmpty()) {
            processEntity.setProcessingPersonnel(user.getUserName());
        }
        processEntity.setEndTime(new Date());
        /*
        if (processEntity.getFlowStatus() == 8){
            ProductEntity productEntity = new ProductEntity();
            productEntity.setId(UUID.randomUUID().toString());
            productEntity.setProcessId(processEntity.getId());
            productEntity.setProductStatus(0);
            productService.add(productEntity);
        }
        */
        processService.update(processEntity1);
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, processService.add(processEntity));
    }

    /**
     * 加价
     */
    @Log(description = "加价", module = "加价管理")
    @RequestMapping(value = "/saveAddFee", method = RequestMethod.POST, consumes = "application/json")
    public Object saveAddFee(@RequestBody String body, HttpServletRequest request) throws ParseException {
        logger.info("The ProcessController saveOrUpdate method params are {}", body);
        ProcessEntity processEntity = JSONObject.parseObject(body, ProcessEntity.class);
        Map processMap = new HashMap();
        processMap.put("id", processEntity.getId());
        ProcessEntity processEntity1 = processService.queryProcess(processMap);
        processEntity1.setAdd_fee(processEntity.getAdd_fee().add(processEntity1.getAdd_fee()));
        return processService.update(processEntity1);
    }

    /**
     * 根据Id查询数据
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/querySelectId", method = RequestMethod.GET)
    public List<ProcessEntity> querySelectId(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The ProcessController queryById method params are {}", params);
        ProcessEntity processEntity = (ProcessEntity) processService.get(params);
        return processMapper.querySelectId(processEntity.getProductName());
    }

    /**
     * 查询成品
     */
    @RequestMapping(value = "/queryProcessPage", method = RequestMethod.GET)
    public Object queryProcessPage(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        params.put("currentPage", params.get("page"));
        params.put("pageSize", params.get("rows"));
        params.put("typePage", "queryProcessPage");
        params.put("flowStatus", 8);
        logger.info("The ProcessController queryPage method params are {}", params);
        return processService.queryPage(params);
    }

    /**
     * 添加半成品
     */
    @Log(description = "添加半成品", module = "半成品加工管理")
    @RequestMapping(value = "/doSubSemiprocessSum", method = RequestMethod.POST, consumes = "application/json")
    public Object doSubSemiprocessSum(@RequestBody String body, HttpServletRequest request) throws ParseException {
        logger.info("The ProcessController saveOrUpdate method params are {}", body);
        ProcessEntity processEntity = JSONObject.parseObject(body, ProcessEntity.class);
        processEntity.setId(UUID.randomUUID().toString());
        processEntity.setMaterialOutId(UUID.randomUUID().toString());
        processEntity.setType(0);
        processEntity.setIsQuality(0);
        processEntity.setIsStandard(0);
        processEntity.setFee(BigDecimal.ZERO);
        processEntity.setAdd_fee(BigDecimal.ZERO);
        // 获取当前登录用户
        SysUserEntity user = (SysUserEntity) request.getSession().getAttribute("user");
        processEntity.setUserId(user.getId());
        processEntity.setProcessingPersonnel(user.getUserName());
        processEntity.setEndTime(new Date());
        /*
        if (processEntity.getFlowStatus() == 8){
            ProductEntity productEntity = new ProductEntity();
            productEntity.setId(UUID.randomUUID().toString());
            productEntity.setProcessId(processEntity.getId());
            productEntity.setProductStatus(0);
            productService.add(productEntity);
        }
        */
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, processService.add(processEntity));
    }

    /**
     * 查看是否已加工
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/queryProcess", method = RequestMethod.GET)
    public List queryProcess(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The ProcessController queryProcess method params are {}", params);
        List<ProcessEntity> processEntities = processService.queryProcessByName(params);
        return processEntities;
    }

    /**
     * 物理删除
     */
    @Log(description = "库存质检通过操作", module = "库存质检管理")
    @RequestMapping(value = "/addQuality", method = RequestMethod.GET)
    public Object addQuality(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The ProductController deleteForce method params is {}", params);
        String id = params.get("id").toString();
        ProcessEntity processEntity = new ProcessEntity();
        if (id.contains(",")) {
            String[] split = id.split(",");
            for (int i = 0; i < split.length; i++) {
                processEntity  = (ProcessEntity) processService.get(split[i]);
                processEntity.setIsQuality(1);
                processService.update(processEntity);
            }
        } else {
            processEntity  = (ProcessEntity) processService.get(id);
            processEntity.setIsQuality(1);
            processService.update(processEntity);
        }
        return new RestResult();
    }

    /**
     * 物理删除
     */
    @Log(description = "库存质检不通过操作", module = "库存质检管理")
    @RequestMapping(value = "/updateQuality", method = RequestMethod.GET)
    public Object updateQuality(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The ProductController deleteForce method params is {}", params);
        String id = params.get("id").toString();
        ProcessEntity processEntity = new ProcessEntity();
        if (id.contains(",")) {
            String[] split = id.split(",");
            for (int i = 0; i < split.length; i++) {
                processEntity  = (ProcessEntity) processService.get(split[i]);
                processEntity.setFlowStatus(7);
                processService.update(processEntity);
            }
        } else {
            processEntity  = (ProcessEntity) processService.get(id);
            processEntity.setFlowStatus(7);
            processService.update(processEntity);
        }
        return new RestResult();
    }

    /**
     * 查询库存质量审核页面
     */
    @RequestMapping(value = "/queryQualityPage", method = RequestMethod.GET)
    public Object queryQualityPage(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        params.put("currentPage", params.get("page"));
        params.put("pageSize", params.get("rows"));
        params.put("typePage", "queryProcessPage");
        params.put("flowStatus", 8);
        params.put("isQuality", 0);
        params.put("isStandard", 0);
        logger.info("The ProcessController queryPage method params are {}", params);
        return processService.queryPage(params);
    }

    /**
     * 查询库存审核页面
     */
    @RequestMapping(value = "/queryStandardPage", method = RequestMethod.GET)
    public Object queryStandardPage(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        params.put("currentPage", params.get("page"));
        params.put("pageSize", params.get("rows"));
        params.put("typePage", "queryProcessPage");
        params.put("flowStatus", 8);
        params.put("isQuality", 1);
        params.put("isStandard", 0);
        logger.info("The ProcessController queryPage method params are {}", params);
        return processService.queryPage(params);
    }

    /**
     * 物理删除
     */
    @Log(description = "库存质检通过操作", module = "库存质检管理")
    @RequestMapping(value = "/addStandard", method = RequestMethod.GET)
    public Object addStandard(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The ProductController deleteForce method params is {}", params);
        String id = params.get("id").toString();
        ProcessEntity processEntity = new ProcessEntity();
        if (id.contains(",")) {
            String[] split = id.split(",");
            for (int i = 0; i < split.length; i++) {
                processEntity  = (ProcessEntity) processService.get(split[i]);
                processEntity.setIsStandard(1);
                processService.update(processEntity);
            }
        } else {
            processEntity  = (ProcessEntity) processService.get(id);
            processEntity.setIsStandard(1);
            processService.update(processEntity);
        }
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(UUID.randomUUID().toString());
        productEntity.setProcessId(processEntity.getId());
        productEntity.setProductStatus(0);
        productService.add(productEntity);
        return new RestResult();
    }

    /**
     * 物理删除
     */
    @Log(description = "库存质检不通过操作", module = "库存质检管理")
    @RequestMapping(value = "/updateStandard", method = RequestMethod.GET)
    public Object updateStandard(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The ProductController deleteForce method params is {}", params);
        String id = params.get("id").toString();
        ProcessEntity processEntity = new ProcessEntity();
        if (id.contains(",")) {
            String[] split = id.split(",");
            for (int i = 0; i < split.length; i++) {
                processEntity  = (ProcessEntity) processService.get(split[i]);
                processEntity.setIsQuality(0);
                processService.update(processEntity);
            }
        } else {
            processEntity  = (ProcessEntity) processService.get(id);
            processEntity.setIsQuality(0);
            processService.update(processEntity);
        }
        return new RestResult();
    }


    /**
     * 财务报表分析加工阶段
     */
    @RequestMapping(value = "/queryFinancial", method = RequestMethod.GET)
    public Object queryFinancial(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        params.put("currentPage", params.get("page"));
        params.put("pageSize", params.get("rows"));
        params.put("typePage", "queryFinancial");
        logger.info("The ProcessController queryPage method params are {}", params);
        List<ProcessEntity> processEntityList = (List<ProcessEntity>) processService.queryPage(dealTimeFormat(params));
        for (ProcessEntity processEntity : processEntityList) {
            if (processEntity.getFlowStatus() == 0){
                processEntity.setProcessFlowName("未加工");
            }
        }
        return processEntityList;
    }
}
