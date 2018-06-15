package com.hz.junxinbaoan.activity.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.hz.junxinbaoan.R;
import com.hz.junxinbaoan.adapter.ApprovalTypeAdapter;

import java.util.List;

/**
 * Created by linzp on 2017/10/26.
 */

public class ApprovalTypeDialog extends BaseDialog {
    GetString getString;
    TextView title;

    ListView type;
    private Context context;
    private List<String> list;
    private ApprovalTypeAdapter adapter;

    public ApprovalTypeDialog(Context context, List<String> phone,GetString getString) {
        super(context, R.style.dim_dialog);
        this.context = context;
        this.list = phone;
        this.getString=getString;
        adapter=new ApprovalTypeAdapter(list,context);
        type.setAdapter(adapter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_approval;
    }

    @Override
    protected void findViews() {
        title = (TextView) findViewById(R.id.title);
        type = (ListView) findViewById(R.id.type);
        addListeners();
    }

    private void addListeners() {
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getString.getString(list.get(position));
                dismiss();
            }
        });

    }

    @Override
    protected void setWindowParam() {
        setWindowParams(-1, -2, Gravity.TOP, 0);
    }

    public void setList(List<String> listType) {
        list=listType;
        adapter.notifyDataSetChanged();
    }

    public interface GetString{
        void getString(String s);
    }
}
