package com.example.model.dto;

import com.example.model.Category;
import com.example.model.dto.ProductAvatarDTO;
import com.example.model.dto.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductResDTO {
    private Long id;
    private String productName;
    private BigDecimal price;
    private Long quantity;
    private String description;
    private String avatarId;
    private String fileFolder;
    private String fileName;
    private String fileUrl;
    private Category category;

    public ProductDTO toProductDTO(ProductAvatarDTO productAvatarDTO){
        return new ProductDTO()
                .setId(id)
                .setProductName(productName)
                .setPrice(price)
                .setQuantity(quantity)
                .setDescription(description)
                .setCategory(category)
                .setProductAvatar(productAvatarDTO)
                ;
    }
}
