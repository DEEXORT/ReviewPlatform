package com.javarush.reviewplatform.user;

import com.javarush.reviewplatform.common.BaseTo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class UserTo extends BaseTo {
    @NotBlank
    @Size(min = 2, max = 20)
    String username;
    @Size(min = 5)
    String password;
    @NotBlank
    @Size(min = 2, max = 20)
    String firstName;
    String lastName;
    String email;
    @Builder.Default
    Role role = Role.USER;
}
