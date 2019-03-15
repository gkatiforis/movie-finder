package com.katiforis.assignment;


import java.io.IOException;
import java.util.Arrays;

public class Main {

    public static void main(String...args) throws IOException {

        if(args.length != 4){
            System.out.println("Wrong number of parameters");
            return;
        }

        String outputFilePath = args[3];
        LogManager logManager = new LogManager();
        logManager.read(args);

        logManager.write(outputFilePath, logManager.getMoviesByMinutesWatched());
        logManager.write(outputFilePath, logManager.getAverageMinutesWatchedPerMovie());
        logManager.write(outputFilePath, logManager.getAverageMinutesWatchedPerPerson());
        logManager.write(outputFilePath, logManager.getFavouriteGenre());
        logManager.write(outputFilePath, logManager.getRecommendGenres());
        logManager.write(outputFilePath, Arrays.asList(logManager.getRecommendMovieLength()));
    }
}
