package com.github.javarar.lucky.ticket;

import lombok.extern.log4j.Log4j2;
import lombok.val;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Log4j2
public class LuckyTicket {

    private static final int THREADS_NUMBER = 20;

    private static final ExecutorService fixedExecutor = Executors.newFixedThreadPool(THREADS_NUMBER);
    private static final ExecutorService cachedExecutor = Executors.newCachedThreadPool();
    public static final int BATCH_SIZE = 100_000;

    public static double luckyTicketProbabilityCached(int ticketLength) {
        val format = "%0" + ticketLength + "d";
        int totalTickets = (int) Math.pow(10, ticketLength);
        // Если количество билетов меньше размера пакета по умолчанию, вычисляем в один поток
        val batchSize = Math.min(BATCH_SIZE, totalTickets);
        val totalBatches = totalTickets / batchSize;

        val results = Stream.iterate(0, i -> i + 1)
                .map(i -> {
                    log.info("Запуск в обработку пакета {}/{}", i+1, totalBatches);
                    return IntStream.range(i * batchSize, (i+1) * batchSize).mapToObj(j -> String.format(format, j));
                })
                .map(batch -> cachedExecutor.submit(new CountLuckyTicketsTask(ticketLength, batch.toList())))
                .map(future -> {
                    try {
                        return future.get();
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                })
                .limit(totalBatches).toList();

        return getAnswer(results, totalTickets);
    }

    public static double luckyTicketProbabilityFixed(int ticketLength) {
        val format = "%0" + ticketLength + "d";
        int totalTickets = (int) Math.pow(10, ticketLength);
        List<String> allTickets = IntStream.range(0, totalTickets)
                .mapToObj(i -> String.format(format, i))
                .toList();
        val batchSize =totalTickets / THREADS_NUMBER;
        val batches = IntStream.range(0, THREADS_NUMBER)
                .mapToObj(i -> allTickets.subList(i * batchSize, (i + 1) * batchSize))
                .toList();

        val results = batches.stream()
                .map(batch -> fixedExecutor.submit(new CountLuckyTicketsTask(ticketLength, batch)))
                .map(future -> {
                    try {
                        return future.get();
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                }).toList();

        return getAnswer(results, totalTickets);
    }

    private static double getAnswer(List<Result> results, int totalTickets) {
        val spbTickets = results.stream()
                .flatMap(result -> result.spbTickets().stream())
                .toList();
        log.info("Счастливые билеты по-ленинградски");
        printTickets(spbTickets, totalTickets);

        val mskTickets = results.stream()
                .flatMap(result -> result.mskTickets().stream())
                .toList();
        log.info("Счастливые билеты по-московски");
        printTickets(mskTickets, totalTickets);

        return spbTickets.size() / (double) totalTickets;
    }

    private static void printTickets(List<String> tickets, int totalTickets) {
        log.info("Количество: {}", tickets.size());
        log.info("Вероятность выпадения: {}", tickets.size() / (double) totalTickets);
        // Слишком большой вывод на консоль
//        log.info("Список билетов: {}", String.join(", ", tickets));
    }
}