package com.gmail.chickenpowerrr.ranksync.api;

import lombok.AccessLevel;
import lombok.Getter;

public final class ApiManager {

    @Getter(AccessLevel.PACKAGE) private static RankSyncApi rankSyncApi = null;

    public static void setInstance(RankSyncApi rankSyncApi) throws IllegalStateException {
        if(ApiManager.rankSyncApi == null) {
            ApiManager.rankSyncApi = rankSyncApi;
        } else {
            throw new IllegalStateException("The API instance has already been set");
        }
    }
}
