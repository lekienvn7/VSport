package model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.sql.Date;

public class QuyVonShop {
    private int maQuyVon;
    private BigDecimal soDuHienTai;
    private BigDecimal soTienCongMoiNgay;
    private Date ngayCapNhatCongCuoi;
    private Timestamp ngayTao;
    private Timestamp ngayCapNhat;

    public int getMaQuyVon() {
        return maQuyVon;
    }

    public void setMaQuyVon(int maQuyVon) {
        this.maQuyVon = maQuyVon;
    }

    public BigDecimal getSoDuHienTai() {
        return soDuHienTai;
    }

    public void setSoDuHienTai(BigDecimal soDuHienTai) {
        this.soDuHienTai = soDuHienTai;
    }

    public BigDecimal getSoTienCongMoiNgay() {
        return soTienCongMoiNgay;
    }

    public void setSoTienCongMoiNgay(BigDecimal soTienCongMoiNgay) {
        this.soTienCongMoiNgay = soTienCongMoiNgay;
    }

    public Date getNgayCapNhatCongCuoi() {
        return ngayCapNhatCongCuoi;
    }

    public void setNgayCapNhatCongCuoi(Date ngayCapNhatCongCuoi) {
        this.ngayCapNhatCongCuoi = ngayCapNhatCongCuoi;
    }

    public Timestamp getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(Timestamp ngayTao) {
        this.ngayTao = ngayTao;
    }

    public Timestamp getNgayCapNhat() {
        return ngayCapNhat;
    }

    public void setNgayCapNhat(Timestamp ngayCapNhat) {
        this.ngayCapNhat = ngayCapNhat;
    }
}