package trex.com.web.gallery.service;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import trex.com.web.config.CloudinaryService;
import trex.com.web.exception.ResourceNotFoundException;
import trex.com.web.gallery.model.GalleryModel;
import trex.com.web.gallery.repository.GalleryRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GalleryService {
    private final GalleryRepository repository;
    private final CloudinaryService cloudinaryService;

    @Cacheable(value = "galleries")
    public List<GalleryModel> getAllGalleries() {
        try {
            log.info("Fetching all galleries");
            return repository.findAll();
        } catch (Exception e) {
            log.error("Error fetching galleries: {}", e.getMessage());
            throw new RuntimeException("Failed to fetch galleries", e);
        }
    }

    public GalleryModel getGallery(Long id) {
        try {
            log.info("Fetching gallery with ID: {}", id);
            return repository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Gallery", "id", id));
        } catch (ResourceNotFoundException e) {
            log.error("Gallery not found: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error fetching gallery: {}", e.getMessage());
            throw new RuntimeException("Failed to fetch gallery", e);
        }
    }

    @Transactional
    public GalleryModel addGallery(MultipartFile image) {
        try {
            log.info("Adding new gallery image");
            String imageUrl = cloudinaryService.uploadImage(image);
            GalleryModel gallery = new GalleryModel();
            gallery.setImg(imageUrl);
            return repository.save(gallery);
        } catch (Exception e) {
            log.error("Error adding gallery: {}", e.getMessage());
            throw new RuntimeException("Failed to add gallery", e);
        }
    }

    @Transactional
    @CacheEvict(value = "galleries", allEntries = true)
    public void deleteGallery(Long id) {
        try {
            log.info("Deleting gallery with ID: {}", id);
            GalleryModel gallery = repository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Achievement", "id", id));

            String imageUrl = gallery.getImg();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                String publicId = imageUrl.substring(imageUrl.lastIndexOf("/") + 1, imageUrl.lastIndexOf("."));
                cloudinaryService.deleteImage(publicId);
            }
            repository.deleteById(id);
        } catch (ResourceNotFoundException e) {
            log.error("Gallery not found: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error deleting gallery: {}", e.getMessage());
            throw new RuntimeException("Failed to delete gallery", e);
        }
    }
}