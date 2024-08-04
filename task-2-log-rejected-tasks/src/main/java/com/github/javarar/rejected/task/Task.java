package com.github.javarar.rejected.task;

import lombok.extern.log4j.Log4j2;

import static java.lang.Thread.sleep;

@Log4j2
public class Task implements Runnable {

    final String id;

    public Task(String id) {
        this.id = id;
    }

    @Override
    public void run() {
        try {
            log.info("Запуск {}", id);
            sleep(1_000);
            log.info("Завершение {}", id);
        } catch (InterruptedException e) {
            log.error("Ошибка", e);
            throw new RuntimeException(e);
        }
    }
}
