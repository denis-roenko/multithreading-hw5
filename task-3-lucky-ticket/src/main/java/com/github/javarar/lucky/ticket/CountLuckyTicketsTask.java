package com.github.javarar.lucky.ticket;

import lombok.AllArgsConstructor;
import lombok.val;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.IntStream;

@AllArgsConstructor
class CountLuckyTicketsTask implements Callable<Result> {

    private int length;
    private List<String> tickets;

    @Override
    public Result call() {
        val spbTickets = tickets.stream().filter(this::isSpbTicket).toList();
        val mskTickets = tickets.stream().filter(this::isMskTicket).toList();
        return new Result(spbTickets, mskTickets);
    }

    private boolean isSpbTicket(String ticket) {
        val digits = ticket.toCharArray();
        int firstHalfSum = IntStream.range(0, length / 2)
                .map(i -> Character.getNumericValue(digits[i]))
                .sum();
        int secondHalfSum = IntStream.range(length / 2, length)
                .map(i -> Character.getNumericValue(digits[i]))
                .sum();
        return firstHalfSum == secondHalfSum;
    }

    private boolean isMskTicket(String ticket) {
        val digits = ticket.toCharArray();
        int evenNumbersSum = IntStream.range(0, length)
                .filter(i -> (i + 1) % 2 == 0)
                .map(i -> Character.getNumericValue(digits[i]))
                .sum();
        int oddNumbersSum = IntStream.range(0, length)
                .filter(i -> (i + 1) % 2 != 0)
                .map(i -> Character.getNumericValue(digits[i]))
                .sum();
        return evenNumbersSum == oddNumbersSum;
    }
}
