package com.github.marcelektro.mastermind;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    private static final Scanner scannerIn = new Scanner(System.in);
    private static final SecureRandom random = new SecureRandom();

    private static final int MAX_ATTEMPTS = 8;
    private static final int WIDTH = 4;

    private static Piece[] targetSequence; // length = WIDTH

    private static int currentGuess;


    public static void main(String[] args) {

        System.out.println("===================");
        System.out.println("==[ MASTER MIND ]==");
        System.out.println("===================");

        // Generate sequence to guess
        newGame();

        System.out.println("☑ | I've got a new sequence ready in my mind!");

        // Let the guessing begin~
        r:
        while (currentGuess < MAX_ATTEMPTS) {

            System.out.println();
            System.out.println("❔ | Take a guess! | ("+(currentGuess + 1)+" / "+MAX_ATTEMPTS+")");
            System.out.print("> ");
            String guessSequence = scannerIn.nextLine();

            if (guessSequence.length() != WIDTH) {
                System.out.println("❌ | Your guess must be a string of first color letters. E.g. `RRBY` (for Red Red Blue and Yellow)");
                continue;
            }

            // Parse input

            Piece[] guess = new Piece[WIDTH];

            for (int i = 0; i < WIDTH; i++) {
                Piece guessed = Piece.fromChar(guessSequence.charAt(i));

                if (guessed == null) {
                    System.out.println("❌ | Your guess contains a color (`"+guessSequence.charAt(i)+"`) that does not exist.");
                    System.out.println("❌ | You can choose one of the following colors: " + Arrays.toString(Piece.values()));
                    continue r;
                }

                guess[i] = guessed;
            }


            int existsRightSpot = 0;
            boolean[] rightSpots = new boolean[WIDTH];
            int existsWrongSpot = 0;
            boolean[] wrongSpots = new boolean[WIDTH];

            for (int t = 0; t < WIDTH; t++) {
                Piece targetAt = targetSequence[t];

                if (targetAt == guess[t]) {
                    existsRightSpot++;
                    rightSpots[t] = true;
                    continue;
                }

                // if not at rightSpot, maybe it's on a wrongSpot.
                for (int j = 0; j < WIDTH; j++) {

                    // We're looking for wrongSpots here, so it cannot be
                    // a rightSpot, also a wrongSpot can't be twice.
                    if (rightSpots[j] || wrongSpots[j]) {
                        continue;
                    }

                    if (targetAt == guess[j]) {
                        existsWrongSpot++;
                        wrongSpots[j] = true;
                        break;
                    }

                }

            }

            System.out.println("🤔 | Your guess ("+guessSequence+") result: " +
                    "RIGHT SPOT: " + "⬛".repeat(existsRightSpot) + " ("+existsRightSpot+"), " +
                    "WRONG SPOT: " + "⬜".repeat(existsWrongSpot) + " ("+existsWrongSpot+")!");

            // Check win
            if (existsRightSpot == WIDTH) {
                printGameEnd(true);
                break;
            }


            if (++currentGuess < MAX_ATTEMPTS) {
                continue;
            }

            // when there are no more moves:
            printGameEnd(false);

        }


    }

    private static void newGame() {

        targetSequence = new Piece[WIDTH];

        for (int i = 0; i < targetSequence.length; i++) {

            targetSequence[i] = Piece.values()[random.nextInt(Piece.values().length)];

        }


        currentGuess = 0;

    }

    private static void printGameEnd(boolean won) {
        System.out.println();

        if (won) {
            System.out.println("===[ ✅ YOU WIN! ✅ ]===");
            System.out.println();
            System.out.println("That's impressive! :o");
            System.out.println("You truly have a master mind.");
            System.out.println();
            System.out.println("The sequence was indeed: ");
            printResult();
            System.out.println("========================");

            return;
        }

        System.out.println("===[ ❌ GAME OVER ❌ ]===");
        System.out.println();
        System.out.println("Sadly, you did not win this time.");
        System.out.println("Heads up, just try again! :D");
        System.out.println();
        System.out.println("The sequence I thought of was: ");
        printResult();
        System.out.println("==========================");

    }

    private static void printResult() {

        for (Piece piece : targetSequence) {
            System.out.print(piece.string + " ");
        }
        System.out.println();

    }



    enum Piece {
        RED("R"),
        GREEN("G"),
        BLUE("B"),
        YELLOW("Y"),
        ORANGE("O");

        public final String string;

        Piece(String string) {
            this.string = string;
        }

        public static Piece fromChar(char c) {
            for (Piece value : Piece.values()) {
                if (value.string.equalsIgnoreCase(String.valueOf(c)))
                    return value;
            }
            return null;
        }
    }

}