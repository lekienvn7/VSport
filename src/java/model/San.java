package model;

public class San {

    private int id;
    private String tenSan;
    private String moTa;
    private String diaChi;
    private String hinhAnh;

    public San() {
    }

    public San(int id, String tenSan, String moTa, String diaChi, String hinhAnh) {
        this.id = id;
        this.tenSan = tenSan;
        this.moTa = moTa;
        this.diaChi = diaChi;
        this.hinhAnh = hinhAnh;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenSan() {
        return tenSan;
    }

    public void setTenSan(String tenSan) {
        this.tenSan = tenSan;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }
}
