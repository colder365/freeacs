package com.github.freeacs.v2;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.Wither;

import static lombok.AccessLevel.PRIVATE;

@Data
@Wither
@Builder
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class UnittypeParameterValue {
    String value;
    Integer priority;
    String type;
    Long unittypeParameterId;
}
