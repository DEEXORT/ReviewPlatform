package com.javarush.reviewplatform.user;

import com.javarush.reviewplatform.common.BaseTo;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import net.minidev.json.annotate.JsonIgnore;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class UserTo extends BaseTo {
    @NotBlank
    @Size(min = 2, max = 20)
    String username;
    @Size(min = 5)
    String password;

    String confirmPassword;

    @NotBlank
    @Size(min = 2, max = 20)
    String firstName;
    String lastName;
    String email;
    @Builder.Default
    Role role = Role.USER;

    @AssertTrue(message = "Пароли не совпадают")
    @JsonIgnore
    public boolean isPasswordMatch() {
        return password.equals(confirmPassword);
    }
}
