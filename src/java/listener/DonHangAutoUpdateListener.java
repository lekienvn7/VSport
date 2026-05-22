package listener;

import dao.DonHangDAO;
import dao.MaGiamGiaDAO;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@WebListener
public class DonHangAutoUpdateListener implements ServletContextListener {

    private ScheduledExecutorService scheduler;

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        scheduler = Executors.newSingleThreadScheduledExecutor();
        System.out.println("Listener STARTED - DonHangAutoUpdateListener is running");

        scheduler.scheduleAtFixedRate(() -> {
            try {
                System.out.println("⏱ Scheduler tick: " + new java.util.Date());
                DonHangDAO donHangDAO = new DonHangDAO();
                MaGiamGiaDAO maGiamGiaDAO = new MaGiamGiaDAO();

                // auto update đơn hàng
                int a = donHangDAO.tuDongChuyenChoLayHangSau2Phut();
                int b = donHangDAO.tuDongChuyenDangGiaoSau2PhutLayHang();
                int c = donHangDAO.capNhatDonHangDaGiaoVaCongDaBan();
                int d = donHangDAO.capNhatThanhToanSauKhiHoanThanh();

                // 🧨 thêm cái này
                maGiamGiaDAO.capNhatMaHetHan();
                
                maGiamGiaDAO.capNhatMaGiamGiaHetHan();

                if (a > 0 || b > 0 || c > 0 || d > 0) {
                    System.out.println(
                            "Auto update: "
                            + (a + b + c + d)
                            + " đơn thay đổi"
                            + " | voucher_checked=true"
                    );
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 30, TimeUnit.SECONDS);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }
}
