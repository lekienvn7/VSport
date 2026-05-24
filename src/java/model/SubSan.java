package model;

import java.math.BigDecimal;

public class SubSan {

    private int id;
    private int sanId;
    private String maSubSan;
    private String loaiSan;   // "7" or "11"
    private String viTri;     // "1","2","3"
    private BigDecimal giaTheoGio;
    private String moTa;
    private String trangThai;

    // Joined field
    private String tenSan;

    public SubSan() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSanId() {
        return sanId;
    }

    public void setSanId(int sanId) {
        this.sanId = sanId;
    }

    public String getMaSubSan() {
        return maSubSan;
    }

    public void setMaSubSan(String maSubSan) {
        this.maSubSan = maSubSan;
    }

    public String getLoaiSan() {
        return loaiSan;
    }

    public void setLoaiSan(String loaiSan) {
        this.loaiSan = loaiSan;
    }

    public String getViTri() {
        return viTri;
    }

    public void setViTri(String viTri) {
        this.viTri = viTri;
    }

    public BigDecimal getGiaTheoGio() {
        return giaTheoGio;
    }

    public void setGiaTheoGio(BigDecimal giaTheoGio) {
        this.giaTheoGio = giaTheoGio;
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

    public String getTenSan() {
        return tenSan;
    }

    public void setTenSan(String tenSan) {
        this.tenSan = tenSan;
    }

    public String getDisplayName() {
        if ("11".equals(loaiSan)) {
            return "Sân " + maSubSan + " (11 người)";
        }
        return "Sân " + maSubSan + " (7 người - Vị trí " + viTri + ")";
    }
}
