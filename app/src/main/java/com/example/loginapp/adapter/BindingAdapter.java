package com.example.loginapp.adapter;

import android.annotation.SuppressLint;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.StringRes;

import com.bumptech.glide.Glide;
import com.example.loginapp.R;
import com.example.loginapp.model.entity.OrderStatus;
import com.example.loginapp.view.commonUI.OrderStatusConverter;

import java.text.SimpleDateFormat;
import java.util.Locale;

public final class BindingAdapter {

    @androidx.databinding.BindingAdapter("imgRes")
    public static void loadImage(ImageView view, int res) {
        view.setImageResource(res);
    }

    @androidx.databinding.BindingAdapter("imgUrl")
    public static void loadImageUrl(ImageView view, String imgUrl) {
        if (imgUrl != null && !imgUrl.isEmpty()) {
            Glide.with(view.getContext())
                .load(imgUrl)
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_broken_image)
                .into(view);
        } else {
            view.setImageResource(R.drawable.ic_user);
        }
    }

    @androidx.databinding.BindingAdapter("bindTextView")
    public static void bindTextView(TextView textView, @StringRes int res) {
        textView.setText(res);
    }

    @androidx.databinding.BindingAdapter("bindRatingRounded")
    public static void bindRatingRounded(TextView view, double value) {
        String formattedValue = String.format(Locale.getDefault(), "%.1f", value);
        view.setText(formattedValue);
    }

    @androidx.databinding.BindingAdapter("timeFormat")
    public static void timeFormat(TextView textView, Long time) {
        textView.setText(new SimpleDateFormat("dd-MM-yyyy h:mm a", Locale.getDefault()).format(time));
    }

    @SuppressLint("StringFormatMatches")
    @androidx.databinding.BindingAdapter("layoutVoucherDateFormat")
    public static void layoutVoucherDateFormat(TextView textView, Long time) {
        String timeFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(time);
        textView.setText(String.format(textView.getContext().getString(R.string.valid_till, timeFormat)));
    }

    @androidx.databinding.BindingAdapter("bindOrderStatusRes")
    public static void bindOrderStatusRes(TextView view, OrderStatus orderStatus) {
        view.setText(OrderStatusConverter.convertToStringRes(orderStatus));
    }

    @androidx.databinding.BindingAdapter("bindFormattedOrderStatus")
    public static void bindFormattedOrderStatus(TextView textView, OrderStatus orderStatus) {
        String text = textView.getContext().getString(
                R.string.order_status,
                textView.getContext()
                        .getString(OrderStatusConverter.convertToStringRes(orderStatus))
                        .toLowerCase()
        );
        textView.setText(text);
    }
}