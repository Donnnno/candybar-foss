package candybar.lib.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.data.DataFetcher;
import com.danimahardhika.android.helpers.core.utils.LogUtil;

import java.io.IOException;
import java.io.InputStream;

import candybar.lib.preferences.Preferences;
import sarsamurmu.adaptiveicon.AdaptiveIcon;

public class CommonDataFetcher implements DataFetcher<Bitmap> {
    private final Context mContext;
    private final String mModel;

    CommonDataFetcher(Context context, String model) {
        mContext = context;
        mModel = model;
    }

    @Override
    public void loadData(@NonNull Priority priority, @NonNull DataCallback<? super Bitmap> callback) {
        if (mModel.startsWith("drawable://")) {
            callback.onDataReady(getDrawable(mModel));
        } else if (mModel.startsWith("package://")) {
            callback.onDataReady(getPackage(mModel));
        } else if (mModel.startsWith("assets://")) {
            callback.onDataReady(getAsset(mModel));
        }
    }

    @Nullable
    private Bitmap getPackage(String uri) {
        PackageManager packageManager = mContext.getPackageManager();
        String componentName = uri.replaceFirst("package://", "");

        int slashIndex = componentName.indexOf("/");
        String packageName = componentName.substring(0, slashIndex);
        String activityName = componentName.substring(slashIndex + 1);

        Intent intent = new Intent();
        intent.setComponent(new ComponentName(packageName, activityName));
        ResolveInfo resolveInfo = packageManager.resolveActivity(intent, 0);

        assert resolveInfo != null;
        Drawable drawable = resolveInfo.loadIcon(packageManager);
        if (drawable != null) {
            if (drawable instanceof BitmapDrawable) return ((BitmapDrawable) drawable).getBitmap();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && drawable instanceof AdaptiveIconDrawable) {
                return new AdaptiveIcon()
                        .setDrawable((AdaptiveIconDrawable) drawable)
                        .setPath(AdaptiveIcon.PATH_CIRCLE)
                        .render();
            }
        }

        return null;
    }

    @Nullable
    private Bitmap getDrawable(String uri) {
        String drawableIdStr = uri.replaceFirst("drawable://", "");
        int drawableId = Integer.parseInt(drawableIdStr);
        Drawable drawable = ContextCompat.getDrawable(mContext, drawableId);

        if (drawable instanceof BitmapDrawable) return ((BitmapDrawable) drawable).getBitmap();
        if (drawable instanceof LayerDrawable || drawable instanceof VectorDrawable) {
            final boolean isVector = drawable instanceof VectorDrawable;
            final int width = isVector ? 256 : drawable.getIntrinsicWidth();
            final int height = isVector ? 256 : drawable.getIntrinsicHeight();
            final Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            drawable.setBounds(0, 0, width, height);
            drawable.draw(new Canvas(bitmap));
            return bitmap;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && drawable instanceof AdaptiveIconDrawable) {
            if (Preferences.get(mContext).getIconShape() == -1) {
                // System default icon shape
                Bitmap bitmap = Bitmap.createBitmap(256, 256, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                drawable.setBounds(new Rect(0, 0, 256, 256));
                drawable.draw(canvas);
                return bitmap;
            }

            return new AdaptiveIcon()
                    .setDrawable((AdaptiveIconDrawable) drawable)
                    .setPath(Preferences.get(mContext).getIconShape())
                    .render();
        }

        return null;
    }

    @Nullable
    private Bitmap getAsset(String uri) {
        try (InputStream stream = mContext.getAssets().open(uri.replaceFirst("assets://", ""))) {
            return BitmapFactory.decodeStream(stream);
        } catch (IOException e) {
            LogUtil.e(Log.getStackTraceString(e));
        }

        return null;
    }

    @Override
    public void cleanup() {
    }

    @Override
    public void cancel() {
    }

    @NonNull
    @Override
    public Class<Bitmap> getDataClass() {
        return Bitmap.class;
    }

    @NonNull
    @Override
    public DataSource getDataSource() {
        // Because transitions do not work with local resources
        return DataSource.REMOTE;
    }
}