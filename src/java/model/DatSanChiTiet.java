package model;

import java.math.BigDecimal;
import java.sql.Time;

public class DatSanChiTiet {

    private int id;
    private int datSanId;
    private int lichSanId;
    private Time gioBatDau;
    private Time gioKetThuc;
    private BigDecimal gia;

    public DatSanChiTiet() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDatSanId() {
        return datSanId;
    }

    public void setDatSanId(int datSanId) {
        this.datSanId = datSanId;
    }

    public int getLichSanId() {
        return lichSanId;
    }

    public void setLichSanId(int lichSanId) {
        this.lichSanId = lichSanId;
    }

    public Time getGioBatDau() {
        return gioBatDau;
    }

    public void setGioBatDau(Time gioBatDau) {
        this.gioBatDau = gioBatDau;
    }

    public Time getGioKetThuc() {
        return gioKetThuc;
    }

    public void setGioKetThuc(Time gioKetThuc) {
        this.gioKetThuc = gioKetThuc;
    }

    public BigDecimal getGia() {
        return gia;
    }

    public void setGia(BigDecimal gia) {
        this.gia = gia;
    }
}
