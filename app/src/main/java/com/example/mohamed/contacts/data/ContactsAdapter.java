package com.example.mohamed.contacts.data;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mohamed.contacts.R;


public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    private Cursor mCursor;

    public void setmCursor(Cursor mCursor) {
        this.mCursor = mCursor;
        notifyDataSetChanged();
    }

    @Override
    public ContactsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_contact, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ContactsAdapter.ViewHolder holder, int position) {

        if (!mCursor.moveToPosition(position)) return;

        holder.name.setText(mCursor.getString
                (mCursor.getColumnIndexOrThrow(ContactsContract.PersonEntry.COLUMN_NAME)));

        holder.phoneNumber.setText(mCursor.getString
                (mCursor.getColumnIndexOrThrow(ContactsContract.PersonEntry.COLUMN_PHONE_NUMBER)));

        int id = mCursor.getInt((mCursor.getColumnIndexOrThrow(ContactsContract.PersonEntry._ID)));
        holder.itemView.setTag(id);
    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        } else {
            return  mCursor.getCount();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name;
        TextView phoneNumber;

        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);
            phoneNumber = itemView.findViewById(R.id.tv_phone_number);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mCursor.moveToPosition(position);
            int id = mCursor.getInt((mCursor.getColumnIndexOrThrow(ContactsContract.PersonEntry._ID)));

            mOnItemClickedListner.onClick(id);
        }
    }

    private OnItemClickedListner mOnItemClickedListner;

    public interface OnItemClickedListner {
        void onClick(int id);
    }

    public ContactsAdapter(OnItemClickedListner mOnItemClickedListner) {
        this.mOnItemClickedListner = mOnItemClickedListner;
    }
}
