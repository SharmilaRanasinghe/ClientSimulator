package org.reservation.system.config;


import java.util.Properties;
import java.util.Scanner;

public class ConfigReader
{
    public static final Properties config = new Properties();
    public static void readConfig(Scanner scanner)
    {
        config.setProperty("no_of_users", getInputFromUser("Enter the number of users: ", scanner));
        config.setProperty("command", getInputFromUser("Optional - Enter the command (check-availability or reserve, default: both): ", scanner));
        config.setProperty("origin", getInputFromUser("Optional - Enter the origin (default: A): ", scanner));
        config.setProperty("destination", getInputFromUser("Optional - Enter the destination (default: D): ", scanner));
        config.setProperty("passengers_count", getInputFromUser("Optional - Enter the passengers count (default: 5): ", scanner));
        config.setProperty("travel_date", getInputFromUser("Optional - Enter the travel date (yyyy-MM-dd) (default: tomorrow): ", scanner));
    }
    protected static String getInputFromUser(String prompt, Scanner scanner)
    {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}