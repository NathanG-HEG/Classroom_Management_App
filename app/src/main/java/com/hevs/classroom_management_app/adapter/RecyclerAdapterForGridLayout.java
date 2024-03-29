package com.hevs.classroom_management_app.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hevs.classroom_management_app.R;
import com.hevs.classroom_management_app.database.entity.Classroom;
import com.hevs.classroom_management_app.util.RecyclerViewItemClickListener;

import java.util.List;

public class RecyclerAdapterForGridLayout<T> extends RecyclerView.Adapter<RecyclerAdapterForGridLayout.ViewHolder> {
    private List<T> mData;
    private RecyclerViewItemClickListener mListener;
    private ViewGroup parent;

    public RecyclerAdapterForGridLayout(RecyclerViewItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public RecyclerAdapterForGridLayout.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent = parent;

        TextView v = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view, parent, false);
        GridLayoutManager.LayoutParams lp = (GridLayoutManager.LayoutParams) v.getLayoutParams();
        lp.height = parent.getMeasuredHeight() / 5;
        v.setLayoutParams(lp);
        final ViewHolder viewHolder = new ViewHolder(v);
        v.setOnClickListener(view -> mListener.onItemClick(view, viewHolder.getAdapterPosition()));
        v.setOnLongClickListener(view -> {
            mListener.onItemLongClick(view, viewHolder.getAdapterPosition());
            return true;
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerAdapterForGridLayout.ViewHolder holder, int position) {
        T item = mData.get(position);
        if (item.getClass().equals(Classroom.class))
            holder.mTextView.setText(((Classroom) item).getName());
    }

    @Override
    public int getItemCount() {
        if (mData != null) {
            return mData.size();
        } else {
            return 0;
        }
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
                    if (mData instanceof Classroom) {
                        return ((Classroom) mData.get(oldItemPosition)).equals(((Classroom) data.get(newItemPosition)));
                    }
                    return false;
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    if (mData instanceof Classroom) {
                        Classroom newAccount = (Classroom) data.get(newItemPosition);
                        Classroom oldAccount = (Classroom) mData.get(newItemPosition);
                        return newAccount.equals(oldAccount);
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
