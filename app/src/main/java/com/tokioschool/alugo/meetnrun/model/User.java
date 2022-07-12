package com.tokioschool.alugo.meetnrun.model;

public class User {
    String name;
    String surname;
    String pwd = null;
    int id = 0;
    String phone;
    String email;
    byte[] photo = null;

    byte[] schedule;

    public void setSchedule(byte[] schedule) {
        this.schedule = schedule;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getProfessional_id() {
        return professional_id;
    }

    Integer professional_id;

    public User(String name, String surname, String pwd, int id, String phone, String email, byte[] photo, Integer professional_id, byte[] schedule) {
        this.name = name;
        this.surname = surname;
        this.pwd = pwd;
        this.id = id;
        this.phone = phone;
        this.email = email;
        this.photo = photo;
        this.professional_id = professional_id;
        this.schedule = schedule;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPwd() {
        return pwd;
    }

    public int getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public byte[] getSchedule() {
        return schedule;
    }
}
