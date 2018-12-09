package com.access;

import java.util.ArrayList;
import java.util.List;


public class UserList {
    private static final List<User> userList = new ArrayList();
    
    private UserList(){
    }
    
 /*   static{
        userList.add(new User("Test1","E68184sad","25-10-1992",0));
        userList.add(new User("Test2","8464684","25-10-1992",1));
        userList.add(new User("Test3","151861","25-10-1992",2));
        userList.add(new User("Test4","31531","25-10-1992",1));
        userList.add(new User("Test5","ssdf8s4","25-10-1992",0));
        }
  */  
    public static List <User> getInstance(){
        return userList;
    }
}
