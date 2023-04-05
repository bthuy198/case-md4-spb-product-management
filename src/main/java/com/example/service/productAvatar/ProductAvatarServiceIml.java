package com.example.service.productAvatar;

import com.example.model.ProductAvatar;
import com.example.repository.ProductAvatarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductAvatarServiceIml implements IProductAvatarService{
    @Autowired
    private ProductAvatarRepository productAvatarRepository;
    @Override
    public List<ProductAvatar> findAll() {
        return productAvatarRepository.findAll();
    }

    @Override
    public Optional<ProductAvatar> findById(String id) {
        return productAvatarRepository.findById(id);
    }

    @Override
    public Boolean existById(String id) {
        return productAvatarRepository.existsById(id);
    }

    @Override
    public ProductAvatar save(ProductAvatar productAvatar) {
        return null;
    }

    @Override
    public void delete(ProductAvatar productAvatar) {
        productAvatarRepository.delete(productAvatar);
    }

    @Override
    public void deleteById(String id) {
        productAvatarRepository.deleteById(id);
    }
}
