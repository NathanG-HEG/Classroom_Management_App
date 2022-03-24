package com.hevs.classroom_management_app.adapter;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.hevs.classroom_management_app.R;
import com.hevs.classroom_management_app.database.pojo.ReservationWithTeacher;
import com.hevs.classroom_management_app.ui.Settings;
import com.hevs.classroom_management_app.util.RecyclerViewItemClickListener;

import java.util.List;

public class RecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private List<T> mData;
    private RecyclerViewItemClickListener mListener;
    private ViewGroup parent;
    private SharedPreferences sharedPreferences;

    public RecyclerAdapter(RecyclerViewItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent = parent;

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(parent.getContext());
        TextView v = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_2, parent, false);
        final RecyclerAdapter.ViewHolder viewHolder = new RecyclerAdapter.ViewHolder(v);
        v.setOnClickListener(view -> mListener.onItemClick(view, viewHolder.getAdapterPosition()));
        v.setOnLongClickListener(view -> {
            mListener.onItemLongClick(view, viewHolder.getAdapterPosition());
            return true;
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, int position) {
        T item = mData.get(position);
        if (item.getClass().equals(ReservationWithTeacher.class)) {
            ReservationWithTeacher reservation = (ReservationWithTeacher) item;
            holder.mTextView.setText(getReservationText(reservation, sharedPreferences.getBoolean(Settings.US_DATE_FORMAT, false)));
        }
    }

    @Override
    public int getItemCount() {
        if (mData != null) {
            return mData.size();
        } else {
            return 0;
        }
    }

    private String getReservationText(ReservationWithTeacher rwt, boolean usDateFormat) {
        StringBuilder sb = new StringBuilder();
        sb.append(rwt.teacher.getFirstname().charAt(0)).
                append(". ").
                append(rwt.teacher.getLastname()).
                append("\t\t").
                append(usDateFormat ? prettyTime(rwt.reservation.getStartTime().getMonthValue()) : prettyTime(rwt.reservation.getStartTime().getDayOfMonth())).
                append("/").
                append(usDateFormat ? prettyTime(rwt.reservation.getStartTime().getDayOfMonth()) : prettyTime(rwt.reservation.getStartTime().getMonthValue())).
                append("/").
                append(rwt.reservation.getStartTime().getYear()).
                append(" ").
                append(prettyTime(rwt.reservation.getStartTime().getHour(), rwt.reservation.getStartTime().getMinute(), usDateFormat)).
                append("-").
                append(prettyTime(rwt.reservation.getEndTime().getHour(), rwt.reservation.getEndTime().getMinute(), usDateFormat));

        return sb.toString();
    }

    private String prettyTime(int hour, int minute, boolean usFormat) {
        if (usFormat) {
            StringBuilder sb = new StringBuilder();
            int hourUs;
            hourUs = hour > 12 ? hour - 12 : hour;
            sb.append(prettyTime(hourUs)).append(":").append(prettyTime(minute));
            if (hour < 12) {
                sb.append("am");
                return sb.toString();
            }
            sb.append("pm");
            return sb.toString();
        }
        return prettyTime(hour) + ":" + prettyTime(minute);
    }

    private String prettyTime(int time) {
        return time < 10 ? "0" + time : Integer.toString(time);
    }

    public void setData(final List<T> data) {
        if (mData == null) {
            mData = data;
            notifyItemRangeInserted(0, data.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mData.size();
                }

                @Override
                public int getNewListSize() {
                    return data.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    if (mData instanceof ReservationWithTeacher) {
                        return ((ReservationWithTeacher) mData.get(oldItemPosition)).equals(
                                ((ReservationWithTeacher) data.get(newItemPosition)));
                    }
                    return false;
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    if (mData instanceof ReservationWithTeacher) {
                        ReservationWithTeacher newClient = (ReservationWithTeacher) data.get(newItemPosition);
                        ReservationWithTeacher oldClient = (ReservationWithTeacher) mData.get(newItemPosition);
                        return newClient.equals(oldClient);
                    }
                    return false;
                }
            });
            mData = data;
            result.dispatchUpdatesTo(this);
        }
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView mTextView;

        ViewHolder(TextView textView) {
            super(textView);
            mTextView = textView;
        }
    }
}
