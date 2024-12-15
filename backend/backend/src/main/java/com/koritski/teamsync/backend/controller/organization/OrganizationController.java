package com.koritski.teamsync.backend.controller.organization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.koritski.teamsync.backend.annotation.AccessRole;
import com.koritski.teamsync.backend.dto.organization.CreateOrganizationRq;
import com.koritski.teamsync.backend.dto.organization.FillBankInfoRq;
import com.koritski.teamsync.backend.entity.RoleEnum;
import com.koritski.teamsync.backend.service.organization.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.koritski.teamsync.backend.constants.AuthConstants.AUTH_HEADER;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/tracker/v1/organization", produces = APPLICATION_JSON_VALUE)
public class OrganizationController {
    private final OrganizationService service;

    @PostMapping("create")
    public ResponseEntity<?> createOrganization(@RequestBody CreateOrganizationRq rq,
                                                @RequestHeader(AUTH_HEADER) String token) throws JsonProcessingException {
        return service.createOrganization(rq, token);
    }

    @PutMapping("fill-bank-info/{organizationId}")
    public ResponseEntity<?> fillBankInfoForOrganization(@PathVariable Long organizationId,
                                                         @RequestBody FillBankInfoRq rq,
                                                         @RequestHeader(AUTH_HEADER) String token) {
        return service.fillBankInfoForOrganization(organizationId, rq, token);
    }

    @PutMapping("edit/{organizationId}")
    public ResponseEntity<?> editOrganization(@RequestBody CreateOrganizationRq rq,
                                              @PathVariable Long organizationId) {
        return service.editOrganizationInfo(rq, organizationId);
    }

    @GetMapping("getById/{organizationId}")
    public ResponseEntity<?> getOrganizationById(@PathVariable Long organizationId,
                                                 @RequestHeader(AUTH_HEADER) String token) {
        return service.getOrganizationById(organizationId, token);
    }

    @GetMapping("getAll")
    @AccessRole(role = {RoleEnum.ADMIN, RoleEnum.MODERATOR})
    public ResponseEntity<?> getAllOrganizations(@RequestHeader(AUTH_HEADER) String token) {
        return service.getAllOrganizations();
    }

    @GetMapping("get-organizations-by-owner/{ownerUserId}")
    public ResponseEntity<?> getOrganizationsByOwner(@PathVariable Long ownerUserId) {
        return service.getOrganizationsByOwnerId(ownerUserId);
    }

    @GetMapping("my")
    public ResponseEntity<?> getMyOrganizations(@RequestHeader(AUTH_HEADER) String token) {
        return service.getMyOrganizations(token);
    }

    @PutMapping("{organizationId}/add/admin/{workerId}")
    public ResponseEntity<?> addAdminForOrganization(@PathVariable Long organizationId,
                                                     @PathVariable Long workerId,
                                                     @RequestHeader(AUTH_HEADER) String token) {
        return service.addAdminForOrganization(organizationId, workerId, token);
    }

    @PutMapping("{organizationId}/delete/admin/{workerId}")
    public ResponseEntity<?> deleteAdminFromOrganization(@PathVariable Long organizationId,
                                                         @PathVariable Long workerId,
                                                         @RequestHeader(AUTH_HEADER) String token) {
        return service.deleteAdminFromOrganization(organizationId, workerId, token);
    }

    @GetMapping("stats/{organizationId}")
    public ResponseEntity<?> getStatsOfOrganization(@PathVariable Long organizationId,
                                                    @RequestHeader(AUTH_HEADER) String token) {
        return service.getStatsForOrganization(organizationId, token);
    }

    @GetMapping("stats/workers/{organizationId}")
    public ResponseEntity<?> getStatsForOrganizationWorkers(@PathVariable Long organizationId,
                                                            @RequestHeader(AUTH_HEADER) String token) {
        return service.getStatsForOrganizationWorkers(organizationId, token);
    }
}
