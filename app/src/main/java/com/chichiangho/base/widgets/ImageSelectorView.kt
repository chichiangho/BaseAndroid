package com.chichiangho.base.widgets

import android.annotation.TargetApi
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chichiangho.base.R
import com.chichiangho.base.extentions.dpToPx
import com.chichiangho.base.extentions.toastShort
import java.util.*

/**
 * Note:if local path image was not show on api >= 23(6.0),make sure you have request the permission
 * Created by chichiangho on 2017/7/15.
 */

class ImageSelectorView : RecyclerView {
    private val ImagePaths = ArrayList<String>(4)
    private var onItemClickListener: OnItemClickListener? = null
    private var maxCount = 4
    private var columns = 4

    interface OnItemClickListener {
        fun onItemClick(v: View, position: Int)
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        layoutManager = GridLayoutManager(context, columns)
        adapter = Adapter()
        ImagePaths.add("")
    }

    val imagePaths: ArrayList<String>
        get() {
            val paths = ArrayList<String>(maxCount)
            ImagePaths.filterTo(paths) { it != "" }
            return paths
        }

    fun setData(paths: ArrayList<String>) {
        ImagePaths.clear()
        ImagePaths.addAll(paths)
        if (ImagePaths.size < maxCount) {
            ImagePaths.add("")
        }
        adapter.notifyDataSetChanged()
    }

    fun setData(position: Int, path: String) {
        if (position >= ImagePaths.size)
            return
        if (position == ImagePaths.size - 1 && position < maxCount - 1)
            ImagePaths.add(position, path)
        else
            ImagePaths[position] = path
        adapter.notifyDataSetChanged()
    }

    fun setData(position: Int, uri: Uri) {
        val path = getRealFilePath(uri)
        if (path == null) {
            toastShort("第" + (position + 1) + "张图片不存在，请选择其他图片")
            return
        }
        if (position == ImagePaths.size - 1 && position < maxCount - 1)
            ImagePaths.add(position, path)
        else
            ImagePaths[position] = path
        adapter.notifyDataSetChanged()
    }

    fun setColumns(columns: Int) {
        this.columns = columns
        if (maxCount < columns)
            maxCount = columns
        layoutManager = GridLayoutManager(context, columns)
    }

    fun setMaxCount(maxCount: Int) {
        if (maxCount < columns)
            setColumns(maxCount)
        this.maxCount = maxCount
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (width != 0) {
            super.onMeasure(width, width / columns * if (ImagePaths.size % columns == 0) ImagePaths.size / columns else ImagePaths.size / columns + 1)
        } else {
            val heightMeasureSpec = View.MeasureSpec
                    .makeMeasureSpec(Integer.MAX_VALUE shr 2, View.MeasureSpec.AT_MOST)
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    private inner class Adapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val iv = ImageView(context)
            var width = width
            if (width == 0)
                width = measuredWidth
            val params = ViewGroup.LayoutParams(width / columns, width / columns)
            iv.layoutParams = params
            val padding = dpToPx(5)
            iv.setPadding(padding, padding, padding, padding)
            return Holder(iv)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            Glide.with(context).load(ImagePaths[position]).placeholder(R.drawable.btn_add).into((holder as Holder).iv)
            holder.iv.setOnClickListener {
                onItemClickListener?.onItemClick(holder.iv, position)
            }
        }

        override fun getItemCount(): Int = ImagePaths.size
    }

    private inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var iv: ImageView = itemView as ImageView
    }

    private fun getRealFilePath(uri: Uri?): String? {
        if (null == uri)
            return null
        val scheme = uri.scheme
        var data: String? = null
        if (scheme == null)
            data = uri.path
        else if (ContentResolver.SCHEME_FILE == scheme) {
            data = uri.path
        } else if (ContentResolver.SCHEME_CONTENT == scheme) {
            val cursor = context.contentResolver.query(uri, arrayOf(MediaStore.Images.ImageColumns.DATA), null, null, null)
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    val index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                    if (index > -1) {
                        data = cursor.getString(index)
                    }
                }
                cursor.close()
            }
            if (data == null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    data = getImageAbsolutePath(context, uri)
                }
            }

        }
        return data
    }

    /**
     * 根据Uri获取图片绝对路径，解决Android4.4以上版本Uri转换
     */
    @TargetApi(19)
    private fun getImageAbsolutePath(context: Context?, imageUri: Uri?): String? {
        if (context == null || imageUri == null)
            return null
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                val docId = DocumentsContract.getDocumentId(imageUri)
                val split = docId.split(":".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }
            } else if (isDownloadsDocument(imageUri)) {
                val id = DocumentsContract.getDocumentId(imageUri)
                val contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)!!)
                return getDataColumn(context, contentUri, null, null)
            } else if (isMediaDocument(imageUri)) {
                val docId = DocumentsContract.getDocumentId(imageUri)
                val split = docId.split(":".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                val selection = MediaStore.Images.Media._ID + "=?"
                val selectionArgs = arrayOf<String>(split[1])
                return getDataColumn(context, contentUri!!, selection, selectionArgs)
            }
        } // MediaStore (and general)
        else if ("content".equals(imageUri.scheme, ignoreCase = true)) {
            // Return the remote address
            if (isGooglePhotosUri(imageUri))
                return imageUri.lastPathSegment
            return getDataColumn(context, imageUri, null, null)
        } else if ("file".equals(imageUri.scheme, ignoreCase = true)) {
            return imageUri.path
        }// File
        return null
    }

    private fun getDataColumn(context: Context, uri: Uri, selection: String?, selectionArgs: Array<String>?): String? {
        var cursor: Cursor? = null
        val column = MediaStore.Images.Media.DATA
        val projection = arrayOf(column)
        try {
            cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            if (cursor != null)
                cursor.close()
        }
        return null
    }

    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    private fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }
}
