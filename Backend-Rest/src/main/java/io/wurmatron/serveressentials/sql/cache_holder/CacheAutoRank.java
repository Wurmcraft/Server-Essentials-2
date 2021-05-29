package io.wurmatron.serveressentials.sql.cache_holder;

import io.wurmatron.serveressentials.models.AutoRank;

public class CacheAutoRank implements Cache {

    public AutoRank autoRank;
    public long lastSync;

    public CacheAutoRank(AutoRank autoRank, long lastSync) {
        this.autoRank = autoRank;
        this.lastSync = lastSync;
    }

    public CacheAutoRank(AutoRank autoRank) {
        this.autoRank = autoRank;
        this.lastSync = System.currentTimeMillis();
    }

    @Override
    public long lastSync() {
        return lastSync;
    }
}
