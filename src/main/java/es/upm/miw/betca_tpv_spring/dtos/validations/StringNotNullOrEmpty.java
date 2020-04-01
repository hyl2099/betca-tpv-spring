package es.upm.miw.betca_tpv_spring.dtos.validations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StringNotNullOrEmptyValidator.class)
public @interface StringNotNullOrEmpty {

    String message() default "Expected not null or empty";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
