package br.ifsp.hospital.functional.utils;

public record EqualsHashCodeScenario(
        Object base,
        Object matching,
        Object different,
        Object otherType
) {}
