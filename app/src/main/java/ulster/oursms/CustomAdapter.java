package ulster.oursms;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by John
 */

public class CustomAdapter extends BaseAdapter{

    private Activity mActivity;
    private ArrayList<Attendance> mList;
    private static LayoutInflater mInflater = null;
    private Attendance tempValues = null;
    private int i = 0;

    public CustomAdapter(Activity a, ArrayList d){
        mActivity=a;
        mList=d;

    }

    @Override
    public int getCount() {
        if(mList.size() <=0)
            return 1;
            return mList.size();

    }//cojnt

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder{
        public TextView txtTagID;
        public TextView txtStudentID;
        public TextView txtClassroomID;
        public TextView txtTimestamp;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;
        if(convertView==null)
        {
            vi=mInflater.inflate(R.layout.activity_attend, null);
            holder = new ViewHolder();
            holder.txtTagID = (TextView) vi.findViewById(R.id.tvTagID);
            holder.txtStudentID = (TextView) vi.findViewById(R.id.tvStudentID);
            holder.txtClassroomID = (TextView) vi.findViewById(R.id.tvClassroomID);
            holder.txtTimestamp = (TextView) vi.findViewById(R.id.tvTimestamp);
            vi.setTag(holder);
        }else
        {
            holder = (ViewHolder) vi.getTag();
            try
            {
                if(mList.size()<=0){
                    holder.txtTagID.setText("No Data");
                    holder.txtStudentID.setText("No Data");
                    holder.txtClassroomID.setText("No Data");
                    holder.txtTimestamp.setText("No Data");

                }else{
                    tempValues=null;
                    tempValues=(Attendance) mList.get(position);
                    holder.txtTagID.setText(tempValues.getTagID());
                    holder.txtStudentID.setText(tempValues.getStudentID());
                    holder.txtClassroomID.setText(tempValues.getClassroomID());
                    holder.txtTimestamp.setText(tempValues.getTimestamp());

                }//end of else
            }catch(Exception ex){
                ex.printStackTrace();
            }//end of try
        }//end if elseif
        return vi;
    }//end of getView
}//end of class
