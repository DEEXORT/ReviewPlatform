package com.javarush.reviewplatform.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

@Data
@SuperBuilder
@NoArgsConstructor
public abstract class BaseTo implements HasId {
    Long id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseTo baseTo = (BaseTo) o;
        return Objects.equals(id, baseTo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
