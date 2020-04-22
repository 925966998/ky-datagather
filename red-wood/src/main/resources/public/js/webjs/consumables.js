// 加载树
function doQuery(url) {
    $("#table").datagrid({
        method: "get",
        iconCls: "icon-left02",
        url: url,
        fitColumns: true,
        striped: true,
        pagination: true,
        pageSize: 10,
        width: '100%',
        rownumbers: true,
        pageList: [10, 20],
        pageNumber: 1,
        nowrap: true,
        height: 'auto',
        checkOnSelect: true,
        toolbar: '#tabelBut',
        singleSelect: true,
        onSortColumn: function (sort, order) {
            mySort('table', sort, order);
        },
        columns: [[
            {
                field: 'materialName',
                title: '材料名称',
                width: 100,
                align: 'center',
                sortable: true
            },
            {
                field: 'amount',
                title: '数量',
                width: 100,
                align: 'center',
                sortable: true
            },
            {
                field: 'price',
                title: '单价',
                width: 100,
                align: 'center',
                sortable: true
            },
        ]],
        onLoadError: function (request) {
            if (request.status == 401) {
                $.messager.confirm('登录失效', '您的身份信息已过期请重新登录', function (r) {
                    if (r) {
                        parent.location.href = "/login.html";
                    }
                });
            }
        }
    })

}

