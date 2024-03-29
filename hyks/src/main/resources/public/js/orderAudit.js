// 加载表格
function doQuery(url) {
    // 加载表格
    $("#table").datagrid({
        title: "数据列表",
        iconCls: "icon-left02",
        url: url,
        queryParams: {orderListId: getUrlParam('orderListId')},
        fitColumns: true,
        striped: true,
        pagination: true,
        pageSize: 20,
        method: "GET",
        width: '100%',
        rownumbers: true,
        pageList: [20, 50, 100],
        pageNumber: 1,
        nowrap: true,
        height: 'auto',
        sortName: 'id',
        checkOnSelect: true,
        singleSelect: true,
        sortOrder: 'asc',
        toolbar: '#tabelBut',
        columns: [[
            {
                field: 'orderId',
                title: '订单名称',
                width: 100,
                align: 'center',
                hidden: 'true'
            },
            {
                field: 'orderName',
                title: '订单名称',
                width: 100,
                align: 'center'
            },
            // {
            //     field: 'orderNum',
            //     title: '编号',
            //     width: 100,
            //     align: 'center',
            // },
            // {
            //     field: 'name',
            //     title: '名称',
            //     width: 100,
            //     align: 'center',
            // },
            // {
            //     field: 'specs',
            //     title: '规格',
            //     width: 100,
            //     align: 'center',
            // },
            // {
            //     field: 'totalAmount',
            //     title: '数量',
            //     width: 100,
            //     align: 'center',
            // },
            // {
            //     field: 'unit',
            //     title: '单位',
            //     width: 100,
            //     align: 'center',
            // },
            // {
            //     field: 'orderType',
            //     title: '请购类型',
            //     width: 100,
            //     align: 'center',
            // },
            // {
            //     field: 'oddNum',
            //     title: '请购单号',
            //     width: 100,
            //     align: 'center',
            // },
            // {
            //     field: 'orderOrg',
            //     title: '库存组织',
            //     width: 100,
            //     align: 'center',
            // },
            // {
            //     field: 'companyName',
            //     title: '公司名称',
            //     width: 100,
            //     align: 'center'
            // },
            {
                field: 'nastNum',
                title: '数量',
                width: 100,
                align: 'center'
            },
            {
                field: 'companyName',
                title: '公司名称',
                width: 100,
                align: 'center'
            },
            {
                field: 'price',
                title: '价格',
                width: 100,
                align: 'center'
            },
            {
                field: 'state',
                title: '审批意见',
                width: 100,
                align: 'center',
                formatter: function (val, row) {
                    if (val == 1) {
                        return '通过'
                    } else if (val == 0) {
                        return '未通过'
                    }
                },
                editor: {
                    type: 'combobox',
                    options: {
                        required: true,
                        editable: false,
                        data: [{'value': '1', 'text': '通过'}, {'value': '0', 'text': '未通过'}]
                    }
                }
            },
            // {
            //     field: "opr",
            //     title: '操作',
            //     width: 100,
            //     align: 'center',
            //     formatter: function (val, row) {
            //         return '<a  id="look"   class=" operA" class="easyui-linkbutton"  href="../web/personName.html?projectId=' + row.id + '")">审核</a> ';
            //     }
            // }
        ]],
        onClickRow: function (rowIndex, rowData) {
            var rows = $("#table").datagrid("getSelections");
            if (rows.length > 1) {
                $.messager.alert('提示', '每次选择一条审批记录', 'info');
            }
        },
        onLoadSuccess: function (data) {
            if (data.rows.length > 0) {
                customMergeCells("table", ["orderId", "orderName"], "orderId");
            }
        },
        onClickCell: function (index, field, value) {
            $(this).datagrid('beginEdit', index);
            var ed = $(this).datagrid('getEditor', {index: index, field: field});
        }

    })
}

function customMergeCells(tableID, field_arr, judge) {
    var tTable = $("#" + tableID);
    var rows = tTable.edatagrid("getRows");
    if ((typeof (field_arr) === "undefined" || field_arr === "" || field_arr == null || field_arr === "null")
        || (typeof (field_arr) === "undefined" || field_arr === "" || field_arr == null || field_arr === "null")) {
        return;
    }
    for (var i = 1; i < rows.length; i++) {
        for (var k = 0; k < field_arr.length; k++) {
            var field = field_arr[k]; // 要排序的字段
            if (rows[i][field] === rows[i - 1][field]) { // 相邻的上下两行
                if (!(typeof (judge) === "undefined" || judge === "" || judge == null || judge === "null")) {
                    if (rows[i][judge] !== rows[i - 1][judge]) {
                        continue;
                    }
                }
                var rowspan = 2;
                for (var j = 2; i - j >= 0; j++) { // 判断上下多行内容一样
                    if (rows[i][field] !== rows[i - j][field]) {
                        break;
                    } else {
                        if (!(typeof (judge) === "undefined" || judge === "" || judge == null || judge === "null")) {
                            if (rows[i][judge] !== rows[i - j][judge]) {
                                break;
                            }
                        }
                        rowspan = j + 1;
                    }
                }
                tTable.edatagrid("mergeCells", { // 合并
                    index: i - rowspan + 1,
                    field: field,
                    rowspan: rowspan
                });
            }
        }
    }
}

