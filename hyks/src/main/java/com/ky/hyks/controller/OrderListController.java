package com.ky.hyks.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ky.hyks.entity.*;
import com.ky.hyks.logUtil.Log;
import com.ky.hyks.mapper.CompanyOrderMapper;
import com.ky.hyks.mapper.OrderInfoMapper;
import com.ky.hyks.mapper.OrderListInfoMapper;
import com.ky.hyks.mapper.OrderListMapper;
import com.ky.hyks.mybatis.PagerResult;
import com.ky.hyks.mybatis.RestResult;
import com.ky.hyks.service.OrderListService;
import com.ky.hyks.utils.HttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.*;


@RestController
@RequestMapping("/ky-supplier/orderList")
public class OrderListController {

    private static final Logger logger = LoggerFactory.getLogger(OrderListController.class);

    @Autowired
    OrderListService orderListService;
    @Autowired
    CompanyOrderMapper companyOrderMapper;
    @Autowired
    OrderListInfoMapper orderListInfoMapper;
    @Autowired
    OrderInfoMapper orderInfoMapper;
    @Autowired
    OrderListMapper orderListMapper;

    /**
     * 查询全部数据不分页
     */
    @RequestMapping(value = "queryByParams", method = RequestMethod.GET, produces = "application/json;UTF-8")
    public Object queryByParams(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The OrderListController queryByParams method params are {}", params);
        return orderListService.queryAll(params);
    }

    @RequestMapping(value = "queryById", method = RequestMethod.GET)
    public Object queryById(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The PersonController queryByParams method params are {}", params);
        return orderListService.get(params);
    }

    /**
     * 新增OR更新数据
     */
    @Log(description = "角色管理新增，修改操作", module = "角色管理")
    @RequestMapping(value = "saveOrUpdate", method = RequestMethod.POST, produces = "application/json;UTF-8")
    public Object saveOrUpdate(@RequestBody String body) {
        logger.info("The OrderListController saveOrUpdate method params are {}", body);
        OrderListEntity orderListEntity = JSONObject.parseObject(body, OrderListEntity.class);
        if (StringUtils.isNotEmpty(orderListEntity.getId())) {
            return orderListService.update(orderListEntity);
        } else {
            orderListEntity.setId(UUID.randomUUID().toString());
            return orderListService.add(orderListEntity);
        }
    }

    /**
     * 根据条件分页查询
     */
    @RequestMapping(value = "/queryPage", method = RequestMethod.GET)
    public Object queryPage(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The OrderListController queryPage method params are {}", params);
        params.put("currentPage", params.get("page"));
        params.put("pageSize", params.get("rows"));
        return orderListService.queryPage(params);
    }


    @RequestMapping(value = "/select", method = RequestMethod.GET)
    public OrderListEntity select(String id) {
        logger.info("The OrderListController queryPage method params are {}", id);
        RestResult restResult = orderListService._get(id);
        OrderListEntity data = (OrderListEntity) restResult.getData();
        return data;
    }

    /**
     * 删除
     */
    @SuppressWarnings("rawtypes")
    @Log(description = "角色管理删除操作", module = "角色管理")
    @RequestMapping(value = "/deleteOne", method = RequestMethod.GET)
    public Object deleteOne(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The OrderListController delete method params is {}", params);
        return orderListService.delete(params.get("id").toString());
    }

