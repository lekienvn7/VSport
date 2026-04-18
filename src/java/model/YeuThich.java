/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Date;

public class YeuThich {

    
    private int maYeuThich;
    private int maNguoiDung;
    private int maSanPham;
    private Date ngayThem;
    private String tenSanPham;
    private String slug;
    private String anhChinh;

    
    private Double giaNiemYet;
    private Double giaKhuyenMai;
    private Double giaSanPham;
    private String moTaNgan;
    
    public int getMaYeuThich() {
        return maYeuThich;
    }

    public void setMaYeuThich(int maYeuThich) {
        this.maYeuThich = maYeuThich;
    }

    public int getMaNguoiDung() {
        return maNguoiDung;
    }

    public void setMaNguoiDung(int maNguoiDung) {
        this.maNguoiDung = maNguoiDung;
    }

    public int getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(int maSanPham) {
        this.maSanPham = maSanPham;
    }

    public Date getNgayThem() {
        return ngayThem;
    }

    public void setNgayThem(Date ngayThem) {
        this.ngayThem = ngayThem;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getAnhChinh() {
        return anhChinh;
    }

    public void setAnhChinh(String anhChinh) {
        this.anhChinh = anhChinh;
    }
    
    public Double getGiaNiemYet() {
        return giaNiemYet;
    }

    public void setGiaNiemYet(Double giaNiemYet) {
        this.giaNiemYet = giaNiemYet;
    }

    public Double getGiaKhuyenMai() {
        return giaKhuyenMai;
    }

    public void setGiaKhuyenMai(Double giaKhuyenMai) {
        this.giaKhuyenMai = giaKhuyenMai;
    }

    public Double getGiaSanPham() {
        return giaSanPham;
    }

    public void setGiaSanPham(Double giaSanPham) {
        this.giaSanPham = giaSanPham;
    }

    public String getMoTaNgan() {
        return moTaNgan;
    }

    public void setMoTaNgan(String moTaNgan) {
        this.moTaNgan = moTaNgan;
    }
}
