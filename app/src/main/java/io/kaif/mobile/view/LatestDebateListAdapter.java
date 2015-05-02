package io.kaif.mobile.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import io.kaif.mobile.R;
import io.kaif.mobile.kmark.KmarkProcessor;
import io.kaif.mobile.view.viewmodel.DebateViewModel;
import io.kaif.mobile.view.widget.ClickableSpanTouchListener;

public class LatestDebateListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  static class DebateViewHolder extends RecyclerView.ViewHolder {

    @InjectView(R.id.content)
    public TextView content;
    @InjectView(R.id.last_update_time)
    public TextView lastUpdateTime;
    @InjectView(R.id.vote_score)
    public TextView voteScore;
    @InjectView(R.id.debater_name)
    public TextView debaterName;
    @InjectView(R.id.zone)
    public TextView zone;

    public DebateViewHolder(View itemView) {
      super(itemView);
      ButterKnife.inject(this, itemView);
      content.setOnTouchListener(new ClickableSpanTouchListener());
    }

    public void update(DebateViewModel debateViewModel) {
      final Context context = itemView.getContext();
      debaterName.setText(debateViewModel.getDebaterName());
      content.setText(KmarkProcessor.process(context, debateViewModel.getContent()));
      lastUpdateTime.setText(DateUtils.getRelativeTimeSpanString(debateViewModel.getLastUpdateTime()
          .getTime(), System.currentTimeMillis(), 0, DateUtils.FORMAT_ABBREV_RELATIVE));
      voteScore.setText(String.valueOf(debateViewModel.getVoteScore()));
      zone.setText(itemView.getContext().getString(R.string.zone_path, debateViewModel.getZone()));
    }

  }

  private final List<DebateViewModel> debates;

  public LatestDebateListAdapter() {
    this.debates = new ArrayList<>();
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    final View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
    return new DebateViewHolder(view);
  }

  @Override
  public int getItemViewType(int position) {
    return R.layout.item_debate_latest;
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    DebateViewModel debateVm = debates.get(position);
    DebateViewHolder debateViewHolder = (DebateViewHolder) holder;
    debateViewHolder.update(debateVm);
  }

  @Override
  public int getItemCount() {
    return debates.size();
  }

  public void refresh(List<DebateViewModel> debates) {
    this.debates.clear();
    this.debates.addAll(debates);
    notifyDataSetChanged();
  }

  public void addAll(List<DebateViewModel> debates) {
    this.debates.addAll(debates);
    notifyItemRangeInserted(this.debates.size() - debates.size(), debates.size());
  }

  public String getLastDebateId() {
    return debates.get(debates.size() - 1).getDebateId();
  }

  public DebateViewModel getItem(int position) {
    return debates.get(position);
  }
}