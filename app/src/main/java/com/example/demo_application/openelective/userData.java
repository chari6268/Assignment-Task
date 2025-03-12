package com.example.demo_application.openelective;

public class userData {
    private String name;
    private String pass;
    private String branch;
    private String year;
    private String section;

    public userData() {
    }

    public userData(String name, String pass, String branch, String year, String section) {
        this.name = name;
        this.pass = pass;
        this.branch = branch;
        this.year = year;
        this.section = section;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }
}
