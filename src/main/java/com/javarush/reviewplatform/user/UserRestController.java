package com.javarush.reviewplatform.user;

import com.javarush.reviewplatform.util.Constant;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constant.Path.API_USERS)
@RequiredArgsConstructor
public class UserRestController {
    private final UserService userService;

    @GetMapping
    public List<UserTo> getUsers() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public UserTo getUser(@PathVariable Long id) {
        return userService.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public UserTo createUser(@Valid @RequestBody UserTo userTo) {
        return userService.save(userTo);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public UserTo updateUser(@Valid @RequestBody UserTo userTo) {
        if (userTo.getId() == null) throw new IllegalArgumentException("User ID is required for update");
        return userService.save(userTo);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        boolean deleted = userService.deleteById(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
