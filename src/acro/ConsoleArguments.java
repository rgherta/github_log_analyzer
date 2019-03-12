package acro;

import com.google.devtools.common.options.Option;
import com.google.devtools.common.options.OptionsBase;

public class ConsoleArguments extends OptionsBase  {


    @Option(
            name = "owner",
            abbrev = 'o',
            help = "The owner name of the Github pull request, example: emberjs",
            category = "Documentation",
            defaultValue = ""
    )
    public String owner;

    @Option(
            name = "repo",
            abbrev = 'r',
            help = "The repository name of the Github pull request, example: rfcs",
            category = "Documentation",
            defaultValue = ""
    )
    public String repo;

    @Option(
            name = "pull",
            abbrev = 'p',
            help = "The id of the Github pull request, example 272",
            category = "Documentation",
            defaultValue = ""
    )
    public String pull;

}


