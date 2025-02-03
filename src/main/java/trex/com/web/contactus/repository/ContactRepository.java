package trex.com.web.contactus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import trex.com.web.contactus.model.ContactModel;

@Repository
public interface ContactRepository extends JpaRepository<ContactModel, Long> {
}
