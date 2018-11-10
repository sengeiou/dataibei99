package com.likeit.currenciesapp.ui.home;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.api.AppConfig;
import com.likeit.currenciesapp.app.MyApplication;
import com.likeit.currenciesapp.model.TransferAccountInfoEntity;
import com.likeit.currenciesapp.ui.Greenteahy.TransferDialog02Fragment;
import com.likeit.currenciesapp.ui.MainActivity;
import com.likeit.currenciesapp.ui.chat.server.widget.LoadDialog;
import com.likeit.currenciesapp.utils.HttpUtil;
import com.likeit.currenciesapp.utils.MyActivityManager;
import com.likeit.currenciesapp.utils.StringUtil;
import com.likeit.currenciesapp.utils.ToastUtil;
import com.likeit.currenciesapp.utils.UtilPreference;
import com.likeit.currenciesapp.views.PswInputView;
import com.likeit.currenciesapp.views.RoundImageView;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class PayFragment extends DialogFragment implements View.OnClickListener {


    private String money;
    private RoundImageView iv_avatar;
    private ImageView iv_close;
    private TextView tv_name;
    private TextView tv_money;
    private PswInputView psw_input;
    private String avatar;
    private String truename;
    private String account;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCanceledOnTouchOutside(false);
        View view = inflater.inflate(R.layout.fragment_pay, container, false);
        Bundle bundle = getArguments();
      //  transferAccountInfoEntity = (TransferAccountInfoEntity) bundle.getSerializable("transferAccountInfoEntity");


        avatar = bundle.getString("avatar");
        truename = bundle.getString("truename");
        account = bundle.getString("account");
        money = bundle.getString("money");
        initView(view);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        // showKeyboard();
        return view;
    }

    private void initView(final View view) {
        iv_avatar = (RoundImageView) view.findViewById(R.id.iv_avatar);
        iv_close = (ImageView) view.findViewById(R.id.iv_close);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_money = (TextView) view.findViewById(R.id.tv_money);
        psw_input = (PswInputView) view.findViewById(R.id.psw_input);
        psw_input.requestFocus();
        iv_close.setOnClickListener(this);
        String url = avatar;
        if (StringUtil.isBlank(url)) {
            iv_avatar.setBackground(this.getResources().getDrawable(R.mipmap.icon_avatar));
        } else {
            String portraitUri = AppConfig.LIKEIT_LOGO1 + url;
            io.rong.imageloader.core.ImageLoader.getInstance().displayImage(portraitUri, iv_avatar, MyApplication.getOptions());
        }
        tv_name.setText("向" + truename + "付款");
        tv_money.setText("¥ " + money);
        psw_input.setInputCallBack(new PswInputView.InputCallBack() {
            @Override
            public void onInputFinish(final String result) {
                psw_input.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // view.clearResult();
                        check_paypwd(result);
                        LoadDialog.show(getActivity());
                    }
                }, 500);

            }
        });
    }


    private void showKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(psw_input, 0);
    }

    private void check_paypwd(final String result) {
        String url = AppConfig.LIKEIT_CHECK_PAYPWD;
        RequestParams params = new RequestParams();
        params.put("ukey", UtilPreference.getStringValue(getActivity(), "ukey"));
        params.put("paypwd", result);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.optString("status");
                    if ("true".equals(status)) {
                        refer(result);
                    } else {
                        LoadDialog.dismiss(getActivity());
                        psw_input.clearResult();
                        ToastUtil.showS(getActivity(), obj.optString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable e) {

            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

//    private void refer2(String result) {
//        String url = AppConfig.LIKEIT_TRANSFER_USER_DIAN;
//        RequestParams params = new RequestParams();
//        params.put("ukey", UtilPreference.getStringValue(getActivity(), "ukey"));
//        params.put("transfer_rongcloudid", targetId);
//        params.put("dian", money);
//        params.put("paypwd", result);
//        params.put("shiming", name);
//        params.put("remark", "");
//        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
//            @Override
//            public void success(String response) {
//                LoadDialog.dismiss(getActivity());
//                try {
//                    JSONObject obj = new JSONObject(response);
//                    String status = obj.optString("status");
//                    if ("true".equals(status)) {
//                        ToastUtil.showS(getActivity(), "付款成功");
//                        //getActivity().onBackPressed();
//                        //MyActivityManager.getInstance().backToMain();
//                        Log.e("TAG11", "money-->" + money);
//                        ToastUtil.showS(getActivity(), "发送成功");
//                        Intent intent = getActivity().getIntent();
//                        intent.putExtra("message", "转账给" + account);
//                        intent.putExtra("money", money);
//                        intent.putExtra("userid", targetId);
//                        intent.putExtra("extra", "");
//                        getActivity().setResult(100, intent);
//                        getActivity().finish();
//                    } else {
//                        ToastUtil.showS(getActivity(), obj.optString("msg"));
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void failed(Throwable e) {
//
//            }
//
//            @Override
//            public void onFinish() {
//                super.onFinish();
//                LoadDialog.dismiss(getActivity());
//            }
//        });
//    }

    private void refer(String result) {
        String url = AppConfig.LIKEIT_TRANSFER_USER_DIAN;
        RequestParams params = new RequestParams();
        params.put("ukey", UtilPreference.getStringValue(getActivity(), "ukey"));
        params.put("transfer_user", account);
        params.put("dian", money);
        params.put("paypwd", result);
        params.put("shiming", truename);
        params.put("remark", "");
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                LoadDialog.dismiss(getActivity());
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.optString("status");
                    if ("true".equals(status)) {
                        ToastUtil.showS(getActivity(), "付款成功");
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.putExtra("page", 1);
                        startActivity(intent);
                        //getActivity().onBackPressed();
                        MyActivityManager.getInstance().backToMain();
                    } else {
                        ToastUtil.showS(getActivity(), obj.optString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable e) {

            }

            @Override
            public void onFinish() {
                super.onFinish();
                LoadDialog.dismiss(getActivity());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                getDialog().dismiss();
                break;
        }
    }


}
