package model;

import java.sql.Time;

public class KhungGio {

    private int id;
    private Time gioBatDau;
    private Time gioKetThuc;
    private String tenKhung;

    public KhungGio() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Time getGioBatDau() {
        return gioBatDau;
    }

    public void setGioBatDau(Time gioBatDau) {
        this.gioBatDau = gioBatDau;
    }

    public Time getGioKetThuc() {
        return gioKetThuc;
    }

    public void setGioKetThuc(Time gioKetThuc) {
        this.gioKetThuc = gioKetThuc;
    }

    public String getTenKhung() {
        return tenKhung;
    }

    public void setTenKhung(String tenKhung) {
        this.tenKhung = tenKhung;
    }
}
