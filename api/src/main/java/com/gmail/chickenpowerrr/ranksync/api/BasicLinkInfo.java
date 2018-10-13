package com.gmail.chickenpowerrr.ranksync.api;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * This class is one of the easiest possible implementations
 * of the LinkInfo interface
 *
 * @author Chickenpowerrr
 * @since  1.0.0
 */
@EqualsAndHashCode
@AllArgsConstructor
public class BasicLinkInfo implements LinkInfo {

    @Getter private final String name;
    @Getter private final String linkExplanation;
}
