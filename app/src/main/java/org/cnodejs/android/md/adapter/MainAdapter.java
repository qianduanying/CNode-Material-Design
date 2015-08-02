package org.cnodejs.android.md.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.activity.MainActivity;
import org.cnodejs.android.md.activity.TopicActivity;
import org.cnodejs.android.md.model.api.ApiClient;
import org.cnodejs.android.md.model.entity.Topic;
import org.cnodejs.android.md.util.FormatUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_LOAD_MORE = 1;

    private MainActivity activity;
    private LayoutInflater inflater;
    private List<Topic> topicList;

    private boolean loading = false;

    public MainAdapter(MainActivity activity, List<Topic> topicList) {
        this.activity = activity;
        inflater = LayoutInflater.from(activity);
        this.topicList = topicList;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    public boolean canLoadMore() {
        return topicList.size() >= 20 && !loading;
    }

    @Override
    public int getItemCount() {
        return topicList.size() >= 20 ? topicList.size() + 1 : topicList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (topicList.size() >= 20 && position == getItemCount() - 1) {
            return TYPE_LOAD_MORE;
        } else {
            return TYPE_NORMAL;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_LOAD_MORE:
                return new LoadMoreViewHolder(inflater.inflate(R.layout.activity_item_load_more, parent, false));
            default: // TYPE_NORMAL
                return new NormalViewHolder(inflater.inflate(R.layout.activity_main_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.update(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public void update(int position) {}

    }

    public class LoadMoreViewHolder extends ViewHolder {

        @Bind(R.id.item_load_more_icon_loading)
        protected View iconLoading;

        @Bind(R.id.item_load_more_icon_finish)
        protected View iconFinish;

        public LoadMoreViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void update(int position) {
            iconLoading.setVisibility(loading ? View.VISIBLE : View.GONE);
            iconFinish.setVisibility(loading ? View.GONE : View.VISIBLE);
        }

    }

    public class NormalViewHolder extends ViewHolder {

        @Bind(R.id.main_item_tv_tab)
        protected TextView tvTab;

        @Bind(R.id.main_item_tv_title)
        protected TextView tvTitle;

        @Bind(R.id.main_item_img_avatar)
        protected ImageView imgAvatar;

        @Bind(R.id.main_item_tv_author)
        protected TextView tvAuthor;

        @Bind(R.id.main_item_tv_create_time)
        protected TextView tvCreateTime;

        @Bind(R.id.main_item_tv_reply_count)
        protected TextView tvReplyCount;

        @Bind(R.id.main_item_tv_visit_count)
        protected TextView tvVisitCount;

        @Bind(R.id.main_item_tv_last_reply_time)
        protected TextView tvLastReplyTime;

        @Bind(R.id.main_item_icon_good)
        protected View iconGood;

        protected Topic topic;

        public NormalViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void update(int position) {
            topic = topicList.get(position);

            tvTitle.setText(topic.getTitle());
            tvTab.setText(topic.isTop() ? "置顶" : activity.getString(topic.getTab().getNameId()));
            tvTab.setBackgroundResource(topic.isTop() ? R.drawable.topic_tab_top_background : R.drawable.topic_tab_normal_background);
            tvTab.setTextColor(activity.getResources().getColor(topic.isTop() ? android.R.color.white : R.color.text_color_secondary));
            Picasso.with(activity).load(ApiClient.ROOT_HOST + topic.getAuthor().getAvatarUrl()).error(R.drawable.image_default).into(imgAvatar);
            tvAuthor.setText(topic.getAuthor().getLoginName());
            tvCreateTime.setText("创建于：" + topic.getCreateAt().toString("yyyy-MM-dd HH:mm:ss"));
            tvReplyCount.setText(String.valueOf(topic.getReplyCount()));
            tvVisitCount.setText(String.valueOf(topic.getVisitCount()));
            tvLastReplyTime.setText(FormatUtils.getRecentlyTimeFormatText(topic.getLastReplyAt()));
            iconGood.setVisibility(topic.isGood() ? View.VISIBLE : View.GONE);
        }

        @OnClick(R.id.main_item_btn_item)
        protected void onBtnItemClick() {
            Intent intent = new Intent(activity, TopicActivity.class);
            intent.putExtra("topicId", topic.getId());
            activity.startActivity(intent);
        }

    }

}