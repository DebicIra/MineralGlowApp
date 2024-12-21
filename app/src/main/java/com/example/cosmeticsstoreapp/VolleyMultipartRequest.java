package com.example.cosmeticsstoreapp;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class VolleyMultipartRequest extends Request<NetworkResponse> {

    private final Response.Listener<NetworkResponse> mListener;
    private final Response.ErrorListener mErrorListener;
    private final Map<String, String> mHeaders;

    public VolleyMultipartRequest(int method, String url,
                                  Response.Listener<NetworkResponse> listener,
                                  Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mListener = listener;
        this.mErrorListener = errorListener;
        this.mHeaders = new HashMap<>();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHeaders != null ? mHeaders : super.getHeaders();
    }

    @Override
    public String getBodyContentType() {
        return "multipart/form-data; boundary=" + getBoundary();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            writeMultipartData(byteArrayOutputStream);
        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        }
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {
        return Response.success(response, null);
    }

    @Override
    protected void deliverResponse(NetworkResponse response) {
        mListener.onResponse(response);
    }

    @Override
    public void deliverError(com.android.volley.VolleyError error) {
        mErrorListener.onErrorResponse(error);
    }

    protected Map<String, String> getParams() throws AuthFailureError {
        return null;
    }

    protected Map<String, DataPart> getByteData() throws AuthFailureError {
        return null;
    }

    private String getBoundary() {
        return "----WebKitFormBoundary" + System.currentTimeMillis();
    }

    private void writeMultipartData(ByteArrayOutputStream outputStream) throws IOException, AuthFailureError {
        final String boundary = getBoundary();

        // Параметры
        Map<String, String> params = getParams();
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                outputStream.write(("--" + boundary + "\r\n").getBytes());
                outputStream.write(("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"\r\n").getBytes());
                outputStream.write(("\r\n").getBytes());
                outputStream.write((entry.getValue() + "\r\n").getBytes());
            }
        }

        // Данные файла
        Map<String, DataPart> data = getByteData();
        if (data != null && data.size() > 0) {
            for (Map.Entry<String, DataPart> entry : data.entrySet()) {
                outputStream.write(("--" + boundary + "\r\n").getBytes());
                outputStream.write(("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"; filename=\""
                        + entry.getValue().getFileName() + "\"\r\n").getBytes());
                outputStream.write(("Content-Type: " + entry.getValue().getType() + "\r\n").getBytes());
                outputStream.write(("\r\n").getBytes());
                outputStream.write(entry.getValue().getContent());
                outputStream.write(("\r\n").getBytes());
            }
        }

        outputStream.write(("--" + boundary + "--\r\n").getBytes());
    }

    public static class DataPart {
        private final String fileName;
        private final byte[] content;
        private final String type;

        public DataPart(String fileName, byte[] content, String type) {
            this.fileName = fileName;
            this.content = content;
            this.type = type;
        }

        public String getFileName() {
            return fileName;
        }

        public byte[] getContent() {
            return content;
        }

        public String getType() {
            return type;
        }
    }
}
