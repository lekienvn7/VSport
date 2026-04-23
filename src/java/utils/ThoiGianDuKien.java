package utils;

public class ThoiGianDuKien {

    public static long parseToMillis(String thoiGianDuKien) {
        if (thoiGianDuKien == null || thoiGianDuKien.trim().isEmpty()) {
            return 0;
        }

        String text = thoiGianDuKien.trim().toLowerCase();

        int maxValue = laySoLonNhat(text);
        if (maxValue <= 0) {
            return 0;
        }

        if (text.contains("phút")) {
            return maxValue * 60L * 1000L;
        }

        if (text.contains("giờ")) {
            return maxValue * 60L * 60L * 1000L;
        }

        if (text.contains("ngày")) {
            return maxValue * 24L * 60L * 60L * 1000L;
        }

        return 0;
    }

    private static int laySoLonNhat(String text) {
        String[] parts = text.split("[^0-9]+");
        int max = 0;

        for (String part : parts) {
            if (part != null && !part.isEmpty()) {
                int value = Integer.parseInt(part);
                if (value > max) {
                    max = value;
                }
            }
        }

        return max;
    }
}