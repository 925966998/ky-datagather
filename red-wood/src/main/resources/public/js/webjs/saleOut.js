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
        remoteSort: false,
        onSortColumn: function (sort, order) {
            mySort('table', sort, order);
        },
        columns: [[
            {
                field: 'processName',
                title: '单据名称',
                width: 100,
                align: 'center',
                sortable: true
            },
            {
                field: 'productName',
                title: '商品名称',
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
                s
            }
        }
    })
}

function Query() {
    $.ajax({
        url: '/ky-redwood/sale/queryPage',
        type: 'get',
        dataType: 'json',
        success: function (data) {
            console.log(data)
            $("#orderCustomer").val(data[0].customName);
            $("#orderTime").val(data[0].sellDate);
            $("#orderNo").val(data[0].invoiceId);
            $("#orderManager").val(data[0].manager);
            $("#orderAccountant").val(data[0].accountant);
            $("#orderCurator").val(data[0].curator);
            $("#orderOperator").val(data[0].operator);
            var str = "";//定义用于拼接的字符串
            for (var i = 0; i < data.length; i++) {
               //拼接表格的行和列
                str = "<tr><td>" + data[i].productName + "</td><td>" + data[i].goodsSpecs + "</td><td>" + data[i].goodsModel + "</td><td>" + data[i].goodsUnit + "</td><td>" + data[i].goodsNum + "</td><td>" + data[i].unitPrice + "</td><td>" + data[i].sumPrice + "</td></tr>";
                //追加到table中
               $("#orderTable").append(str);
            }
        }
    })
}
$(function () {
    doQuery('/ky-redwood/product/queryPage?productStatus='+1);
    $("#tabelShowBox").hide();
    Query();
});
obj = {
    // 查询
    find: function () {
        doQuery('/ky-redwood/product/queryPage?' + $("#tableFindForm").serialize())
    },
    // 添加
    addBox: function () {
        $("#addBox").dialog({
            closed: false

        });
        $("#addForm").form('clear');
    },
    // 编辑
    edit: function () {
        var rows = $("#table").datagrid("getSelections");
        if (rows.length > 0) {
            id = $("#table").datagrid("getSelected").id;
            $("#addBox").dialog({
                closed: false,
            })
            $.ajax({
                url: '/ky-redwood/process/queryById?id=' + id,
                type: 'get',
                dataType: 'json',
                success: function (data) {
                    $.messager.progress('close');
                    var data = data.data;
                    console.log(data);
                    $("#id").val(data.id);
                    $("#processParentId").val(data.processParentId);
                    $("#productName").val(data.productName);
                    var type = data.type;
                    $("input[name='type']").each(function () {
                        if (type == $(this).val()) {
                            $(this).prop("checked", true);
                        }
                    });
                    $("#amount").numberbox('setValue', data.amount);
                    $("#fee").numberbox('setValue', data.fee);
                    $("#add_fee").numberbox('setValue', data.add_fee);
                    var flowStatus = data.flowStatus;
                    $("#flowStatusCombo").combobox("setValue", flowStatus);
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
            $.messager.alert('提示', '请选择要编辑的单据', 'info');
        }
    },
    // 查看
    show: function () {
        var rows = $("#table").datagrid("getSelections");
        if (rows.length ==1) {
            id = $("#table").datagrid("getSelected").id;
            console.log(id);
            //doQueryShow('/ky-redwood/process/queryById?id='+id);
            $("#tabelShowBox").show();
            $("#tableShow").datagrid({
                method: "get",
                url: '/ky-redwood/process/querySelectId?id=' + id,
                'fitColumns': true,//自动适应列大小
                'autoRowHeight': true,//自动调整行的高度
                'pagination': false,//设置分页
                /*
                'pageSize': 10,//设置显示页面数据行数
                'pageList': [10, 20],//设置显示页面的行数的选择
                */
                'rownumbers': true,//是否在行前面添加序号
                'toolbar': '#tabelShowBar',//添加工具栏，制定一个容器的id
                'columns': [[    //指定数据的key值，以及列的名称
                    {
                        field: 'productName',
                        title: '商品名称',
                        width: 100,
                        align: 'center',
                    },
                    {
                        field: 'flowStatus',
                        title: '加工流程',
                        width: 100,
                        align: 'center',
                        formatter: function (flowStatus) {
                            switch (flowStatus) {
                                case 0:
                                    return '<div>未加工</div>';
                                case 1:
                                    return '<div>开料</div>';
                                case 2:
                                    return '<div>木工定型</div>';
                                case 3:
                                    return '<div>机雕</div>';
                                case 4:
                                    return '<div>手雕</div>';
                                case 5:
                                    return '<div>木工组装</div>';
                                case 6:
                                    return '<div>刮磨</div>';
                                case 7:
                                    return '<div>组装铜件</div>';
                                case 8:
                                    return '<div>上蜡</div>';
                            }
                        }
                    },
                    {
                        field: 'type',
                        title: '是否半成品入库',
                        width: 100,
                        align: 'center',
                        formatter: function (type) {
                            if (type == 1) {
                                return '<div>否</div>';
                            } else {
                                return '<div>是</div>';
                            }
                        }
                    },
                    {
                        field: 'amount',
                        title: '用料数量',
                        width: 100,
                        align: 'center',
                    },
                    {
                        field: 'processingPersonnel',
                        title: '加工人员',
                        width: 100,
                        align: 'center',
                        sortable: true
                    },
                    {
                        field: 'fee',
                        title: '加工费',
                        width: 100,
                        align: 'center',
                        sortable: true
                    },
                    {
                        field: 'add_fee',
                        title: '补价费',
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
                },
            })

        } else {
            $.messager.alert('提示', '请选择一条要查看的单据', 'info');
        }
    },
    // 提交表单
    sum: function () {
        $('#addForm').form('submit', {
            onSubmit: function () {
                var lag = $("#addForm").form('validate');
                console.log(lag)
                console.log(form2Json("addForm"));
                if (lag == true) {
                    $.ajax({
                        url: '/ky-redwood/process/saveOrUpdate',
                        type: 'POST',
                        dataType: "json",
                        contentType: "application/json; charset=utf-8",
                        data: form2Json("addForm"),
                        success: function (data) {
                            if ($("#id").val()) {
                                $.messager.show({
                                    title: '提示',
                                    msg: '修改成功'
                                })
                                $("#table").datagrid('reload');
                                $("#tableShow").datagrid('reload');
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
                } else {
                    return false;
                }
            },
            success: function () {
                $.messager.progress('close');
                $("#addBox").dialog({
                    closed: true
                })
                $("#table").datagrid('reload');
                $("#tableShow").datagrid('reload');
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
    showClose: function () {
        $("#tabelShowBox").hide();
    },
    // 删除多个
    del: function () {
        var rows = $("#table").datagrid("getSelections");
        console.log(rows);
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
                        url: "/ky-redwood/product/deleteForce?id=" + ids.join(','),
                        beforeSend: function () {
                            $("#table").datagrid('loading');
                        },
                        success: function (data) {
                            console.log(data);
                            if (data) {
                                $("#table").datagrid('reload');
                                $("#tableShow").datagrid('reload');
                                $.messager.show({
                                    title: '提示',
                                    msg: num + '个用户被删除'
                                })
                                $("#table").datagrid('reload');
                                $("#tableShow").datagrid('reload');
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


}

