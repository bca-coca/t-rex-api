package trex.com.web.gallery.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import trex.com.web.gallery.model.GalleryModel;
import trex.com.web.gallery.service.GalleryService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/galleries")
public class GalleryController {
    private final GalleryService service;

    @GetMapping
    public ResponseEntity<List<GalleryModel>> getAllGalleries() {
        log.info("Fetching all galleries");
        return ResponseEntity.ok(service.getAllGalleries());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GalleryModel> getGallery(@PathVariable Long id) {
        log.info("Fetching gallery with ID: {}", id);
        return ResponseEntity.ok(service.getGallery(id));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<GalleryModel> addGallery(@RequestPart("image") MultipartFile image) {
        log.info("Adding new gallery image");
        return new ResponseEntity<>(service.addGallery(image), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGallery(@PathVariable Long id) {
        log.info("Deleting gallery with ID: {}", id);
        service.deleteGallery(id);
        return ResponseEntity.noContent().build();
    }
}