package net.ostis.jesc.client.model.element;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import net.ostis.jesc.client.model.type.ScType;

@Data
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ScReference {
    private ScReferenceType type;
    private Object value;
    private String alias;

    public static ScReference addr(Long scAddr, String alias) {
        return ScReference.builder()
                .type(ScReferenceType.ADDR)
                .value(scAddr)
                .alias(alias)
                .build();
    }

    public static ScReference addr(Long scAddr) {
        return addr(scAddr, null);
    }

    public static ScReference ref(Long value, String alias) {
        return ScReference.builder()
                .type(ScReferenceType.REF)
                .value(value)
                .alias(alias)
                .build();
    }

    public static ScReference ref(Long value) {
        return ref(value, null);
    }

    public static ScReference type(ScType type, String alias) {
        return ScReference.builder()
                .type(ScReferenceType.TYPE)
                .value(Long.valueOf(type.numeric))
                .alias(alias)
                .build();
    }

    public static ScReference type(ScType type) {
        return type(type, null);
    }

    public static ScReference alias(String alias) {
        return ScReference.builder()
                .type(ScReferenceType.ALIAS)
                .value(alias)
                .build();
    }

}
