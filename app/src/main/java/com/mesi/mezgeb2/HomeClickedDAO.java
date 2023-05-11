package com.mesi.mezgeb2;

import java.util.ArrayList;

public class HomeClickedDAO {

    private int id;
    private String fdate;
    private String fname;
    private String phone;
    private String shoulder;
    private String chest;
    private String height;

    private String height1;

    private String dateap;
    private String dale;
    private String waist;
    private String footWidth;
    private String appointment;
    private String totalFee;
    private String advancePayment;
    private String comment;
    private String style;
    public HomeClickedDAO(){

    }

    public HomeClickedDAO(int id, String fname, String phone, String shoulder, String chest, String height, String height1, String dateap, String dale, String waist, String footWidth, String appointment, String totalFee, String advancePayment, String comment, String style) {
        this.id = id;
        this.fname = fname;
        this.phone = phone;
        this.shoulder = shoulder;
        this.chest = chest;
        this.height = height;
        this.height1 = height1;
        this.dateap = dateap;
        this.dale = dale;
        this.waist = waist;
        this.footWidth = footWidth;
        this.appointment = appointment;
        this.totalFee = totalFee;
        this.advancePayment = advancePayment;
        this.comment = comment;
        this.style = style;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFdate(){

        return fdate;
    }
    public void setFdate(String fdate){
        this.fdate = fdate;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getShoulder() {
        return shoulder;
    }

    public void setShoulder(String shoulder) {
        this.shoulder = shoulder;
    }

    public String getChest() {
        return chest;
    }

    public void setChest(String chest) {
        this.chest = chest;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getHeight1(){

        return height1;
    }

    public void setHeight1(String height1){

        this.height1 = height1;
    }

    public String getDateap(){
        return dateap;
    }

    public void setDateap(String dateap){

        this.dateap = dateap;
    }

    public String getDale() {
        return dale;
    }

    public void setDale(String dale) {
        this.dale = dale;
    }

    public String getWaist() {
        return waist;
    }

    public void setWaist(String waist) {
        this.waist = waist;
    }

    public String getFootWidth() {
        return footWidth;
    }

    public void setFootWidth(String footWidth) {
        this.footWidth = footWidth;
    }

    public String getAppointment() {
        return appointment;
    }

    public void setAppointment(String appointment) {
        this.appointment = appointment;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public String getAdvancePayment() {
        return advancePayment;
    }

    public void setAdvancePayment(String advancePayment) {
        this.advancePayment = advancePayment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
}

