package acro;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.Scanner;

class NetworkUtils {

    //Oauth2 key/secret authentication based on credentials from github settings
    private static final String CLIENT_ID = "1d944440385c1c8c707a";
    private static final String CLIENT_SECRET = "a91ea2bbb3ba5c3e0cfc016cf27662dcd6bdf17a";
    private static final String scheme = "https";
    private static final String host = "api.github.com";
    private static final String query = String.format("client_id=%s&client_secret=%s", CLIENT_ID, CLIENT_SECRET);

    //Builds URL
    static URL buildUri(String owner, String repo, String pull, Main.Spec spec) throws URISyntaxException, MalformedURLException {

        String path = null;
        URL url = null;

        switch (spec){
            case FILES:
                path = "/repos/" + owner + "/" + repo + "/pulls/" + pull + "/files";
                break;
            case PULL:
                path = "/repos/" + owner + "/" + repo + "/pulls/" + pull;
                break;
			case COMMENTS:
				break;
			case DIFF:
				break;
			case RAW:
				break;
			default:
				break;
        }

        URI builtUri = new URI(scheme, host, path, query, "");
        url = builtUri.toURL();

        return url;
    }


    static URL buildUri(String rawUrl) throws MalformedURLException, URISyntaxException {
        rawUrl = rawUrl + "?" + query;
        URI builtUri = new URI(rawUrl);
        return builtUri.toURL();

    }


    //Gets Data
    static String getResponseFromHttpUrl(URL url) throws  IOException{
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setConnectTimeout(5000);
        urlConnection.setReadTimeout(10000);
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        }
        finally {
           urlConnection.disconnect();
        }

    }




}
