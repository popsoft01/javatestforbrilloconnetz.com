package org.example;


import java.time.Duration;
import java.time.LocalTime;
import java.util.Random;

public class PancakeShopNonConcurrent {

    private static final int MAX_PANCAKES_PER_USER = 5;
    private static final int MAX_PANCAKES_PER_SLOT = 12;
    private static final int NUM_USERS = 3;
    private static final int NUM_SLOTS = 10;

    private static final Random random = new Random();

    public static void main(String[] args) {
        LocalTime startTime = LocalTime.now();

        for (int slot = 1; slot <= NUM_SLOTS; slot++) {
            System.out.println("Slot " + slot);
            System.out.println("Start Time: " + startTime);

            int[] pancakeOrders = generatePancakeOrders();
            int totalPancakeOrders = getTotalPancakeOrders(pancakeOrders);

            if (totalPancakeOrders <= MAX_PANCAKES_PER_SLOT) {
                System.out.println("Shopkeeper was able to meet the needs of all the customers.");
                int wastedPancakes = MAX_PANCAKES_PER_SLOT - totalPancakeOrders;
                System.out.println("Wasted Pancakes: " + wastedPancakes);
            } else {
                int unmetOrders = totalPancakeOrders - MAX_PANCAKES_PER_SLOT;
                System.out.println("Shopkeeper was not able to meet the needs of all the customers.");
                System.out.println("Unmet Pancake Orders: " + unmetOrders);
            }

            LocalTime endTime = LocalTime.now();
            System.out.println("End Time: " + endTime);
            System.out.println();

            // Wait for 30 seconds
            try {
                Thread.sleep(Duration.between(startTime, endTime).toMillis() + 30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            startTime = endTime;
        }
    }

    private static int[] generatePancakeOrders() {
        int[] pancakeOrders = new int[NUM_USERS];
        for (int i = 0; i < NUM_USERS; i++) {
            pancakeOrders[i] = random.nextInt(MAX_PANCAKES_PER_USER + 1);
        }
        return pancakeOrders;
    }

    private static int getTotalPancakeOrders(int[] pancakeOrders) {
        int total = 0;
        for (int order : pancakeOrders) {
            total += order;
        }
        return total;
    }
}
