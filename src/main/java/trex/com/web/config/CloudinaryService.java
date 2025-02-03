package trex.com.web.config;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CloudinaryService {
    private final Cloudinary cloudinary;
    public String uploadImage(MultipartFile file) {
        try {
            Map result = cloudinary.uploader().upload(file.getBytes(), Map.of());
            return result.get("url").toString();
        } catch (IOException e) {
            log.error("Error uploading image to Cloudinary: {}", e.getMessage());
            throw new RuntimeException("Failed to upload image", e);
        }
    }

    public void deleteImage(String publicId) {
        try {
            Map<String, String> params = Map.of("invalidate", "true");
            cloudinary.uploader().destroy(publicId, params);
        } catch (IOException e) {
            log.error("Error deleting image from Cloudinary: {}", e.getMessage());
            throw new RuntimeException("Failed to delete image from Cloudinary", e);
        }
    }
}
