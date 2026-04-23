package model;

import java.sql.Timestamp;

public class MaGiamGiaSoHuu {
    private int maDoi;
    private int maNguoiDung;
    private int maGiamGia;
    private String maCode;
    private String tenMa;
    private double giaTriGiam;
    private String loaiGiam;
    private double giamToiDa;
    private double dieuKienToiThieu;
    private int soXuDaDoi;
    private Timestamp ngayDoi;
    private String trangThai;

    public int getMaDoi() {
        return maDoi;
    }

    public void setMaDoi(int maDoi) {
        this.maDoi = maDoi;
    }

    public int getMaNguoiDung() {
        return maNguoiDung;
    }

    public void setMaNguoiDung(int maNguoiDung) {
        this.maNguoiDung = maNguoiDung;
    }

    public int getMaGiamGia() {
        return maGiamGia;
    }

    public void setMaGiamGia(int maGiamGia) {
        this.maGiamGia = maGiamGia;
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

    public double getGiaTriGiam() {
        return giaTriGiam;
    }

    public void setGiaTriGiam(double giaTriGiam) {
        this.giaTriGiam = giaTriGiam;
    }

    public String getLoaiGiam() {
        return loaiGiam;
    }

    public void setLoaiGiam(String loaiGiam) {
        this.loaiGiam = loaiGiam;
    }

    public double getGiamToiDa() {
        return giamToiDa;
    }

    public void setGiamToiDa(double giamToiDa) {
        this.giamToiDa = giamToiDa;
    }

    public double getDieuKienToiThieu() {
        return dieuKienToiThieu;
    }

    public void setDieuKienToiThieu(double dieuKienToiThieu) {
        this.dieuKienToiThieu = dieuKienToiThieu;
    }

    public int getSoXuDaDoi() {
        return soXuDaDoi;
    }

    public void setSoXuDaDoi(int soXuDaDoi) {
        this.soXuDaDoi = soXuDaDoi;
    }

    public Timestamp getNgayDoi() {
        return ngayDoi;
    }

    public void setNgayDoi(Timestamp ngayDoi) {
        this.ngayDoi = ngayDoi;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}