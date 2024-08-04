package com.github.javarar.lucky.ticket;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LuckyTicketTest {

    @DisplayName("Задание 7. Счастливый билет (CachedThreadPool)")
    @ParameterizedTest
    @MethodSource("casesCached")
    public void luckyTicketProbabilityCachedTest(int serialNumberLength, double expectedProbability) {
        double actualProbability = LuckyTicket.luckyTicketProbabilityCached(serialNumberLength);
        assertEquals(expectedProbability, actualProbability);
    }

    private static Stream<Arguments> casesCached() {
        return Stream.of(
                Arguments.of(4, 0.067),
                Arguments.of(6, 0.055252)
                // Длительный по времени тест (~ 8 минут)
//                Arguments.of(8, 0.0481603)
        );
    }

    @DisplayName("Задание 7. Счастливый билет (FixedThreadPool)")
    @ParameterizedTest
    @MethodSource("casesFixed")
    public void luckyTicketProbabilityFixedTest(int serialNumberLength, double expectedProbability) {
        double actualProbability = LuckyTicket.luckyTicketProbabilityFixed(serialNumberLength);
        assertEquals(expectedProbability, actualProbability);
    }

    private static Stream<Arguments> casesFixed() {
        return Stream.of(
                Arguments.of(4, 0.067),
                Arguments.of(6, 0.055252)
        );
    }
}