$("#mainBox").layout({
    fit: true,
    border: false
})
$("#mean").menu('show', {
    left: 200,
    top: 100
})
$("#left01").accordion({
    border: false

})
$("#con").tabs({
    fit: true,
    border: false
})
$("#myMes").dialog({
    title: "个人信息详情",
    width: 400,
    height: 420,
    modal: true,
    iconCls: 'icon-mes',
    maximizable: true,
    closed: true

})

function openMes() {
    $("#myMes").dialog({
        closed: false
    })
    $.ajax({
        url: '/ky-redwood/sysUser/queryById?id=' + sessionStorage.getItem("userId"),
        type: 'get',
        dataType: 'json',
        success: function (data) {
            var data = data.data;
            $("#userName").val(data.userName);
            $("#fullName").val(data.fullName);
            $("#roleName").val(data.roleName);
            $("#departmentName").val(data.departmentName);
        },
        error: function (request) {
            $.messager.progress('close');
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

function loginOut() {
    $.messager.confirm('退出确认', '你是否退出系统？', function (r) {
        if (r) {
            $.ajax({
                url: "/ky-redwood/loginOut",
                type: "POST",
                success: function (returnData) {
                    window.location.href = "login.html";
                },
                error: function (request) {
                    window.location.href = "login.html";
                }
            });
        }
    })

}

/*$(function () {
    var user = JSON.parse(sessionStorage.getItem("user"));
    if (!user) {
        $.messager.confirm('登录失效', '您的身份信息已过期请重新登录', function (r) {
            if (r) {
                parent.location.href = "/login.html";
            }
        });
    }
    console.log(user.menuEntities);
    var menuEntities = user.menuEntities;
    var flag = true;
    for (var i = 0; i < menuEntities.length; i++) {
        var str = "";
        for (var j = 0; j < menuEntities[i].menuChildren.length; j++) {
            str += "<p class='menuC' ><i class='i_a " + menuEntities[i].menuChildren[j].menuIcon + "'></i> <a href='" + menuEntities[i].menuChildren[j].menuUrl + "' target='frameName'  style='text-decoration: none;color: #333333;' onclick='targetFrame(this)'>" + menuEntities[i].menuChildren[j].menuName + "</a></p>"
        }
        if (i != 0)
            flag = false;
        $('#left01').accordion('add', {
            title: menuEntities[i].menuName,
            content: str,
            class: 'mainPanle',
            iconCls: menuEntities[i].menuIcon,
            selected: flag
        });
    }
});*/

$(".topText a").click(function () {
    $(this).addClass('textActive').siblings().removeClass('textActive');

})

function targetFrame(obj) {
    var menuC = $(".menuC");
    for (var i = 0; i < menuC.length; i++) {
        $(menuC).removeAttr("style");
    }
    $(obj).parent("p").attr("style", "background-color:rgba(188, 218, 233, 0.93)");
    var testVal = $(obj).text();
    var thisUrl = $(obj).attr('href');
    console.log(testVal, thisUrl);
    var con = '<iframe scrolling="no" frameborder="0"  src="' + thisUrl + '" style="width:100%;height:100%;">';
    if ($('#con').tabs('exists', testVal)) {
        $('#con').tabs('select', testVal);
        var currTab = $('#con').tabs('getSelected');
        currTab.panel('refresh');
        /*var currTab = $('#con').tabs('getSelected');
        var url = $(currTab.panel('options').content).attr('src');
        $('#con').tabs('update', {
            tab: currTab,
            options: {
                href: url
            }
        });*/
    } else {
        $('#con').tabs('add', {
            title: testVal,
            selected: true,
            closable: true,
            cache: false,
            fit: true,
            content: con
        });
    }
}

function closeAllTabs() {
    var tabs = $('#con').tabs('tabs');
    console.log(tabs)
    console.log(tabs.length);
    for (var i = 0; i < tabs.length; i++) {
        var index = $('#con').tabs('getTabIndex', tabs[i]);
        if (index != 0)
            $('#con').tabs('close', index);
    }
}

$("#left01 a").click(function () {
    var testVal = $(this).text();
    var thisUrl = $(this).attr('href');
    console.log(testVal, thisUrl);
    var con = '<iframe scrolling="no" frameborder="0"  src="' + thisUrl + '" style="width:100%;height:100%;">';
    if ($('#con').tabs('exists', testVal)) {
        $('#con').tabs('select', testVal);
        /* var currTab = $('#con').tabs('getSelected');
         var url = $(currTab.panel('options').content).attr('src');
         $('#con').tabs('update', {
             tab: currTab,
             options: {
                 href: url
             }
         });*/
    } else {
        $('#con').tabs('add', {
            title: testVal,
            selected: true,
            closable: true,
            fit: true,
            content: con
        });
    }
})
$("#con").tabs({
    onSelect: function (tit, ind) {
        if (ind == 0) {
            $("#ifDiv").attr('src', "home.html");
        }
    }
})
