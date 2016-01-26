<%@ page import="ru.javawebinar.topjava.util.TimeUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<jsp:include page="fragments/bodyHeader.jsp"/>
<section>
    <link rel="stylesheet" href="webjars/datatables/1.10.9/css/jquery.dataTables.min.css">
    <link rel="stylesheet" href="webjars/datetimepicker/2.3.4/jquery.datetimepicker.css">
    <%--http://stackoverflow.com/questions/10327390/how-should-i-get-root-folder-path-in-jsp-page--%>
    <h3><a href="${pageContext.request.contextPath}">Home</a></h3>
    <div class="jumbotron">
        <div class="container">
            <div class="shadow">
                <h3><fmt:message key="meals.title"/></h3>
                <div class="view-box">
                    <form method="post" action="meals/filter">
                        <div>
                            <table cellspacing="150">
                                <tr>
                                    <dl>
                                        <td>
                                            <dt>From Date:</dt>
                                        </td>
                                        <td>
                                            <dd><input type="date" name="startDate" value="${startDate}"></dd>

                                        <td>
                                            <dt align="center">To Date:</dt>
                                        </td>
                                        <td>
                                            <dd><input type="date" name="endDate" value="${endDate}"></dd>
                                    </dl>
                                    </td>

                                </tr>
                                <tr>
                                    <td>
                                        <p></p>
                                    </td>
                                </tr>


                                <tr>
                                    <td>
                                        <dt>From Time:</dt>
                                    </td>
                                    <td>
                                        <dd><input type="time" name="startTime" value="${startTime}"></dd>
                                    </td>

                                    <td align="center">
                                        <dt>To Time:</dt>
                                    </td>
                                    <td>
                                        <dd><input type="time" name="endTime" value="${endTime}"></dd>
                                        </dl>
                                    </td>
                                </tr>

                                <td>
                                    <p></p>
                                    <button type="submit" a class="btn btn-xs btn-primary">Filter</button>
                                </td>

                            </table>
                        </div>
                    </form>
                    <hr>
                    <a class="btn btn-sm btn-info" id="add"><fmt:message key="meals.add"/></a>
                    <%--  <a class="btn btn-sm btn-info" href="meals/create">Add Meal</a>--%>

                    <hr>

                    <table class="table table-striped display" id="mealdata">
                        <%--    <h3><fmt:message key="meals.title"/></h3>--%>



                            <thead>
                            <tr>
                                <th>Date</th>
                                <th>Description</th>
                                <th>Calories</th>
                                <th></th>
                                <th></th>
                            </tr>
                            </thead>
                            <c:forEach items="${mealList}" var="meal">
                                <jsp:useBean id="meal" scope="page"
                                             type="ru.javawebinar.topjava.to.UserMealWithExceed"/>
                                <tr class="${meal.exceed ? 'exceeded' : 'normal'}">
                                    <td>
                                            <%--<fmt:parseDate value="${meal.dateTime}" pattern="y-M-dd'T'H:m" var="parsedDate"/>--%>
                                            <%--<fmt:formatDate value="${parsedDate}" pattern="yyyy.MM.dd HH:mm" />--%>
                                        <%=TimeUtil.toString(meal.getDateTime())%>
                                    </td>
                                    <td>${meal.description}</td>
                                    <td>${meal.calories}</td>
                                    <td><a class="btn btn-xs btn-primary edit" id="${meal.id}">Update</a></td>
                                    <td><a class="btn btn-xs btn-danger delete" id="${meal.id}">Delete</a></td>
                                    </td>
                                </tr>
                            </c:forEach>
                        </table>
                    </table>
                </div>
            </div>
        </div>

    </div>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
<div class="modal fade" id="editRow">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h2 class="modal-title"><fmt:message key="meals.edit"/></h2>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" method="post" id="detailsForm">
                    <input type="text" hidden="hidden" id="id" name="id">
                    <div class="form-group">
                        <label for="date" class="control-label col-xs-3">Date</label>
                        <div class="col-xs-9">
                            <input type="text" class="form-control" id="date" name="date" placeholder="Date">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="description" class="control-label col-xs-3">Description</label>
                        <div class="col-xs-9">
                            <input type="description" class="form-control" id="description" name="description"
                                   placeholder="Description">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="calories" class="control-label col-xs-3">Calories</label>
                        <div class="col-xs-9">
                            <input type="calories" class="form-control" id="calories" name="calories"
                                   placeholder="2000">
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-xs-offset-3 col-xs-9">
                            <button type="submit" class="btn btn-primary">Save</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
<script type="text/javascript" src="webjars/jquery/2.1.4/jquery.min.js"></script>
<script type="text/javascript" src="webjars/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<script type="text/javascript" src="webjars/datetimepicker/2.3.4/jquery.datetimepicker.js"></script>
<script type="text/javascript" src="webjars/datatables/1.10.9/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="webjars/noty/2.2.4/jquery.noty.packaged.min.js"></script>
<script type="text/javascript" src="resources/js/datatablesUtil.js"></script>
<script type="text/javascript">


    var ajaxUrl = 'ajax/admin/meals/';
    var oTable_mealdata;
    var oTable_mealdata_params;
    $(document).ready(function () {
        // $(function () {
        oTable_mealdata = $('#mealdata');
        oTable_mealdata_params = {
            "bPaginate": false,
            "bInfo": false,
            "aoColumns": [
                {
                    "mData": "dateTime"
                },
                {
                    "mData": "description"
                },
                {
                    "mData": "calories"
                },
                {
                    "sDefaultContent": "",
                    "bSortable": false
                },
                {
                    "sDefaultContent": "",
                    "bSortable": false
                }
            ],
            "aaSorting": [
                [
                    0,
                    "desc"
                ]
            ]
        };
        oTable_mealdata.dataTable(oTable_mealdata_params);
        makeEditable();
    });
</script>
</html>
