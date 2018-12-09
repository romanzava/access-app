package com.access;

/**
 *
 * @author luperalt
 */
public class User {

    private String name;
    private String surname;
    private String uid;
    private String valid;
    private int acctype;

    public User(String name, String surname, String uid, String valid, int acctype) {
        this.name = name;
        this.surname = surname;
        this.uid = uid;
        this.valid = valid;
        this.acctype = acctype;   
    }

    public String getName() {
        return name;
    }
    
    public String getSurname() {
        return surname;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public void setSurname(String surname) {
        this.name = surname;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }

    public int getAcctype() {
        return acctype;
    }

    public void setAcctype(int acctype) {
        this.acctype = acctype;
    }

    @Override
    public String toString() {
        return "User{" + ", name=" + name + 
                ", surname" + surname +", uid=" + uid + ", valid=" + valid + 
                ", Acctype=" + acctype;
    }

    
}
