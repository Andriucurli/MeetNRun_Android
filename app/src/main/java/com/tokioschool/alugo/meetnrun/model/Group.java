package com.tokioschool.alugo.meetnrun.model;

import java.util.List;

public class Group {

    int id;
    String name;
    String description;
    byte[] photo = null;
    List<User> user = null;
    User professional = null;
}
