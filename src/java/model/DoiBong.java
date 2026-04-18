package model;

public class DoiBong {
    private int maDoiBong;
    private String tenDoiBong;

    
    private String fanClub;

   
    private String doiSlug;


    private String loai;
    private String logo;

    public DoiBong() {
    }

    public DoiBong(int maDoiBong, String tenDoiBong, String doiSlug, String loai, String logo, String fanClub) {
        this.maDoiBong = maDoiBong;
        this.tenDoiBong = tenDoiBong;
        this.doiSlug = doiSlug;
        this.fanClub = fanClub;
        this.loai = loai;
        this.logo = logo;
    }

    public int getMaDoiBong() {
        return maDoiBong;
    }

    public void setMaDoiBong(int maDoiBong) {
        this.maDoiBong = maDoiBong;
    }

    public String getTenDoiBong() {
        return tenDoiBong;
    }

    public void setTenDoiBong(String tenDoiBong) {
        this.tenDoiBong = tenDoiBong;
    }

    public String getDoiSlug() {
    return doiSlug;
    }

    public void setDoiSlug(String doiSlug) {
        this.doiSlug = doiSlug;
    }

    public String getLoai() {
        return loai;
    }
    
    public String getFanClub() {
        return fanClub;
    }

    public void setFanClub(String fanClub) {
        this.fanClub = fanClub;
    }

    public void setLoai(String loai) {
        this.loai = loai;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}