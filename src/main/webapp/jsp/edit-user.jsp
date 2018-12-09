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
            <form action="/users" method="post"  role="form" data-toggle="validator" >
                <c:if test ="${empty action}">                        	
                    <c:set var="action" value="edit"/>
                </c:if>
                <input type="hidden" id="action" name="action" value="${action}">
                <h2>User</h2>
                <div class="form-group col-xs-4">
                    <label for="name" class="control-label col-xs-4">name:</label>
                    <input type="textbox" name="name" id="name" class="form-control" value="${param.name}" required="true"/>                                   
                    
                    <label for="surname" class="control-label col-xs-4">surname:</label>
                    <input type="textbox" name="surname" id="surname" class="form-control" value="${param.surname}" required="true"/>  
                    
                    <label for="uid" class="control-label col-xs-4">uid:</label>                   
                    <input type="text" name="uid" id="uid" class="form-control" value="${param.uid}" required="false" readonly/> 

                    <label for="valid" class="control-label col-xs-4">valid</label>                 
                    <input type="text"  pattern="^\d{2}-\d{2}-\d{4}$" name="valid" id="valid" class="form-control" value="${param.valid}" maxlength="10" required="true"/>

                    <label for="acctype" class="control-label col-xs-4">Acctype:</label>
                    <select name=acctype class="form-control" value="${param.acctype}" required="true">
                        <option value="0" ${param.acctype == '0' ? 'selected' : ''}>Disabled</option>
                        <option value="1" ${param.acctype == '1' ? 'selected' : ''}>Granted</option>
                        <option value="99" ${param.acctype == '99' ? 'selected' : ''}>Admin</option>
                    </select>                   
                    <br></br>
                    <button type="submit" class="btn btn-primary  btn-md">Accept</button> 
                </div>                                                      
            </form>
        </div>
    </body>
</html>