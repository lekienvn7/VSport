package controller;

import dao.LichSanDAO;
import model.LichSan;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

public class ApiServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        String path = req.getPathInfo();
        try {
            LichSanDAO lichSanDAO = new LichSanDAO();

            if ("/lich-san".equals(path)) {
                // GET /api/lich-san?subSanId=1
                int subSanId = Integer.parseInt(req.getParameter("subSanId"));
                Map<String, List<LichSan>> lichSanMap = lichSanDAO.getLichSanTheoTuan(subSanId);
                out.print(toJson(lichSanMap));

            } else if ("/next-slots".equals(path)) {
                // GET /api/next-slots?subSanId=1&lichSanId=5
                int subSanId = Integer.parseInt(req.getParameter("subSanId"));
                int lichSanId = Integer.parseInt(req.getParameter("lichSanId"));
                List<LichSan> nextSlots = lichSanDAO.getNextTrongSlots(subSanId, lichSanId);
                out.print(listToJson(nextSlots));

            } else if ("/reset".equals(path)) {
                lichSanDAO.resetTrangThaiTuDong();
                out.print("{\"status\":\"ok\"}");
            } else {
                resp.setStatus(404);
                out.print("{\"error\":\"Not found\"}");
            }
        } catch (Exception e) {
            resp.setStatus(500);
            out.print("{\"error\":\"" + e.getMessage().replace("\"", "'") + "\"}");
        }
    }

    private String toJson(Map<String, List<LichSan>> map) {
        StringBuilder sb = new StringBuilder("{");
        boolean firstDay = true;
        for (Map.Entry<String, List<LichSan>> entry : map.entrySet()) {
            if (!firstDay) sb.append(",");
            firstDay = false;
            sb.append("\"").append(entry.getKey()).append("\":").append(listToJson(entry.getValue()));
        }
        sb.append("}");
        return sb.toString();
    }

    private String listToJson(List<LichSan> list) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) sb.append(",");
            LichSan ls = list.get(i);
            sb.append("{")
              .append("\"id\":").append(ls.getId()).append(",")
              .append("\"subSanId\":").append(ls.getSubSanId()).append(",")
              .append("\"ngay\":\"").append(ls.getNgay()).append("\",")
              .append("\"khungGioId\":").append(ls.getKhungGioId()).append(",")
              .append("\"trangThai\":\"").append(ls.getTrangThai()).append("\",")
              .append("\"tenKhung\":\"").append(ls.getTenKhung() != null ? ls.getTenKhung() : "").append("\",")
              .append("\"gioBatDau\":\"").append(ls.getGioBatDau() != null ? ls.getGioBatDau().toString().substring(0,5) : "").append("\",")
              .append("\"gioKetThuc\":\"").append(ls.getGioKetThuc() != null ? ls.getGioKetThuc().toString().substring(0,5) : "").append("\"")
              .append("}");
        }
        sb.append("]");
        return sb.toString();
    }
}