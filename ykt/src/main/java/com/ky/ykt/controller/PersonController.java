package com.ky.ykt.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ky.ykt.entity.*;
import com.ky.ykt.excle.ExcelHead;
import com.ky.ykt.excle.ExcelStyle;
import com.ky.ykt.excle.ExcelUtils;
import com.ky.ykt.excle.ExportExcel;
import com.ky.ykt.logUtil.Log;
import com.ky.ykt.mapper.*;
import com.ky.ykt.mybatis.RestResult;
import com.ky.ykt.service.PersonService;
import com.ky.ykt.service.PersonUploadService;
import com.ky.ykt.utils.HttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 */
@RestController
@RequestMapping("/ky-ykt/person")
public class PersonController {

    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);

    @Autowired
    PersonService personService;
    @Autowired
    PersonMapper personMapper;
    @Autowired
    ProjectDetailMapper projectDetailMapper;
    @Autowired
    ProjectMapper projectMapper;
    @Autowired
    PersonUploadMapper personUploadMapper;
    @Autowired
    DepartmentMapper departmentMapper;
    @Autowired
    PersonUploadService personUploadService;
    @Autowired
    AreasMapper areasMapper;

    /**
     * 根据条件查询数据（不分页）
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "queryByParams", method = RequestMethod.GET)
    public Object queryByParams(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        params = setDepartmentIdForMap(request, params);
        logger.info("The PersonController queryByParams method params are {}", params);
        return personService.queryAll(params);
    }

    /**
     * 根据Id查询数据
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "queryById", method = RequestMethod.GET)
    public Object queryById(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The PersonController queryByParams method params are {}", params);
        return personService.queryByAll(params);
    }

    /**
     * 新增OR更新数据
     */
    @Log(description = "人员管理新增，修改操作", module = "人员管理")
    @RequestMapping(value = "saveOrUpdate", method = RequestMethod.POST, produces = "application/json;UTF-8")
    public Object saveOrUpdate(@RequestBody String body, HttpServletRequest request) {
        logger.info("The PersonController saveOrUpdate method params are {}", body);
        PersonEntity personEntity = JSONObject.parseObject(body, PersonEntity.class);
        if (personEntity.getId() != null && personEntity.getId().length() > 0) {
            personService.update(personEntity);
        } else {
            SysUserEntity user = (SysUserEntity) request.getSession().getAttribute("user");
            //单个添加
            personEntity.setId(UUID.randomUUID().toString());
            personEntity.setStatus("3");
            personEntity.setProjectId(user.getDepartmentId());
            //获取当前操作人信息
            personEntity.setDepartmentId(user.getDepartmentId());
            personEntity.setUserId(user.getId());
            personService.add(personEntity);
        }
        return new RestResult();
    }

    /**
     * 逻辑删除
     */
    @SuppressWarnings("rawtypes")
    @Log(description = "人员管理逻辑删除操作", module = "人员管理")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public Object delete(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The PersonController delete method params is {}", params);
        PersonUploadEntity personUploadEntity = personUploadMapper._queryPersonId(params.get("id").toString());
        String personUploadEntityId = personUploadEntity.getId();
        personUploadService.delete(personUploadEntityId);
        return personService.delete(params.get("id").toString());
    }

    /**
     * 物理删除
     */
    @Log(description = "人员管理物理删除操作", module = "人员管理")
    @RequestMapping(value = "deleteForce", method = RequestMethod.GET)
    public Object deleteForce(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The PersonController deleteForce method params is {}", params);
        String id = params.get("id").toString();
        if (id.contains(",")) {
            String[] split = id.split(",");
            for (int i = 0; i < split.length; i++) {
                personService._deleteForce(split[i]);
            }
        } else {
            personService._deleteForce(params.get("id").toString());
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
        logger.info("The PersonController queryPage method params are {}", params);
        params = setDepartmentIdForMap(request, params);
        /*
        List<ProjectDetailEntity> projectDetailEntities = projectDetailMapper._queryPage(params);
        if (projectDetailEntities.size() > 0) {
            params.put("projectId", projectDetailEntities.get(0).getId());
        }
        */
        return personService.queryPage(params);
        //return personUploadService.queryPage(params);
    }

    @Log(description = "银行回执提交业务部门修复数据", module = "银行管理")
    @RequestMapping(value = "/submitToBuss", method = RequestMethod.POST)
    public Object submitToBuss() {
        Map map = new HashMap();
        map.put("status", "2");
        List<PersonEntity> personEntities = personMapper._queryAll(map);
        for (PersonEntity personEntity :
                personEntities) {
            personService.submitToBuss(personEntity.getId());
        }
        return new RestResult();
    }

    @Log(description = "人员管理再次提交审核操作", module = "人员管理")
    @RequestMapping(value = "/doSubmitAudit", method = RequestMethod.POST)
    @Transactional
    public Object doSubmitAudit(HttpServletRequest request) {
        Map map = new HashMap();
        map.put("status", "3");
        List<PersonEntity> personEntities = personMapper._queryAll(map);
        SysUserEntity user = (SysUserEntity) request.getSession().getAttribute("user");
        String projectDetailId = UUID.randomUUID().toString();
        BigDecimal amount = new BigDecimal("0");
        for (PersonEntity personEntity : personEntities) {
            amount = amount.add(new BigDecimal(personEntity.getGrantAmount()).setScale(2, BigDecimal.ROUND_HALF_UP));
            personService.doSubmitAudit(personEntity.getId(), projectDetailId);
        }
        logger.info("The PersonController doSubmitAudit success");
        ProjectDetailEntity projectDetailEntity1 = projectDetailMapper._get(personEntities.get(0).getProjectId());
        projectDetailEntity1.setPaymentAmount(projectDetailEntity1.getPaymentAmount().subtract(amount).setScale(2, BigDecimal.ROUND_HALF_UP));
        projectDetailMapper._updateEntity(projectDetailEntity1);
        logger.info("update parent project PaymentAmount success {}", projectDetailEntity1.getPaymentAmount().subtract(amount).setScale(2, BigDecimal.ROUND_HALF_UP));
        ProjectEntity projectEntity = projectMapper._get(projectDetailEntity1.getProjectId());
        ProjectDetailEntity projectDetailEntity = new ProjectDetailEntity();
        projectDetailEntity.setId(projectDetailId);
        projectDetailEntity.setParentId(projectDetailEntity1.getId());
        projectDetailEntity.setTotalAmount(amount);
        projectDetailEntity.setPaymentAmount(amount);
        projectDetailEntity.setProjectId(projectDetailEntity1.getProjectId());
        projectDetailEntity.setProjectName(projectDetailEntity1.getProjectName());
        projectDetailEntity.setStartTime(new Date());
        projectDetailEntity.setOperUser(user.getId());
        projectDetailEntity.setOperDepartment(user.getDepartmentId());

        projectDetailEntity.setPaymentDepartment(projectEntity.getOperDepartment());
        projectDetailEntity.setState(0);
        projectDetailMapper._addEntity(projectDetailEntity);
        logger.info("Create new projectDetailEntity success {}", JSON.toJSONString(projectDetailEntity));

        return new RestResult();
    }

    @Log(description = "人员管理审核操作", module = "人员管理")
    @RequestMapping(value = "/audit", method = RequestMethod.POST)
    public Object audit(@RequestBody String body) {
        logger.info("The PersonController audit method params are {}", body);
        Map params = (Map) JSON.parse(body);
        return personService.audit(params);
    }

    @Log(description = "人员管理推送银行进行发放资金操作", module = "人员管理")
    @RequestMapping(value = "/push", method = RequestMethod.POST)
    public Object push(@RequestBody String id) {
        logger.info("The push push method params are {}", id);
        if (id.contains(",")) {
            if (id.contains(",")) {
                String[] split = id.split(",");
                for (int i = 0; i < split.length; i++) {
                    personService.push(split[i]);
                }
            } else {
                personService.push(id);
            }
        }
        return null;
    }


    @RequestMapping(value = "/export", method = RequestMethod.GET)
    protected void export(HttpServletRequest request, HttpServletResponse response) {
        Map params = HttpUtils.getParams(request);
        Map map = this.fieldExport(params);
        String[] header = (String[]) map.get("header");
        List<String[]> data = (List<String[]>) map.get("data");
        ExcelStyle style = (ExcelStyle) map.get("style");
        try {
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String((style.getXlsName() + ".xls").getBytes(), "iso-8859-1"));
            OutputStream out = response.getOutputStream();
            ExportExcel.export(header, data, style, out);
        } catch (Exception e) {
            logger.error("exportExcel error:{}", e);
        }
    }


    public Map fieldExport(Map params) {
        Map resultMap = new HashMap();
        ExcelStyle style = new ExcelStyle();
        List<String[]> data = new ArrayList();
        ProjectDetailEntity projectDetailEntity = projectDetailMapper._get(params.get("id").toString());
        ProjectEntity projectEntity = projectMapper._get(projectDetailEntity.getProjectId());
        Map map = new HashMap();
        map.put("projectId", projectDetailEntity.getId());
        List<PersonEntity> entities = personMapper._queryAll(map);
        SimpleDateFormat dfs = new SimpleDateFormat("yyyyMMddHHmmss");// 设置日期格式
        String tStamp = dfs.format(new Date());
        style.setColumnWidth(25);
        style.setSheetName("导出");
        style.setXlsName("发放人员信息表_" + tStamp);
        for (PersonEntity entity : entities) {
            data.add(new String[]{
                    entity.getName(),
                    entity.getPhone(),
                    entity.getIdCardNo(),
                    entity.getBankCardNo(),
                    projectEntity.getProjectName(),
                    entity.getGrantAmount(),
                    entity.getCounty(),
                    entity.getAddress(),
                    projectDetailEntity.getId()
            });
        }
        resultMap.put("header",
                new String[]{"姓名", "手机号", "身份证号", "社保卡号", "项目资金名称", "发放金额", "所属区县", "详细地址", "流水号", "回执状态"});
        resultMap.put("data", data);
        resultMap.put("style", style);
        return resultMap;


    }


    @RequestMapping(value = "/import", method = RequestMethod.POST)
    @Transactional
    public RestResult importExcel(@RequestParam MultipartFile file, HttpServletRequest request) {
        logger.info("The file is {}", file);
        //String projectId = request.getParameter("projectId");
        if (file == null || file.getName().equals("") || file.getSize() <= 0) {
            return new RestResult(40000, RestResult.ERROR_MSG, "文件不合法,请检查文件是否为Excel文件");
        }

        String fileName = file.getOriginalFilename();
        try {
            ExcelUtils.checkFile(fileName);
        } catch (Exception e) {
            return new RestResult(40000, RestResult.ERROR_MSG, "文件不合法,请检查文件是否为Excel文件");
        }
        //String filePath = request.getSession().getServletContext().getRealPath("upload/");
        String filePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String path = filePath + fileName;
        File uploadFile = new File(path);
        List<ExcelHead> headList = personMapper._queryColumnAndComment();
        SysUserEntity user = (SysUserEntity) request.getSession().getAttribute("user");
        try {
            file.transferTo(uploadFile);
            InputStream inputStream = new FileInputStream(uploadFile);
            List<PersonEntity> personEntities = ExcelUtils.readExcelToEntity(PersonEntity.class, inputStream, uploadFile.getName(), headList);

            //计算发放剩余金额
            //校验录入的表的字段是否合格
            //EXCAL表身份证号校验
            String idCardNoRegex = "(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)|(^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)";
            //EXCAL手机号校验
            String phoneRegex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";
            int i = 1;
            String projectDetailId = UUID.randomUUID().toString();
            for (PersonEntity personEntity : personEntities) {
                if (personEntity.getName() != null || personEntity.getBankCardNo() != null || personEntity.getAddress() != null
                        || personEntity.getCounty() != null || personEntity.getIdCardNo() != null || personEntity.getPhone() != null
                        || personEntity.getGrantAmount() != null) {

                    if (StringUtils.isEmpty(personEntity.getName()) || StringUtils.isEmpty(personEntity.getBankCardNo()) || StringUtils.isEmpty(personEntity.getGrantAmount())
                            || StringUtils.isEmpty(personEntity.getIdCardNo())) {
                        return new RestResult(RestResult.ERROR_CODE, RestResult.ERROR_MSG, "姓名/银行卡号/身份证号/发放金额均不能为空");
                    }


                    i++;
                    if (personEntity.getGrantAmount() == null || personEntity.getGrantAmount() == "") {
                        return new RestResult(RestResult.ERROR_CODE, RestResult.ERROR_MSG, "该表中第" + i + "行发放金额有误，请重新录入");
                    }
                    AreasEntity areasEntity = areasMapper._queryCname(personEntity.getCounty());
                    if (areasEntity == null) {
                        return new RestResult(40000, RestResult.ERROR_MSG, "该表中第" + i + "行所属区域有误，请重新录入");
                    }
                    boolean idCardMatches = personEntity.getIdCardNo().matches(idCardNoRegex);
                    if (personEntity.getIdCardNo() == null || personEntity.getIdCardNo() == "" || idCardMatches == false) {
                        return new RestResult(40000, RestResult.ERROR_MSG, "该表中第" + i + "行身份证号有误，请重新录入");
                    }
                    boolean phoneMatches = personEntity.getPhone().matches(phoneRegex);
                    if (personEntity.getIdCardNo() == null || personEntity.getIdCardNo() == "" || phoneMatches == false) {
                        return new RestResult(40000, RestResult.ERROR_MSG, "该表中第" + i + "行手机号有误，请重新录入");
                    }


                    //身份账号+银行卡号+发放部门+资金项目 需要唯一
                    List<PersonUploadEntity> uploadEntity = personUploadMapper.queryByIdCardNo(personEntity.getIdCardNo());
                    for (PersonUploadEntity personUploadEntity : uploadEntity) {
                        if (personEntity.getIdCardNo().equals(personUploadEntity.getIdCardNo()) && personEntity.getBankCardNo().equals(personUploadEntity.getBankCardNo())) {
                            return new RestResult(RestResult.ERROR_CODE, RestResult.ERROR_MSG, "该人员已存在");
                        }
                    }
                    String personId = UUID.randomUUID().toString();
                    AreasEntity areasEntity1 = areasMapper._queryCname(personEntity.getCounty());
                    personEntity.setCounty(areasEntity1.getId());
                    personEntity.setId(personId);
                    personEntity.setProjectId(projectDetailId);
                    personEntity.setStatus("3");//新增状态是未提交 3
                    personEntity.setDepartmentId(user.getDepartmentId());
                    personEntity.setUserId(user.getId());
                    personMapper._addEntity(personEntity);

                }
            }
            logger.info("execute success {}", personEntities.size());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.toString());
        } finally {
            uploadFile.delete();
        }
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, "上传成功");
    }

    @RequestMapping(value = "/compareExcel", method = RequestMethod.POST)
    @Transactional
    public RestResult compareExcel(@RequestParam MultipartFile file, HttpServletRequest request) {
        logger.info("The file is {}", file);
        if (file == null) {
            return new RestResult(40000, RestResult.ERROR_MSG, "文件不合法,请检查文件是否为Excel文件");
        }
        SysUserEntity user = (SysUserEntity) request.getSession().getAttribute("user");
        String fileName = file.getOriginalFilename();
        //String filePath = request.getSession().getServletContext().getRealPath("upload/");
        String filePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String path = filePath + fileName;
        File uploadFile = new File(path);
        List<ExcelHead> headList = personMapper._queryColumnAndComment();
        try {
            file.transferTo(uploadFile);
            InputStream inputStream = new FileInputStream(uploadFile);
            List<PersonEntity> personEntities = ExcelUtils.readExcelToEntity(PersonEntity.class, inputStream, uploadFile.getName(), headList);
            List<PersonEntity> personEntities1 = new ArrayList<>();
            for (PersonEntity personEntity : personEntities) {
                if (personEntity.getName() != null || personEntity.getBankCardNo() != null
                        || personEntity.getIdCardNo() != null || personEntity.getPhone() != null
                        || personEntity.getGrantAmount() != null) {

                    if (StringUtils.isEmpty(personEntity.getProjectId()) || StringUtils.isEmpty(personEntity.getName()) || StringUtils.isEmpty(personEntity.getBankCardNo()) || StringUtils.isEmpty(personEntity.getGrantAmount())
                            || StringUtils.isEmpty(personEntity.getIdCardNo())) {
                        return new RestResult(RestResult.ERROR_CODE, RestResult.ERROR_MSG, "流水号/姓名/银行卡号/身份证号/发放金额/回执状态均不能为空");
                    }
                    Map map = new HashMap();
                    map.put("projectId", personEntity.getProjectId());
                    map.put("bankCardNo", personEntity.getBankCardNo());
                    map.put("idCardNo", personEntity.getIdCardNo());
                    map.put("departmentId", user.getDepartmentId());
                    personEntities1 = personMapper._queryAll(map);
                    String status = "";
                    if (personEntity.getStatus().contains("成功")) {
                        status = "1";
                    } else if (personEntity.getStatus().contains("失败")) {
                        status = "2";
                    }
                    for (PersonEntity personEntity1 : personEntities1) {
                        personEntity1.setStatus(status);
                        personMapper._updateEntity(personEntity1);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.toString());
        } finally {
            uploadFile.delete();
        }
        return new RestResult();
    }


    public static Map setDepartmentIdForMap(HttpServletRequest request, Map params) {
        SysUserEntity user = (SysUserEntity) request.getSession().getAttribute("user");
        params.put("departmentId", user.getDepartmentId());
        return params;

    }

    /**
     * 查询身份证
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "checkIdCard", method = RequestMethod.GET)
    public Boolean checkIdCard(String idCardNo) {
        logger.info("The PersonController queryByParams method params are {}", idCardNo);
        PersonEntity personEntity = personMapper.queryByIdCardNo(idCardNo);
        if (personEntity != null) {
            return true;
        }
        return false;
    }


    @Log(description = "发放录入提交操作", module = "人员管理")
    @RequestMapping(value = "/doSubmit/{projectId}", method = RequestMethod.POST)
    @Transactional
    public Object doSubmit(HttpSession session, @PathVariable String projectId) {
        SysUserEntity user = (SysUserEntity) session.getAttribute("user");
        HashMap params = new HashMap();
        params.put("userId", user.getId());
        params.put("status", "3");//未提交

        List<PersonEntity> personEntities = personMapper._queryAll(params);
        String projectDetailId = "";
        if (personEntities.size() > 0) {
            BigDecimal totalAmount = new BigDecimal("0");
            for (PersonEntity personEntity : personEntities) {
                projectDetailId = personEntity.getProjectId();
                BigDecimal bigDecimal = new BigDecimal(personEntity.getGrantAmount());
                totalAmount = totalAmount.add(bigDecimal);
                PersonUploadEntity personUploadEntity1 = personUploadMapper._queryPersonId(personEntity.getId());
                if (personUploadEntity1 != null) {
                    BeanUtils.copyProperties(personEntity, personUploadEntity1);
                    personUploadMapper._updateEntity(personUploadEntity1);
                } else {
                    PersonUploadEntity personUploadEntity = new PersonUploadEntity();
                    BeanUtils.copyProperties(personEntity, personUploadEntity);
                    personUploadEntity.setPersonId(personEntity.getId());
                    personUploadMapper._addEntity(personUploadEntity);
                }
                personEntity.setStatus("4");//已提交
                personMapper._updateEntity(personEntity);
            }

            //ProjectDetailEntity projectDetailEntity = projectDetailMapper._get(projectDetailId);
            ProjectDetailEntity projectDetailEntity = new ProjectDetailEntity();
            projectDetailEntity.setId(projectDetailId);
            ProjectEntity projectEntity = projectMapper._get(projectId);
            BigDecimal totalAmount1 = projectEntity.getTotalAmount();
            projectDetailEntity.setTotalAmount(totalAmount1);
            projectDetailEntity.setPaymentAmount(totalAmount);
            //发放剩余金额
            projectDetailEntity.setSurplusAmount(totalAmount1.subtract(totalAmount));
            projectDetailEntity.setProjectId(projectId);
            projectDetailEntity.setProjectName(projectEntity.getProjectName());
            projectDetailEntity.setStartTime(new Date());
            //操作人
            projectDetailEntity.setOperUser(user.getId());
            //操作单位
            projectDetailEntity.setOperDepartment(user.getDepartmentId());
            projectDetailEntity.setPaymentDepartment(projectEntity.getPaymentDepartment());
            projectDetailEntity.setState(0);
            /*
            if (projectDetailEntity != null) {
                projectDetailMapper._updateEntity(projectDetailEntity);
            } else {
                projectDetailEntity.setId(projectDetailId);
                projectDetailMapper._addEntity(projectDetailEntity);
            }
            */
            projectDetailMapper._addEntity(projectDetailEntity);
        } else {
            return new RestResult(RestResult.ERROR_CODE, RestResult.ERROR_MSG, "数据不能为空，请重新录入！！");
        }
        return new RestResult();
    }

    /**
     * 根据条件分页查询
     */
    @RequestMapping(value = "/queryByPage", method = RequestMethod.GET)
    public Object queryByPage(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        params.put("currentPage", params.get("page"));
        params.put("pageSize", params.get("rows"));
        logger.info("The PersonController queryPage method params are {}", params);
        params = setDepartmentIdForMap(request, params);
        return personService.queryByPage(params);
    }

}
