package model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

public class DatSan {
    private int id;
    private Integer userId;
    private String tenKhach;
    private String soDienThoai;
    private String email;
    private int subSanId;
    private Date ngayDat;
    private BigDecimal tongTien;
    private String trangThai; // "cho_duyet","da_duyet","tu_choi","da_thanh_toan"
    private String phuongThucThanhToan;
    private String ghiChu;
    private String maGiaoDich;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Joined / transient
    private String maSubSan;
    private String tenSan;
    private List<DatSanChiTiet> chiTietList;

    public DatSan() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getTenKhach() { return tenKhach; }
    public void setTenKhach(String tenKhach) { this.tenKhach = tenKhach; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getSubSanId() { return subSanId; }
    public void setSubSanId(int subSanId) { this.subSanId = subSanId; }

    public Date getNgayDat() { return ngayDat; }
    public void setNgayDat(Date ngayDat) { this.ngayDat = ngayDat; }

    public BigDecimal getTongTien() { return tongTien; }
    public void setTongTien(BigDecimal tongTien) { this.tongTien = tongTien; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public String getPhuongThucThanhToan() { return phuongThucThanhToan; }
    public void setPhuongThucThanhToan(String phuongThucThanhToan) { this.phuongThucThanhToan = phuongThucThanhToan; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }

    public String getMaGiaoDich() { return maGiaoDich; }
    public void setMaGiaoDich(String maGiaoDich) { this.maGiaoDich = maGiaoDich; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    public String getMaSubSan() { return maSubSan; }
    public void setMaSubSan(String maSubSan) { this.maSubSan = maSubSan; }

    public String getTenSan() { return tenSan; }
    public void setTenSan(String tenSan) { this.tenSan = tenSan; }

    public List<DatSanChiTiet> getChiTietList() { return chiTietList; }
    public void setChiTietList(List<DatSanChiTiet> chiTietList) { this.chiTietList = chiTietList; }

    public String getTrangThaiDisplay() {
        switch (trangThai) {
            case "cho_duyet": return "Chờ duyệt";
            case "da_duyet": return "Đã duyệt";
            case "tu_choi": return "Từ chối";
            case "da_thanh_toan": return "Đã thanh toán";
            default: return trangThai;
        }
    }
}