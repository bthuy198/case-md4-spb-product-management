package com.example.service.product;

import com.example.exception.DataInputException;
import com.example.model.Category;
import com.example.model.Product;
import com.example.model.ProductAvatar;
import com.example.model.dto.*;
import com.example.repository.ProductAvatarRepository;
import com.example.repository.ProductRepository;
import com.example.service.uploadMedia.UploadService;
import com.example.utils.UploadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class ProductServiceImpl implements IProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductAvatarRepository productAvatarRepository;

    @Autowired
    private UploadUtils uploadUtils;
    @Autowired
    private UploadService uploadService;

    static final String fileName = "null-image.png";
    static final String fileFolder = "product_images";
    static final String fileURL = "https://res.cloudinary.com/dank9jrti/image/upload/v1680255909/product_images/null-image.png";
    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public List<ProductDTO> getAllProductsDTODeletedIsFalse() {
        return productRepository.getAllProductsDTODeletedIsFalse();
    }

    @Override
    public List<ProductResDTO> getAllProductResDTODeletedIsFalse() {
        return productRepository.getAllProductResDTODeletedIsFalse();
    }

//    @Override
//    public List<Product> findAllByDeletedIsFalse(Pageable pageable) {
//        return productRepository.findAllByDeletedIsFalse(pageable);
//    }

    @Override
    public Page<ProductResDTO> findAllPagesByDeletedIsFalse(Pageable pageable) {
        Page<ProductResDTO> productResDTOS = productRepository.findAllPagesByDeletedIsFalse(pageable);
        return productResDTOS;
    }

    @Override
    public Page<ProductResDTO> findAllPagesByKeySearchAndDeletedIsFalse(String keySearch, Pageable pageable) {
        return productRepository.findAllPagesByKeySearchAndDeletedIsFalse(keySearch, pageable);
    }

    @Override
    public Optional<ProductResDTO> getProductResDTOByIdDeletedIsFalse(Long id) {
        return productRepository.getProductResDTOByIdDeletedIsFalse(id);
    }

    @Override
    public List<ProductResDTO> getProductByDeletedIsFalseAndNameLike(String name) {
//        if(name != null){
//            return productRepository.getProductByDeletedIsFalseAndNameLike(name);
//        }
//        return productRepository.getAllProductResDTODeletedIsFalse();
        return productRepository.getProductByDeletedIsFalseAndNameLike(name);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public List<Product> getProductByDeletedIsFalseAndProductNameLike(String name) {
        return productRepository.getProductByDeletedIsFalseAndProductNameLike(name);
    }

    @Override
    public Boolean existById(Long id) {
        return productRepository.existsById(id);
    }

    @Override
    public ProductCreateResDTO create(Category category, ProductCreateReqDTO productCreateReqDTO) {
        ProductAvatar productAvatar = new ProductAvatar();
        productAvatarRepository.save(productAvatar);

        if (productCreateReqDTO.getAvatarFile() == null) {
            productAvatar.setFileFolder(fileFolder);
            productAvatar.setFileName(fileName);
            productAvatar.setFileUrl(fileURL);
            productAvatar.setCloudId(fileFolder + "/" + productAvatar.getId());
        } else {
            uploadAndSaveProductAvatar(productCreateReqDTO.getAvatarFile(), productAvatar);
        }

        Product product = productCreateReqDTO.toProduct(category, productAvatar);
        product.setId(null);
        productRepository.save(product);

        ProductCreateResDTO productCreateResDTO = new ProductCreateResDTO(product, productAvatar);

        return productCreateResDTO;
    }

    private void uploadAndSaveProductAvatar(MultipartFile file, ProductAvatar productAvatar) {
        try {
            Map uploadResult = uploadService.uploadImage(file, uploadUtils.buildImageUploadParams(productAvatar));
            String fileUrl = (String) uploadResult.get("secure_url");
            String fileFormat = (String) uploadResult.get("format");

            productAvatar.setFileName(productAvatar.getId() + "." + fileFormat);
            productAvatar.setFileUrl(fileUrl);
            productAvatar.setFileFolder(uploadUtils.IMAGE_UPLOAD_FOLDER);
            productAvatar.setCloudId(productAvatar.getFileFolder() + "/" + productAvatar.getId());
            productAvatarRepository.save(productAvatar);

        } catch (IOException e) {
            e.printStackTrace();
            throw new DataInputException("Upload hình ảnh thất bại");
        }
    }

    @Override
    public ProductUpdateResDTO update(Product product) {
        productRepository.save(product);
        return new ProductUpdateResDTO(product, product.getProductAvatar());
    }

    @Override
    public ProductUpdateResDTO updateWithAvatar(MultipartFile avatarFile,Long id, ProductUpdateReqDTO productUpdateReqDTO) throws IOException {
        //Tổng hợp lại kiến thức

        //3 dòng đầu này để xử lý avatar
        ProductAvatar productAvatar = productRepository.findById(id).get().getProductAvatar();
        uploadService.destroyImage(productAvatar.getCloudId(), uploadUtils.buildImageUploadParams(productAvatar));
        uploadAndSaveProductAvatar(avatarFile, productAvatar);

        //2 dòng này xử lý category
        Category category = new Category();
        category.setId(Long.parseLong(productUpdateReqDTO.getCategoryId()));
        category.setCategoryName(productUpdateReqDTO.getCategoryName());


        //dòng còn lại xử lý các trường còn lại trong product
        Product product = productUpdateReqDTO.toProduct(category, productAvatar);
        product.setId(id);
        product = productRepository.save(product);

        //chuyển sang res để render
        return new ProductUpdateResDTO(product, productAvatar);
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void delete(Product product) {
        product.setDeleted(true);
        productRepository.save(product);
    }

    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }
}
