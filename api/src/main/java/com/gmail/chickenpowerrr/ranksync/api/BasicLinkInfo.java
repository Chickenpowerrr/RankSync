package com.gmail.chickenpowerrr.ranksync.api;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@AllArgsConstructor
public class BasicLinkInfo implements LinkInfo {

    @Getter private final String name;
    @Getter private final String linkExplanation;
}
