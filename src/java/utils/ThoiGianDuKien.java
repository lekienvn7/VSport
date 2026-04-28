package utils;

public class ThoiGianDuKien {

    public static long parseToMillis(String text) {
    if (text == null || text.isEmpty()) return 0;

    text = text.toLowerCase();

    long total = 0;

    // phút
    total += extract(text, "phút") * 60L * 1000L;

    // giờ
    total += extract(text, "giờ") * 60L * 60L * 1000L;

    // ngày
    total += extract(text, "ngày") * 24L * 60L * 60L * 1000L;

    return total;
    }

    private static int extract(String text, String unit) {
        int index = text.indexOf(unit);
        if (index == -1) return 0;

        String sub = text.substring(0, index);
        String[] nums = sub.split("[^0-9]+");

        if (nums.length > 0) {
            return Integer.parseInt(nums[nums.length - 1]);
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