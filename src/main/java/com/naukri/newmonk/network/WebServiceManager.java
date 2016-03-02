package com.naukri.newmonk.network;


import com.naukri.newmonk.Monk;
import com.naukri.newmonk.config.NewMonkConfiguration;
import com.naukri.newmonk.logger.LogMessage;
import com.naukri.newmonk.logger.NewMonkLogger;
import com.naukri.newmonk.metadata.App;
import com.naukri.newmonk.metadata.Device;
import com.naukri.newmonk.metadata.Environment;
import com.naukri.newmonk.metadata.ExceptionModel;
import com.naukri.newmonk.metadata.OS;
import com.naukri.newmonk.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Map;

public class WebServiceManager {

    private static String BASE_URL = null;

    public WebServiceManager() {

    }

    public static void setUrl(String url) {
        BASE_URL = url;
    }

    /**
     * Send exception to api
     *
     * @param networkRequest
     * @return
     */
    public int sendException(NetworkRequest networkRequest) {
        int responseCode = HttpURLConnection.HTTP_INTERNAL_ERROR;
        try {
            NewMonkLogger.info(LogMessage.HIT_WEB_SERVICE);
            String request = getRequestString(networkRequest);
            NewMonkLogger.info(LogMessage.NWK_REQUEST + request);
            responseCode = sendRequest("", request, NetworkConstant.HTTP_METHOD_POST);
        } catch (Exception exception) {
            NewMonkLogger.info(LogMessage.ERROR_EXCEPTION, exception);
        }
        return responseCode;
    }

    /**
     * Handle Json Request
     *
     * @param serverURL
     * @param jsonRequest
     * @param httpMethod
     * @return
     */
    private int sendRequest(String serverURL, String jsonRequest, int httpMethod) {
        int responseCode = HttpURLConnection.HTTP_INTERNAL_ERROR;
        InputStream inputStream;
        String jsonResponse = null;
        OutputStream outputStream;
        Exception exception = null;

        //Get Http Client/Connection
        HttpURLConnection httpURLConnection = getHttpClient(BASE_URL, httpMethod);

        if (httpURLConnection != null) {
            try {
                //Get Output Stream
                outputStream = httpURLConnection.getOutputStream();

                //Write
                OutputStreamWriter ouStreamWriter = new OutputStreamWriter(outputStream);
                ouStreamWriter.write(jsonRequest);

                //Flush and Close
                ouStreamWriter.flush();
                ouStreamWriter.close();

                //Get Response Code
                responseCode = httpURLConnection.getResponseCode();

                //Check StatusCode
                if (Util.isSuccessResponse(responseCode)) //SUCCESS
                {
                    //Get Input Stream
                    inputStream = httpURLConnection.getInputStream();
                    //Get Json
                    jsonResponse = readInputStream(inputStream);
                    //Close
                    inputStream.close();
                    NewMonkLogger.info(LogMessage.SEND_SUCCESS + responseCode);
                } else //ERROR
                {
                    NewMonkLogger.error(LogMessage.INCORRECT_RESPONSE_CODE + responseCode);
                    /*//Get Error Stream
                    inputStream = httpURLConnection.getErrorStream();
                    //Get Json
                    jsonResponse = readInputStream(inputStream); // Here Response is not for further use
                    //Close
                    inputStream.close();*/
                }
            } catch (IOException ex) {
                //Socket Input/Output Exception
                //Monk.sendException(WebServiceManager.class.getName().toString(), ex);
                NewMonkLogger.error(LogMessage.ERROR_SOCKET_TIMEOUT, ex);
            } catch (Exception ex) {
                //Monk.sendException(WebServiceManager.class.getName().toString(), ex);
                NewMonkLogger.error(LogMessage.ERROR_EXCEPTION, ex);

            } finally {
                httpURLConnection.disconnect();
            }
        }

        NewMonkLogger.info(LogMessage.NWK_RESPONSE + jsonResponse);

        /*if(exception == null)       // if no exception occur, set success in response.
        jsonResponse = Constants.SUCCESS;*/

        return responseCode;
    }

    /**
     * Get Http Url Connection Instance
     *
     * @param serverURL
     * @param httpMethod
     * @return
     */
    private HttpURLConnection getHttpClient(String serverURL, int httpMethod) {
        HttpURLConnection httpURLConnection = null;
        try {
            //Create URL
            URL url = new URL(serverURL);

            //Get Url Connection
            URLConnection conn = url.openConnection();
            httpURLConnection = (HttpURLConnection) conn;

            //Set Timeout
            httpURLConnection.setConnectTimeout(NetworkConstant.TIME_CONNECTION_TIMEOUT); //Timeout until a connection is established
            httpURLConnection.setReadTimeout(NetworkConstant.TIME_READ_TIMEOUT); //Timeout for waiting for data to come

            //Set Headers
            prepareHeader(httpURLConnection);

            //Set Input
            httpURLConnection.setDoInput(true);

            //Check Http Method
            if (httpMethod == NetworkConstant.HTTP_METHOD_POST) {
                //Post Request - Set Body Output
                httpURLConnection.setDoOutput(true);
            } else {
                //Get Request - Set Body Output False
                httpURLConnection.setDoOutput(false);
            }
            //Connect
            httpURLConnection.connect();
        } catch (IOException exception) {
            //Connection Timeout Exception
            //Monk.sendException(WebServiceManager.class.getName().toString(), exception);
            NewMonkLogger.error(LogMessage.ERROR_CONNECTION_TIME_OUT, exception);
        } catch (Exception exception) {
            //Monk.sendException(WebServiceManager.class.getName().toString(), exception);
            NewMonkLogger.info(LogMessage.ERROR_EXCEPTION, exception);
        }
        return httpURLConnection;
    }

