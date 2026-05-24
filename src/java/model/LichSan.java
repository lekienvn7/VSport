package model;

import java.sql.Date;
import java.sql.Time;

public class LichSan {

    private int id;
    private int subSanId;
    private Date ngay;
    private int khungGioId;
    private String trangThai; // "trong", "da_dat", "dang_xu_ly"
    private Integer datSanId;

    // Joined fields
    private String tenKhung;
    private Time gioBatDau;
    private Time gioKetThuc;
    private String maSubSan;

    public LichSan() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSubSanId() {
        return subSanId;
    }

    public void setSubSanId(int subSanId) {
        this.subSanId = subSanId;
    }

    public Date getNgay() {
        return ngay;
    }

    public void setNgay(Date ngay) {
        this.ngay = ngay;
    }

    public int getKhungGioId() {
        return khungGioId;
    }

    public void setKhungGioId(int khungGioId) {
        this.khungGioId = khungGioId;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public Integer getDatSanId() {
        return datSanId;
    }

    public void setDatSanId(Integer datSanId) {
        this.datSanId = datSanId;
    }

    public String getTenKhung() {
        return tenKhung;
    }

    public void setTenKhung(String tenKhung) {
        this.tenKhung = tenKhung;
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

    public String getMaSubSan() {
        return maSubSan;
    }

    public void setMaSubSan(String maSubSan) {
        this.maSubSan = maSubSan;
    }

    public boolean isTrong() {
        return "trong".equals(trangThai);
    }
}
