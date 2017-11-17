package cbaexcercise.android.com.cbaexcercise;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class TransactionActivity extends AppCompatActivity implements CustomViewAdapter.ViewClickListener{

    private static final String TAG = "TransactionActivity";
    private CustomViewAdapter mAdapter;
    private AccountInformation mAccountInfo;
    private String AppTitle = "Account details";
    private String jsonFile = "exercise.json";
    private String jsondata;
    private ListView mListView;
    private String DATE_FORMAT = "dd/MM/yyyy";
    private HashMap<String,ArrayList<GroupedTransactions>> mTransactions;

    Map<Date, List<GroupedTransactions>> map = new TreeMap<Date,List<GroupedTransactions>>(Collections.reverseOrder());

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate ");
        setContentView(R.layout.activity_transaction);
        setupActionBarLog();
        mListView = findViewById(R.id.listview);
    }

    @Override
    public void onImageClicked(int position) {
        Log.d(TAG,"Position = "+position);
    }

    private void setupActionBarLog() {
        getSupportActionBar().setTitle(AppTitle);
        getSupportActionBar().setIcon(R.drawable.icon_welcome_logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void onResume() {
        super.onResume();
        Log.d(TAG,"OnResume ");
        if(mAdapter == null) mAdapter = new CustomViewAdapter(this);
        if(mAccountInfo == null) {
            jsondata = extractJSONData();
            Gson gson = new Gson();
            mAccountInfo = (AccountInformation)gson.fromJson(jsondata, AccountInformation.class);
        }
        mAdapter.addAccountInfo(mAccountInfo.getAccount());

        groupTransactionsList();
        publishTransactions();
        mListView.setAdapter(mAdapter);

    }
    private boolean addFirsHeader = false;
    private void publishTransactions() {
        for (Map.Entry<Date, List<GroupedTransactions>> entry : map.entrySet()) {
            Date key = entry.getKey();
            List<GroupedTransactions> values = entry.getValue();
            Log.d(TAG,"publishTransactions size = "+values.size());
            mAdapter.addSectionHeaderItem(values.get(0));
            if(!addFirsHeader)
               mAdapter.addSectionHeaderItem(values.get(0));
            for(int i =0;i<values.size();i++) {
                Log.d(TAG,"publishTransactions = "+values.get(i).getTransacion_desc());
                mAdapter.addItem(values.get(i));
            }
            addFirsHeader = true;
        }
    }
    private void groupTransactionsList() {

        for(int i = 0;i<mAccountInfo.getTransactions().size();i++) {
            List<GroupedTransactions> list = new ArrayList<GroupedTransactions>();
            GroupedTransactions grp = new GroupedTransactions();
            grp.setTransaction_amount(mAccountInfo.getTransactions().get(i).getAmount());
            grp.setTransaction_date(mAccountInfo.getTransactions().get(i).getEffectiveDate());
            grp.setTransaction_desc(mAccountInfo.getTransactions().get(i).getDescription());
            grp.setTransaction_type("SUCCESS");
            list.add(grp);
            if(map.containsKey(formatDate(mAccountInfo.getTransactions().get(i).getEffectiveDate()))) {
                map.get(formatDate(mAccountInfo.getTransactions().get(i).getEffectiveDate())).add(grp);
            } else {
                map.put(formatDate(mAccountInfo.getTransactions().get(i).getEffectiveDate()),list);
            }
        }

        for(int k = 0;k<mAccountInfo.getPending().size();k++) {
            List<GroupedTransactions> list = new ArrayList<GroupedTransactions>();
            GroupedTransactions grp = new GroupedTransactions();
            grp.setTransaction_amount(mAccountInfo.getPending().get(k).getAmount());
            grp.setTransaction_date(mAccountInfo.getPending().get(k).getEffectiveDate());
            grp.setTransaction_desc(mAccountInfo.getPending().get(k).getDescription());
            grp.setTransaction_type("PENDING");
            list.add(grp);
            if(map.containsKey(formatDate(mAccountInfo.getPending().get(k).getEffectiveDate()))) {
                map.get(formatDate(mAccountInfo.getPending().get(k).getEffectiveDate())).add(grp);
            } else {
                map.put(formatDate(mAccountInfo.getPending().get(k).getEffectiveDate()),list);
            }
        }
    }

    private Date formatDate(String date) {
        SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
        Date converteDate = null;
        try {
            converteDate = df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return converteDate;
    }

    public void onPause() {
        super.onPause();
        Log.d(TAG,"OnPause ");
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"OnDestroy ");
        map.clear();
        if(mAdapter != null)
            mAdapter = null;
        if(mAccountInfo != null)
            mAccountInfo = null;
    }
    public String extractJSONData() {
        String jsonData = null;
        Log.d(TAG,"extractJSONData E");
        try {
            InputStream jsonStream = getAssets().open(jsonFile);
            int size = jsonStream.available();
            byte[] buffer = new byte[size];
            jsonStream.read(buffer);
            jsonStream.close();
            jsonData = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        Log.d(TAG,"extractJSONData X");
        return jsonData;
    }
}
