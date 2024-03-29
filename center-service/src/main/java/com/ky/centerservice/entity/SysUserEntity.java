package com.ky.centerservice.entity;


import com.ky.centerservice.mybatis.BaseEntity;

import java.util.List;

public class SysUserEntity extends BaseEntity {

    private String userName;

    private String password;

    private Integer status;

    private String phone;

    private String fullName;

    private String idCardNo;

    private String roleId;

    private String departmentId;

    private String areaId;
    private String areaName;
    //as
    private String departmentName;
    private String roleName;
    private String userNote;

    private String userProjectList;

    public String getUserProjectList() {
        return userProjectList;
    }

    public void setUserProjectList(String userProjectList) {
        this.userProjectList = userProjectList;
    }

    public String getUserNote() {
        return userNote;
    }

    public void setUserNote(String userNote) {
        this.userNote = userNote;
    }

    private List<MenuEntity> menuEntities;

    public List<MenuEntity> getMenuEntities() {
        return menuEntities;
    }

    public void setMenuEntities(List<MenuEntity> menuEntities) {
        this.menuEntities = menuEntities;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
}
