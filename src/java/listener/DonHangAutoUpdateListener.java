package listener;

import dao.DonHangDAO;
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

        scheduler.scheduleAtFixedRate(() -> {
            try {
                DonHangDAO dao = new DonHangDAO();
                int a = dao.tuDongChuyenChoLayHangSau2Phut();
                int b = dao.capNhatDonHangDaGiaoVaCongDaBan();
                int c = dao.capNhatThanhToanSauKhiHoanThanh();

                if (a > 0 || b > 0 || c > 0) {
                    System.out.println("Auto update: " + (a+b+c) + " đơn thay đổi");
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