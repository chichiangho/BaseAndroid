package com.chichiangho.base.http;

import com.chichiangho.base.base.BaseResponse;
import com.chichiangho.base.utils.JsonUtil;
import com.chichiangho.base.utils.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by chichiangho on 2017/4/1.
 */

public class OkHttpClient {
    private static final String TAG = "OkHttpClient";

    public interface ProgressListener {
        void onProgress(int progress, Exception e);
    }

    public static Response get(String url) throws IOException {
        Logger.d(TAG, url);
        Request request = new Request.Builder().url(url).build();
        Response response = OkHttpClientManager.get().newCall(request).execute();
        return response;
    }

    public static <T> T post(String url, RequestParam param, Class<T> clz) throws Exception {
        String json = JsonUtil.toJson(param);
        Logger.d(TAG, url);
        Logger.json(TAG, "request " + json);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8")
                , json);
        Request request = new Request.Builder().url(url).post(body).build();
        Response response = OkHttpClientManager.get().newCall(request).execute();
        if (response.isSuccessful()) {
            String result = response.body().string();
            Logger.json(TAG, "response " + result);
            return JsonUtil.fromJson(result, clz);
        } else {
            Logger.d(TAG, "error " + response.code() + ":" + response.message());
            T res = clz.newInstance();
            ((BaseResponse) res).setCode(response.code() + "");
            ((BaseResponse) res).setMsg(response.message());
            return res;
        }
    }

    public static void downLoad(String url, String path) throws Exception {
        downLoad(url, path, null);
    }

    public static void downLoad(final String url, final String path, final ProgressListener listener) throws Exception {
        Logger.d(TAG, "downloading " + url + " to " + path);
        Request request = new Request.Builder().url(url).build();
        OkHttpClientManager.get().newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                if (listener != null)
                    listener.onProgress(-1, e);
                Logger.d(TAG, "download " + url + " failure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                FileOutputStream fos = null;
                byte[] buf = new byte[2048];
                int len;
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    File file = new File(path);
                    if (!file.exists())
                        file.createNewFile();
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        if (listener != null) {
                            sum += len;
                            int progress = (int) (sum * 1.0f / total * 100);
                            listener.onProgress(progress, null);
                        }
                    }
                    fos.flush();
                    Logger.d(TAG, "download " + url + " success");
                } catch (Exception e) {
                    if (listener != null)
                        listener.onProgress(-1, e);
                    Logger.d(TAG, "download " + url + " failure");
                } finally {
                    if (is != null)
                        is.close();
                    if (fos != null)
                        fos.close();
                }
            }
        });
    }

    public static void upLoad(String url, String path) throws Exception {
        upLoad(url, path, null);
    }

    public static void upLoad(String url, final String path, final ProgressListener listener) throws Exception {
        Logger.d(TAG, "uploading " + path + " to " + url);
        File file = new File(path);
        if (!file.exists())
            throw new Exception(path + " not exists!");

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"another\";filename=\"another.dex\""),
                RequestBody.create(MediaType.parse("application/octet-stream"), file));

        if (listener == null)
            builder.addFormDataPart("file", file.getName(), builder.build());
        else
            builder.addFormDataPart("file", file.getName(), new ProgressRequestBody(builder.build(), listener));

        RequestBody requestBody = builder.build();

        Request request = new Request.Builder().url(url).post(requestBody).build();

        OkHttpClientManager.get().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Logger.d(TAG, "upload " + path + " failure");
                if (listener != null)
                    listener.onProgress(-1, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Logger.d(TAG, "upload " + path + " success");
                    if (listener != null)
                        listener.onProgress(100, null);
                } else {
                    Logger.d(TAG, "upload " + path + " failure");
                    if (listener != null)
                        listener.onProgress(-1, new Exception(response.message()));

                }
            }
        });
    }
}
