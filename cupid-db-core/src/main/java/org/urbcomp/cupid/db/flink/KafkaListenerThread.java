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
package org.urbcomp.cupid.db.flink;

import java.util.concurrent.*;

public class KafkaListenerThread {
    private static volatile ThreadPoolExecutor instance;
    public final static ConcurrentMap<String, Boolean> threadRunningMap = new ConcurrentHashMap<>();

    private KafkaListenerThread() {}

    public static ThreadPoolExecutor getInstance() {
        if (instance == null) {
            synchronized (KafkaListenerThread.class) {
                if (instance == null) {
                    int corePoolSize = 3;
                    int maximumPoolSize = 6;
                    long keepAliveTime = 60;
                    TimeUnit unit = TimeUnit.SECONDS;
                    BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(3);
                    ThreadFactory threadFactory = Executors.defaultThreadFactory();
                    RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();

                    instance = new ThreadPoolExecutor(
                        corePoolSize,
                        maximumPoolSize,
                        keepAliveTime,
                        unit,
                        workQueue,
                        threadFactory,
                        handler
                    );
                }
            }
        }
        return instance;
    }

    public static void shutdown() {
        if (instance != null) {
            instance.shutdown();
            instance = null;
        }
    }
}
