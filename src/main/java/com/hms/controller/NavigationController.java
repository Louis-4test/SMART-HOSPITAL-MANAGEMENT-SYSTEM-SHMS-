package com.hms.controller;

import com.hms.service.NavigationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/navigation")
public class NavigationController {

    private final NavigationService navigationService;

    public NavigationController(NavigationService navigationService) {
        this.navigationService = navigationService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST')")
    public ResponseEntity<Void> navigateTo(@RequestBody Map<String, String> body) {
        String page = body.get("page");
        if (page == null || page.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        navigationService.addNavigation(page);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/back")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST')")
    public ResponseEntity<Map<String, String>> goBack() {
        String current = navigationService.goBack();
        if (current == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "No previous page"));
        }
        return ResponseEntity.ok(Map.of("currentPage", current));
    }

    @GetMapping("/history")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST')")
    public ResponseEntity<List<String>> getHistory() {
        return ResponseEntity.ok(new ArrayList<>(navigationService.getHistory()));
    }

    @GetMapping("/current")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST')")
    public ResponseEntity<Map<String, String>> getCurrent() {
        String current = navigationService.peekCurrentPage();
        if (current == null) {
            return ResponseEntity.ok(Map.of("currentPage", "home"));
        }
        return ResponseEntity.ok(Map.of("currentPage", current));
    }
}