$(function () {
    doQuery('/ky-redwood/materialOut/queryPage?consumablesIs='+1);
});
obj = {
    // 查询
    find: function () {
        doQuery('/ky-redwood/materialOut/queryPage?' + $("#tableFindForm").serialize())
    },
    // 添加
    addBox: function () {
        $("#addBox").dialog({
            closed: false

        });
        $("#addForm").form('clear');
        $("#materialName").combobox({
            url:'/ky-redwood/consumable/queryByConsumable',
            method: 'get',
            valueField: 'id',
            textField: 'materialName'
        });
    },
    // 编辑

    edit: function () {
        var rows = $("#table").datagrid("getSelections");
        if (rows.length>0){
            var id = $("#table").datagrid('getSelected').id;
            $("#editBoxMaterialName").combobox({
                url:'/ky-redwood/consumable/queryByConsumable',
                method: 'get',
                valueField: 'id',
                textField: 'materialName'
            });
            $("#editBoxB").dialog({
                closed: false,
            })
            $.ajax({
                url: '/ky-redwood/materialOut/queryById?id=' + id,
                type: 'get',
                dataType: 'json',
                success: function (data) {
                    var data = data.data;
                    console.log(data);
                    if (data) {
                        $('#editBoxForm').form('load', {
                            id: data.id,
                            materialId:data.materialId,
                            materialName: data.materialName,
                            amount: data.amount,
                        });
                    }
                },
                error: function (request) {
                    if (request.status == 401) {
                        $.messager.confirm('登录失效', '您的身份信息已过期请重新登录', function (r) {
                            if (r) {
                                parent.location.href = "/login.html";
                            }
                        });
                    }
                }
            })
        } else {
            $.messager.alert('提示', '请选择要修改的记录', 'info');
        }
    },
    editBoxsum: function(){
        $('#editBoxForm').form('submit', {
            onSubmit: function () {
                var lag = $("#editBoxForm").form('validate');
                var newAmount = document.getElementById('newAmount').value;
                var amount =  document.getElementById('editBoxAmount').value;
                console.log(lag)
                if (lag == true) {
                    $.ajax({
                        url: '/ky-redwood/materialOut/update?newAmount='+newAmount +'&amount='+amount,
                        type: 'POST',
                        dataType: "json",
                        contentType: "application/json; charset=utf-8",
                        data: form2Json("editBoxForm"),
                        success: function (data) {
                            if (data.code==50000){
                                $.messager.show({
                                    title: '提示',
                                    msg: '增加失败，数量不足'
                                })
                            }else {
                                $("#table").datagrid('reload');
                                $.messager.show({
                                    title: '提示',
                                    msg: '修改成功'
                                })
                            }
                        },
                        error: function (request) {
                            if (request.status == 401) {
                                $.messager.confirm('登录失效', '您的身份信息已过期请重新登录', function (r) {
                                    if (r) {
                                        parent.location.href = "/login.html";
                                    }
                                });
                            }
                        }
                    })
                } else {
                    return false;
                }
            },
            success: function () {
                $.messager.progress('close');
                $("#editBoxB").dialog({
                    closed: true
                })
                $("#table").datagrid('reload')
            }
        });
    },
    // 提交表单
    sum: function () {
        $('#addForm').form('submit', {
            onSubmit: function () {
                var lag = $("#addForm").form('validate');
                console.log(lag)
                if (lag == true) {
                    $.ajax({
                        url: '/ky-redwood/consumable/saveOrUpdate',
                        type: 'POST',
                        dataType: "json",
                        contentType: "application/json; charset=utf-8",
                        data: form2Json("addForm"),
                        success: function (data) {
                            console.log(data.code)
                            if (data.code==50000){
                                $.messager.show({
                                    title: '提示',
                                    msg: '增加失败，数量不足'
                                })
                            }else {
                                $("#table").datagrid('reload');
                                    $.messager.show({
                                        title: '提示',
                                        msg: '新增成功'
                                    })
                            }
                        },
                        error: function (request) {
                            if (request.status == 401) {
                                $.messager.confirm('登录失效', '您的身份信息已过期请重新登录', function (r) {
                                    if (r) {
                                        parent.location.href = "/login.html";
                                    }
                                });
                            }
                        }
                    })
                } else {
                    return false;
                }

            },
            success: function () {
                $.messager.progress('close');
                $("#addBox").dialog({
                    closed: true
                })
                $("#table").datagrid('reload')
            }
        });

    },

    // 重置表单
    res: function () {
        $("#addForm").form('clear');
    },
    // 取消表单
    can: function () {
        $("#addBox").dialog({
            closed: true
        })
    },
    editBoxres: function () {
        $("#editBoxForm").form('clear');
    },
    // 取消表单
    editBoxcan: function () {
        $("#editBoxB").dialog({
            closed: true
        })
    },
    // 删除多个
    del: function () {
        var rows = $("#table").datagrid("getSelections");
        if (rows.length > 0) {
            $.messager.confirm('确定删除', '你确定要删除你选择的记录吗？', function (flg) {
                if (flg) {
                    var ids = [];
                    for (i = 0; i < rows.length; i++) {
                        ids.push(rows[i].id);
                    }
                    var num = ids.length;
                    $.ajax({
                        type: 'get',
                        url: "/ky-redwood/materialOut/deleteForce?id=" + ids.join(','),
                        beforeSend: function () {
                            $("#table").datagrid('loading');
                        },
                        success: function (data) {
                            if (data) {
                                $("#table").datagrid('reload');
                                $.messager.show({
                                    title: '提示',
                                    msg: num + '个用户被删除'
                                })
                            } else {
                                $.messager.show({
                                    title: '警示信息',
                                    msg: "信息删除失败"
                                })
                            }
                        },
                        error: function (request) {
                            if (request.status == 401) {
                                $.messager.confirm('登录失效', '您的身份信息已过期请重新登录', function (r) {
                                    if (r) {
                                        parent.location.href = "/login.html";
                                    }
                                });
                            }
                        }
                    })
                }
            })
        } else {
            $.messager.alert('提示', '请选择要删除的记录', 'info');
        }
    },

    //删除一个
    delOne: function () {
        var id = $("#table").datagrid('getSelected').id;
        $.messager.confirm('提示信息', '是否删除所选择记录', function (flg) {
            if (flg) {
                $.ajax({
                    type: 'get',
                    url: '/ky-redwood/materialOut/deleteForce?id=' + id,
                    beforeSend: function () {
                        $("#table").datagrid('loading');
                    },
                    success: function (data) {
                        if (data) {
                            $("#table").datagrid("reload");
                            $.messager.show({
                                title: '提示信息',
                                msg: "数据删除成功"
                            })
                        } else {
                            $.messager.show({
                                title: '警示信息',
                                msg: "数据删除失败"
                            })
                        }
                    }, error: function (request) {
                        if (request.status == 401) {
                            $.messager.confirm('登录失效', '您的身份信息已过期请重新登录', function (r) {
                                if (r) {
                                    parent.location.href = "/login.html";
                                }
                            });
                        }
                    }
                })
            }
        })
    }
}

// 弹出框加载
$("#addBox").dialog({
    title: "新增数据",
    width: 500,
    height: 400,
    resizable: true,
    minimizable: true,
    maximizable: true,
    closed: true,
    modal: true,
    shadow: true
})

// 弹出框加载
$("#editBoxB").dialog({
    title: "修改数据",
    width: 500,
    height: 400,
    resizable: true,
    minimizable: true,
    maximizable: true,
    closed: true,
    modal: true,
    shadow: true
})

