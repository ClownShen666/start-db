/* 
 * Copyright (C) 2022  ST-Lab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.urbcomp.cupid.db.flink.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class GlobalCache {

    // todo:设置合适的TTL(s)
    public static final long KAFKA_DATA_TTL = 300;

    private static final int MAXIMUM_SIZE = 1000;
    public static final Cache<String, Long> girdIdLatestTimestamp = Caffeine.newBuilder()
        .maximumSize(MAXIMUM_SIZE)
        .build();

    private static final GenericObjectPoolConfig<Jedis> config = new JedisPoolConfig();

    static {    // 设置连接池中最多允许放100个Jedis对象
        config.setMaxTotal(100);
        // 设置连接池中最大允许空闲连接
        config.setMaxIdle(100);
        // 设置连接池中最小允许的连接数
        config.setMinIdle(10);
        // 借出连接的时候是否测试有效性,推荐false
        config.setTestOnBorrow(false);
        // 归还时是否测试,推荐false
        config.setTestOnReturn(false);
        // 创建时是否测试有效 开发的时候设置为false,实践运行的时候设置为true
        config.setTestOnCreate(false);
        // 当连接池内jedis无可用资源时,是否等待资源,true
        config.setBlockWhenExhausted(true);
        // 没有获取资源时最长等待1秒,1秒后没有还没有的话就报错
        config.setMaxWaitMillis(1000);
    }

    public static JedisPool pool = new JedisPool(config, "localhost", 6379);

    public static void main(String[] args) {
        try (Jedis jedis = pool.getResource()) {

            for (String key : jedis.keys("*")) {
                jedis.del(key);
            }
        }
    }

}
