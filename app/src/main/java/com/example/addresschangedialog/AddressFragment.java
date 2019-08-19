package com.example.addresschangedialog;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.example.addresschangedialog.bean.ListAddressBean;
import com.example.addresschangedialog.databinding.FragmentMainBinding;


@SuppressWarnings("AlibabaClassMustHaveAuthor")
public class AddressFragment extends Fragment implements AddressContract.View, AddressItemView {

    private static final String TAG = "AddressFragment";
    private AddressPresenter addressPresenter;
    private static AddressFragment fragment;
    private AddressViewModule module;
    public FragmentMainBinding binding;
    private ListAddressBean addressInfo;
    public static AddressFragment newInstance() {
        if (null == fragment) {
            fragment = new AddressFragment();
        }
        return fragment;
    }

    protected AddressContract.Presenter getPresenter() {
        if (null == addressPresenter) {
            addressPresenter = new AddressPresenter();
        }

        return addressPresenter;
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, null, false);
        module = new AddressViewModule(getActivity(), this);
        binding.setModel(module);
        if (null != addressInfo){
            module.setData(addressInfo);
        }

        return binding.getRoot();
    }

    public interface ItemClickCallBack {
        void returnAddress(ListAddressBean addressInfo);

    }
}
