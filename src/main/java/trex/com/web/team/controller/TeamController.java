package trex.com.web.team.controller;

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
import trex.com.web.team.model.TeamModel;
import trex.com.web.team.service.TeamService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/teams")
public class TeamController {
    private final TeamService service;

    @GetMapping
    public ResponseEntity<List<TeamModel>> getAllTeamMembers() {
        return ResponseEntity.ok(service.getAllTeamMembers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamModel> getTeamMember(@PathVariable Long id) {
        return ResponseEntity.ok(service.getTeamMember(id));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TeamModel> createTeamMember(
        @ModelAttribute @Valid TeamModel teamMember,
        @RequestPart("image") MultipartFile image
    ) {
        return new ResponseEntity<>(service.addTeamMember(teamMember, image), HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TeamModel> updateTeamMember(
        @PathVariable Long id,
        @ModelAttribute @Valid TeamModel teamMember,
        @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        return ResponseEntity.ok(service.updateTeamMember(id, teamMember, image));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeamMember(@PathVariable Long id) {
        service.deleteTeamMember(id);
        return ResponseEntity.noContent().build();
    }
}