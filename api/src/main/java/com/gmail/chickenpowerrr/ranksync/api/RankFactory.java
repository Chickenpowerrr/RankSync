package com.gmail.chickenpowerrr.ranksync.api;

import java.util.Collection;

public interface RankFactory<R> {

    Rank getRankFromRole(R internalRole);

    Collection<Rank> getRanksFromRoles(Collection<R> internalRoles);

    R getRoleFromName(String string);

    Collection<R> getRolesFromNames(Collection<String> strings);

    R getRoleFromRank(Rank rank);

    Collection<R> getRolesFromRanks(Collection<Rank> ranks);

    Bot<?, R> getBot();
}