    /**
     * 删除多个
     */
    @Log(description = "角色管理删除操作", module = "角色管理")
    @RequestMapping(value = "deleteForce", method = RequestMethod.GET)
    public Object deleteMoney(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The OrderListController deleteForce method params is {}", params);
        String id = params.get("id").toString();
        if (id.contains(",")) {
            String[] split = id.split(",");
            for (int i = 0; i < split.length; i++) {
                List<OrderListInfoEntity> orderListInfoEntityList = orderListInfoMapper.queryByListId(split[i]);
                if (orderListInfoEntityList.size() > 0) {
                    for (OrderListInfoEntity orderListInfoEntity : orderListInfoEntityList) {
                        OrderInfoEntity orderInfoEntity = orderInfoMapper._get(orderListInfoEntity.getOrderInfoId());
                        orderInfoEntity.setState(0);
                        orderInfoMapper._updateEntity(orderInfoEntity);
                        companyOrderMapper.deleteByOrderId(orderListInfoEntity.getOrderInfoId());
                    }
                }
                orderListService._deleteForce(split[i]);
                orderListInfoMapper.deleteByListId(split[i]);
            }
        } else {
            List<OrderListInfoEntity> orderListInfoEntityList = orderListInfoMapper.queryByListId(params.get("id").toString());
            if (orderListInfoEntityList.size() > 0) {
                for (OrderListInfoEntity orderListInfoEntity : orderListInfoEntityList) {
                    OrderInfoEntity orderInfoEntity = orderInfoMapper._get(orderListInfoEntity.getOrderInfoId());
                    orderInfoEntity.setState(0);
                    orderInfoMapper._updateEntity(orderInfoEntity);
                    companyOrderMapper.deleteByOrderId(orderListInfoEntity.getOrderInfoId());
                }
            }
            orderListService._deleteForce(params.get("id").toString());
            orderListInfoMapper.deleteByListId(params.get("id").toString());
        }
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG);
    }


    @Log(description = "成本管理新增/删除操作", module = "成本管理")
    @RequestMapping(value = "/save", method = RequestMethod.POST, produces = "application/json;UTF-8")
    public Object save(String orderListEntities, HttpServletRequest request) {
        logger.info("The OrderListController saveOrUpdate method params are {}", orderListEntities);
        SysUserEntity user = (SysUserEntity) request.getSession().getAttribute("user");
        List<OrderListEntity> orderListEntityList = JSON.parseArray(orderListEntities, OrderListEntity.class);
        for (OrderListEntity orderListEntity : orderListEntityList) {
            if (StringUtils.isNotEmpty(orderListEntity.getId())) {
                orderListService.update(orderListEntity);
            } else {
                orderListEntity.setId(UUID.randomUUID().toString());
                orderListService.add(orderListEntity);
            }
        }
        return new RestResult();
    }


    @Log(description = "发放录入提交操作", module = "人员管理")
    @RequestMapping(value = "/doSubmit", method = RequestMethod.GET)
    public Object doSubmit(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The OrderListController deleteForce method params is {}", params);
        OrderListEntity orderListEntity = new OrderListEntity();
        String id = UUID.randomUUID().toString();
        orderListEntity.setId(id);
        orderListEntity.setListName(params.get("listName").toString());
        orderListEntity.setUserName(params.get("userName").toString());
        orderListEntity.setUserCell(params.get("userCell").toString());
        orderListEntity.setTalkNum(Integer.valueOf(params.get("talkNum").toString()));
        orderListService.add(orderListEntity);
        String orderInfoId = params.get("orderInfoId").toString();
        if (orderInfoId.contains(",")) {
            String[] split = orderInfoId.split(",");
            for (int i = 0; i < split.length; i++) {
                Map map = new HashMap();
                map.put("orderInfoId", split[i]);
                map.put("orderListId", id);
                List<OrderListInfoEntity> orderListInfoEntityList = orderListInfoMapper._queryRelation(map);
                if (orderListInfoEntityList.size() < 1) {
                    OrderListInfoEntity orderListInfoEntity = new OrderListInfoEntity();
                    orderListInfoEntity.setId(UUID.randomUUID().toString());
                    orderListInfoEntity.setOrderInfoId(split[i]);
                    orderListInfoEntity.setOrderListId(id);
                    OrderInfoEntity orderInfoEntity = orderInfoMapper._get(split[i]);
                    orderInfoEntity.setState(1);
                    orderInfoMapper._updateEntity(orderInfoEntity);
                    orderListInfoMapper._addEntity(orderListInfoEntity);
                } else {
                    return new RestResult(RestResult.ERROR_CODE, RestResult.ERROR_MSG, "第" + i + "个客商已询价");
                }
            }
        } else {
            Map map = new HashMap();
            map.put("orderInfoId", orderInfoId);
            map.put("orderListId", id);
            List<OrderListInfoEntity> orderListInfoEntityList = orderListInfoMapper._queryRelation(map);
            if (orderListInfoEntityList.size() < 1) {
                OrderListInfoEntity orderListInfoEntity = new OrderListInfoEntity();
                orderListInfoEntity.setId(UUID.randomUUID().toString());
                orderListInfoEntity.setOrderInfoId(orderInfoId);
                orderListInfoEntity.setOrderListId(id);
                OrderInfoEntity orderInfoEntity = orderInfoMapper._get(orderInfoId);
                orderInfoEntity.setState(1);
                orderInfoMapper._updateEntity(orderInfoEntity);
                orderListInfoMapper._addEntity(orderListInfoEntity);
            } else {
                return new RestResult(RestResult.ERROR_CODE, RestResult.ERROR_MSG, "该客商已询价");
            }
        }
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG);
    }

    @Log(description = "信息发布", module = "角色管理")
    @RequestMapping(value = "publishOrder", method = RequestMethod.GET)
    public Object publishOrder(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The OrderListController publishOrder method params is {}", params);
        String id = params.get("id").toString();
        if (id.contains(",")) {
            String[] split = id.split(",");
            for (int i = 0; i < split.length; i++) {
                OrderListEntity orderListEntity = orderListMapper._get(split[i]);
                orderListEntity.setState(1);
                orderListMapper._updateEntity(orderListEntity);
            }
        } else {
            OrderListEntity orderListEntity = orderListMapper._get(params.get("id").toString());
            orderListEntity.setState(1);
            orderListMapper._updateEntity(orderListEntity);
        }
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG);
    }
}
