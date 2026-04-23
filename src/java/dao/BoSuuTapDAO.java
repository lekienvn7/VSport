package dao;

import utils.DBConnection;
import model.BoSuuTap;
import model.SanPham;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import utils.DBConnection;

public class BoSuuTapDAO {

    public List<BoSuuTap> getAllBoSuuTapHienThi() {
        List<BoSuuTap> ds = new ArrayList<>();

        String sql = """
            SELECT ma_bo_suu_tap, ten_bo_suu_tap, bst_slug, mo_ta, anh_bia, trang_thai, ngay_tao
            FROM bo_suu_tap
            WHERE trang_thai = 'hien'
            ORDER BY ngay_tao DESC, ma_bo_suu_tap DESC
        """;

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
        ) {
            while (rs.next()) {
                BoSuuTap bst = new BoSuuTap();
                bst.setMaBoSuuTap(rs.getInt("ma_bo_suu_tap"));
                bst.setTenBoSuuTap(rs.getString("ten_bo_suu_tap"));
                bst.setBstSlug(rs.getString("bst_slug"));
                bst.setMoTa(rs.getString("mo_ta"));
                bst.setAnhBia(rs.getString("anh_bia"));
                bst.setTrangThai(rs.getString("trang_thai"));
                bst.setNgayTao(rs.getString("ngay_tao"));
                ds.add(bst);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ds;
    }

    public BoSuuTap getBoSuuTapBySlug(String slug) {
        String sql = """
            SELECT ma_bo_suu_tap, ten_bo_suu_tap, bst_slug, mo_ta, anh_bia, trang_thai, ngay_tao
            FROM bo_suu_tap
            WHERE bst_slug = ?
              AND trang_thai = 'hien'
            LIMIT 1
        """;

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
        ) {
            ps.setString(1, slug);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    BoSuuTap bst = new BoSuuTap();
                    bst.setMaBoSuuTap(rs.getInt("ma_bo_suu_tap"));
                    bst.setTenBoSuuTap(rs.getString("ten_bo_suu_tap"));
                    bst.setBstSlug(rs.getString("bst_slug"));
                    bst.setMoTa(rs.getString("mo_ta"));
                    bst.setAnhBia(rs.getString("anh_bia"));
                    bst.setTrangThai(rs.getString("trang_thai"));
                    bst.setNgayTao(rs.getString("ngay_tao"));
                    return bst;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<SanPham> getSanPhamTheoBoSuuTapSlug(String slug) {
        List<SanPham> ds = new ArrayList<>();

        String sql = """
            SELECT 
                sp.ma_san_pham,
                sp.ma_danh_muc,
                sp.ma_thuong_hieu,
                sp.ma_doi_bong,
                sp.ten_san_pham,
                sp.slug,
                sp.loai_san_pham,
                sp.mo_ta_ngan,
                sp.mo_ta_chi_tiet,
                sp.gia_niem_yet,
                sp.gia_khuyen_mai,
                sp.anh_chinh,
                sp.trang_thai,
                COALESCE(
                    CASE 
                        WHEN sp.gia_khuyen_mai IS NOT NULL AND sp.gia_khuyen_mai > 0 
                        THEN sp.gia_khuyen_mai
                        ELSE sp.gia_niem_yet
                    END,
                    0
                ) AS gia_san_pham
            FROM san_pham_bo_suu_tap sbst
            JOIN bo_suu_tap bst ON sbst.ma_bo_suu_tap = bst.ma_bo_suu_tap
            JOIN san_pham sp ON sbst.ma_san_pham = sp.ma_san_pham
            WHERE bst.bst_slug = ?
              AND bst.trang_thai = 'hien'
              AND sp.trang_thai = 'hien'
            ORDER BY sp.ma_san_pham DESC
        """;

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
        ) {
            ps.setString(1, slug);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SanPham sp = new SanPham();
                    sp.setMaSanPham(rs.getInt("ma_san_pham"));
                    sp.setMaDanhMuc(rs.getInt("ma_danh_muc"));
                    sp.setMaThuongHieu(rs.getInt("ma_thuong_hieu"));
                    sp.setMaDoiBong(rs.getInt("ma_doi_bong"));
                    sp.setTenSanPham(rs.getString("ten_san_pham"));
                    sp.setSlug(rs.getString("slug"));
                    sp.setLoaiSanPham(rs.getString("loai_san_pham"));
                    sp.setMoTaNgan(rs.getString("mo_ta_ngan"));
                    sp.setMoTaChiTiet(rs.getString("mo_ta_chi_tiet"));
                    sp.setGiaNiemYet(rs.getDouble("gia_niem_yet"));
                    sp.setGiaKhuyenMai(rs.getDouble("gia_khuyen_mai"));
                    sp.setGiaSanPham(rs.getDouble("gia_san_pham"));
                    sp.setAnhChinh(rs.getString("anh_chinh"));
                    sp.setTrangThai(rs.getString("trang_thai"));

                    ds.add(sp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ds;
    }

    public List<SanPham> getSanPhamTheoMaBoSuuTap(int maBoSuuTap) {
        List<SanPham> ds = new ArrayList<>();

        String sql = """
            SELECT 
                sp.ma_san_pham,
                sp.ma_danh_muc,
                sp.ma_thuong_hieu,
                sp.ma_doi_bong,
                sp.ten_san_pham,
                sp.slug,
                sp.loai_san_pham,
                sp.mo_ta_ngan,
                sp.mo_ta_chi_tiet,
                sp.gia_niem_yet,
                sp.gia_khuyen_mai,
                sp.anh_chinh,
                sp.trang_thai,
                COALESCE(
                    CASE 
                        WHEN sp.gia_khuyen_mai IS NOT NULL AND sp.gia_khuyen_mai > 0 
                        THEN sp.gia_khuyen_mai
                        ELSE sp.gia_niem_yet
                    END,
                    0
                ) AS gia_san_pham
            FROM san_pham_bo_suu_tap sbst
            JOIN san_pham sp ON sbst.ma_san_pham = sp.ma_san_pham
            WHERE sbst.ma_bo_suu_tap = ?
              AND sp.trang_thai = 'hien'
            ORDER BY sp.ma_san_pham DESC
        """;

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
        ) {
            ps.setInt(1, maBoSuuTap);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SanPham sp = new SanPham();
                    sp.setMaSanPham(rs.getInt("ma_san_pham"));
                    sp.setMaDanhMuc(rs.getInt("ma_danh_muc"));
                    sp.setMaThuongHieu(rs.getInt("ma_thuong_hieu"));
                    sp.setMaDoiBong(rs.getInt("ma_doi_bong"));
                    sp.setTenSanPham(rs.getString("ten_san_pham"));
                    sp.setSlug(rs.getString("slug"));
                    sp.setLoaiSanPham(rs.getString("loai_san_pham"));
                    sp.setMoTaNgan(rs.getString("mo_ta_ngan"));
                    sp.setMoTaChiTiet(rs.getString("mo_ta_chi_tiet"));
                    sp.setGiaNiemYet(rs.getDouble("gia_niem_yet"));
                    sp.setGiaKhuyenMai(rs.getDouble("gia_khuyen_mai"));
                    sp.setGiaSanPham(rs.getDouble("gia_san_pham"));
                    sp.setAnhChinh(rs.getString("anh_chinh"));
                    sp.setTrangThai(rs.getString("trang_thai"));

                    ds.add(sp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ds;
    }
}