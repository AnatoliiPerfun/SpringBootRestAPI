package com.parser.api.handlers;


import java.util.Objects;

/**
 * @author tolik
 * @project xyz
 * @created 04.01.2023 - 12:05
 */

public final class EntityValidationException {
    private final String error;

    EntityValidationException(String error) {
        this.error = error;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (EntityValidationException) obj;
        return Objects.equals(this.error, that.error);
    }

    @Override
    public int hashCode() {
        return Objects.hash(error);
    }

    @Override
    public String toString() {
        return "EntityValidationException[" +
                "error=" + error + ']';
    }

    public static EntityValidationException from(CustomException customException) {
        return new EntityValidationException(customException.getMessage());
    }
}