$(function () {
    doQuery('/ky-supplier/companyOrder/queryPage',);
})


obj = {
    // 查询
    find: function () {
        $("#table").datagrid('load', {
            projectName: $("#projectNameSearch").val(),
        })
    },
    // 添加
    addBox: function () {
        $("#addBox").dialog({
            closed: false
        });
        $("#name").val('');
        $("#pass").val('');
        $("#part01").combotree('setValue', '');
        var date = new Date();
        var y = date.getFullYear();
        var m = date.getMonth() + 1;
        var d = date.getDate();
        var str = y + '-' + m + '-' + d
        $("#time").datebox('setValue', str);
    },
    audit: function () {
        var rows = $("#table").datagrid("getSelections");
        console.log(rows.length)
        if (rows.length > 1) {
            $.messager.alert('提示', '每次选择一条审批记录', 'info');
        } else if (rows.length < 1) {
            $.messager.alert('提示', '请选择一条审批记录', 'info');
        } else {
            $("#auditBox").dialog({
                title: '审核信息',
                closed: false
            });
            $("#table2").edatagrid({
                title: "公司列表",
                iconCls: "icon-left02",
                url: '/ky-supplier/companyOrder/queryPage',
                queryParams: {orderId: rows[0].id},
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
                        field: 'companyName',
                        title: '公司名称',
                        width: 100,
                        align: 'center'
                    },
                    {
                        field: 'orderName',
                        title: '订单名称',
                        width: 100,
                        align: 'center'
                    },
                    {
                        field: 'amount',
                        title: '数量',
                        width: 100,
                        align: 'center'
                    },
                    {
                        field: 'price',
                        title: '价格',
                        width: 100,
                        align: 'center'
                    },
                    {
                        field: 'state',
                        title: '审批意见',
                        width: 100,
                        align: 'center',
                        formatter: function (val, row) {
                            if (val == 1) {
                                return '通过'
                            } else if (val == 0) {
                                return '未通过'
                            }
                        },
                        editor: {
                            type: 'combobox',
                            options: {
                                required: true,
                                editable: false,
                                data: [{'value': '1', 'text': '通过'}, {'value': '0', 'text': '未通过'}]
                            }
                        }
                    },
                ]],
            })
        }
    },
    can: function () {
        $("#auditBox").dialog({
            closed: true
        })
    },
    save: function () {
        var eaRows = $("#table2").datagrid('getRows');
        $.each(eaRows, function (index, item) {
            $("#table2").datagrid('endEdit', index);
        });
        var updateRows = $('#table2').edatagrid('getChanges', 'updated');
        var changesRows = {
            companyOrderEntities: [],
        };
        if (updateRows.length > 0) {
            for (var k = 0; k < updateRows.length; k++) {
                changesRows.companyOrderEntities.push(updateRows[k]);
            }
        }
        $.ajax({
            url: "/ky-supplier/companyOrder/save",
            type: "post",
            data: {companyOrderEntities: JSON.stringify(changesRows.companyOrderEntities)},
            success: function (data) {
                if (data.code = '10000') {
                    $("#table2").edatagrid('loaded');
                    $("#table2").edatagrid('load');
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
    auditSum: function () {
        $("#auditForm").form('submit', {
            url: "/ky-ykt/projectDetail/audit",
            method: "post",
            onSubmit: function () {
                var state = $("input[name='state']:checked").val();
                if (state == 2) {
                    if (!$("#reason").val()) {
                        $.messager.alert("", "请输入失败原因", 'warning');
                        return false;
                    }
                }
                return $(this).form('validate')
            },
            success: function (data) {
                if (data.code = '10000') {
                    $("#table").datagrid('loaded');
                    $("#table").datagrid('load');
                    $("#auditBox").dialog({
                        closed: true
                    })
                    $.messager.show({
                        title: '提示',
                        msg: '审核完成'
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
    look: function (id) {
        $("#lookTail").dialog({
            closed: false
        })
        $.ajax({
            url: '/ky-ykt/projectDetail/queryByParams',
            type: 'get',
            dataType: 'json',
            data: {projectId: id},
            success: function (res) {
                if (res != null) {
                    var rows = res;
                    var topstr = '';
                    var downstr = '';
                    for (var i = 0; i < rows.length; i++) {
                        topstr += '<div class="lookTailDiv01">' + new Date(rows[i].lastTime).Format("yyyy-MM-dd") + '</div>\n' +
                            '        <div class="floatRight maginRt">\n' +
                            '            <span class="circleStyle01"></span>\n' +
                            '        </div>\n' +
                            '\n' +
                            '        <div class="clear"></div>\n' +
                            '        <div class="floatRight maginRt">\n' +
                            '            <span class="lineH magingLeft"></span>\n' +
                            '        </div>\n' +
                            '\n' +
                            '        <div class="clear"></div>\n' +
                            '        <div class="lookTailDiv02">*</div>\n' +
                            '        <div class="floatRight maginRt">\n' +
                            '            <span class="circleStyle02 backGreen "></span>\n' +
                            '        </div>\n' +
                            '\n' +
                            '        <div class="clear"></div>\n' +
                            '        <div class="floatRight maginRt">\n' +
                            '            <span class="lineH magingLeft"></span>\n' +
                            '        </div>\n' +
                            '        <div class="clear"></div>\n' +
                            '        <div class="lookTailDiv02">*</div>\n' +
                            '        <div class="floatRight maginRt">\n' +
                            '            <span class="circleStyle02 backRed "></span>\n' +
                            '        </div>\n' +
                            '        <div class="clear"></div>\n' +
                            '        <div class="floatRight maginRt">\n' +
                            '            <span class="lineH magingLeft"></span>\n' +
                            '        </div><div class="clear"></div>';

                        if (i > 0) {
                            downstr += '       <div class="clear"></div>\n<div class="divBorder magintTop03 floatLeft borderGreen"><span class="footBlood">发放到:' + rows[i].paymentDepartment + '--发放金额:' + rows[i].paymentAmount + '</span>';
                            if (rows[i].remark != null && rows[i].remark != '') {
                                downstr += '<span class="footBlood">— 描述：' + rows[i].remark + '</span></div></div>\n';
                            } else {
                                downstr += '</div>\n';
                            }
                            downstr += '        <div class="clear"></div>\n' +
                                '        <div class="divBorder magintTop02  floatLeft borderGreen"><span class="footBlood">已发放总金额：' + rows[i].totalAmount + '|剩余金额：' + rows[i].surplusAmount;
                            if (rows[i].reason != null && rows[i].reason != '') {
                                downstr += '-此次未发放原因：' + rows[i].reason + '</span></div>\n'
                            } else {
                                downstr += '</span></div>\n';
                            }
                        } else {
                            downstr += '       <div class="clear"></div>\n<div class="divBorder magintTop01 floatLeft borderGreen"><span class="footBlood">发放到:' + rows[i].paymentDepartment + '--发放金额:' + rows[i].paymentAmount + '</span>';
                            if (rows[i].remark != null && rows[i].remark != '') {
                                downstr += '<span class="footBlood">— 描述：' + rows[i].remark + '</span></div></div>\n';
                            } else {
                                downstr += '</div>\n';
                            }
                            downstr += '        <div class="clear"></div>\n' +
                                '        <div class="divBorder magintTop02  floatLeft borderGreen"><span class="footBlood">已发放总金额：' + rows[i].totalAmount + '|剩余金额：' + rows[i].surplusAmount;
                            if (rows[i].reason != null && rows[i].reason != '') {
                                downstr += '-此次未发放原因：' + rows[i].reason + '</span></div>\n'
                            } else {
                                downstr += '</span></div>\n';
                            }
                        }
                    }
                    $('#topDetail').empty();
                    $('#downDetail').empty();
                    $('#topDetail').append(topstr);
                    $('#downDetail').append(downstr);
                } else {
                    $.messager.show({
                        title: '提示',
                        msg: '查询失败'
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
    sum: function () {
        $("#addForm").form('submit', {
            url: "",
            onSubmit: function () {
                return $(this).form('validate')
            },
            success: function (data) {
                var name = $("#name").val();
                var pass = $("#pass").val();
                var time = $("#time").datebox('getValue');
                var part = $("#part01").combotree('getValue');
                var id = parseInt(Math.random() * 100000);
                $("#table").datagrid('insertRow', {
                    index: 1,
                    row: {
                        id: id,
                        name: name,
                        pass: pass,
                        time: time,
                        part: part
                    }
                })
                $("#addBox").dialog({
                    closed: true
                })
                $.messager.show({
                    title: '提示',
                    msg: '信息保存成功'
                })
            }
        })
    },
}
// 弹出框加载
$("#addBox").dialog({
    title: "信息内容",
    width: 500,
    height: 300,
    closed: true,
    modal: true,
    shadow: true
})
// 加载物流详情
$("#auditBox").dialog({
    title: "信息内容",
    width: 650,
    height: 410,
    closed: true,
    modal: true,
    shadow: true
})
Date.prototype.Format = function (fmt) { //author: meizz
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "H+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg);  //匹配目标参数
    if (r != null) return unescape(r[2]);
    return null; //返回参数值
}
