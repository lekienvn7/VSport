package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class LichSuVonShop {
    private int maLichSu;
    private int maQuyVon;
    private Integer maSanPham;
    private String loaiBienDong;
    private BigDecimal soTienBienDong;
    private BigDecimal soDuTruoc;
    private BigDecimal soDuSau;
    private String ghiChu;
    private Timestamp ngayTao;

    public int getMaLichSu() {
        return maLichSu;
    }

    public void setMaLichSu(int maLichSu) {
        this.maLichSu = maLichSu;
    }

    public int getMaQuyVon() {
        return maQuyVon;
    }

    public void setMaQuyVon(int maQuyVon) {
        this.maQuyVon = maQuyVon;
    }

    public Integer getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(Integer maSanPham) {
        this.maSanPham = maSanPham;
    }

    public String getLoaiBienDong() {
        return loaiBienDong;
    }

    public void setLoaiBienDong(String loaiBienDong) {
        this.loaiBienDong = loaiBienDong;
    }

    public BigDecimal getSoTienBienDong() {
        return soTienBienDong;
    }

    public void setSoTienBienDong(BigDecimal soTienBienDong) {
        this.soTienBienDong = soTienBienDong;
    }

    public BigDecimal getSoDuTruoc() {
        return soDuTruoc;
    }

    public void setSoDuTruoc(BigDecimal soDuTruoc) {
        this.soDuTruoc = soDuTruoc;
    }

    public BigDecimal getSoDuSau() {
        return soDuSau;
    }

    public void setSoDuSau(BigDecimal soDuSau) {
        this.soDuSau = soDuSau;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public Timestamp getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(Timestamp ngayTao) {
        this.ngayTao = ngayTao;
    }
}