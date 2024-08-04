package com.github.javarar.rejected.task;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(MockitoJUnitRunner.class)
class CustomTreadExecutorsTest {

    @Test
    public void threadPoolDoesNotThrowExceptionOnQueueOverflow() {
        val customExecutor = CustomThreadExecutors.logRejectedThreadPoolExecutor(1, 1);
        assertDoesNotThrow(() -> executeTasks(customExecutor));

        val abortPolicyExecutor = new ThreadPoolExecutor(1, 1, 0, MILLISECONDS,
                new ArrayBlockingQueue<>(1),
                new ThreadPoolExecutor.AbortPolicy());
        assertThrows(RejectedExecutionException.class, () -> executeTasks(abortPolicyExecutor));
    }

    private void executeTasks(Executor executor) {
        for (int i = 0; i < 5; i++) {
            try {
                Thread.sleep(500);
                executor.execute(new Task(Integer.toString(i)));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
