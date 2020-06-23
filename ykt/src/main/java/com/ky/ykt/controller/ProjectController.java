package com.ky.ykt.controller;

import com.alibaba.fastjson.JSONObject;
import com.ky.ykt.entity.ProjectEntity;
import com.ky.ykt.entity.SysUserEntity;
import com.ky.ykt.logUtil.Log;
import com.ky.ykt.mybatis.PagerResult;
import com.ky.ykt.mybatis.RestResult;
import com.ky.ykt.service.ProjectService;
import com.ky.ykt.utils.HttpUtils;
import com.ky.ykt.utils.NameToCode;
import com.ky.ykt.utils.PathUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName ProjectController
 * @Description: TODO
 * @Author czw
 * @Date 2020/2/24
 **/
@RestController
@RequestMapping("/ky-ykt/project")
public class ProjectController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    @Autowired
    ProjectService projectService;

    @RequestMapping(value = "queryByParams", method = RequestMethod.GET, produces = "application/json;UTF-8")
    public Object queryParams(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The ProjectController queryByParams method params are {}", params);
        return projectService.queryAll(params, request);
    }

    @RequestMapping(value = "queryCount", method = RequestMethod.GET, produces = "application/json;UTF-8")
    public Object queryCount(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The ProjectController queryByParams method params are {}", params);
        return projectService.queryCount(params);
    }

    /**
     * 根据条件分页查询
     */
    @RequestMapping(value = "/queryPage", method = RequestMethod.GET)
    public Object queryPage(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The ProjectController queryPage method params are {}", params);
        RestResult restResult = projectService.queryPage(params);
        PagerResult data = (PagerResult) restResult.getData();
        return this.toJson(data);
    }

    @Log(description = "资金管理发放完成操作", module = "资金管理")
    @RequestMapping(value = "/upstate", method = RequestMethod.GET, produces = "application/json;UTF-8")
    public Object upstate(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The DepartmentController deleteForce method params is {}", params);
        if (params.get("ids") != null) {
            String idsStr = params.get("ids").toString();
            String[] ids = idsStr.split(",");
            for (String id : ids
            ) {
                ProjectEntity projectEntity = new ProjectEntity();
                projectEntity.setId(id);
                projectEntity.setState(1);
                projectEntity.setEndTime(new Date());
                projectService.update(projectEntity);
            }
        }
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG);
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public Object upload(@RequestParam MultipartFile file, HttpServletRequest request) {
        String fileName = file.getOriginalFilename();
        String uploadDirRealPath = PathUtil.getClasspath() + "projectFile";
        File filePath = new File(uploadDirRealPath);
        if (!filePath.exists()) {
            filePath.mkdir();
        }
        String filePathName = uploadDirRealPath + "/" + fileName;
        try {
            //拿到输出流，同时重命名上传的文件

            FileOutputStream os = new FileOutputStream(filePathName);
            //拿到上传文件的输入流
            FileInputStream in = (FileInputStream) file.getInputStream();
            //以写字节的方式写文件
            int b = 0;
            while ((b = in.read()) != -1) {
                os.write(b);
            }
            os.flush();
            os.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filePathName;
    }


    @Log(description = "项目管理新增，修改操作", module = "项目管理")
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST, produces = "application/json;UTF-8")
    public Object saveOrUpdate(ProjectEntity projectEntity, HttpServletRequest request) {
        logger.info("The ProjectController saveOrUpdate method params are {}", projectEntity);
        SysUserEntity user = (SysUserEntity) request.getSession().getAttribute("user");
        if (StringUtils.isNotEmpty(projectEntity.getId())) {
            projectEntity.setPaymentDepartment(user.getDepartmentId());
            return projectService.update(projectEntity);
        } else {
            projectEntity.setId(null);
            projectEntity.setSurplusAmount(projectEntity.getTotalAmount());
            NameToCode nameToCode = new NameToCode();
            String allFirstLetter = nameToCode.getAllFirstLetter(projectEntity.getProjectName());
            projectEntity.setProjectCode(allFirstLetter);
            projectEntity.setPaymentDepartment(user.getDepartmentId());
            projectEntity.setOperUser(user.getId());
            projectEntity.setOperDepartment(user.getDepartmentId());
            return projectService.add(projectEntity);
        }
    }

    @RequestMapping(value = "/select", method = RequestMethod.GET)
    public ProjectEntity select(String id) {
        logger.info("The ProjectController select method params are {}", id);
        RestResult restResult = projectService._get(id);
        ProjectEntity data = (ProjectEntity) restResult.getData();
        return data;
    }

    /**
     * 删除多个
     */
    @Log(description = "资金下达删除操作", module = "资金下达")
    @RequestMapping(value = "deleteMoney", method = RequestMethod.GET)
    public Object deleteMoney(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The ProjectController deleteMoney method params is {}", params);
        if (params.get("ids") != null) {
            String idsStr = params.get("ids").toString();
            String[] ids = idsStr.split(",");
            for (String id : ids
            ) {
                projectService.delete(id);
            }
        }
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG);
    }

    public JSONObject toJson(PagerResult data) {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("total", data.getTotalItemsCount());
        jsonObj.put("rows", data.getItems());
        return jsonObj;
    }

    /**
     * 删除多个
     */
    @RequestMapping(value = "selectHomeNum", method = RequestMethod.GET)
    public Object selectHomeNum(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        return projectService.selectHomeNum(request);
    }

    @RequestMapping(value = "queryAllProject", method = RequestMethod.GET, produces = "application/json;UTF-8")
    public Object queryAllProject(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The ProjectController queryByParams method params are {}", params);
        return projectService.queryAllProject(params, request);
    }

}
