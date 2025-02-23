package org.reservation.system;


import org.reservation.system.client.ClientSimulator;
import org.reservation.system.config.ConfigReader;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RestClient {

    private static final int NUM_THREADS = 10;

    public static void main(String[] args) throws IOException {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("<---------------- Reservation System--------------->");
            ConfigReader.readConfig(scanner);
            int noOfUsers = Integer.parseInt(ConfigReader.config.getProperty("no_of_users"));

            ExecutorService executor = Executors.newFixedThreadPool(noOfUsers);
            for (int i = 0; i < noOfUsers; i++) {
                executor.submit(ClientSimulator::simulate);
            }
            executor.shutdown();
        }

    }


}