package com.likeit.currenciesapp.ui.chat.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.ui.chat.ui.adapter.SubConversationListAdapterEx;

import io.rong.imkit.RongContext;
import io.rong.imkit.fragment.SubConversationListFragment;

public class SubConversationListActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sub_conversation_list);
        SubConversationListFragment fragment = new SubConversationListFragment();
        fragment.setAdapter(new SubConversationListAdapterEx(RongContext.getInstance()));
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.rong_content, fragment);
        transaction.commit();

        Intent intent = getIntent();
        if (intent.getData() == null) {
            return;
        }
        //聚合会话参数
        String type = intent.getData().getQueryParameter("type");

        if (type == null)
            return;

        if (type.equals("group")) {
            setTitle(R.string.de_actionbar_sub_group);
        } else if (type.equals("private")) {
            setTitle(R.string.de_actionbar_sub_private);
        } else if (type.equals("discussion")) {
            setTitle(R.string.de_actionbar_sub_discussion);
        } else if (type.equals("system")) {
            setTitle(R.string.de_actionbar_sub_system);
        } else {
            setTitle(R.string.de_actionbar_sub_defult);
        }
    }


}
