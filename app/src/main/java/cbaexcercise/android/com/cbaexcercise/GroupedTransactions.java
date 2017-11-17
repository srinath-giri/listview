package cbaexcercise.android.com.cbaexcercise;


public class GroupedTransactions {

    private String mTransaction_date;
    private String mTransaction_type;
    private String mTransaction_amount;
    private String mTransacion_desc;
    private int mNumberofDays;
    public GroupedTransactions(String date, String type, String amount, int num) {
        mTransaction_date = date;
        mTransaction_type = type;
        mTransaction_amount = amount;
        mNumberofDays = num;
    }

    public GroupedTransactions() {

    }

    public String getTransaction_date() {
        return mTransaction_date;
    }

    public String getTransaction_type() {
        return mTransaction_type;
    }

    public String getTransaction_amount() {
        return mTransaction_amount;
    }

    public String getTransacion_desc() {
        return mTransacion_desc;
    }

    public int getTransaction_days() {
        return mNumberofDays;
    }

    public void setTransaction_date(String date) {
        mTransaction_date = date;
    }

    public void setTransaction_type(String type) {
        mTransaction_type = type;
    }

    public void setTransaction_amount(String amount) {
        mTransaction_amount = amount;
    }

    public void setTransaction_desc(String desc) {
        mTransacion_desc = desc;
    }

    public void setTransaction_days(int days) {
        mNumberofDays = days;
    }


}
