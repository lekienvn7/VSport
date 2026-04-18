package model;

public class BienTheSanPham {

    private int maBienThe;
    private int maSanPham;
    private Integer maSize;
    private int soLuongTon;
    private Double giaRieng;

    // thêm
    private String tenSize;
    private double giaHienThi;

    // ===== Getter Setter =====

    public int getMaBienThe() {
        return maBienThe;
    }

    public void setMaBienThe(int maBienThe) {
        this.maBienThe = maBienThe;
    }

    public int getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(int maSanPham) {
        this.maSanPham = maSanPham;
    }

    public Integer getMaSize() {
        return maSize;
    }

    public void setMaSize(Integer maSize) {
        this.maSize = maSize;
    }

    public int getSoLuongTon() {
        return soLuongTon;
    }

    public void setSoLuongTon(int soLuongTon) {
        this.soLuongTon = soLuongTon;
    }

    public Double getGiaRieng() {
        return giaRieng;
    }

    public void setGiaRieng(Double giaRieng) {
        this.giaRieng = giaRieng;
    }


    public String getTenSize() {
        return tenSize;
    }

    public void setTenSize(String tenSize) {
        this.tenSize = tenSize;
    }

    public double getGiaHienThi() {
        return giaHienThi;
    }

    public void setGiaHienThi(double giaHienThi) {
        this.giaHienThi = giaHienThi;
    }
}