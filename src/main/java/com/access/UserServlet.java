package com.access;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(
        name = "UserServlet",
        urlPatterns = {"/users"}
)
public class UserServlet extends HttpServlet {
    //URI uri = URI.create("ws://192.168.0.143/ws");
    URI uri = URI.create("ws://localhost:1337/");
    
    DatabaseConnector dbconn;
    UserService userService = new UserService();   
    EventClient evtclient = new EventClient();

    public UserServlet() {
        try {
            dbconn = new DatabaseConnector();
        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {       
        String action = req.getParameter("searchAction");
 /*       if(!evtclient.isOnline(uri)){
            req.setAttribute("visible", false);
        }
   */     
        if (action!=null){
            switch (action) {           
            case "searchByUid":
                searchUserByUid(req, resp);
                break;           
            case "searchByName":
                searchUserByName(req, resp);
                break;
            }
        }else{
            List<User> result = userService.getAllUsers();
            forwardListUsers(req, resp, result);
        }
    }

    private void searchUserByUid(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String idUid = req.getParameter("idUid");
        User user = null;
        try {
            user = userService.getUser(idUid);
        } catch (Exception ex) {
            Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        req.setAttribute("user", user);
        req.setAttribute("action", "edit");
        String nextJSP = "/jsp/new-user.jsp";
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
        dispatcher.forward(req, resp);
    }
    
    private void searchUserByName(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String userName = req.getParameter("userName");
        List<User> result = userService.searchUserByNameOrUid(userName);        
        forwardListUsers(req, resp, result);
    }

    private void forwardListUsers(HttpServletRequest req, HttpServletResponse resp, List userList)
            throws ServletException, IOException {
        String nextJSP = "/jsp/list-users.jsp";
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
        req.setAttribute("userList", userList);
        dbconn.getAllUsers();
        dispatcher.forward(req, resp);
    }   
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        switch (action) {
            case "add":
                addUserAction(req, resp);
                break;
            case "edit":
                editUserAction(req, resp);
                break;            
            case "remove":
                removeUserByName(req, resp);
                break;
        }

    }

    private void addUserAction(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            if(evtclient.isOnline(uri)){
                evtclient.start(uri,"admin","admin");
                String name = req.getParameter("name");
                String surname = req.getParameter("surname");
                String uid = req.getParameter("uid");
                String valid = req.getParameter("valid");
                int acctype = Integer.valueOf(req.getParameter("acctype"));
                User user = new User(name, surname, uid, valid, acctype);
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                Date date = dateFormat.parse(valid);
                long unixTime = (long) date.getTime()/1000;
                System.out.println(unixTime);
                if(!dbconn.isDuplicatePK(uid)){
                String idUser = userService.addUser(user);
                    evtclient.sendMessage("{\"command\":\"userfile\",\"uid\":\""+ uid +
                            "\",\"user\":\""+ name + surname +"\",\"acctype\":"+ acctype +
                            ",\"validuntil\": "+ unixTime + "}");
                    System.out.println("{\"command\":\"userfile\",\"uid\":\""+ uid +
                            "\",\"user\":\""+ name + surname +"\",\"acctype\":"+ acctype +
                            ",\"validuntil\": "+ unixTime + "}");
                    Thread.sleep(300);
                    dbconn.addDataToDB(user.getName(),user.getSurname(), user.getUid(), user.getValid(), String.valueOf(user.getAcctype()));
                    List<User> userList = userService.getAllUsers();
                    req.setAttribute("idUser", idUser);
                    String message = "\nPouzivatel uspesne vytvoreny.";
                    req.setAttribute("message", evtclient.getResponse() + message);
                    forwardListUsers(req, resp, userList);
                    evtclient.close();
                } else {
                    String message = "Duplikatne UID.";
                    req.setAttribute("alert", message);
                    List<User> userList = userService.getAllUsers();
                    forwardListUsers(req, resp, userList);
                    return;
                }
            } else {
                String message = "Ziadne pripojenie k zamku.";
                req.setAttribute("alert", message);
                List<User> userList = userService.getAllUsers();
                forwardListUsers(req, resp, userList);
            }
            
        } catch (Exception ex) {
            Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
            
        }
    }

    private void editUserAction(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String name = req.getParameter("name");
        String surname = req.getParameter("surname");
        String uid = req.getParameter("uid");
        String valid = req.getParameter("valid");
        int acctype = Integer.valueOf(req.getParameter("acctype"));
        User user = new User(name, surname, uid, valid, acctype);
        user.setUid(uid);
        boolean success = userService.updateUser(user);
        boolean dbUpdate = dbconn.updateData(user);
        String message = null;
        if (success && dbUpdate) {
            message = "\nThe user has been successfully updated.";
            req.setAttribute("uid", uid);
            req.setAttribute("message", message);
        } else {
            message = "\nThere was a problem with updating the user";
            req.setAttribute("uid", uid);
            req.setAttribute("alert", message);
        }
        List<User> userList = userService.getAllUsers();

        forwardListUsers(req, resp, userList);
    }  
    
    private void removeUserByName(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
         //   System.out.println(evtclient.isOnline());
            if(evtclient.isOnline(uri)){               
                evtclient.start(uri,"admin","admin");
                String uid = req.getParameter("uid");
                boolean confirm = userService.deleteUser(uid); 
                System.out.println("{\"command\":\"remove\",\"uid\":"+ uid + "}");
                evtclient.sendMessage("{\"command\":\"remove\",\"uid\":"+ uid + "}");
                if (confirm){
                    dbconn.deleteDataFromDB(uid);
                    String message = "\nThe user has been successfully removed.";
                    req.setAttribute("message", evtclient.getResponse() + message);
                }
                List<User> userList = userService.getAllUsers();
                forwardListUsers(req, resp, userList);
                evtclient.close();
            } else {
                String message = "No connection.";
                req.setAttribute("alert", message);
                List<User> userList = userService.getAllUsers();
                forwardListUsers(req, resp, userList);
            }
            
        } catch (Exception ex) {
            Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
            
        }
    }
}
