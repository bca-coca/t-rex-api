package trex.com.web.contactus.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import trex.com.web.contactus.model.ContactModel;
import trex.com.web.contactus.service.ContactService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/contacts")
public class ContactController {
    private final ContactService service;

    @GetMapping("/{id}")
    public ResponseEntity<ContactModel> getContact(@PathVariable Long id) {
        log.info("Fetching contact with ID: {}", id);
        ContactModel contact = service.getContact(id);
        return ResponseEntity.ok(contact);
    }

    @PostMapping
    public ResponseEntity<ContactModel> createContact(@Valid @RequestBody ContactModel contact) {
        log.info("Creating contact: {}", contact);
        ContactModel created = service.addContact(contact);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        log.info("Deleting contact with ID: {}", id);
        service.deleteContact(id);
        return ResponseEntity.noContent().build();
    }
}