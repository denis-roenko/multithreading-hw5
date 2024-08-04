package com.github.javarar.rejected.task;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.SECONDS;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CustomThreadExecutors {

    public static final RejectedExecutionHandler rejectedExecutionHandler = (r, executor) -> log.error("Задача отклонена {}", r);

    public static Executor logRejectedThreadPoolExecutor(int corePoolSize, int maximumPoolSize) {
        return new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                0L,
                SECONDS,
                new ArrayBlockingQueue<>(maximumPoolSize),
                Executors.defaultThreadFactory(),
                rejectedExecutionHandler
        );
    }
}
