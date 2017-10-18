package br.com.damasceno.agenda.util;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import br.com.damasceno.agenda.activity.R;

/**
 * Created by dmscn on 18/10/17.
 */

public class HttpUtil {
    public static String attemptHttpRequest(Context context, String strUrl, String jsonParam, String method, Boolean hasResponse) throws Exception {

        String response = null;

        // New URL
        URL url = new URL(strUrl);

        // Open Connection
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

        // Config HttpUrlConnection
        httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        httpURLConnection.setRequestMethod(method);
        httpURLConnection.setConnectTimeout(10000);
        httpURLConnection.setReadTimeout(10000);
        httpURLConnection.setDoInput(true);

        if(jsonParam != null) {
            httpURLConnection.setDoOutput(true);

            // send parameters
            DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
            dataOutputStream.write(jsonParam.getBytes("UTF-8"));
            dataOutputStream.flush();
            dataOutputStream.close();
        }

        // Attempt Connection
        httpURLConnection.connect();

        if(hasResponse) {
            if(httpURLConnection.getResponseCode() != 200) {
                throw new Exception(context.getString(R.string.msg_error_getting_web_service_response));
            } else {
                InputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                response = convertStreamToString(inputStream);
                inputStream.close();
            }
        } else {
            if(httpURLConnection.getResponseCode() != 200) {
                throw new Exception(context.getString(R.string.msg_error_getting_web_service_response));
            }
        }

        return response;

    }

    public static String convertStreamToString(InputStream in) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        StringBuilder sb = new StringBuilder();

        String line = null;

        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {

            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }
}
