<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <head>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <link rel="stylesheet" href="../css/bootstrap.min.css">
        <script src="../js/bootstrap.min.js"></script>   
        <script src="../js/jquery-3.3.1.min.js" type="text/javascript"></script>
    </head>

    <body>          
        <div class="container">
            <h2>Pouzivatelia</h2>
            <!--Search Form -->
            <form action="/users" method="get" id="seachUserForm" role="form">
                <input type="hidden" id="searchAction" name="searchAction" value="searchByName">
                <div class="form-group col-xs-5">
                    <input type="text" name="userName" id="userName" class="form-control" placeholder="Napiste meno alebo priezvisko alebo UID karty"/>
                </div>
                <button type="submit" class="btn btn-info">
                    <i class="fa fa-search"></i> Search
                </button>
                <br></br>
            </form>

            <!--User List-->
            <c:if test="${not empty message}">                
                <div class="alert alert-success" id="messages">
                    ${message}
                </div>
            </c:if>
            <c:if test="${not empty alert}">                
                <div class="alert alert-danger" id="messages">
                    ${alert}
                </div>
            </c:if>
            <form action="/users" method="post" id="userForm" role="form" >              
                <input type="hidden" id="uid" name="uid">
                <input type="hidden" id="action" name="action">
                <c:choose>
                    <c:when test="${not empty userList}">
                        <table class="table table-striped">
                            <thead>
                                <tr>
                                    <td>UID</td>
                                    <td>Meno</td>
                                    <td>Priezvisko</td>
                                    <td>Pristup</td>
                                    <td>Platnost</td>
                                    <td></td>
                                </tr>
                            </thead>
                           <c:forEach var="user" items="${userList}">
                                <c:set var="classSucess" value=""/>
                                <c:if test ="${uid == user.uid}">                        	
                                    <c:set var="classSucess" value="info"/>
                                </c:if>
                                <tr class="${classSucess}">                                   
                                    <td>${user.uid}</td>
                                    <td>${user.name}</td>
                                    <td>${user.surname}</td>
                                    <td>${user.acctype}</td>
                                    <td>${user.valid}</td>
                                    <td>
                                    <c:set var="edit">
                                            <c:url value="jsp/edit-user.jsp">
                                                <c:param name="uid" value="${user.uid}"/>
                                                <c:param name="name" value="${user.name}"/>
                                                <c:param name="surname" value="${user.surname}"/>  
                                                <c:param name="acctype" value="${user.acctype}"/>       
                                                <c:param name="valid" value="${user.valid}"/>
                                            </c:url>
                                    </c:set>
                                        <a href="${edit}" class="btn btn-info btn-lg">
                                            <i class="fa fa-edit"></i></a>      
                                        <a href="#" id="remove" class="btn btn-info btn-lg" 
                                           onclick="document.getElementById('action').value = 'remove';
                                                    document.getElementById('uid').value = '${user.uid}';                                                    
                                                    document.getElementById('userForm').submit();"> 
                                            <i class="fa fa-trash-o"></i></a>
                                        <a href="#" id="upload" class="btn btn-info btn-lg" onclick="">
                                            <i class="fa fa-arrow-circle-o-up"/></i></a>
                                        <a href="#" class="btn btn-info btn-lg" onclick="">
                                            <i class="fa fa-arrow-circle-o-up"/></i></a>
                                    </td>
                                </tr>
                            </c:forEach>               
                        </table>  
                    </c:when>         
                    <c:when test="${empty userList}">
                        <br>           
                        <div class="alert alert-info">
                            Ziadny pouzivatel v database
                        </div>
                    </c:when>
                    <c:otherwise>
                        <br>           
                        <div class="alert alert-info">
                            Nenasiel sa ziaden uzivatel so zadanymi udajmi
                        </div>

                        <a href="/users" >
                            <button type="button" class="btn btn-info"><i class="fa fa-search"></i>
                                Vsetci pouzivatelia</button></a>
                    </c:otherwise>
                </c:choose>                        
            </form>
            <form action ="jsp/new-user.jsp">            
                <br></br>
                <button type="submit" class="btn btn-primary  btn-md">Novy uzivatel</button> 
            </form>
        </div>
    </body>

</html>
