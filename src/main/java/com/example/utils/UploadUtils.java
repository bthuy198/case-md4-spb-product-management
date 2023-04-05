package com.example.utils;

import com.example.exception.DataInputException;
import com.example.model.Product;
import com.cloudinary.utils.ObjectUtils;
import com.example.model.ProductAvatar;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UploadUtils {
    public final String IMAGE_UPLOAD_FOLDER = "product_images";
    public final String VIDEO_UPLOAD_FOLDER = "product_videos";

    public Map buildImageUploadParams(ProductAvatar productAvatar) {
        if (productAvatar == null || productAvatar.getId() == null)
            throw new DataInputException("Không thể upload hình ảnh của sản phẩm chưa được lưu");

        String publicId = String.format("%s/%s", IMAGE_UPLOAD_FOLDER, productAvatar.getId());

        return ObjectUtils.asMap(
                "public_id", publicId,
                "overwrite", true,
                "resource_type", "image"
        );
    }

    public Map buildImageDestroyParams(Product product, String publicId) {
        if (product == null || product.getId() == null)
            throw new DataInputException("Không thể destroy hình ảnh của sản phẩm không xác định");

        return ObjectUtils.asMap(
                "public_id", publicId,
                "overwrite", true,
                "resource_type", "image"
        );
    }

    public Map buildVideoUploadParams(ProductAvatar productAvatar) {
        if (productAvatar == null || productAvatar.getId() == null)
            throw new DataInputException("Không thể upload video của sản phẩm chưa được lưu");

        String publicId = String.format("%s/%s", VIDEO_UPLOAD_FOLDER, productAvatar.getId());

        return ObjectUtils.asMap(
                "public_id", publicId,
                "overwrite", true,
                "resource_type", "video"
        );
    }

    public Map buildVideoDestroyParams(Product product, String publicId) {
        if (product == null || product.getId() == null)
            throw new DataInputException("Không thể destroy video của sản phẩm không xác định");

        return ObjectUtils.asMap(
                "public_id", publicId,
                "overwrite", true,
                "resource_type", "video"
        );
    }
}
