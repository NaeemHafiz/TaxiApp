package com.icanstudioz.taxi.utils;

import android.content.Context;
import android.util.Log;

import com.icanstudioz.taxi.interfaces.NetworkListener;
import com.icanstudioz.taxi.models.Parameter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class NetworkTask extends BackgroundTask {

    private static final String devHost = "http://zealchemical.com/taxi/";
    private static final String host = devHost;
    protected NetworkListener listener = null;
    protected String results = null;
    protected int statusCode = -1;
    String baseUrl = host;
    //    String baseUrl = host + "api/";
    String type;
    ArrayList<Parameter> params;
    String url = "";
    String charset = "UTF-8";
    public NetworkTask(Context context, String type, String url, ArrayList<Parameter> params) {
        super(context);
        this.type = type;
        this.params = params;
        this.url = url;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        if (host.equals(devHost) || I_AM_DEV)
//            Log.d("NetworkTask", "URL: " + getAbsoluteUrl() + "?" + getParamsString());
    }

    @Override
    protected Void doInBackground(Void... param) {
        StringBuilder result = new StringBuilder();
        HttpURLConnection conn = null;
        InputStream inputStream = null, bis = null;
        try {
            String fullUrl = getAbsoluteUrl();
            if (type.equals("GET")) {
                fullUrl += ("?" + getParamsString());
            }
            URL urlObj = new URL(fullUrl);
            conn = (HttpURLConnection) urlObj.openConnection();
            if (!type.equals("GET")) conn.setDoOutput(true);

            conn.setRequestMethod(type);
            conn.setRequestProperty("Accept-Charset", charset);
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.connect();

            if (!type.equals("GET")) {
                // required for post request only
                DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                wr.writeBytes(getParamsString());
                wr.flush();
                wr.close();
            }

            //response from the server
            statusCode = conn.getResponseCode();
            inputStream = (statusCode == HttpURLConnection.HTTP_OK ? conn.getInputStream() : conn.getErrorStream());
            bis = new BufferedInputStream(inputStream);
            BufferedReader reader = new BufferedReader(new InputStreamReader(bis));

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            //conn.disconnect();
            results = result.toString();
        } catch (IOException e) {
            e.printStackTrace();
            results = e.getMessage();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (bis != null) {
                    bis.close();
                }
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Log.d("NetworkTask", "URL: " + getAbsoluteUrl() + "?" + getParamsString());
        Log.d("NetworkTask", "Result " + results);
        if (statusCode != HttpURLConnection.HTTP_OK) {
            // this is probably an error so result string will hold error message
            if (listener != null) {
                listener.onError("Code: " + statusCode + " - Error: " + results);
            }
            return;
        }

        try {
            String s = results.toString().trim();
            JSONObject obj = new JSONObject(s);
            Object stat = obj.get("status");
            if (stat instanceof Boolean) {
                boolean status = obj.getBoolean("status");
                if (status) {
                    if (listener != null) {
                        listener.onSuccess(s);
                    }
                } else {
                    if (listener != null) {
                        listener.onError(s);
                    }
                }
            } else if (stat instanceof String) {
                String status = obj.getString("status");
                if (status.equalsIgnoreCase("success")) {
                    if (listener != null) {
                        listener.onSuccess(s);
                    }
                } else {
                    if (listener != null) {
                        listener.onError(s);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            if (listener != null) {
                listener.onError("Invalid JSON response from server.");
            }
        }
    }

    protected String getAbsoluteUrl() {
        return baseUrl + url;
    }

    public void setListener(NetworkListener listener) {
        this.listener = listener;
    }

    public String getParamsString() {
        String p = "";
        String charset = "UTF-8";

        try {
            StringBuilder sbParams = new StringBuilder();
            for (Parameter param : params) {
                String val = param.getValue();
                if (val == null) val = ""; // to avoid NullPointer crash while encoding
                sbParams.append(param.getKey()).append("=")
                        .append(URLEncoder.encode(val, charset));
                sbParams.append("&");
            }
            p = sbParams.toString();
            if (p.length() > 0) {
                sbParams = sbParams.deleteCharAt(sbParams.toString().length() - 1);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }

        return p;
    }
}
