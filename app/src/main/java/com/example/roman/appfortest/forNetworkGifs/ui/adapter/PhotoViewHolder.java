package com.example.roman.appfortest.forNetworkGifs.ui.adapter;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.roman.appfortest.R;
import com.example.roman.appfortest.forNetworkGifs.ux.dto.GifDTO;

//ханит ссылки на view
public class PhotoViewHolder extends RecyclerView.ViewHolder{

    private RequestManager imageLoader;
    private ImageView ivGif;
    private ProgressBar progressBar;
    private static final int LayuotItemGif = R.layout.network_gif_recycler_item_gif;

    public PhotoViewHolder(@NonNull View itemView, RequestManager glideRequestManager ) {
        super(itemView);
        this.imageLoader = glideRequestManager;
        findViews(itemView);
    }

    public static PhotoViewHolder create(@NonNull ViewGroup parent, RequestManager glaRequestManager) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(LayuotItemGif, parent, false);
        return new PhotoViewHolder(view, glaRequestManager);
    }

    private void findViews (@NonNull View itemView) {
        ivGif = itemView.findViewById(R.id.iv_gif);
        progressBar = itemView.findViewById(R.id.progress_bar);
    }

    public void bindItem (@NonNull GifDTO gif) {

        progressBar.setVisibility(View.VISIBLE);

        imageLoader.load(gif.getUrl())
                   .listener(new RequestListener<Drawable>() {
                       @Override
                       public boolean onLoadFailed(@Nullable GlideException e,
                                                   Object model,
                                                   Target<Drawable> target,
                                                   boolean isFirstResource) {
                           progressBar.setVisibility(View.GONE);
                           return false;
                       }

                       @Override
                       public boolean onResourceReady(Drawable resource,
                                                      Object model,
                                                      Target<Drawable> target,
                                                      DataSource dataSource,
                                                      boolean isFirstResource) {
                           progressBar.setVisibility(View.GONE);
                           return false;
                       }
                   })
                   .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                   .thumbnail(0.3f)
                   .into(ivGif);
    }

}
