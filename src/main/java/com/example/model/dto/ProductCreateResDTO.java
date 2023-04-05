package com.example.model.dto;

import com.example.model.Category;
import com.example.model.Product;
import com.example.model.ProductAvatar;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductCreateResDTO {
    private Long id;
    private String productName;
    private BigDecimal price;
    private Long quantity;
    private String description;
    private Category category;
    private ProductAvatarDTO productAvatar;

    public ProductCreateResDTO(Product product, ProductAvatar productAvatar){
        this.id = product.getId();
        this.productName = product.getProductName();
        this.price = product.getPrice();
        this.quantity = product.getQuantity();
        this.description = product.getDescription();
        this.category = product.getCategory();
        this.productAvatar = productAvatar.toProductAvatarDTO();
    }
}
