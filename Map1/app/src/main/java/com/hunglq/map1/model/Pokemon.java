package com.hunglq.map1.model;

import java.io.Serializable;

public class Pokemon implements Serializable {
    int ma;
    String ten;
    String loai;
    int cp;
    String moTa;
    String hinh;
    int catchPoint;

    public Pokemon(int ma, String ten, String loai, int cp, String moTa, String hinh, int catchPoint) {
        this.ma = ma;
        this.ten = ten;
        this.loai = loai;
        this.cp = cp;
        this.moTa = moTa;
        this.hinh = hinh;
        this.catchPoint = catchPoint;
    }

    public int getMa() {
        return ma;
    }

    public void setMa(int ma) {
        this.ma = ma;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getLoai() {
        return loai;
    }

    public void setLoai(String loai) {
        this.loai = loai;
    }

    public int getCp() {
        return cp;
    }

    public void setCp(int cp) {
        this.cp = cp;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getHinh() {
        return hinh;
    }

    public void setHinh(String hinh) {
        this.hinh = hinh;
    }

    public int getCatchPoint() {
        return catchPoint;
    }

    public void setCatchPoint(int catchPoint) {
        this.catchPoint = catchPoint;
    }
}
