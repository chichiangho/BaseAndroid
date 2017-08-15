package com.chichiangho.base.http

import com.chichiangho.base.base.BaseResponse
import com.chichiangho.base.utils.JsonUtil
import com.chichiangho.base.utils.Logger
import okhttp3.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

/**
 * Created by chichiangho on 2017/4/1.
 */

object OkHttpClient {
    private val TAG = "OkHttpClient"

    interface ProgressListener {
        fun onProgress(progress: Int, e: Exception?)
    }

    @Throws(IOException::class)
    operator fun get(url: String): Response {
        Logger.d(TAG, url)
        val request = Request.Builder().url(url).build()
        val response = OkHttpClientManager.get().newCall(request).execute()
        return response
    }

    @Throws(Exception::class)
    fun <T> post(url: String, param: RequestParam, clz: Class<T>): T {
        val json = JsonUtil.toJson(param)
        Logger.d(TAG, url)
        Logger.json(TAG, "request " + json)
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        val request = Request.Builder().url(url).post(body).build()
        val response = OkHttpClientManager.get().newCall(request).execute()
        if (response.isSuccessful) {
            val result = response.body().string()
            Logger.json(TAG, "response " + result)
            return JsonUtil.fromJson(result, clz)
        } else {
            Logger.d(TAG, "error " + response.code() + ":" + response.message())
            val res = clz.newInstance()
            (res as BaseResponse).code = response.code().toString() + ""
            (res as BaseResponse).msg = response.message()
            return res
        }
    }

    @Throws(Exception::class)
    @JvmOverloads fun downLoad(url: String, path: String, listener: ProgressListener? = null) {
        Logger.d(TAG, "downloading $url to $path")
        val request = Request.Builder().url(url).build()
        OkHttpClientManager.get().newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                listener?.onProgress(-1, e)
                Logger.d(TAG, "download $url failure")
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                var `is`: InputStream? = null
                var fos: FileOutputStream? = null
                val buf = ByteArray(2048)
                var len: Int
                try {
                    `is` = response.body().byteStream()
                    val total = response.body().contentLength()
                    val file = File(path)
                    if (!file.exists())
                        file.createNewFile()
                    fos = FileOutputStream(file)
                    var sum: Long = 0
                    while (true) {
                        len = `is`!!.read(buf)
                        if (len == -1)
                            break
                        fos.write(buf, 0, len)
                        if (listener != null) {
                            sum += len.toLong()
                            val progress = (sum * 1.0f / total * 100).toInt()
                            listener.onProgress(progress, null)
                        }
                    }
                    fos.flush()
                    Logger.d(TAG, "download $url success")
                } catch (e: Exception) {
                    listener?.onProgress(-1, e)
                    Logger.d(TAG, "download $url failure")
                } finally {
                    if (`is` != null)
                        `is`.close()
                    if (fos != null)
                        fos.close()
                }
            }
        })
    }

    @Throws(Exception::class)
    @JvmOverloads fun upLoad(url: String, path: String, listener: ProgressListener? = null) {
        Logger.d(TAG, "uploading $path to $url")
        val file = File(path)
        if (!file.exists())
            throw Exception(path + " not exists!")

        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)

        builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"another\";filename=\"another.dex\""),
                RequestBody.create(MediaType.parse("application/octet-stream"), file))

        if (listener == null)
            builder.addFormDataPart("file", file.name, builder.build())
        else
            builder.addFormDataPart("file", file.name, ProgressRequestBody(builder.build(), listener))

        val requestBody = builder.build()

        val request = Request.Builder().url(url).post(requestBody).build()

        OkHttpClientManager.get().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Logger.d(TAG, "upload $path failure")
                listener?.onProgress(-1, e)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    Logger.d(TAG, "upload $path success")
                    listener?.onProgress(100, null)
                } else {
                    Logger.d(TAG, "upload $path failure")
                    listener?.onProgress(-1, Exception(response.message()))

                }
            }
        })
    }
}
