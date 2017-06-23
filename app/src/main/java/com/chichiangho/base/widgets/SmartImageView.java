package com.chichiangho.base.widgets;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;

public class SmartImageView extends AppCompatImageView {


    public SmartImageView(Context context) {
        super(context);
    }

    public SmartImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SmartImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 常规加载
     *
     * @param url
     */
    public void load(String url) {
        Glide.with(getContext()).load(url).into(this);
    }

    /**
     * @param url
     * @param placeholder 默认
     * @param error       错误
     */

    public void load(String url, @DrawableRes int placeholder, @DrawableRes int error) {
        Glide.with(getContext()).load(url).placeholder(placeholder).error(error).into(this);
    }

    /**
     * 先加载缩略图
     *
     * @param url
     * @param thunbnail 0.0f~1.0f
     */
    public void load(String url, float thunbnail) {
        Glide.with(getContext()).load(url).thumbnail(thunbnail).into(this);
    }

    /**
     * 带监听的加载
     *
     * @param url
     * @param listener
     */
    public void load(String url, RequestListener<? super String, GlideDrawable> listener) {
        Glide.with(getContext()).load(url).listener(listener).into(this);
    }

    /**
     * 加载圆图
     *
     * @param url
     */
    public void loadCircle(String url) {
        Glide.with(getContext()).load(url).transform(new GlideCircleTransform(getContext())).into(this);
    }

    /**
     * 加载圆图
     *
     * @param url
     * @param dp
     */
    public void loadRound(String url, float dp) {
        Glide.with(getContext()).load(url).transform(new GlideRoundTransform(getContext(), dp)).into(this);
    }

    /**
     * 圆角转换器
     */
    public class GlideRoundTransform extends BitmapTransformation {
        private float radius = 0f;

        public GlideRoundTransform(Context context, float dp) {
            super(context);
            this.radius = Resources.getSystem().getDisplayMetrics().density * dp;
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return roundCrop(pool, toTransform);
        }

        private Bitmap roundCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            }
            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
            canvas.drawRoundRect(rectF, radius, radius, paint);
            return result;
        }

        @Override
        public String getId() {
            return getClass().getName() + Math.round(radius);
        }
    }

    public class GlideCircleTransform extends BitmapTransformation {

        public GlideCircleTransform(Context context) {
            super(context);
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return roundCrop(pool, toTransform);
        }

        private Bitmap roundCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            }
            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);

            if (source.getWidth() > source.getHeight()) {
                RectF rectF = new RectF((source.getWidth() - source.getHeight()) / 2, 0f, source.getHeight() + (source.getWidth() - source.getHeight()) / 2, source.getHeight());
                canvas.drawRoundRect(rectF, source.getHeight(), source.getHeight(), paint);
            } else {
                RectF rectF = new RectF(0f, (source.getHeight() - source.getWidth()) / 2, source.getWidth(), source.getWidth() + (source.getHeight() - source.getWidth()) / 2);
                canvas.drawRoundRect(rectF, source.getWidth(), source.getWidth(), paint);
            }
            return result;
        }

        @Override
        public String getId() {
            return getClass().getName() + Math.round(100);
        }
    }
}
