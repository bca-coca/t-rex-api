package trex.com.web.sales.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import trex.com.web.sales.model.ItemModel;
import trex.com.web.sales.service.ItemService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/items")
public class ItemController {
    private final ItemService service;

    @GetMapping
    public ResponseEntity<List<ItemModel>> getAllItems() {
        log.info("Fetching all items");
        return ResponseEntity.ok(service.getAllItems());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemModel> getItem(@PathVariable Long id) {
        log.info("Fetching item with ID: {}", id);
        return ResponseEntity.ok(service.getItem(id));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ItemModel> createItem(
        @ModelAttribute @Valid ItemModel item,
        @RequestPart("images") List<MultipartFile> images        
    ) {
        log.info("Creating new item with images");
        return new ResponseEntity<>(service.addItem(item, images), HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ItemModel> updateItem(
        @PathVariable Long id,
        @ModelAttribute @Valid ItemModel item,
        @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) {
        log.info("Updating item with ID: {}", id);
        return ResponseEntity.ok(service.updateItem(id, item, images));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        log.info("Deleting item with ID: {}", id);
        service.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}