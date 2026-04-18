package dao;

import model.DoiBong;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DoiBongDao {

    public List<DoiBong> getAllDoiBong() {
        List<DoiBong> list = new ArrayList<>();

        String sql = "SELECT * FROM doi_bong ORDER BY ten_doi_bong ASC";

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
        ) {
            while (rs.next()) {
                DoiBong doiBong = new DoiBong();
                doiBong.setMaDoiBong(rs.getInt("ma_doi_bong"));
                doiBong.setTenDoiBong(rs.getString("ten_doi_bong"));
                doiBong.setDoiSlug(rs.getString("doi_slug"));
                doiBong.setFanClub(rs.getString("fan_club"));
                doiBong.setLoai(rs.getString("loai"));
                doiBong.setLogo(rs.getString("logo"));
                list.add(doiBong);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public DoiBong getDoiBongBySlug(String slug) {
        String sql = "SELECT * FROM doi_bong WHERE doi_slug = ? LIMIT 1";

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
        ) {
            ps.setString(1, slug);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    DoiBong doiBong = new DoiBong();
                    doiBong.setMaDoiBong(rs.getInt("ma_doi_bong"));
                    doiBong.setTenDoiBong(rs.getString("ten_doi_bong"));
                    doiBong.setDoiSlug(rs.getString("doi_slug"));
                    doiBong.setFanClub(rs.getString("fan_club"));
                    doiBong.setLoai(rs.getString("loai"));
                    doiBong.setLogo(rs.getString("logo"));
                    return doiBong;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}