package com.github.freeacs.v2;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.Wither;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Data
@Wither
@Builder
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class Unittype {
    Long id;
    String name;
    String description;
    Protocol protocol;
    List<UnittypeParameter> unittypeParameters;
}
