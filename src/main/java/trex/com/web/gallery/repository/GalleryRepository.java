package trex.com.web.gallery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import trex.com.web.gallery.model.GalleryModel;

@Repository
public interface GalleryRepository extends JpaRepository<GalleryModel, Long> {
}
