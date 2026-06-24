package org.yearup.DTO;

public class ProductSearchDto {
    private int productId;
    private String name;
    private double price;
    private String subCategory;
    private String imageUrl;
    public ProductSearchDto(int productId, String name, double price,
                            String subCategory, String imageUrl) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.subCategory = subCategory;
        this.imageUrl = imageUrl;
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

    public String getSubCategory() {
        return subCategory;
    }

    public String getImageUrl() {
        return imageUrl;
    }

}
