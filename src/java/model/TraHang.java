package model;

import java.sql.Timestamp;

public class TraHang {

    private int maTraHang;
    private int maDonHang;
    private String lyDo; // het_nhu_cau, hang_hong, hang_that_lac
    private double soTienHoan;
    private String trangThai; // cho_xu_ly, da_hoan, tu_choi
    private Timestamp ngayYeuCau;
    private Timestamp ngayHoan;
    private String ghiChu;

    // Dùng để hiển thị bên admin, không lưu DB
    private String tenKhachHang;
    private double tongTienDonHang;
    private boolean hoanTonKho;

    public TraHang() {
    }

    public TraHang(int maDonHang, String lyDo, double soTienHoan, String ghiChu) {
        this.maDonHang = maDonHang;
        this.lyDo = lyDo;
        this.soTienHoan = soTienHoan;
        this.ghiChu = ghiChu;
        this.trangThai = "cho_xu_ly";
    }

    // Getters & Setters
    public int getMaTraHang() {
        return maTraHang;
    }

    public void setMaTraHang(int maTraHang) {
        this.maTraHang = maTraHang;
    }

    public int getMaDonHang() {
        return maDonHang;
    }

    public void setMaDonHang(int maDonHang) {
        this.maDonHang = maDonHang;
    }

    public String getLyDo() {
        return lyDo;
    }

    public void setLyDo(String lyDo) {
        this.lyDo = lyDo;
    }

    public double getSoTienHoan() {
        return soTienHoan;
    }

    public void setSoTienHoan(double soTienHoan) {
        this.soTienHoan = soTienHoan;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public Timestamp getNgayYeuCau() {
        return ngayYeuCau;
    }

    public void setNgayYeuCau(Timestamp ngayYeuCau) {
        this.ngayYeuCau = ngayYeuCau;
    }

    public Timestamp getNgayHoan() {
        return ngayHoan;
    }

    public void setNgayHoan(Timestamp ngayHoan) {
        this.ngayHoan = ngayHoan;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public String getTenKhachHang() {
        return tenKhachHang;
    }

    public void setTenKhachHang(String tenKhachHang) {
        this.tenKhachHang = tenKhachHang;
    }

    public double getTongTienDonHang() {
        return tongTienDonHang;
    }

    public void setTongTienDonHang(double tongTienDonHang) {
        this.tongTienDonHang = tongTienDonHang;
    }

    public void setHoanTonKho(boolean hoanTonKho) {
        this.hoanTonKho = hoanTonKho;
    }

    // Tính từ lyDo — không lưu DB
    public boolean isHoanTonKho() {
        if (lyDo == null) {
            return true;
        }
        return !lyDo.equals("hang_hong") && !lyDo.equals("hang_that_lac");
    }
}
