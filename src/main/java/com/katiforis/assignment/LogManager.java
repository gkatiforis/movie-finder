package com.katiforis.assignment;

import com.katiforis.assignment.model.LogEntry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LogManager {

    private final List<LogEntry> list = new ArrayList<>();

    /**
     * Reads all the files and initializes the list of LogEntries
     * @param args
     * @throws IOException
     */
    public void read(String []args) throws IOException {
        for(String file:args){
            Path path = Paths.get(file);
            try (Stream<String> lines = Files.lines(path)) {
                Pattern p = Pattern.compile("^(\\d+)/(\\d+)/(\\d+) (.*) (\\d+)min (\\d+)min (\\S+)$");
                list.addAll(lines.map(p::matcher)
                        .filter(Matcher::matches)
                        .map(m-> new LogEntry(path.getFileName().toString(), m.group(3), m.group(2),m.group(1),
                                        m.group(4), m.group(5), m.group(6), m.group(7)))
                        .collect(Collectors.toList()));
            }
        }
    }

    /**
     * Append the given List elements to the file as String
     * @param filePath output file path
     * @param lines  list elements
     * @throws IOException
     */
    public void write(String filePath, List lines) throws IOException {
        String out = lines.toString()+"\n";
        Files.write(Paths.get(filePath), out.getBytes(), StandardOpenOption.APPEND);
    }

    /**
     * Append the given Map elements to the file as String
     * @param filePath output file path
     * @param lines  list elements
     * @throws IOException
     */
    public void write(String filePath, Map lines) throws IOException {
        String out = lines.toString()+"\n";
        Files.write(Paths.get(filePath), out.getBytes(), StandardOpenOption.APPEND);
    }


    /**
     * Calc ordered list of the movies watched by all persons combined, ordered by minutes watched
     * @return ordered list of the movies watched by all persons combined, ordered by minutes watched
     */
    public List<Object> getMoviesByMinutesWatched(){
        return list
                .stream()
                .sorted(Comparator.comparing(LogEntry::getMinutesWatched))
                .map(LogEntry::getMovie)
                .collect(Collectors.toList());
    }


    /**
     * Calc the average minutes watched grouped by movie
     * @return a Map of movies names and their average minutes watched
     */
    public Map getAverageMinutesWatchedPerMovie(){
        return  list
                .stream()
                 .collect(Collectors.groupingBy(e -> e.getMovie().getTitle(),
                 Collectors.averagingDouble(e->((double)e.getMinutesWatched() / e.getMovie().getMovieLength()) *100)));

    }

    /**
     * Calc the average minutes watched grouped by person
     * @return a Map of persons names and their average minutes watched
     */
    public Map<String, Double> getAverageMinutesWatchedPerPerson(){
        return  list
                .stream()
                .collect(Collectors.groupingBy(e -> e.getName(),
                        Collectors.averagingDouble(e->((double)e.getMinutesWatched() / e.getMovie().getMovieLength()) *100)));

    }

    /**
     * Calc the favourite genre per person
     * @return a Map of persons names and their favourite genre
     */
    public Map getFavouriteGenre(){
        Map result = new HashMap();
        Map mapMap =
                list.stream()
                .filter(e->{
                    double perc = (double)e.getMinutesWatched() / e.getMovie().getMovieLength() * 100;
                    return perc > 60;
                })
                .collect(Collectors.groupingBy(e -> e.getName(), Collectors.groupingBy(e -> e.getMovie().getGenre(), Collectors.counting())));

        Iterator<Map.Entry> person = mapMap.entrySet().iterator();
        while (person.hasNext()) {
            Map.Entry<String, Map<String, Integer>> genre = person.next();
            String maxGenre = genre.getValue()
                        .entrySet().stream()
                        .max(Map.Entry.comparingByValue())
                        .get().getKey();
            result.put(genre.getKey(), maxGenre);
        }
      return  result;

    }

    /**
     * Recommends genre based on <code>sortBasedOnPopularity()</code>
     * @return a List of String of genre
     */
    public List<String> getRecommendGenres(){
        List<String> recommendedGenres =
                sortBasedOnPopularity()
                .map(log->log.getMovie().getGenre())
               .distinct()
               .limit(2)
               .collect(Collectors.toList());

      return recommendedGenres;
    }

    /**
     * Recommends  movie length based on <code>sortBasedOnPopularity()</code>
     * @return a String  movie length
     */
    public String getRecommendMovieLength(){

        Double averagePopularMovieLength =
                sortBasedOnPopularity()
                        .mapToInt(log->log.getMovie().getMovieLength())
                        .average().getAsDouble();


        Double averagePerPersonMovieLength =
                getAverageMinutesWatchedPerPerson().values()
                        .stream()
                        .mapToDouble(i->i.doubleValue())
                        .average().getAsDouble();

        Double recommendedMovieLength = (averagePopularMovieLength + averagePerPersonMovieLength)/2;

        return String.valueOf(recommendedMovieLength);
    }

    /**
     * Filter and sort LogEntries based on person's favourite genre, watch duration and the date that a movie was viewed
     * @return a Stream of LogEntry
     */
    private Stream<LogEntry> sortBasedOnPopularity(){
        Stream<LogEntry> mostPopular = list.stream()
                .filter(log -> getFavouriteGenre().values().contains(log.getMovie().getGenre()))
                .sorted(Comparator.comparing(LogEntry::getMinutesWatched).thenComparing(LogEntry::getDate).reversed());
        return mostPopular;
    }
}
