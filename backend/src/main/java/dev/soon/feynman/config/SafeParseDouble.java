package dev.soon.feynman.config;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class SafeParseDouble {
    public Double safeParseDouble(String value) {
        try {
            if (value != null && !value.isEmpty() && !"-".equals(value)) {
                return Double.parseDouble(value);
            }
        } catch (NumberFormatException e) {
            log.warn("Failed to parse double value: {}", value);
        }
        return null;
    }
}