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

    public List<BoLoc> getSizeOptions(String doiSlug, String... loaiSizes) {
    List<BoLoc> list = new ArrayList<>();
    List<Object> params = new ArrayList<>();

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
        params.add(doiSlug.trim());
    }

    if (loaiSizes != null && loaiSizes.length > 0) {
        List<String> validLoaiSizes = new ArrayList<>();
        for (String item : loaiSizes) {
            if (item != null && !item.trim().isEmpty()) {
                validLoaiSizes.add(item.trim());
            }
        }

        if (!validLoaiSizes.isEmpty()) {
            sql.append(" AND s.loai_size IN (");
            appendPlaceholders(sql, validLoaiSizes.size());
            sql.append(") ");
            params.addAll(validLoaiSizes);
        }
    }

    sql.append("""
        GROUP BY s.ma_size, s.ten_size, s.loai_size
        ORDER BY
            CASE
                WHEN s.loai_size = 'ao' THEN 1
                WHEN s.loai_size = 'gang' THEN 2
                WHEN s.loai_size = 'giay' THEN 3
                WHEN s.loai_size = 'bong' THEN 4
                ELSE 99
            END ASC,

            CASE
                WHEN s.ten_size = 'XS' THEN 1
                WHEN s.ten_size = 'S' THEN 2
                WHEN s.ten_size = 'M' THEN 3
                WHEN s.ten_size = 'L' THEN 4
                WHEN s.ten_size = 'XL' THEN 5
                WHEN s.ten_size = '2XL' THEN 6

                WHEN s.ten_size LIKE 'Size %'
                    THEN CAST(SUBSTRING_INDEX(SUBSTRING_INDEX(s.ten_size, ' (', 1), ' ', -1) AS UNSIGNED)

                WHEN s.ten_size REGEXP '^[0-9]+'
                    THEN CAST(SUBSTRING_INDEX(s.ten_size, ' ', 1) AS UNSIGNED)

                ELSE 999
            END ASC,

            s.ten_size ASC
    """);

    try (
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql.toString())
    ) {
        setParams(ps, params);

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

    public List<BoLoc> getLoaiSanPhamOptionsTheoDanhMuc(int maDanhMuc) {
        List<BoLoc> list = new ArrayList<>();

        String sql = """
            SELECT sp.loai_san_pham AS value, COUNT(*) AS total
            FROM san_pham sp
            WHERE sp.trang_thai = 'dang_ban'
              AND sp.ma_danh_muc = ?
              AND sp.loai_san_pham IS NOT NULL
              AND sp.loai_san_pham <> ''
            GROUP BY sp.loai_san_pham
            ORDER BY sp.loai_san_pham ASC
        """;

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, maDanhMuc);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                BoLoc item = new BoLoc();
                item.setValue(rs.getString("value"));
                item.setLabel(formatLoaiSanPhamLabel(rs.getString("value")));
                item.setCount(rs.getInt("total"));
                list.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<BoLoc> getThuongHieuOptionsTheoDanhMuc(int maDanhMuc) {
        List<BoLoc> list = new ArrayList<>();

        String sql = """
            SELECT 
                th.ma_thuong_hieu AS value,
                th.ten_thuong_hieu AS label,
                COUNT(sp.ma_san_pham) AS total
            FROM thuong_hieu th
            INNER JOIN san_pham sp ON sp.ma_thuong_hieu = th.ma_thuong_hieu
            WHERE sp.trang_thai = 'dang_ban'
              AND sp.ma_danh_muc = ?
            GROUP BY th.ma_thuong_hieu, th.ten_thuong_hieu
            ORDER BY th.ten_thuong_hieu ASC
        """;

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, maDanhMuc);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                BoLoc item = new BoLoc();
                item.setValue(String.valueOf(rs.getInt("value")));
                item.setLabel(rs.getString("label"));
                item.setCount(rs.getInt("total"));
                list.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<BoLoc> getSizeOptionsTheoDanhMuc(int maDanhMuc, String... loaiSizes) {
    List<BoLoc> list = new ArrayList<>();
    List<Object> params = new ArrayList<>();

    StringBuilder sql = new StringBuilder("""
        SELECT
            sz.ma_size AS value,
            sz.ten_size AS label,
            COUNT(DISTINCT sp.ma_san_pham) AS total
        FROM size_san_pham sz
        INNER JOIN bien_the_san_pham bt ON bt.ma_size = sz.ma_size
        INNER JOIN san_pham sp ON sp.ma_san_pham = bt.ma_san_pham
        WHERE sp.trang_thai = 'dang_ban'
          AND sp.ma_danh_muc = ?
          AND bt.so_luong_ton > 0
    """);

    params.add(maDanhMuc);

    appendLoaiSizeCondition(sql, params, loaiSizes);

    sql.append("""
        GROUP BY sz.ma_size, sz.ten_size, sz.loai_size
        ORDER BY
            CASE
                WHEN sz.loai_size = 'ao' THEN 1
                WHEN sz.loai_size = 'gang' THEN 2
                WHEN sz.loai_size = 'giay' THEN 3
                WHEN sz.loai_size = 'bong' THEN 4
                ELSE 99
            END ASC,

            CASE
                WHEN sz.ten_size = 'XS' THEN 1
                WHEN sz.ten_size = 'S' THEN 2
                WHEN sz.ten_size = 'M' THEN 3
                WHEN sz.ten_size = 'L' THEN 4
                WHEN sz.ten_size = 'XL' THEN 5
                WHEN sz.ten_size = '2XL' THEN 6

                WHEN sz.ten_size LIKE 'Size %'
                    THEN CAST(SUBSTRING_INDEX(SUBSTRING_INDEX(sz.ten_size, ' (', 1), ' ', -1) AS UNSIGNED)

                WHEN sz.ten_size REGEXP '^[0-9]+'
                    THEN CAST(SUBSTRING_INDEX(sz.ten_size, ' ', 1) AS UNSIGNED)

                ELSE 999
            END ASC,

            sz.ten_size ASC
    """);

    try (
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql.toString())
    ) {
        setParams(ps, params);

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            BoLoc item = new BoLoc();
            item.setValue(String.valueOf(rs.getInt("value")));
            item.setLabel(rs.getString("label"));
            item.setCount(rs.getInt("total"));
            list.add(item);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}
    
    public List<BoLoc> getSizeOptionsTheoDanhMuc(int maDanhMuc) {
    return getSizeOptionsTheoDanhMuc(maDanhMuc, new String[0]);
}

    public int demSanPhamGiayGangSauLoc(
            int maDanhMuc,
            String[] loaiList,
            String[] thuongHieuList,
            String[] sizeList,
            String giaMin,
            String giaMax
    ) {
        List<Object> params = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
            SELECT COUNT(DISTINCT sp.ma_san_pham) AS total
            FROM san_pham sp
            WHERE sp.trang_thai = 'dang_ban'
              AND sp.ma_danh_muc = ?
        """);

        params.add(maDanhMuc);

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
                return rs.getInt("total");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    private String formatLoaiSanPhamLabel(String value) {
        if (value == null || value.trim().isEmpty()) return "";

        String label = value.trim().replace("-", " ").replace("_", " ");
        String[] words = label.split("\\s+");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            if (word.isEmpty()) continue;
            result.append(Character.toUpperCase(word.charAt(0)))
                  .append(word.substring(1))
                  .append(" ");
        }

        return result.toString().trim();
    }
    
    private void appendLoaiSizeCondition(StringBuilder sql, List<Object> params, String... loaiSizes) {
    if (loaiSizes == null || loaiSizes.length == 0) return;

    List<String> validLoaiSizes = new ArrayList<>();
    for (String item : loaiSizes) {
        if (item != null && !item.trim().isEmpty()) {
            validLoaiSizes.add(item.trim());
        }
    }

    if (validLoaiSizes.isEmpty()) return;

    sql.append(" AND sz.loai_size IN (");
    appendPlaceholders(sql, validLoaiSizes.size());
    sql.append(") ");

    params.addAll(validLoaiSizes);
}
}