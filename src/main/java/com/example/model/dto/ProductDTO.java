package com.example.model.dto;

import com.example.model.Category;
import com.example.model.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductDTO {
    private Long id;
    private String productName;
    private BigDecimal price;
    private Long quantity;
    private String description;
    private Category category;
    private ProductAvatarDTO productAvatar;

    public ProductDTO(Long id, String productName, BigDecimal price, Long quantity, String description, Category category) {
        this.id = id;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.category = category;
    }

    public Product toProduct(){
        return new Product()
                .setId(id)
                .setProductName(productName)
                .setPrice(price)
                .setQuantity(quantity)
                .setDescription(description)
                .setCategory(category)
                .setProductAvatar(productAvatar.toProductAvatar())
                ;
    }
}
