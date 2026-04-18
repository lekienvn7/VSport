package model;

import java.sql.Timestamp;
import java.util.Date;

public class NguoiDung {
    private int maNguoiDung;
    private String hoTen;

    
    private String avatar;
    private Date ngaySinh;
    private String diaChi;
    private String email;
    private String soDienThoai;

    
    private Date lanNhanXuDangNhapCuoi;
    private String matKhau;
    private String vaiTro;
    private String trangThai;
    private Timestamp ngayTao;
    private int soXu;

    public NguoiDung() {
    }

    public int getMaNguoiDung() {
        return maNguoiDung;
    }

    public void setMaNguoiDung(int maNguoiDung) {
        this.maNguoiDung = maNguoiDung;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }
    
    public Date getLanNhanXuDangNhapCuoi() {
        return lanNhanXuDangNhapCuoi;
    }

    public void setLanNhanXuDangNhapCuoi(Date lanNhanXuDangNhapCuoi) {
        this.lanNhanXuDangNhapCuoi = lanNhanXuDangNhapCuoi;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }
    
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Date getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(Date ngay_sinh) {
        this.ngaySinh = ngay_sinh;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String dia_chi) {
        this.diaChi = dia_chi;
    }

    public String getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(String vaiTro) {
        this.vaiTro = vaiTro;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public Timestamp getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(Timestamp ngayTao) {
        this.ngayTao = ngayTao;
    }

    public int getSoXu() {
        return soXu;
    }

    public void setSoXu(int soXu) {
        this.soXu = soXu;
    }
}