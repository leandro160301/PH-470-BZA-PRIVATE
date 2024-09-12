package com.jws.jwsapi.general.user;

public class UserModel {

    public int id;
    public String name;
    public String user;
    public String password;
    public String code;
    public String type;

    public UserModel(int id, String name, String user, String password, String code, String type) {

        this.id=id;
        this.name = name;
        this.password=password;
        this.user = user;
        this.code = code;
        this.type = type;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }




}