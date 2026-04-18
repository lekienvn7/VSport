package dao;

import model.BoLoc;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BoLocSanPhamDAO {

    public List<BoLoc> getLoaiSanPhamOptions(String doiSlug) {
        List<BoLoc> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
            SELECT 
                sp.loai_san_pham AS value,
                sp.loai_san_pham AS label,
                COUNT(DISTINCT sp.ma_san_pham) AS total
            FROM san_pham sp
            LEFT JOIN doi_bong db ON sp.ma_doi_bong = db.ma_doi_bong
            WHERE sp.trang_thai = 'dang_ban'
        """);

        if (doiSlug != null && !doiSlug.trim().isEmpty()) {
            sql.append(" AND db.doi_slug = ? ");
        }

        sql.append("""
            GROUP BY sp.loai_san_pham
            ORDER BY total DESC, sp.loai_san_pham ASC
        """);

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql.toString())
        ) {
            if (doiSlug != null && !doiSlug.trim().isEmpty()) {
                ps.setString(1, doiSlug);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String value = rs.getString("value");
                String label = rs.getString("label");
                int count = rs.getInt("total");

                if (value != null && !value.trim().isEmpty()) {
                    list.add(new BoLoc(value, label, count));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<BoLoc> getThuongHieuOptions(String doiSlug) {
        List<BoLoc> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
            SELECT
                CAST(th.ma_thuong_hieu AS CHAR) AS value,
                th.ten_thuong_hieu AS label,
                COUNT(DISTINCT sp.ma_san_pham) AS total
            FROM thuong_hieu th
            JOIN san_pham sp ON sp.ma_thuong_hieu = th.ma_thuong_hieu
            LEFT JOIN doi_bong db ON sp.ma_doi_bong = db.ma_doi_bong
            WHERE sp.trang_thai = 'dang_ban'
        """);

        if (doiSlug != null && !doiSlug.trim().isEmpty()) {
            sql.append(" AND db.doi_slug = ? ");
        }

        sql.append("""
            GROUP BY th.ma_thuong_hieu, th.ten_thuong_hieu
            ORDER BY total DESC, th.ten_thuong_hieu ASC
        """);

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql.toString())
        ) {
            if (doiSlug != null && !doiSlug.trim().isEmpty()) {
                ps.setString(1, doiSlug);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new BoLoc(
                    rs.getString("value"),
                    rs.getString("label"),
                    rs.getInt("total")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<BoLoc> getSizeOptions(String doiSlug) {
        List<BoLoc> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
            SELECT
                CAST(s.ma_size AS CHAR) AS value,
                s.ten_size AS label,
                COUNT(DISTINCT sp.ma_san_pham) AS total
            FROM size_san_pham s
            JOIN bien_the_san_pham bt ON bt.ma_size = s.ma_size
            JOIN san_pham sp ON sp.ma_san_pham = bt.ma_san_pham
            LEFT JOIN doi_bong db ON sp.ma_doi_bong = db.ma_doi_bong
            WHERE sp.trang_thai = 'dang_ban'
              AND bt.so_luong_ton > 0
        """);

        if (doiSlug != null && !doiSlug.trim().isEmpty()) {
            sql.append(" AND db.doi_slug = ? ");
        }

        sql.append("""
            GROUP BY s.ma_size, s.ten_size
            ORDER BY 
                CASE s.ten_size
                    WHEN 'XS' THEN 1
                    WHEN 'S' THEN 2
                    WHEN 'M' THEN 3
                    WHEN 'L' THEN 4
                    WHEN 'XL' THEN 5
                    WHEN '2XL' THEN 6
                    ELSE 99
                END ASC
        """);

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql.toString())
        ) {
            if (doiSlug != null && !doiSlug.trim().isEmpty()) {
                ps.setString(1, doiSlug);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new BoLoc(
                    rs.getString("value"),
                    rs.getString("label"),
                    rs.getInt("total")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public int demSanPhamSauLoc(
            String doiSlug,
            String[] loaiList,
            String[] thuongHieuList,
            String[] sizeList,
            String giaMin,
            String giaMax
    ) {
        int total = 0;

        List<Object> params = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
            SELECT COUNT(DISTINCT sp.ma_san_pham) AS total
            FROM san_pham sp
            LEFT JOIN doi_bong db ON sp.ma_doi_bong = db.ma_doi_bong
            WHERE sp.trang_thai = 'dang_ban'
        """);

        if (doiSlug != null && !doiSlug.trim().isEmpty()) {
            sql.append(" AND db.doi_slug = ? ");
            params.add(doiSlug);
        }

        if (loaiList != null && loaiList.length > 0) {
            sql.append(" AND sp.loai_san_pham IN (");
            appendPlaceholders(sql, loaiList.length);
            sql.append(") ");
            for (String s : loaiList) params.add(s);
        }

        if (thuongHieuList != null && thuongHieuList.length > 0) {
            sql.append(" AND sp.ma_thuong_hieu IN (");
            appendPlaceholders(sql, thuongHieuList.length);
            sql.append(") ");
            for (String s : thuongHieuList) params.add(Integer.parseInt(s));
        }

        if (giaMin != null && !giaMin.trim().isEmpty()) {
            sql.append(" AND sp.gia_san_pham >= ? ");
            params.add(Double.parseDouble(giaMin));
        }

        if (giaMax != null && !giaMax.trim().isEmpty()) {
            sql.append(" AND sp.gia_san_pham <= ? ");
            params.add(Double.parseDouble(giaMax));
        }

        if (sizeList != null && sizeList.length > 0) {
            sql.append("""
                AND EXISTS (
                    SELECT 1
                    FROM bien_the_san_pham bt
                    WHERE bt.ma_san_pham = sp.ma_san_pham
                      AND bt.so_luong_ton > 0
                      AND bt.ma_size IN (
            """);
            appendPlaceholders(sql, sizeList.length);
            sql.append(")) ");

            for (String s : sizeList) params.add(Integer.parseInt(s));
        }

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql.toString())
        ) {
            setParams(ps, params);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                total = rs.getInt("total");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }

    private void appendPlaceholders(StringBuilder sql, int length) {
        for (int i = 0; i < length; i++) {
            sql.append("?");
            if (i < length - 1) {
                sql.append(",");
            }
        }
    }

    private void setParams(PreparedStatement ps, List<Object> params) throws Exception {
        for (int i = 0; i < params.size(); i++) {
            Object value = params.get(i);

            if (value instanceof Integer) {
                ps.setInt(i + 1, (Integer) value);
            } else if (value instanceof Double) {
                ps.setDouble(i + 1, (Double) value);
            } else {
                ps.setString(i + 1, String.valueOf(value));
            }
        }
    }
}