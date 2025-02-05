package trex.com.web.contactus.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import trex.com.web.contactus.model.ContactModel;
import trex.com.web.contactus.repository.ContactRepository;
import trex.com.web.exception.ResourceNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContactService {
    private final ContactRepository repository;

    @Transactional
    public ContactModel addContact(ContactModel contact) {
        try{
            log.info("Adding contact {}", contact);
            return repository.save(contact);
        }catch (Exception e){
            log.error("Error adding contact: {}", e.getMessage());
            throw new RuntimeException("Failed to add contact", e);
        }
    }

    public ContactModel getContact(Long id) {
        try {
            log.info("Fetching contact with ID: {}", id);
            return repository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Contact", "id", id));
        } catch (ResourceNotFoundException e) {
            log.error("Contact not found: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error fetching contact: {}", e.getMessage());
            throw new RuntimeException("Failed to fetch contact", e);
        }
    }

    @Transactional
    public void deleteContact(Long id) {
        try {
            log.info("Deleting contact with ID: {}", id);
            if (!repository.existsById(id)) {
                throw new ResourceNotFoundException("Contact", "id", id);
            }
            repository.deleteById(id);
            log.info("Successfully deleted contact with ID: {}", id);
        } catch (ResourceNotFoundException e) {
            log.error("Contact not found: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error deleting contact: {}", e.getMessage());
            throw new RuntimeException("Failed to delete contact", e);
        }
    }
}
