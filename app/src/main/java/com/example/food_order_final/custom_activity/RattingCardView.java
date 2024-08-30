package com.example.food_order_final.custom_activity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.food_order_final.R;
import com.example.food_order_final.util.LoadImageUtil;

public class RattingCardView extends LinearLayout {

    private TextView tvUsername, tvComment, tvTime;
    private ImageView ivUserAvatar, ivCommentImage;
    private LinearLayout rattingStarContainer, commentImageContainer;
    private RatingBar ratingBar;

    private int rattingId;

    public RattingCardView(Context context) {
        super(context);
        init(context);
    }

    public RattingCardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RattingCardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public RattingCardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void init(Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.ratting_card, this, true);

        tvUsername = view.findViewById(R.id.tvUsername);
        tvComment = view.findViewById(R.id.tvComment);
        tvTime = view.findViewById(R.id.tvTime);
        ivUserAvatar = view.findViewById(R.id.ivUserAvatar);
        ivCommentImage = view.findViewById(R.id.ivCommentImage);
        rattingStarContainer = view.findViewById(R.id.rattingStarContainer);
        commentImageContainer = view.findViewById(R.id.commentImageContainer);
        ratingBar = view.findViewById(R.id.ratingBar);
    }

    public RatingBar getRatingBar() {
        return ratingBar;
    }

    public void setRatingBar(RatingBar ratingBar) {
        this.ratingBar = ratingBar;
    }

    public void setTvUsername(String tvUsername) {
        this.tvUsername.setText(tvUsername);
    }

    public void setTvComment(String tvComment) {
        this.tvComment.setText(tvComment);
    }

    public void setTvTime(String tvTime) {
        this.tvTime.setText(tvTime);
    }

    public void setIvUserAvatar(String avatar) {
        LoadImageUtil.loadImage(ivUserAvatar, avatar);
    }

    private void setRattingId(int rattingId) {
        this.rattingId = rattingId;
    }

    public TextView getTvUsername() {
        return tvUsername;
    }

    public TextView getTvComment() {
        return tvComment;
    }

    public void setTvComment(TextView tvComment) {
        this.tvComment = tvComment;
    }

    public TextView getTvTime() {
        return tvTime;
    }

    public void setTvTime(TextView tvTime) {
        this.tvTime = tvTime;
    }

    public ImageView getIvUserAvatar() {
        return ivUserAvatar;
    }

    public void setIvUserAvatar(ImageView ivUserAvatar) {
        this.ivUserAvatar = ivUserAvatar;
    }

    public ImageView getIvCommentImage() {
        return ivCommentImage;
    }

    public void setIvCommentImage(String image) {
        LoadImageUtil.loadImage(ivCommentImage, image);
    }

    public LinearLayout getRattingStarContainer() {
        return rattingStarContainer;
    }

    public void setRattingStarContainer(LinearLayout rattingStarContainer) {
        this.rattingStarContainer = rattingStarContainer;
    }

    public LinearLayout getCommentImageContainer() {
        return commentImageContainer;
    }

    public void setCommentImageContainer(LinearLayout commentImageContainer) {
        this.commentImageContainer = commentImageContainer;
    }

    public int getRattingId() {
        return rattingId;
    }
}
