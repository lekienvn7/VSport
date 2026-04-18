package model;

import java.util.List;

public class KhoXu {
    private int tongXu;
    private boolean daNhanHomNay;
    private int mucThuongHomNay;
    private int ngayThu;
    private List<Boolean> trangThai7Ngay;

    public int getTongXu() {
        return tongXu;
    }

    public void setTongXu(int tongXu) {
        this.tongXu = tongXu;
    }

    public boolean isDaNhanHomNay() {
        return daNhanHomNay;
    }

    public void setDaNhanHomNay(boolean daNhanHomNay) {
        this.daNhanHomNay = daNhanHomNay;
    }

    public int getMucThuongHomNay() {
        return mucThuongHomNay;
    }

    public void setMucThuongHomNay(int mucThuongHomNay) {
        this.mucThuongHomNay = mucThuongHomNay;
    }

    public int getNgayThu() {
        return ngayThu;
    }

    public void setNgayThu(int ngayThu) {
        this.ngayThu = ngayThu;
    }

    public List<Boolean> getTrangThai7Ngay() {
        return trangThai7Ngay;
    }

    public void setTrangThai7Ngay(List<Boolean> trangThai7Ngay) {
        this.trangThai7Ngay = trangThai7Ngay;
    }
}