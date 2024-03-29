package uizacoresdk.view.dlg.listentityrelation;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import com.uizacoresdk.R;
import uizacoresdk.util.UZData;
import vn.uiza.core.utilities.LUIUtil;
import vn.uiza.restapi.uiza.model.v2.listallentity.Item;
import vn.uiza.restapi.uiza.model.v2.listallentityrelation.ListAllEntityRelation;

/**
 * Created by loitp on 5/2/2018.
 */

public class UZDligListEntityRelation extends Dialog {
    private final String TAG = getClass().getSimpleName();
    private Activity activity;
    private AlertDialog dialog;
    private boolean isLandscape;
    //private Gson gson = new Gson();

    private ProgressBar progressBar;
    private TextView tvMsg;
    private List<Item> itemList;
    private RecyclerView recyclerView;
    private AdapterPlayList adapterPlayList;

    private CallbackPlayList callbackPlayList;

    public UZDligListEntityRelation(Activity activity, boolean isLandscape, CallbackPlayList callbackPlayList) {
        super(activity);
        this.activity = activity;
        this.isLandscape = isLandscape;
        this.callbackPlayList = callbackPlayList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.v3_dialog_list_entity_relation);

        progressBar = findViewById(R.id.pb);
        LUIUtil.setColorProgressBar(progressBar, ContextCompat.getColor(activity, R.color.colorPrimary));

        tvMsg = findViewById(R.id.tv_msg);
        recyclerView = findViewById(R.id.recycler_view);

        findViewById(R.id.bt_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        getListAllEntityRelation();
    }

    private void getListAllEntityRelation() {
        //TODO iplm
        LUIUtil.showProgressBar(progressBar);

        //TODO remove hardcode
        LUIUtil.setDelay(700, new LUIUtil.DelayCallback() {
            @Override
            public void doAfter(int mls) {
                setupUI(null);
            }
        });
    }

    private void setupUI(ListAllEntityRelation listAllEntityRelation) {
        if (listAllEntityRelation == null || listAllEntityRelation.getItemList() == null || listAllEntityRelation.getItemList().isEmpty()) {
            tvMsg.setVisibility(View.VISIBLE);
        } else {
            itemList = listAllEntityRelation.getItemList();
            if (itemList == null || itemList.isEmpty()) {
                tvMsg.setVisibility(View.VISIBLE);
                return;
            } else {
                tvMsg.setVisibility(View.GONE);
            }
            recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
            adapterPlayList = new AdapterPlayList(activity, itemList, new CallbackPlayList() {
                @Override
                public void onClickItem(Item item, int position) {
                    if (UZData.getInstance().isSettingPlayer()) {
                        return;
                    }
                    dismiss();
                    if (callbackPlayList != null) {
                        callbackPlayList.onClickItem(item, position);
                    }
                }

                @Override
                public void onDismiss() {
                    if (callbackPlayList != null) {
                        callbackPlayList.onDismiss();
                    }
                }
            });
            recyclerView.setAdapter(adapterPlayList);
        }
        LUIUtil.hideProgressBar(progressBar);
    }
}