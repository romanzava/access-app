package com.access;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class UserService {

    List<User> userList = UserList.getInstance();
     
    public List<User> getAllUsers() {
        return userList;
    }

    public List<User> searchUserByNameOrUid(String name) {
        Comparator<User> groupByComparator = Comparator.comparing(User::getName)
                                                .thenComparing(User::getUid);
        List<User> result = userList
                .stream()
                .filter(e -> e.getName().equalsIgnoreCase(name) || e.getSurname().equalsIgnoreCase(name) || e.getUid().equalsIgnoreCase(name))
                .sorted(groupByComparator)
                .collect(Collectors.toList());
        return result;
    }
    
    public User getUser(String uid) throws Exception {
        Optional<User> match
                = userList.stream()
                .filter(e -> e.getUid().equals(uid))
                .findFirst();
        if (match.isPresent()) {
            return match.get();
        } else {
            throw new Exception("The User id " + uid + " not found");
        }
    }   

    public String addUser(User user) {
        userList.add(user);
        return user.getUid();
    }

    public boolean updateUser(User user) {
        int matchIdx = 0;
        
        Optional<User> match = userList.stream()
                .filter(c -> c.getUid().equals(user.getUid()))
                .findFirst();
        if (match.isPresent()) {
            matchIdx = userList.indexOf(match.get());
            userList.set(matchIdx, user);
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteUser(String uid) {
        Predicate<User> user = e -> e.getUid().equals(uid);
        if (userList.removeIf(user)) {
            return true;
        } else {
            return false;
        }
    }
}
