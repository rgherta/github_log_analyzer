package acro;

import com.google.devtools.common.options.OptionsParser;
import org.json.JSONArray;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static acro.NetworkUtils.buildUri;
import static acro.NetworkUtils.getResponseFromHttpUrl;

public class Main {


    enum Spec{
        PULL, FILES, COMMENTS, DIFF, RAW
    }

    static String RAW_URL = "raw_url";

    public static void main(String[] args) {

        //Parse console arguments
        OptionsParser parser = OptionsParser.newOptionsParser(ConsoleArguments.class); parser.parseAndExitUponError(args);
        ConsoleArguments options  =  parser.getOptions(ConsoleArguments.class);

        if (options.owner.isEmpty() || options.repo.isEmpty() || options.pull.isEmpty()) {
            printUsage(parser);
            return;
        }

        try {

            URL url = buildUri(options.owner, options.repo, options.pull, Spec.FILES);
            JSONArray pullFiles;

            //Finds all files of requested pull
            pullFiles = new JSONArray(getResponseFromHttpUrl(url));
            StringBuilder rawContent = new StringBuilder();


            //For each file, append content to stringBuilder
            for(int i = 0; i < pullFiles.length(); i++){

                URL rawUrl = buildUri(pullFiles.getJSONObject(i).getString(RAW_URL));
                rawContent.append(getResponseFromHttpUrl(rawUrl));
            }

            //Maps and sorts
            Stream<String> stream = Stream.of(rawContent.toString().toLowerCase().split("\\W+")).parallel();

            Map<String, Long> wordFreq = stream.collect(Collectors.groupingBy(String::toString,Collectors.counting()));
            Map<String, Long> result = wordFreq.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).limit(10)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                            (oldValue, newValue) -> oldValue, LinkedHashMap::new));

            System.out.println("Pull Request: " + options.pull);
            System.out.println("Owner: " + options.owner);
            System.out.println("Repo: " + options.repo);
            System.out.println(result);


        //TODO: Implement typed exceptions
        } catch (IOException | URISyntaxException e){
            e.printStackTrace();
        }

    }


    private static void printUsage(OptionsParser parser) {
        System.out.println("Usage: java -jar pullwords.jar OPTIONS");
        System.out.println(parser.describeOptions(Collections.<String, String>emptyMap(),
                OptionsParser.HelpVerbosity.LONG));
    }


}
