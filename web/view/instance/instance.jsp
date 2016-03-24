<%--
  Created by IntelliJ IDEA.
  User: chenpengjiang
  Date: 2016/3/22
  Time: 14:55
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c"
           uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@ include file="../../resources/jsp/common_css.jsp"%>
    <%@ include file="../../resources/jsp/commom_js.jsp"%>
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/bootstrap-table/1.10.1/bootstrap-table.min.css">

    <!-- Latest compiled and minified JavaScript -->
    <script src="//cdnjs.cloudflare.com/ajax/libs/bootstrap-table/1.10.1/bootstrap-table.min.js"></script>

    <!-- Latest compiled and minified Locales -->
    <script src="//cdnjs.cloudflare.com/ajax/libs/bootstrap-table/1.10.1/locale/bootstrap-table-zh-CN.min.js"></script>
</head>
<body>
<section id="container" class="">
    <!--header start-->
    <header class="header white-bg">
        <div class="sidebar-toggle-box">
            <div data-original-title="Toggle Navigation" data-placement="right" class="icon-reorder tooltips"></div>
        </div>
        <div id="headtemplate" style="display:inline">

        </div>
    </header>
    <!--header end-->
    <!--sidebar start-->
    <aside>
        <div id="sidebar"  class="nav-collapse">
            <table id="table"></table>
        </div>
    </aside>

    <!--sidebar end-->
    <!--main content start-->
    <section id="main-content">
        <section class="wrapper">

        </section>
    </section>
    <!--main content end-->
</section>
<script>

    //owl carousel

    $(document).ready(function() {
        $("#sidebar").load("/view/general/leftmenu.html");
        $("#headtemplate").load("/view/general/top.html");
        $("#owl-demo").owlCarousel({
            navigation : true,
            slideSpeed : 300,
            paginationSpeed : 400,
            singleItem : true

        });

        $('#table').bootstrapTable({
            url: '/Instance/getcurInstance.do',
            columns: [{
                field: 'name',
                title: '虚拟机名称'
            }, {
                field: 'id',
                title: '虚拟机编号'
            }, ]
        });
    });

    //custom select box

    $(function(){
        $('select.styled').customSelect();
    });

</script>
</body>
</html>