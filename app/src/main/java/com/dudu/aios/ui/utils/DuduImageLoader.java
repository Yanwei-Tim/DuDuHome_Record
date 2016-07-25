package com.dudu.aios.ui.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by robi on 2016-04-18 17:47.
 */
public class DuduImageLoader {
    private static DuduImageLoader imageLoader;
    private LruCache<String, Bitmap> mMemoryCache;

    private Logger logger = LoggerFactory.getLogger("ui.image");

    private DuduImageLoader() {
        //获取系统分配给每个应用程序的最大内存，每个应用系统分配32M
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int mCacheSize = maxMemory / 8;
        //给LruCache分配1/8 4M
        mMemoryCache = new LruCache<String, Bitmap>(mCacheSize) {

            //必须重写此方法，来测量Bitmap的大小
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
    }

    public static DuduImageLoader du() {
        if (imageLoader == null) {
            synchronized (DuduImageLoader.class) {
                if (imageLoader == null) {
                    imageLoader = new DuduImageLoader();
                }
            }
        }
        return imageLoader;
    }

    public void cleanAll() {
        mMemoryCache.evictAll();
    }

    /**
     * 添加Bitmap到内存缓存
     *
     * @param key
     * @param bitmap
     */
    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null && bitmap != null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    /**
     * 从内存缓存中获取一个Bitmap
     *
     * @param key
     * @return
     */
    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    public void display(ImageView imageView, String filePath) {
        Bitmap cache = getBitmapFromMemCache(filePath);
        if (cache == null) {
            Observable.just(filePath)
                    .map(s -> ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(filePath),
                            Math.max(imageView.getMeasuredWidth(), 100), Math.max(imageView.getMeasuredHeight(), 100)))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(bitmap -> {
                        imageView.setImageBitmap(bitmap);
                        addBitmapToMemoryCache(filePath, bitmap);
                    }, throwable -> logger.error("display", throwable));
        } else {
            imageView.setImageBitmap(cache);
        }
    }
}
