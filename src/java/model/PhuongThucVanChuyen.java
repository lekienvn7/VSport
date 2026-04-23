package model;

public class PhuongThucVanChuyen {
    private int maPtvc;
    private String tenPhuongThuc;
    private double phiVanChuyen;
    private String thoiGianDuKien;
    private int trangThai;

    public int getMaPtvc() {
        return maPtvc;
    }

    public void setMaPtvc(int maPtvc) {
        this.maPtvc = maPtvc;
    }

    public String getTenPhuongThuc() {
        return tenPhuongThuc;
    }

    public void setTenPhuongThuc(String tenPhuongThuc) {
        this.tenPhuongThuc = tenPhuongThuc;
    }

    public double getPhiVanChuyen() {
        return phiVanChuyen;
    }

    public void setPhiVanChuyen(double phiVanChuyen) {
        this.phiVanChuyen = phiVanChuyen;
    }

    public String getThoiGianDuKien() {
        return thoiGianDuKien;
    }

    public void setThoiGianDuKien(String thoiGianDuKien) {
        this.thoiGianDuKien = thoiGianDuKien;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }
}