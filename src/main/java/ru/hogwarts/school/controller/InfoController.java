package ru.hogwarts.school.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hogwarts.school.service.StudentServiceImpl;

import java.util.stream.LongStream;
import java.util.stream.Stream;

@RestController
public class InfoController {

    Logger logger = LoggerFactory.getLogger(InfoController.class);

    @Value("${server.port}")
    private String port;

    @GetMapping ("/port")
    public String getPort (){
        return port;
    }

    @GetMapping("/formula")
    public long formula () {
        long startTime1 = System.currentTimeMillis();
        logger.info ("метод 1 запустили");
        long sum = Stream.iterate(1L, a -> a +1) //последовательно числа: 1,2,3,4...
                .limit(1_000_000)
                .reduce(0L, (a, b) -> a + b ); // суммирует все числа
        long finishTime1 = System.currentTimeMillis()-startTime1;
        logger.info ("время работы метода 1 - {}", finishTime1);

        long startTime2 = System.currentTimeMillis();
        logger.info ("метод 2 запустили");
        long sum2 = LongStream.rangeClosed(1, 1_000_000).parallel()
                .reduce(0L, Long::sum );

        long finishTime2 = System.currentTimeMillis()-startTime2;
        logger.info ("время работы метода 2 - {}", finishTime2);
        return sum2;
    }

}
