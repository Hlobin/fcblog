layui.use(['form', 'layedit', 'laydate'], function () {

    var form = layui.form,

        layer = parent.layer === undefined ? layui.layer : top.layer,

        $ = layui.jquery,

        laydate = layui.laydate,

        laytpl = layui.laytpl,

        table = layui.table;


    //监听提交
    form.on("submit(editAdmin)", function (data) {

        //弹出loading
        var index = top.layer.msg('数据提交中，请稍候', {icon: 16, time: false, shade: 0.8});

        $.post("/adminController/editAdmin",
            {
                // 管理员Id
                id: $("#adminEditId").val(),

                // 管理员名称
                adminName: $("#adminEditName").val(),

                // 管理员手机
                adminPhone: $("#adminEditPhone").val(),

                // 管理员邮箱
                adminEmail: $("#adminEditEmail").val(),
            },

            function (res) {

                if (res.info == '请输入管理员名称') {

                    layer.msg("请输入管理员名称");

                    return;
                }

                if (res.info == '请输入管理员邮箱') {

                    layer.msg("请输入管理员邮箱");

                    return;
                }

                if (res.info == '邮箱格式错误') {

                    layer.msg("邮箱格式错误");

                    return;
                }

                if (res.info == '请输入手机号码'){

                    layer.msg("请输入手机号码");

                    return;
                }

                if (res.info == '手机号码格式错误') {

                    layer.msg("手机号码格式错误");

                    return;
                }

                if (res.info == '操作失败') {

                    layer.msg("操作失败，请联系管理员");

                    return;
                }

                if (res.info == '更新成功') {

                    setTimeout(function () {

                        top.layer.close(index);

                        top.layer.msg("更新成功！");

                        layer.closeAll("iframe");

                        //刷新父页面

                        parent.location.reload();

                    }, 500);
                }
            });


        return false;
    })

});