/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.fantasy.coolgif.blur;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

import com.fantasy.coolgif.main.MainApplication;


/**
 * Created by baidu on 16/2/26.
 */
public final class BlurFactory {

    private static final BlurFactory SINSTANCE = new BlurFactory();

    public static final float SCALE_FACTOR = 0.125f;
    public static final int BLUR_RADIUS = 2;

    private static RenderScript sRenderScript;
    private final Context mContext;
    private float mRadius = 2.0f;
    private boolean mRenderScrptInited;

    private BlurFactory() {
        mContext = MainApplication.getInstance().getApplicationContext();
    }

    public static BlurFactory getDefault() {
        return SINSTANCE;
    }

    public Bitmap blur(Bitmap bitmap) {
        final int width = bitmap.getWidth();
        final int height = bitmap.getHeight();
        if (width < 8 || height < 8) {
            return bitmap;
        }

        if (beyondJellyBeanMR1()) {
            RenderScript renderScript = getRenderScript();
            if (renderScript != null) {
                try {
                    return blurBlur(renderScript, bitmap);
                } catch (Throwable th) {

                }
            }
        }

        return blur2(bitmap);
    }

    public static final boolean beyondJellyBeanMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;//17
    }

    /**
     * target 17以下的模糊效果
     *
     * @param bkg
     * @return
     */
    public Bitmap blur2(Bitmap bkg) {
        try {

            Bitmap overlay =
                    Bitmap.createBitmap((int) (bkg.getWidth() * SCALE_FACTOR), (int) (bkg.getHeight() * SCALE_FACTOR),
                            Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(overlay);
            canvas.scale(SCALE_FACTOR, SCALE_FACTOR);
            Paint paint = new Paint();
            paint.setFlags(Paint.FILTER_BITMAP_FLAG);
            canvas.drawBitmap(bkg, 0, 0, paint);
            overlay = FastBlur.doBlur(overlay, BLUR_RADIUS, true);
            return overlay;
        } catch (OutOfMemoryError error) {
            System.gc();
            System.gc();
        }
        return bkg;
    }

    @TargetApi(17)
    private Bitmap blurBlur(RenderScript renderScript, Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap createScaledBitmap = Bitmap.createScaledBitmap(bitmap, width / 4, height / 4, true);
        Allocation createFromBitmap = Allocation.createFromBitmap(renderScript, createScaledBitmap);
        Allocation createTyped = Allocation.createTyped(renderScript, createFromBitmap.getType());
        ScriptIntrinsicBlur create = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        create.setRadius(mRadius);
        create.setInput(createFromBitmap);
        create.forEach(createTyped);
        createTyped.copyTo(createScaledBitmap);
        Bitmap createScaledBitmap2 = Bitmap.createScaledBitmap(createScaledBitmap, width, height, true);
        if (createScaledBitmap2 != createScaledBitmap) {
            createScaledBitmap.recycle();
        }
//        if (createScaledBitmap2 != bitmap) {
//            bitmap.recycle();
//        }

        return createScaledBitmap2;
    }

    private RenderScript getRenderScript() {
        if (!mRenderScrptInited) {
            synchronized (BlurFactory.class) {
                if (!mRenderScrptInited) {
                    mRenderScrptInited = true;
                    try {
                        sRenderScript =
                                RenderScript.create(MainApplication.getInstance().getApplicationContext());
                    } catch (Throwable th) {

                    }
                }
            }
        }

        return sRenderScript;
    }
}
