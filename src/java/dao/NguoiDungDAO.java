package dao;

import model.NguoiDung;
import utils.DBConnection;
import java.util.Date;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class NguoiDungDAO {

    public NguoiDung getById(int maNguoiDung) {
        String sql = "SELECT * FROM nguoi_dung WHERE ma_nguoi_dung = ? LIMIT 1";

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, maNguoiDung);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToNguoiDung(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public NguoiDung getByEmail(String email) {
        String sql = "SELECT * FROM nguoi_dung WHERE email = ? LIMIT 1";

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToNguoiDung(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public NguoiDung getByPhone(String soDienThoai) {
        String sql = "SELECT * FROM nguoi_dung WHERE so_dien_thoai = ? LIMIT 1";

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, soDienThoai);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToNguoiDung(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public NguoiDung login(String dangNhap) {
        String sql = """
            SELECT *
            FROM nguoi_dung
            WHERE (email = ? OR so_dien_thoai = ?)
              AND trang_thai = 'hoat_dong'
            LIMIT 1
        """;

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, dangNhap);
            ps.setString(2, dangNhap);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToNguoiDung(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public NguoiDung findByEmailOrPhone(String dangNhap) {
        String sql = """
            SELECT *
            FROM nguoi_dung
            WHERE email = ? OR so_dien_thoai = ?
            LIMIT 1
        """;

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, dangNhap);
            ps.setString(2, dangNhap);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToNguoiDung(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean insert(NguoiDung nd) {
        String sql = """
            INSERT INTO nguoi_dung
            (ho_ten, email, so_dien_thoai, mat_khau, vai_tro, trang_thai, so_xu)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, nd.getHoTen());
            ps.setString(2, nd.getEmail());
            ps.setString(3, nd.getSoDienThoai());
            ps.setString(4, nd.getMatKhau());
            ps.setString(5, nd.getVaiTro());
            ps.setString(6, nd.getTrangThai());
            ps.setInt(7, nd.getSoXu());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    private NguoiDung mapResultSetToNguoiDung(ResultSet rs) throws Exception {
    NguoiDung nd = new NguoiDung();
    nd.setMaNguoiDung(rs.getInt("ma_nguoi_dung"));
    nd.setHoTen(rs.getString("ho_ten"));
    nd.setEmail(rs.getString("email"));
    nd.setSoDienThoai(rs.getString("so_dien_thoai"));
    nd.setMatKhau(rs.getString("mat_khau"));
    nd.setVaiTro(rs.getString("vai_tro"));
    nd.setTrangThai(rs.getString("trang_thai"));
    nd.setNgayTao(rs.getTimestamp("ngay_tao"));
    nd.setSoXu(rs.getInt("so_xu"));

    nd.setAvatar(rs.getString("avatar"));
    nd.setDiaChi(rs.getString("dia_chi"));
    nd.setNgaySinh(rs.getDate("ngay_sinh"));
    nd.setLanNhanXuDangNhapCuoi(rs.getDate("lan_nhan_xu_dang_nhap_cuoi"));

    return nd;
}
    
    public boolean updateThongTinCaNhan(NguoiDung nd) {
    String sql = """
        UPDATE nguoi_dung
        SET ho_ten = ?,
            email = ?,
            so_dien_thoai = ?,
            dia_chi = ?,
            ngay_sinh = ?,
            avatar = ?
        WHERE ma_nguoi_dung = ?
    """;

    try (
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)
    ) {
        ps.setString(1, nd.getHoTen());
        ps.setString(2, nd.getEmail());
        ps.setString(3, nd.getSoDienThoai());
        ps.setString(4, nd.getDiaChi());
        if (nd.getNgaySinh() != null) {
            ps.setDate(5, new java.sql.Date(nd.getNgaySinh().getTime()));
        } else {
            ps.setNull(5, java.sql.Types.DATE);
        }
        ps.setString(6, nd.getAvatar());
        ps.setInt(7, nd.getMaNguoiDung());

        return ps.executeUpdate() > 0;
    } catch (Exception e) {
        e.printStackTrace();
    }

    return false;
}
    
    public NguoiDung getByEmailExceptId(String email, int maNguoiDung) {
    String sql = """
        SELECT *
        FROM nguoi_dung
        WHERE email = ?
          AND ma_nguoi_dung <> ?
        LIMIT 1
    """;

    try (
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)
    ) {
        ps.setString(1, email);
        ps.setInt(2, maNguoiDung);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return mapResultSetToNguoiDung(rs);
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    return null;
}

public NguoiDung getByPhoneExceptId(String soDienThoai, int maNguoiDung) {
    String sql = """
        SELECT *
        FROM nguoi_dung
        WHERE so_dien_thoai = ?
          AND ma_nguoi_dung <> ?
        LIMIT 1
    """;

    try (
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)
    ) {
        ps.setString(1, soDienThoai);
        ps.setInt(2, maNguoiDung);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return mapResultSetToNguoiDung(rs);
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    return null;
}
}