    /**
     * Read Input Stream
     *
     * @param inputStream
     * @return
     */
    private String readInputStream(InputStream inputStream) {
        //String Builder
        StringBuilder stringBuilder = new StringBuilder();

        String subMessage;
        try {
            InputStreamReader inStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferReader = new BufferedReader(inStreamReader);

            while ((subMessage = bufferReader.readLine()) != null) {
                stringBuilder.append(subMessage);
            }

            //Close
            bufferReader.close();
            inStreamReader.close();
        } catch (Exception exception) {
            //Monk.sendException(WebServiceManager.class.getName().toString(), exception);
            NewMonkLogger.error(LogMessage.ERROR_EXCEPTION, exception);
        }
        return stringBuilder.toString();
    }

    /**
     * Convert NetworkRequest into request string.
     *
     * @param networkRequest
     * @return String
     * @throws JSONException
     */
    private String getRequestString(NetworkRequest networkRequest) throws JSONException {
        JSONObject jsonObject = new JSONObject();

        if (networkRequest != null) {
            jsonObject.put(NetworkConstant.KEY_SOURCE, Util.getString(networkRequest.source));
            jsonObject.put(NetworkConstant.KEY_APP_ID, Util.getString(networkRequest.appId));
            jsonObject.put(NetworkConstant.KEY_UID, Util.getString(networkRequest.uId));

            Environment environment = networkRequest.environment;
            if (environment != null) {
                JSONObject jsonObjectEnv = new JSONObject();
                OS os = environment.os;

                if (os != null) {
                    JSONObject jsonObjectOs = new JSONObject();
                    jsonObjectOs.put(NetworkConstant.KEY_NAME, Util.getString(os.name));
                    jsonObjectOs.put(NetworkConstant.KEY_VERSION, os.version);
                    jsonObjectEnv.put(NetworkConstant.KEY_OS, jsonObjectOs);
                }

                App app = environment.app;

                if (app != null) {
                    JSONObject jsonObjectApp = new JSONObject();
                    jsonObjectApp.put(NetworkConstant.KEY_VERSION, app.version);
                    jsonObjectEnv.put(NetworkConstant.KEY_APP, jsonObjectApp);
                }

                Device device = environment.device;

                if (device != null) {
                    JSONObject jsonObjectDevice = new JSONObject();
                    jsonObjectDevice.put(NetworkConstant.KEY_NAME, Util.getString(device.name));
                    jsonObjectEnv.put(NetworkConstant.KEY_DEVICE, jsonObjectDevice);
                }

                jsonObject.put(NetworkConstant.KEY_ENVIRONMENT, jsonObjectEnv);
            }

            if (networkRequest.exceptionLog != null) {
                ExceptionModel exceptionModel[] = networkRequest.exceptionLog.exceptionModel;

                if (exceptionModel != null) {
                    JSONArray jsonArray = new JSONArray();
                    int totalException = exceptionModel.length;
                    JSONObject jsonObjectException;

                    for (int i = 0; i < totalException; i++) {
                        jsonObjectException = new JSONObject();
                        jsonObjectException.put(NetworkConstant.KEY_TAG, Util.getString(exceptionModel[i].tag));
                        jsonObjectException.put(NetworkConstant.KEY_COUNT, exceptionModel[i].count);
                        jsonObjectException.put(NetworkConstant.KEY_TIME_STAMP, exceptionModel[i].timestamp);
                        jsonObjectException.put(NetworkConstant.KEY_TYPE, Util.getString(exceptionModel[i].type));
                        jsonObjectException.put(NetworkConstant.KEY_MESSAGE, Util.getString(exceptionModel[i].message));
                        jsonObjectException.put(NetworkConstant.KEY_CODE, exceptionModel[i].code);
                        jsonObjectException.put(NetworkConstant.KEY_FILE, Util.getString(exceptionModel[i].file));
                        jsonObjectException.put(NetworkConstant.KEY_LINE, exceptionModel[i].line);
                        jsonObjectException.put(NetworkConstant.KEY_STACKTRACE, Util.getString(exceptionModel[i].stackTrace));
                        jsonArray.put(jsonObjectException);
                    }
                    jsonObject.put(NetworkConstant.KEY_EXCEPTION, jsonArray);
                }
            }
        }
        return jsonObject.toString();
    }

//    private static class HitWebService extends Thread {
//        NetworkRequest networkRequest = null;
//
//        HitWebService(NetworkRequest networkRequest) {
//            this.networkRequest = networkRequest;
//        }
//
//        @Override
//        public void run() {
//            super.run();
//
//            hitWebService();
//        }
//
//    }

   /* */

    /**
     * HIt Web service with logs
     *
     * @param
     *//*
    private int hitWebService(NetworkRequest networkRequest) {
        int responseCode = HttpURLConnection.HTTP_INTERNAL_ERROR;
        try {
            NewMonkLogger.info(LogMessage.HIT_WEB_SERVICE);
            String request = getRequestString(networkRequest);
            NewMonkLogger.info(LogMessage.NWK_REQUEST + request);
            responseCode = sendRequest("", request, NetworkConstant.HTTP_METHOD_POST);
        } catch (Exception exception) {
            //Monk.sendException(WebServiceManager.class.getName().toString(), exception);
            NewMonkLogger.info(LogMessage.ERROR_EXCEPTION, exception);
        }
        return responseCode;
    }*/
    private void prepareHeader(HttpURLConnection httpURLConnection) {
        NewMonkConfiguration newMonkConfiguration = Monk.getNewMonkConfiguration();

        Map headers = newMonkConfiguration.httpHeaders;

        if (headers != null) {
            Iterator iterator = headers.entrySet().iterator();

            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry) iterator.next();

                if (entry != null)
                    httpURLConnection.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
    }
}
