package com.example.model.dto;

import com.example.model.Category;
import com.example.model.Product;
import com.example.model.ProductAvatar;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductUpdateReqDTO implements Validator {
    private Long id;
    private String productName;
    private String price;
    private String quantity;
    private String description;
    private String categoryId;
    private String categoryName;
    @Override
    public boolean supports(Class<?> clazz) {
        return ProductUpdateReqDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ProductUpdateReqDTO productUpdateReqDTO = (ProductUpdateReqDTO) target;

        String productName = productUpdateReqDTO.getProductName();
        String price = productUpdateReqDTO.getPrice();
        String quantity = productUpdateReqDTO.getQuantity();
        String categoryId = productUpdateReqDTO.getCategoryId();

        if (productName.length() == 0) {
            errors.rejectValue("productName", "productName.null", "Product name must not be null");
        }

        if (price != null && price.length() > 0) {
            if (!price.matches("(^$|[0-9]*$)")) {
                errors.rejectValue("price", "price.number", "Price must be a number");
//                return;
            }
            if (price.length() > 8) {
                errors.rejectValue("price", "price.max", "The maximum product price is ten million VND.");
//                return;
            }
            if (price.length() < 5) {
                errors.rejectValue("price", "price.min", "The minimum product price is tem thousand VND.");
//                return;
            }
        } else {
            errors.rejectValue("price", "price.null", "Price must not be null");
        }

        if (quantity != null && quantity.length() > 0) {
            if (!quantity.matches("(^$|[0-9]*$)")) {
                errors.rejectValue("quantity", "quantity.number", "Quantity must be a number");
//                return;
            }
            if (Long.parseLong(quantity) < 0) {
                errors.rejectValue("quantity", "quantity.min", "The minimum product quantity is 0");
//                return;
            }
            if (quantity.length() > 4) {
                errors.rejectValue("quantity", "quantity.max", "The quantity of the product is not more than 1000.");
//                return;
            }
        } else {
            errors.rejectValue("quantity", "price.null", "Quantity must not be null");
        }

        if (categoryId != null && categoryId.length() > 0) {
            if (!categoryId.matches("(^$|[0-9]*$)")) {
                errors.rejectValue("categoryId", "categoryId.number", "Category's id must be a number");
            }
        } else {
            errors.rejectValue("categoryId", "categoryId.null", "Category's id must not be null");
        }

    }

    public ProductDTO toProductDTO(Category category, ProductAvatar productAvatar){
        return new ProductDTO()
                .setProductName(productName)
                .setPrice(BigDecimal.valueOf(Long.parseLong(price)))
                .setQuantity(Long.parseLong(quantity))
                .setDescription(description)
                .setCategory(category)
                .setProductAvatar(productAvatar.toProductAvatarDTO())
                ;
    }

    public ProductUpdateResDTO toProductUpdateResDTO(Category category, ProductAvatar productAvatar){
        return new ProductUpdateResDTO()
                .setProductName(productName)
                .setPrice(BigDecimal.valueOf(Long.parseLong(price)))
                .setQuantity(Long.parseLong(quantity))
                .setDescription(description)
                .setCategory(category)
                .setProductAvatar(productAvatar.toProductAvatarDTO())
                ;
    }

    public Product toProduct(Category category, ProductAvatar productAvatar){
        return new Product()
                .setProductName(productName)
                .setPrice(BigDecimal.valueOf(Long.parseLong(price)))
                .setQuantity(Long.parseLong(quantity))
                .setDescription(description)
                .setCategory(category)
                .setProductAvatar(productAvatar)
                ;
    }



}
