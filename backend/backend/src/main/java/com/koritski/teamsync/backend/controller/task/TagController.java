package com.koritski.teamsync.backend.controller.task;

import com.koritski.teamsync.backend.dto.task.CreateTagRq;
import com.koritski.teamsync.backend.dto.task.LinkTagToTaskRq;
import com.koritski.teamsync.backend.service.task.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.koritski.teamsync.backend.constants.AuthConstants.AUTH_HEADER;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/tracker/v1/tag", produces = APPLICATION_JSON_VALUE)
public class TagController {
    private final TagService service;

    @PostMapping("create")
    public ResponseEntity<?> createTag(@RequestBody CreateTagRq rq,
                                       @RequestHeader(AUTH_HEADER) String token) {
        return service.createTag(rq, token);
    }

    @PutMapping("link/tagToTask")
    public ResponseEntity<?> linkTagToTask(@RequestBody LinkTagToTaskRq rq,
                                           @RequestHeader(AUTH_HEADER) String token) {
        return service.linkTagToTask(rq, token);
    }

    @GetMapping("get-all/{organizationId}")
    public ResponseEntity<?> getAllTagsForOrganization(@PathVariable Long organizationId,
                                                       @RequestHeader(AUTH_HEADER) String token) {
        return service.getAllTagsForOrganization(organizationId, token);
    }

    @DeleteMapping("unlink/tag/{id}")
    public ResponseEntity<?> unlinkTagFromTask(@PathVariable Long id,
                                               @RequestHeader(AUTH_HEADER) String token) {
        return service.unlinkTagFromTask(id, token);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteTag(@PathVariable Long id,
                                       @RequestHeader(AUTH_HEADER) String token) {
        return service.deleteTag(id, token);
    }
}
