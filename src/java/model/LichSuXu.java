package model;

import java.sql.Timestamp;

public class LichSuXu {
    private int maLichSuXu;
    private int maNguoiDung;
    private String loaiGiaoDich;
    private String nguonXu;
    private int soXu;
    private String moTa;
    private Timestamp ngayTao;

    public int getMaLichSuXu() {
        return maLichSuXu;
    }

    public void setMaLichSuXu(int maLichSuXu) {
        this.maLichSuXu = maLichSuXu;
    }

    public int getMaNguoiDung() {
        return maNguoiDung;
    }

    public void setMaNguoiDung(int maNguoiDung) {
        this.maNguoiDung = maNguoiDung;
    }

    public String getLoaiGiaoDich() {
        return loaiGiaoDich;
    }

    public void setLoaiGiaoDich(String loaiGiaoDich) {
        this.loaiGiaoDich = loaiGiaoDich;
    }

    public String getNguonXu() {
        return nguonXu;
    }

    public void setNguonXu(String nguonXu) {
        this.nguonXu = nguonXu;
    }

    public int getSoXu() {
        return soXu;
    }

    public void setSoXu(int soXu) {
        this.soXu = soXu;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public Timestamp getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(Timestamp ngayTao) {
        this.ngayTao = ngayTao;
    }
}