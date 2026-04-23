package dao;

import utils.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;

public class ThongKeVonShopDAO {
    private final VonShopDAO vonShopDAO = new VonShopDAO();

    public BigDecimal laySoDuHienTai() throws Exception {
        try (Connection conn = DBConnection.getConnection()) {
            vonShopDAO.congVonHangNgayNeuCan(conn);
            return vonShopDAO.getSoDuHienTai(conn);
        }
    }
}