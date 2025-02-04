package trex.com.web.team.service;

import java.util.Comparator;
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
import trex.com.web.exception.ResourceNotFoundException;
import trex.com.web.team.model.TeamModel;
import trex.com.web.team.repository.TeamRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamService {
    private final TeamRepository repository;
    private final CloudinaryService cloudinaryService;

    @Cacheable(value = "TeamCache")
    public List<TeamModel> getAllTeamMembers() {
        try {
            log.info("Fetching all team members");
            return repository.findAll().stream()
                    .sorted(Comparator.comparing(TeamModel::getJoining))
                    .toList();
        } catch (Exception e) {
            log.error("Error fetching team members: {}", e.getMessage());
            throw new RuntimeException("Failed to fetch team members", e);
        }
    }

    @Cacheable(value = "TeamCache", key = "#id")
    public TeamModel getTeamMember(Long id) {
        try {
            log.info("Fetching team member with ID: {}", id);
            return repository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Team member not found with id: " + id));
        } catch (ResourceNotFoundException e) {
            log.error("Team member not found: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error fetching team member: {}", e.getMessage());
            throw new RuntimeException("Failed to fetch team member", e);
        }
    }

    @Transactional
    @CacheEvict(value = "TeamCache", allEntries = true)
    public TeamModel addTeamMember(TeamModel teamMember, MultipartFile image) {
        try {
            log.info("Adding new team member with image");
            String imageUrl = cloudinaryService.uploadImage(image);
            teamMember.setImg(imageUrl);
            return repository.save(teamMember);
        } catch (Exception e) {
            log.error("Error adding team member: {}", e.getMessage());
            throw new RuntimeException("Failed to add team member", e);
        }
    }

    @Transactional
    @CachePut(value = "TeamCache", key = "#id")
    public TeamModel updateTeamMember(Long id, TeamModel updatedMember, MultipartFile image) {
        try {
            log.info("Updating team member with ID: {}", id);
            TeamModel existingMember = repository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Team member not found with id: " + id));

            if (image != null && !image.isEmpty()) {
                String imageUrl = cloudinaryService.uploadImage(image);
                existingMember.setImg(imageUrl);
            }
            
            existingMember.setName(updatedMember.getName());
            existingMember.setJoining(updatedMember.getJoining());
            existingMember.setLinkedin(updatedMember.getLinkedin());
            existingMember.setFacebook(updatedMember.getFacebook());
            existingMember.setInstagram(updatedMember.getInstagram());
            
            return repository.save(existingMember);
        } catch (ResourceNotFoundException e) {
            log.error("Team member not found: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error updating team member: {}", e.getMessage());
            throw new RuntimeException("Failed to update team member", e);
        }
    }

    @Transactional
    @CacheEvict(value = "TeamCache", key = "#id")
    public void deleteTeamMember(Long id) {
        try {
            log.info("Deleting team member with ID: {}", id);
            TeamModel member = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Team member not found with id: " + id));

            String imageUrl = member.getImg();
            if(imageUrl != null && !imageUrl.isEmpty()) {
                String publicId = imageUrl.substring(imageUrl.lastIndexOf("/") + 1, imageUrl.lastIndexOf("."));
                cloudinaryService.deleteImage(publicId);
            }

            repository.delete(member);
            log.info("Successfully deleted team member and associated image");
        }catch (ResourceNotFoundException e) {
            log.error("Team member not found: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error deleting team member: {}", e.getMessage());
            throw new RuntimeException("Failed to delete team member", e);
        }
    }
}