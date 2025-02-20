package trex.com.web.sales.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import trex.com.web.config.CloudinaryService;
import trex.com.web.exception.BadRequestException;
import trex.com.web.exception.ResourceNotFoundException;
import trex.com.web.sales.model.ItemModel;
import trex.com.web.sales.repository.ItemRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {
    private final ItemRepository repository;
    private final CloudinaryService cloudinaryService;

    @Cacheable(value = "items")
    public List<ItemModel> getAllItems() {
        try {
            log.info("Fetching all items");
            return repository.findAll();
        } catch (Exception e) {
            log.error("Error fetching items: {}", e.getMessage());
            throw new RuntimeException("Failed to fetch items", e);
        }
    }

    @Cacheable(value = "items", key = "#id")
    public ItemModel getItem(Long id) {
        try {
            log.info("Fetching item with ID: {}", id);
            if ( id == null || id <= 0 ) {
                throw new BadRequestException("Invalid item ID");
            }
            return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item", "id", id));
        } catch ( BadRequestException e ) {
            log.error("Invalid item ID: {}", e.getMessage(), e);
            throw e;
        } catch (ResourceNotFoundException e) {
            log.error("Item not found: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Error fetching item: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch item", e);
        }
    }

    @Transactional
    @CacheEvict(value = "items", allEntries = true)
    public ItemModel addItem(ItemModel item, List<MultipartFile> images) {
        try {
            log.info("Adding new item with images");
            List<String> imageUrls = uploadImages(images);
            item.setImg(imageUrls);
            return repository.save(item);
        } catch (Exception e) {
            log.error("Error adding item: {}", e.getMessage());
            throw new RuntimeException("Failed to add item", e);
        }
    }

    @Transactional
    @CachePut(value = "items", key = "#id")
    public ItemModel updateItem(Long id, ItemModel updatedItem, List<MultipartFile> newImages) {
        try {
            log.info("Updating item with ID: {}", id);
            ItemModel existingItem = repository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Item", "id", id));

            if (newImages != null && !newImages.isEmpty()) {
                deleteExistingImages(existingItem.getImg());
                List<String> newImageUrls = uploadImages(newImages);
                existingItem.setImg(newImageUrls);
            }

            existingItem.setName(updatedItem.getName());
            existingItem.setDescription(updatedItem.getDescription());
            existingItem.setSpecification(updatedItem.getSpecification());
            existingItem.setPrice(updatedItem.getPrice());

            return repository.save(existingItem);
        } catch (ResourceNotFoundException e) {
            log.error("Item not found: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Error updating item: {}", e.getMessage());
            throw new RuntimeException("Failed to update item", e);
        }
    }

    @Transactional
    @CacheEvict(value = "items", allEntries = true)
    public void deleteItem(Long id) {
        try {
            log.info("Deleting item with ID: {}", id);
            ItemModel item = repository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Item", "id", id));
            
            deleteExistingImages(item.getImg());
            repository.deleteById(id);
        } catch (ResourceNotFoundException e) {
            log.error("Item not found: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error deleting item: {}", e.getMessage());
            throw new RuntimeException("Failed to delete item", e);
        }
    }

    private List<String> uploadImages(List<MultipartFile> images) {
        List<String> imageUrls = new ArrayList<>();
        for (MultipartFile image : images) {
            String imageUrl = cloudinaryService.uploadImage(image);
            imageUrls.add(imageUrl);
        }
        return imageUrls;
    }

    private void deleteExistingImages(List<String> imageUrls) {
        for (String imageUrl : imageUrls) {
            String publicId = imageUrl.substring(imageUrl.lastIndexOf("/") + 1, imageUrl.lastIndexOf("."));
            cloudinaryService.deleteImage(publicId);
        }
    }
}