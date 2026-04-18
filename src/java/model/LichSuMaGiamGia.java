package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class LichSuMaGiamGia {
    private String loaiLichSu; // nhan_ma | dung_ma
    private String maCode;
    private String tenMa;
    private Timestamp thoiGian;
    private Integer thayDoiXu;
    private BigDecimal soTienGiam;
    private String moTa;
    private String trangThai;

    public String getLoaiLichSu() {
        return loaiLichSu;
    }

    public void setLoaiLichSu(String loaiLichSu) {
        this.loaiLichSu = loaiLichSu;
    }

    public String getMaCode() {
        return maCode;
    }

    public void setMaCode(String maCode) {
        this.maCode = maCode;
    }

    public String getTenMa() {
        return tenMa;
    }

    public void setTenMa(String tenMa) {
        this.tenMa = tenMa;
    }

    public Timestamp getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(Timestamp thoiGian) {
        this.thoiGian = thoiGian;
    }

    public Integer getThayDoiXu() {
        return thayDoiXu;
    }

    public void setThayDoiXu(Integer thayDoiXu) {
        this.thayDoiXu = thayDoiXu;
    }

    public BigDecimal getSoTienGiam() {
        return soTienGiam;
    }

    public void setSoTienGiam(BigDecimal soTienGiam) {
        this.soTienGiam = soTienGiam;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}