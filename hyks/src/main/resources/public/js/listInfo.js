obj = {
    // 查询
    find: function () {
        doQuery('/ky-supplier/orderList/queryPage?' + $("#tableFindForm").serialize())
        $("#table").datagrid('load', {
            matterType: $("#matterTypeSearch").val(),
            name: $("#nameSearch").val(),
            orderType: $("#orderTypeSearch").val(),
        })
    },
    // 添加
    addBox: function () {
        $("#addForm").form('clear');
        $("#addBox").dialog({
            closed: false
        });
    },
    // 编辑
    edit: function (id) {
        $("#addBox").dialog({
            closed: false
        });
        id = $("#table").datagrid('getSelected').id;
        $.ajax({
            url: '/ky-supplier/orderList/queryById',
            type: 'get',
            dataType: 'json',
            data: {id: id},
            success: function (res) {
                console.log(res)
                if (res.data != null) {
                    $('#addForm').form('load', {
                        id: id,
                        orderNum: res.data.orderNum,
                        name: res.data.name,
                        state: res.data.state,
                        endTime: res.data.endTime,
                        totalAmount: res.data.totalAmount,
                        unit: res.data.unit,
                        specs: res.data.specs,

                    })
                } else {
                    $.messager.show({
                        title: '提示',
                        msg: '更新失败'
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
    },
    reset: function () {
        $("#addForm").form('clear');
    },
    can: function () {
        $("#addBox").dialog({
            closed: true
        })
    },
    // 提交表单
    sum: function () {
        $('#addForm').form('submit', {
            onSubmit: function () {
                var lag = $(this).form('validate');
                if (lag == true) {
                    $.ajax({
                        url: '/ky-supplier/orderList/saveOrUpdate',
                        type: 'POST',
                        dataType: "json",
                        contentType: "application/json; charset=utf-8",
                        data: form2Json("addForm"),
                        success: function (data) {
                            console.log($("#id").val())
                            $("#table").datagrid('reload');
                            if ($("#id").val()) {
                                $.messager.show({
                                    title: '提示',
                                    msg: '修改成功'
                                })
                            } else {
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
                } else
                    return false;
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
                        url: "/ky-supplier/orderList/deleteForce",
                        data: {
                            id: ids.join(',')
                        },
                        beforesend: function () {
                            $("#table").datagrid('loading');
                        },
                        success: function (data) {
                            if (data.code = '10000') {
                                $("#table").datagrid('reload');
                                $.messager.show({
                                    title: '提示',
                                    msg: num + '个记录被删除'
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
                            } else {
                                $.messager.show({
                                    title: '提示',
                                    msg: '信息删除失败'
                                })
                            }
                        }
                    })
                }
            })
        } else {
            $.messager.alert('提示', '请选择要删除的记录', 'info');
        }
    },
    publishOrder: function () {
        var rows = $("#table").datagrid("getSelections");
        if (rows.length > 0) {
            $.messager.confirm('提示信息', '确定发布所选择的记录么？', function (flg) {
                if (flg) {
                    var ids = [];
                    for (i = 0; i < rows.length; i++) {
                        ids.push(rows[i].id);
                    }
                    var num = ids.length;
                    $.ajax({
                        type: 'get',
                        url: "/ky-supplier/orderList/publishOrder",
                        data: {
                            id: ids.join(',')
                        },
                        beforesend: function () {
                            $("#table").datagrid('loading');
                        },
                        success: function (data) {
                            if (data.code = '10000') {
                                $("#table").datagrid('reload');
                                $.messager.show({
                                    title: '提示',
                                    msg: num + '个记录发布成功'
                                })
                            } else {
                                $.messager.show({
                                    title: '警示信息',
                                    msg: "信息发布失败"
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
                            } else {
                                $.messager.show({
                                    title: '提示',
                                    msg: '信息发布失败'
                                })
                            }
                        }
                    })
                }
            })
        } else {
            $.messager.alert('提示', '请选择要发布的记录', 'info');
        }
    },
    save: function () {
        var eaRows = $("#table").datagrid('getRows');
        $.each(eaRows, function (index, item) {
            $("#table").datagrid('endEdit', index);
        });
        var updateRows = $('#table').edatagrid('getChanges', 'updated');
        var changesRows = {
            orderListEntities: [],
        };
        if (updateRows.length > 0) {
            for (var k = 0; k < updateRows.length; k++) {
                changesRows.orderListEntities.push(updateRows[k]);
            }
        }
        // console.log(changesRows);
        $.ajax({
            url: "/ky-supplier/orderList/save",
            type: "post",
            data: {orderListEntities: JSON.stringify(changesRows.orderListEntities)},
            success: function (data) {
                if (data.code = '10000') {
                    $("#table").edatagrid('loaded');
                    $("#table").edatagrid('load');
                    $.messager.show({
                        title: '提示',
                        msg: '信息保存成功'
                    })
                } else {
                    $.messager.show({
                        title: '提示',
                        msg: '信息保存失败'
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
                } else {
                    $.messager.show({
                        title: '提示',
                        msg: '信息保存失败'
                    })
                }
            }
        })
    },
    chooseOrder: function () {
        var rows = $("#table").datagrid("getSelections");
        console.log(rows.length)
        if (rows.length > 1) {
            $.messager.alert('提示', '每次选择一条审批记录', 'info');
        } else if (rows.length < 1) {
            $.messager.alert('提示', '请选择一条审批记录', 'info');
        } else {
            $("#auditBox").dialog({
                title: '采购信息',
                closed: false
            });
            $("#table2").datagrid({
                title: "公司列表",
                iconCls: "icon-left02",
                url: '/ky-supplier/orderInfo/queryPage',
                method: "GET",
                fitColumns: true,
                striped: true,
                pagination: true,
                pageSize: 10,
                width: '100%',
                rownumbers: true,
                pageNumber: 1,
                nowrap: true,
                height: 'auto',
                sortName: 'id',
                checkOnSelect: true,
                sortOrder: 'asc',
                toolbar: '#tabelBut1',
                columns: [[
                    {
                        field: 'orderNum',
                        title: '编号',
                        width: 70,
                        align: 'center',
                    },
                    {
                        field: 'name',
                        title: '名称',
                        width: 70,
                        align: 'center',
                    },
                    {
                        field: 'specs',
                        title: '规格',
                        width: 70,
                        align: 'center',
                    },
                    {
                        field: 'totalAmount',
                        title: '数量',
                        width: 50,
                        align: 'center',
                    },
                    {
                        field: 'unit',
                        title: '单位',
                        width: 50,
                        align: 'center',
                    },
                    {
                        field: 'orderType',
                        title: '请购类型',
                        width: 100,
                        align: 'center',
                    },
                    {
                        field: 'oddNum',
                        title: '请购单号',
                        width: 100,
                        align: 'center',
                    },
                    {
                        field: 'orderTime',
                        title: '请购日期',
                        width: 100,
                        align: 'center',
                    },
                    {
                        field: 'orderOrg',
                        title: '库存组织',
                        width: 100,
                        align: 'center',
                    },
                ]],
            })
        }
    },
    canAuditBox: function () {
        $("#auditBox").dialog({
            closed: true
        })
    },
    saveAuditBox: function () {
        var rows = $("#table").datagrid("getSelections");
        var updateRows = $("#table2").datagrid("getSelections");
        var changesRows = {
            orderInfoEntities: [],
        };
        if (updateRows.length > 0) {
            for (var k = 0; k < updateRows.length; k++) {
                changesRows.orderInfoEntities.push(updateRows[k]);
            }
        }
        $.ajax({
            url: "/ky-supplier/orderListInfo/save",
            type: "post",
            data: {
                orderListId: rows[0].id,
                orderInfoEntities: JSON.stringify(changesRows.orderInfoEntities)
            },
            success: function (data) {
                if (data.code = '10000') {
                    $("#table2").edatagrid('loaded');
                    $("#table2").edatagrid('load');
                    $.messager.show({
                        title: '提示',
                        msg: '信息保存成功'
                    })
                    $("#auditBox").dialog({
                        closed: true
                    })
                } else {
                    $.messager.show({
                        title: '提示',
                        msg: '信息保存失败'
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
                } else {
                    $.messager.show({
                        title: '提示',
                        msg: '信息保存失败'
                    })
                }
            }
        })
    },
}


$(function () {
    doQuery('/ky-supplier/orderList/queryPage');
})

function doQuery(url) {
    $("#table").edatagrid({
        title: "询价单",
        iconCls: "icon-left02",
        url: url,
        fitColumns: true,
        striped: true,
        method: "GET",
        pagination: true,
        pageSize: 10,
        width: '100%',
        rownumbers: true,
        pageList: [10, 20],
        pageNumber: 1,
        nowrap: true,
        height: 'auto',
        sortName: 'id',
        checkOnSelect: true,
        sortOrder: 'asc',
        toolbar: '#tabelBut',
        columns: [[
            {
                field: 'listName',
                title: '采购编号',
                width: 70,
                align: 'center',
                editor: {type: 'validatebox', options: {required: true},}
            },
            {
                field: 'userName',
                title: '采购员',
                width: 70,
                align: 'center',
                editor: {type: 'validatebox', options: {required: true},}
            },
            {
                field: 'userCell',
                title: '手机号',
                width: 70,
                align: 'center',
                editor: {type: 'validatebox', options: {required: true},}
            },
            {
                field: 'talkNum',
                title: '谈判次数',
                width: 50,
                align: 'center',
                editor: {type: 'numberbox', options: {precision: 0}}
            },
            {
                field: 'endTime',
                title: '时间',
                width: 50,
                align: 'center',
                editor: {type: 'datetimebox', options: 'showSeconds:false',}

            },
            {
                field: 'opr',
                title: '操作',
                width: 100,
                align: 'center',
                formatter: function (val, row) {
                    a = '<a  id="look"   class=" operA" class="easyui-linkbutton"  href="../web/purchaseInfo.html?orderId=' + row.id + '">采购需求</a> ';
                    return a;
                }
            }
        ]],
        onLoadError: function (request) {
            if (request.status == 401) {
                $.messager.confirm('登录失效', '您的身份信息已过期请重新登录', function (r) {
                    if (r) {
                        parent.location.href = "/login.html";
                    }
                });
            }
        },
        onClickRow: function (rowIndex, rowData) {
            var rows = $("#table").datagrid("getSelections");
            if (rows.length > 1) {
                $.messager.alert('提示', '每次选择一条记录', 'info');
            }
        }
    })
}

// 弹出框加载
$("#addBox").dialog({
    title: "信息内容",
    width: 400,
    height: 300,
    closed: true,
    modal: true,
    shadow: true
})

$("#auditBox").dialog({
    title: "信息内容",
    width: 650,
    height: 410,
    closed: true,
    modal: true,
    shadow: true
})