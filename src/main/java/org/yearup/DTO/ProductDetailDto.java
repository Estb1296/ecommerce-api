package org.yearup.DTO;

public class ProductDetailDto {
    private int productId;
    private String name;
    private double price;
    private int categoryId;
    private String description;
    private String subCategory;
    private boolean featured;
    private String imageUrl;
    private int stock;

    public ProductDetailDto(int productId, String name, double price, int categoryId,
                            String description, String subCategory, boolean featured, String imageUrl, int stock) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.categoryId = categoryId;
        this.description = description;
        this.subCategory = subCategory;
        this.featured = featured;
        this.imageUrl = imageUrl;
        this.stock = stock;
    }

    public int getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getDescription() {
        return description;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public boolean isFeatured() {
        return featured;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getStock() {
        return stock;
    }

}
