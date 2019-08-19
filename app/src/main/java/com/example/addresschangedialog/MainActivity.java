package com.example.addresschangedialog;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.addresschangedialog.bean.ListAddressBean;

public class MainActivity extends AppCompatActivity implements  AddressFragment.ItemClickCallBack{

    private AddressPresenter addressPresenter;
    private AddressFragment fragment = AddressFragment.newInstance();
    private AddressFragment addressFragment;


    protected AddressContract.Presenter getPresenter() {
        if (null == addressPresenter) {
            addressPresenter = new AddressPresenter();
        }
        return addressPresenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        addFragment();
    }

    private void addFragment() {
        FragmentManager fm = getSupportFragmentManager();

        FragmentTransaction ft = fm.beginTransaction();

        fm.executePendingTransactions();
        addressFragment = AddressFragment.newInstance();
        ft.add(R.id.add_fg, addressFragment);

        ft.commit();
    }


    @Override
    public void returnAddress(ListAddressBean addressInfo) {
        Intent intent = getIntent();
        intent.putExtra("address", addressInfo);
        this.setResult(10, intent);
        this.finish();
    }
}
