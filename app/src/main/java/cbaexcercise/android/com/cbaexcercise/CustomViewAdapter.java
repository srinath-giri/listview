package cbaexcercise.android.com.cbaexcercise;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

class CustomViewAdapter extends BaseAdapter {

    private static final int TYPE_TRANSACTION = 0;
    private static final int TYPE_DIVIDER = 1;
    private static final int TYPE_ACCOUNTINFO = 2;
    private static final String TAG = "CustomViewAdapter";

    private List<GroupedTransactions> mDataGrp = new ArrayList<GroupedTransactions>();
    private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();
    private Account mAccountinfo;

    private LayoutInflater mInflater;
    private final int PaddingLeft = 30;
    private final int PaddingRight = 30;
    private final int PaddingTop = 50;
    private final int PaddingBottom = 50;
    private final String mCurrencysymbol = "$";

    public CustomViewAdapter(Context context) {
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItem(GroupedTransactions item) {

        mDataGrp.add(item);
    }

    public void addAccountInfo(Account info) {
        mAccountinfo = info;
    }

    public void addSectionHeaderItem(GroupedTransactions item) {
        mDataGrp.add(item);
        sectionHeader.add(mDataGrp.size()-1);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0)
            return TYPE_ACCOUNTINFO;
        else
            return sectionHeader.contains(position) ? TYPE_DIVIDER : TYPE_TRANSACTION;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getCount() {
        return mDataGrp.size();
    }

    @Override
    public String getItem(int position) {
        return mDataGrp.get(position).getTransaction_date();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        int rowType = getItemViewType(position);
        switch (rowType) {
            case TYPE_ACCOUNTINFO:
                AccountInfoViewHolder account_info = null;
                if (convertView == null) {
                    account_info = new AccountInfoViewHolder();
                    convertView = mInflater.inflate(R.layout.account_info, null);
                    account_info.accountName = convertView.findViewById(R.id.account_name);
                    account_info.accountNumber = convertView.findViewById(R.id.account_number);
                    account_info.available = convertView.findViewById(R.id.available_fundvalue);
                    account_info.balance = convertView.findViewById(R.id.available_balancevalue);
                    convertView.setPadding(PaddingLeft, PaddingTop, PaddingRight, PaddingBottom);
                    convertView.setTag(account_info);
                } else {
                   account_info = (AccountInfoViewHolder)convertView.getTag();
                }
                account_info.accountName.setText(mAccountinfo.getAccountName());
                account_info.accountNumber.setText(String.valueOf(mAccountinfo.getAccountNumber()));
                account_info.available.setText(mCurrencysymbol+String.valueOf(mAccountinfo.getAvailable()));
                account_info.balance.setText(mCurrencysymbol+String.valueOf(mAccountinfo.getBalance()));
                return convertView;
            case TYPE_TRANSACTION:
                ItemViewHolder holder_trans = null;
                if (convertView == null) {
                    holder_trans = new ItemViewHolder();
                    convertView = mInflater.inflate(R.layout.list_item, null);
                    holder_trans.textViewDes = convertView.findViewById(R.id.description);
                    holder_trans.textViewamount = convertView.findViewById(R.id.amount);
                    convertView.setTag(holder_trans);
                } else {
                    holder_trans = (ItemViewHolder) convertView.getTag();
                }
                if(mDataGrp.get(position).getTransaction_type().equals("PENDING")) {
                    String temp = "PENDING: "+mDataGrp.get(position).getTransacion_desc();
                    final SpannableStringBuilder str = new SpannableStringBuilder(temp);
                    str.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 0,9,0);
                    holder_trans.textViewDes.setText(temp);
                } else {
                    holder_trans.textViewDes.setText(mDataGrp.get(position).getTransacion_desc());
                    holder_trans.textViewamount.setText(mCurrencysymbol + mDataGrp.get(position).getTransaction_amount());
                }
                return convertView;
            case TYPE_DIVIDER:
                DividerViewHolder holder_divider = null;
                if (convertView == null) {
                    holder_divider = new DividerViewHolder();
                    convertView = mInflater.inflate(R.layout.divider, null);
                    holder_divider.textViewDate = convertView.findViewById(R.id.date);
                    holder_divider.textViewDays = convertView.findViewById(R.id.numofdays);
                    convertView.setTag(holder_divider);
                }
                else {
                    holder_divider = (DividerViewHolder) convertView.getTag();
                }
                holder_divider.textViewDate.setText(mDataGrp.get(position).getTransaction_date());
                //holder.textViewDays.setText(mData.get(position));
                return convertView;
                default:
                    Log.e(TAG,"Not a valid type");
                    break;
            }
            return null;
    }

    public static class DividerViewHolder {
        public TextView textViewDate;
        public TextView textViewDays;
    }

    public static class ItemViewHolder {
        public TextView textViewDes;
        public TextView textViewamount;
    }

    public static class AccountInfoViewHolder {
        public TextView accountName;
        public TextView accountNumber;
        public TextView available;
        public TextView balance;
    }


    private ViewClickListener mViewClickListener;

    public interface ViewClickListener {
        void onImageClicked(int position);
    }

    public void setViewClickListener (ViewClickListener viewClickListener) {
        mViewClickListener = viewClickListener;
    }

}