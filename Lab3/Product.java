package Lab3;

/** Một sản phẩm trong catalog — ảnh lấy từ thư mục {@code data/}. */
public final class Product {
    private final String title;
    private final String brand;
    private final double priceUsd;
    private final String subtext;
    private final String imageFileName;

    public Product(String title, String brand, double priceUsd, String subtext, String imageFileName) {
        this.title = title;
        this.brand = brand;
        this.priceUsd = priceUsd;
        this.subtext = subtext;
        this.imageFileName = imageFileName;
    }

    public String getTitle() {
        return title;
    }

    public String getBrand() {
        return brand;
    }

    public double getPriceUsd() {
        return priceUsd;
    }

    public String getSubtext() {
        return subtext;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    /** Tiêu đề rút gọn cho thẻ (giống mẫu "..."). */
    public String getShortTitle(int maxLen) {
        if (title.length() <= maxLen) {
            return title;
        }
        return title.substring(0, Math.max(0, maxLen - 1)).trim() + "…";
    }
}
