package dao;

import model.LichSuVonShop;
import model.QuyVonShop;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class VonShopPageDAO {

    public QuyVonShop getQuyVonShop() throws Exception {
        String sql = """
            SELECT ma_quy_von, so_du_hien_tai, so_tien_cong_moi_ngay,
                   ngay_cap_nhat_cong_cuoi, ngay_tao, ngay_cap_nhat
            FROM quy_von_shop
            ORDER BY ma_quy_von ASC
            LIMIT 1
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                QuyVonShop qv = new QuyVonShop();
                qv.setMaQuyVon(rs.getInt("ma_quy_von"));
                qv.setSoDuHienTai(rs.getBigDecimal("so_du_hien_tai"));
                qv.setSoTienCongMoiNgay(rs.getBigDecimal("so_tien_cong_moi_ngay"));
                qv.setNgayCapNhatCongCuoi(rs.getDate("ngay_cap_nhat_cong_cuoi"));
                qv.setNgayTao(rs.getTimestamp("ngay_tao"));
                qv.setNgayCapNhat(rs.getTimestamp("ngay_cap_nhat"));
                return qv;
            }
        }

        return null;
    }

    public List<LichSuVonShop> getDanhSachLichSuVon() throws Exception {
        String sql = """
            SELECT ma_lich_su_von, ma_quy_von, ma_san_pham, loai_bien_dong,
                   so_tien_bien_dong, so_du_truoc, so_du_sau, noi_dung, ngay_tao
            FROM lich_su_von_shop
            ORDER BY ngay_tao DESC, ma_lich_su_von DESC
        """;

        List<LichSuVonShop> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                LichSuVonShop ls = new LichSuVonShop();
                ls.setMaLichSu(rs.getInt("ma_lich_su_von"));
                ls.setMaQuyVon(rs.getInt("ma_quy_von"));

                Object maSanPhamObj = rs.getObject("ma_san_pham");
                ls.setMaSanPham(maSanPhamObj != null ? rs.getInt("ma_san_pham") : null);

                ls.setLoaiBienDong(rs.getString("loai_bien_dong"));
                ls.setSoTienBienDong(rs.getBigDecimal("so_tien_bien_dong"));
                ls.setSoDuTruoc(rs.getBigDecimal("so_du_truoc"));
                ls.setSoDuSau(rs.getBigDecimal("so_du_sau"));
                ls.setGhiChu(rs.getString("noi_dung"));
                ls.setNgayTao(rs.getTimestamp("ngay_tao"));

                list.add(ls);
            }
        }

        return list;
    }
}