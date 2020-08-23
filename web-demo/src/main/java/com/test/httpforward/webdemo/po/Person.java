package com.test.httpforward.webdemo.po;

/**
 * @author chenl
 * @since 2020-08-23
 */
public class Person{
    private String pName;
    private int pAge;

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public int getpAge() {
        return pAge;
    }

    public void setpAge(int pAge) {
        this.pAge = pAge;
    }

    @Override
    public String toString() {
        return "Person{" +
                "pName='" + pName + '\'' +
                ", pAge=" + pAge +
                '}';
    }
}
