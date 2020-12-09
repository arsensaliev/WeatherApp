package ru.android_basic.model;

import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

import ru.android_basic.Constants;

public class RequestApi {
    public enum Type {
        GET, POST, PUT, DELETE
    }

    private static final String TAG = RequestApi.class.getSimpleName();
    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String PUT = "PUT";
    private static final String DELETE = "DELETE";
    private static final int TIMEOUT = 10000;
    private final String typeRequest;
    private final String URL;
    private final StringBuilder param;
    private final Class c;
    private final OnRequestApiListener onRequestApiListener;
    private final Gson gson;

    public RequestApi(Builder builder) {
        this.URL = builder.URL;
        this.typeRequest = builder.typeRequest;
        this.param = builder.param;
        this.c = builder.c;
        this.onRequestApiListener = builder.onRequestApiListener;
        gson = new Gson();
        request();
    }


    public interface OnRequestApiListener {
        void onSuccess(@Nullable Object object, int responseCode);

        void onError();
    }

    public static class Builder {
        private final String URL;
        private String typeRequest = GET;
        private final StringBuilder param = new StringBuilder();
        private final Class c;
        private OnRequestApiListener onRequestApiListener;

        public Builder(String url, Class c) {
            this.URL = url;
            this.c = c;
        }

        public Builder requestType(Type requestType) {
            switch (requestType) {
                case PUT:
                    typeRequest = PUT;
                    break;
                case POST:
                    typeRequest = POST;
                    break;
                case DELETE:
                    typeRequest = DELETE;
                    break;
                default:
                    typeRequest = GET;
            }
            return this;
        }

        public Builder setOnRequestApiListener(OnRequestApiListener onRequestApiListener) {
            this.onRequestApiListener = onRequestApiListener;
            return this;
        }

        public Builder addPar(String key, String value) {
            if (param.length() > 0) {
                param.append("&");
            } else {
                param.append("?");
            }
            param.append(key).append("=").append(value);
            return this;
        }

        public Builder addPar(String key, int value) {
            if (param.length() > 0) {
                param.append("&");
            } else {
                param.append("?");
            }
            param.append(key).append("=").append(value);
            return this;
        }

        public RequestApi build() {
            return new RequestApi(this);
        }
    }

    private void request() {
        try {
            final URL uri = new URL(URL + param.toString());
            final Handler handler = new Handler();
            new Thread(() -> {
                HttpsURLConnection urlConnection = null;
                try {
                    urlConnection = (HttpsURLConnection) uri.openConnection();
                    urlConnection.setRequestMethod(typeRequest);
                    urlConnection.setReadTimeout(TIMEOUT);
                    urlConnection.connect();

                    InputStream inputStream;

                    int code = urlConnection.getResponseCode();
                    if (code == HttpURLConnection.HTTP_OK) {
                        inputStream = urlConnection.getInputStream();
                    } else {
                        inputStream = urlConnection.getErrorStream();
                    }

                    String result = getLines(inputStream);

                    if (Constants.DEBUG) {
                        Log.d(TAG, result);
                    }

                    Object obj = null;

                    try {
                        obj = gson.fromJson(result, c);
                    } catch (JsonSyntaxException ignored) {
                        Log.e(TAG, "JsonError", ignored);
                    }

                    final Object finalObj = obj;

                    handler.post(() -> {
                        onRequestApiListener.onSuccess(finalObj, code);
                    });
                } catch (Exception e) {
                    Log.d(TAG, "Fail connection");
                    e.printStackTrace();
                    onRequestApiListener.onError();
                }

            }).start();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private String getLines(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                is, StandardCharsets.UTF_8), 8);
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        is.close();
        String response = sb.toString();
        Log.d(TAG, response);
        return response;
    }

}
