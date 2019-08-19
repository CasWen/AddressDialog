package com.example.addresschangedialog;

import android.app.Activity;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.text.TextUtils;

import com.example.addresschangedialog.bean.ListAddressBean;

import java.util.logging.Logger;


@SuppressWarnings("AlibabaClassMustHaveAuthor")
public class AddressViewModule extends BaseObservable implements ChangeAddressDialog.OnAddressCListener {

    public Context context;
    private AddressFragment mAddressFragment;
    private String addressDetails;
    public ListAddressBean userInfo = new ListAddressBean();
    public AddressViewModule(Context context, AddressFragment mAddressFragment) {
        this.context = context;
        this.mAddressFragment = mAddressFragment;
    }

    public void setData(ListAddressBean addAddressBean) {

        setAddressDetails(addAddressBean.getDetailAddress());
        notifyChange();

    }
    /**
     * 点击地址
     */
    public void onClickAddress() {
        if (context != null) {
            ChangeAddressDialog addressDialog = ChangeAddressDialog.newInstance();
            addressDialog.setAddresskListener(this);
            addressDialog.show(context);
        }
    }

    /**
     * 详细地址
     */
    public void setAddressDetails(String string) {

        this.addressDetails = string;
    }

    @Bindable
    public String getAddressDetails() {

        ListAddressBean data = userInfo;
        if (null != data && !TextUtils.isEmpty(data.getProvinceName()) && !TextUtils.isEmpty(data.getCityName())) {
            StringBuilder sb = new StringBuilder();
            sb.append(TextUtils.isEmpty(data.getProvinceName()) ? "" : data.getProvinceName())
                    .append(" ")
                    .append(TextUtils.isEmpty(data.getCityName()) ? "" : data.getCityName())
                    .append(" ")
                    .append(TextUtils.isEmpty(data.getAreaName()) ? "" : data.getAreaName());
            return sb.toString();
        }
        return addressDetails;
    }
//    @Override
//    public void onClick(String province, String provinceId, String city, String cityId, String area, String areaId) {
//
//        userInfo.setProvinceName(province);
//        userInfo.setCityName(city);
//        userInfo.setAreaName(area);
//        userInfo.setProvinceId(provinceId);
//        userInfo.setCityId(cityId);
//        userInfo.setAreaId(areaId);
//        mAddressFragment.binding.addressDetailsTv.setText(getAddressDetails());
//    }


    @Override
    public void onClick(String province, String city, String area) {
        userInfo.setProvinceName(province);
        userInfo.setCityName(city);
        userInfo.setAreaName(area);
        mAddressFragment.binding.addressDetailsTv.setText(getAddressDetails());
    }
}
