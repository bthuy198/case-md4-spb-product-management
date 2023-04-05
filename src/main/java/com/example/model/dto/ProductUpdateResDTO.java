package com.example.model.dto;

import com.example.model.Category;
import com.example.model.Product;
import com.example.model.ProductAvatar;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductUpdateResDTO {
    private Long id;
    private String productName;
    private BigDecimal price;
    private Long quantity;
    private String description;
    private Category category;
    private ProductAvatarDTO productAvatar;
    public ProductUpdateResDTO(Product product, ProductAvatar productAvatar){
        this.id = product.getId();
        this.productName = product.getProductName();
        this.price = product.getPrice();
        this.quantity = product.getQuantity();
        this.description = product.getDescription();
        this.category = product.getCategory();
        this.productAvatar = productAvatar.toProductAvatarDTO();
    }
}
