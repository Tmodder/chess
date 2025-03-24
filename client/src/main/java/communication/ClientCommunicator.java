package communication;

import java.io.*;
import java.net.*;
import java.util.Objects;

import com.google.gson.Gson;

public class ClientCommunicator
{
    private final String serverUrl;
    public ClientCommunicator(String url)
    {
        serverUrl = url;
    }
    public <T> T makeRequest(String method, String path, Object request, Class<T> responseClass,String auth) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setDoOutput(!Objects.equals(method, "GET"));
            http.setRequestMethod(method);

            //write header as needed
            if (auth != null)
            {
                http.setRequestProperty("authorization",auth);
            }
            if (http.getDoOutput())
            {
                writeBody(request, http);
            }

            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);

        } catch (IOException | URISyntaxException ex) {
            if (ex instanceof ConnectException)
            {
                throw new ResponseException(500,"Server offline");
            }
            throw new ResponseException(1000,ex.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (status != 200) {
            try (InputStream respErr = http.getErrorStream()) {
                //not sure how to read this
                if (respErr != null) {
                    throw ResponseException.fromJson(respErr,status);
                }
            }

            throw new ResponseException(status, "other failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

}
