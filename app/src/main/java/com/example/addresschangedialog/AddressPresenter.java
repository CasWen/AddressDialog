package com.example.addresschangedialog;


import android.content.Context;
import android.os.Bundle;

@SuppressWarnings("AlibabaClassMustHaveAuthor")
public class AddressPresenter implements AddressContract.Presenter{

    private AddressFragment fragment;

    private Context mContext;


    public AddressPresenter(){
    }
    public AddressContract.View getView() {
        if (null==fragment){
            fragment=new AddressFragment();

        }
        return fragment;
    }



}
