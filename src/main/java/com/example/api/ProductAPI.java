package com.example.api;

import com.example.exception.DataInputException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.Category;
import com.example.model.Product;
import com.example.model.ProductAvatar;
import com.example.model.dto.*;
import com.example.repository.CategoryRepository;
import com.example.repository.ProductRepository;
import com.example.service.category.ICategoryService;
import com.example.service.product.IProductService;
import com.example.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductAPI {
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private IProductService productService;
    @Autowired
    private AppUtils appUtils;


    @GetMapping("/category")
    public ResponseEntity<?> getAllCategory() {
        List<Category> categories = categoryService.findAll();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

//    @GetMapping("")
//    public ResponseEntity<?> getAllProducts() {
//        List<ProductDTO> productDTOS = new ArrayList<>();
//        List<ProductResDTO> productResDTOS = productService.getAllProductResDTODeletedIsFalse();
//
//        for (ProductResDTO item : productResDTOS) {
//            ProductAvatarDTO productAvatarDTO = new ProductAvatarDTO();
//            productAvatarDTO.setId(item.getAvatarId());
//            productAvatarDTO.setFileName(item.getFileName());
//            productAvatarDTO.setFileFolder(item.getFileFolder());
//            productAvatarDTO.setCloudId(item.getFileUrl());
//            ProductDTO productDTO = item.toProductDTO(productAvatarDTO);
//            productDTOS.add(productDTO);
//        }
//        return new ResponseEntity<>(productDTOS, HttpStatus.OK);
//    }

    @GetMapping("/")
    public ResponseEntity<?> getAllProductPagingAndSorting(@PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 10) Pageable pageable) {
//        int page_size = 5;
//        int total = productService.getAllProductsDTODeletedIsFalse().size();
//        int page_count = (int) Math.ceil(total * 1.0 / page_size);

//        HashMap<Integer, List<ProductDTO>> result = new HashMap<>();
//        for(int i = 0; i < page_count; i++){
//            Pageable sortedByPriceDesc = PageRequest.of(i, page_size, Sort.by("price").descending());
//            List<Product> products = productService.findAllByDeletedIsFalse(sortedByPriceDesc);
//            List<ProductDTO> productDTOS = new ArrayList<>();
//            for(Product p : products){
//                ProductDTO productDTO = p.toProductDTO();
//                productDTOS.add(productDTO);
//            }
//            result.put(i, productDTOS);
//        }

        Page<ProductResDTO> productResDTOS = productService.findAllPagesByDeletedIsFalse(pageable);

        return new ResponseEntity<>(productResDTOS, HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<?> getAllProductByKeySearchPagingAndSorting(@RequestParam("q") String keySearch, @PageableDefault(sort = "price", direction = Sort.Direction.ASC, size = 5) Pageable pageable) {

//        pageable = PageRequest.of(currentPage, 5);

        keySearch = '%' + keySearch + '%';

        Page<ProductResDTO> productResDTOS = productService.findAllPagesByKeySearchAndDeletedIsFalse(keySearch, pageable);

//        if (productResDTOS.getTotalElements() == 0) {
//            pageable = PageRequest.of(currentPageNumber - 1, 5, Sort.by("id").descending());
//            productResDTOS = productService.findAllPagesByKeySearchAndDeletedIsFalse(keySearch, pageable);
//        }
        return new ResponseEntity<>(productResDTOS, HttpStatus.OK);
    }


    @PostMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        Optional<ProductResDTO> optionalProductResDTO = productService.getProductResDTOByIdDeletedIsFalse(id);

        if (!optionalProductResDTO.isPresent()) {
            throw new ResourceNotFoundException("Not found this product");
        }

        ProductResDTO productResDTO = optionalProductResDTO.get();

        ProductAvatarDTO productAvatarDTO = new ProductAvatarDTO();
        productAvatarDTO.setId(productResDTO.getAvatarId());
        productAvatarDTO.setFileFolder(productResDTO.getFileFolder());
        productAvatarDTO.setFileUrl(productResDTO.getFileUrl());
        productAvatarDTO.setFileName(productResDTO.getFileName());
        ProductDTO productDTO = productResDTO.toProductDTO(productAvatarDTO);

        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@Validated ProductCreateReqDTO productCreateReqDTO, BindingResult bindingResult) {
        new ProductCreateReqDTO().validate(productCreateReqDTO, bindingResult);

        Optional<Category> categoryOptional = categoryService.findById(Long.parseLong(productCreateReqDTO.getCategoryId()));

        if (!categoryOptional.isPresent()) {
            throw new ResourceNotFoundException("Not found this category");
        }

        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        ProductCreateResDTO productCreateResDTO = productService.create(categoryOptional.get(), productCreateReqDTO);
        return new ResponseEntity<>(productCreateResDTO, HttpStatus.CREATED);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, MultipartFile file, @Validated ProductUpdateReqDTO productUpdateReqDTO, BindingResult bindingResult) throws IOException {
        new ProductUpdateReqDTO().validate(productUpdateReqDTO, bindingResult);

        Optional<Product> productOptional = productService.findById(id);

        if (!productOptional.isPresent()) {
            throw new ResourceNotFoundException("Not found this product to update");
        }

        Optional<Category> categoryOptional = categoryService.findById(Long.parseLong(productUpdateReqDTO.getCategoryId()));

        if (!categoryOptional.isPresent()) {
            throw new DataInputException("Not found this category");
        }

        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        ProductUpdateResDTO productUpdateResDTO;

        if (file == null) {
            ProductAvatar productAvatar = productOptional.get().getProductAvatar();
            ProductDTO productDTO = productUpdateReqDTO.toProductDTO(categoryOptional.get(), productAvatar);
            Product product = productDTO.toProduct();
            product.setId(id);
            productUpdateResDTO = productService.update(product);
        } else {
            productUpdateResDTO = productService.updateWithAvatar(file, id, productUpdateReqDTO);
        }
        return new ResponseEntity<>(productUpdateResDTO, HttpStatus.OK);
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        Optional<Product> productOptional = productService.findById(id);

        if (!productOptional.isPresent()) {
            throw new ResourceNotFoundException("Not found this product to delete");
        }

        productService.delete(productOptional.get());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/search/q={key}")
    public ResponseEntity<?> searchByProductName(@PathVariable("key") String key) {
//        List<ProductDTO> productDTOS = new ArrayList<>();
//        List<ProductResDTO> productResDTOS = productService.getProductByDeletedIsFalseAndNameLike(key);
//
//        for (ProductResDTO item : productResDTOS) {
//            ProductAvatarDTO productAvatarDTO = new ProductAvatarDTO();
//            productAvatarDTO.setId(item.getAvatarId());
//            productAvatarDTO.setFileName(item.getFileName());
//            productAvatarDTO.setFileFolder(item.getFileFolder());
//            productAvatarDTO.setCloudId(item.getFileUrl());
//            ProductDTO productDTO = item.toProductDTO(productAvatarDTO);
//            productDTOS.add(productDTO);
//        }
//        return new ResponseEntity<>(productDTOS, HttpStatus.OK);

//        if(key == null || key.equals("")){
//            List<ProductDTO> productDTOS = productService.getAllProductsDTODeletedIsFalse();
//            return new ResponseEntity<>(productDTOS, HttpStatus.OK);
//        }
        key = "%" + key + "%";
        List<Product> products = productService.getProductByDeletedIsFalseAndProductNameLike(key);
        List<ProductDTO> productDTOS = new ArrayList<>();
        for (Product item : products) {
            ProductDTO productDTO = new ProductDTO();
            productDTO.setId(item.getId());
            productDTO.setProductName(item.getProductName());
            productDTO.setPrice(item.getPrice());
            productDTO.setQuantity(item.getQuantity());
            productDTO.setDescription(item.getDescription());
            productDTO.setCategory(item.getCategory());
            productDTO.setProductAvatar(item.getProductAvatar().toProductAvatarDTO());
            productDTOS.add(productDTO);
        }
        return new ResponseEntity<>(productDTOS, HttpStatus.OK);
    }

}
