package com.example.addresschangedialog;

import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.addresschangedialog.databinding.SellerCashChangeAddressBinding;
import com.example.addresschangedialog.wheelview.AbstractWheelTextAdapter1;
import com.example.addresschangedialog.wheelview.OnWheelChangedListener;
import com.example.addresschangedialog.wheelview.OnWheelScrollListener;
import com.example.addresschangedialog.wheelview.WheelView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@SuppressWarnings("AlibabaClassMustHaveAuthor")
public class ChangeAddressDialog extends DialogFragment implements ChangeAddressDialogViewModule.View , View.OnClickListener {
    private static final String TAG = "ChangeAddressDialog";
    private Context mContext;

    private WheelView wvProvince;
    private WheelView wvCitys;
    private WheelView wvArea;
    private View lyChangeAddressChild;
    private TextView btnSure;
    private TextView btnCancel;
    private JSONObject mJsonObj;
    /**
     * 所有省
     */
    private ArrayList<Map<String,String>> mProvinceDatas;
//    private String[] mProvinceDatas;
    /**
     * key - 省 value - 市s
     */
    private Map<String, Object> mCitisDatasMap = new HashMap<String,Object>();
//    private Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
    /**
     * key - 市 values - 区s
     */
    private Map<String, Object> mAreaDatasMap = new HashMap<String,Object>();
//    private Map<String, String[]> mAreaDatasMap = new HashMap<String, String[]>();


//    private ArrayList<String> arrProvinces = new ArrayList<String>();
//    private ArrayList<String> arrCitys = new ArrayList<String>();
//    private ArrayList<String> arrAreas = new ArrayList<String>();
    private ArrayList<Map<String,String>> arrProvinces = new ArrayList<Map<String,String>>();
    private ArrayList<Map<String,String>> arrCitys = new ArrayList<Map<String,String>>();
    private ArrayList<Map<String,String>> arrAreas = new ArrayList<Map<String,String>>();
    private AddressTextAdapterLeft provinceAdapter;

    private AddressTextAdapter cityAdapter;

    private AddressTextAdapterRight areaAdapter;

    private String strProvince = "广东省";
    private String strCity = "深圳市";
    private String strArea = "福田区";
    private OnAddressCListener onAddressCListener;

    private int maxsize = 14;
    private int minsize = 12;
    public boolean isProvinceLock = false;
    public boolean isCityLock = false;
    public boolean isAreaLock = false;



    public static ChangeAddressDialog newInstance() {

        Bundle args = new Bundle();

        ChangeAddressDialog fragment = new ChangeAddressDialog();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getDialog().setCanceledOnTouchOutside(false);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        SellerCashChangeAddressBinding binding = SellerCashChangeAddressBinding.inflate(inflater, container, false);
        binding.setModule(new ChangeAddressDialogViewModule(this));

        wvProvince =  binding.wvAddressProvince;
        wvCitys =  binding.wvAddressCity;
        wvArea = (WheelView) binding.wvAddressArea;
        wvProvince.setWheelBackground(R.color.base_color_view_lable_bg);
        lyChangeAddressChild = binding.lyMyinfoChangeaddressChild;
        btnSure = (TextView) binding.btnMyinfoSure;
        btnCancel = (TextView) binding.btnMyinfoCancel;
        lyChangeAddressChild.setOnClickListener(this);
        btnSure.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        initJsonData();
        initDatas();


        initProvinces();
        provinceAdapter = new AddressTextAdapterLeft(mContext, arrProvinces, getProvinceItem(strProvince), maxsize, minsize);
        wvProvince.setVisibleItems(5);
        wvProvince.setViewAdapter(provinceAdapter);
        wvProvince.setCurrentItem(getProvinceItem(strProvince));

        String pCode=arrProvinces.get(getProvinceItem(strProvince)).get("id");
        initCitys((ArrayList<Map<String,String>>) mCitisDatasMap.get(pCode));
        cityAdapter = new AddressTextAdapter(mContext, arrCitys, getCityItem(strCity), maxsize, minsize);
        wvCitys.setVisibleItems(5);
        wvCitys.setViewAdapter(cityAdapter);
        wvCitys.setCurrentItem(getCityItem(strCity));

        String cCode=arrCitys.get(getCityItem(strCity)).get("id");
        initAreas((ArrayList<Map<String,String>>)mAreaDatasMap.get(cCode));
        areaAdapter = new AddressTextAdapterRight(mContext, arrAreas, getAreaItem(strArea), maxsize, minsize);
        wvArea.setVisibleItems(5);
        wvArea.setViewAdapter(areaAdapter);
        wvArea.setCurrentItem(getAreaItem(strArea));

        wvProvince.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                // TODO Auto-generated method stub
                String currentText = (String) provinceAdapter.getItemText(wheel.getCurrentItem());
                String currentCode = (String) provinceAdapter.getItemCode(wheel.getCurrentItem());
                strProvince = currentText;
                setTextviewSize(currentText, provinceAdapter);

                ArrayList<Map<String,String>> citys = (ArrayList<Map<String,String>>)mCitisDatasMap.get(currentCode);
                initCitys(citys);
                cityAdapter = new AddressTextAdapter(mContext, arrCitys, 0, maxsize, minsize);
                wvCitys.setVisibleItems(5);
                wvCitys.setViewAdapter(cityAdapter);
                wvCitys.setCurrentItem(0);
                setTextviewSize("0", cityAdapter);

                //根据市，地区联动
                ArrayList<Map<String,String>> areas = (ArrayList<Map<String,String>>)mAreaDatasMap.get(citys.get(0).get("id"));
                initAreas(areas);
                areaAdapter = new AddressTextAdapterRight(mContext, arrAreas, 0, maxsize, minsize);
                wvArea.setVisibleItems(5);
                wvArea.setViewAdapter(areaAdapter);
                wvArea.setCurrentItem(0);
                setTextviewSize("0", areaAdapter);
            }
        });

        wvProvince.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
                // TODO Auto-generated method stub
                ChangeAddressDialog.this.isProvinceLock = true;
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                // TODO Auto-generated method stub
                String currentText = (String) provinceAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, provinceAdapter);
                ChangeAddressDialog.this.isProvinceLock = false;
            }
        });

        wvCitys.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                // TODO Auto-generated method stub
                String currentText = (String) cityAdapter.getItemText(wheel.getCurrentItem());
                String currentCode = (String) cityAdapter.getItemCode(wheel.getCurrentItem());
                strCity = currentText;
                setTextviewSize(currentText, cityAdapter);

                //根据市，地区联动
                ArrayList<Map<String,String>> areas = (ArrayList<Map<String,String>>) mAreaDatasMap.get(currentCode);
                initAreas(areas);
                areaAdapter = new AddressTextAdapterRight(mContext, arrAreas, 0, maxsize, minsize);
                wvArea.setVisibleItems(5);
                wvArea.setViewAdapter(areaAdapter);
                wvArea.setCurrentItem(0);
                setTextviewSize("0", areaAdapter);


            }
        });

        wvCitys.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
                // TODO Auto-generated method stub
                ChangeAddressDialog.this.isCityLock = true;
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                // TODO Auto-generated method stub
                String currentText = (String) cityAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, cityAdapter);
                ChangeAddressDialog.this.isCityLock = false;
            }
        });

        wvArea.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                // TODO Auto-generated method stub
                String currentText = (String) areaAdapter.getItemText(wheel.getCurrentItem());
                strArea = currentText;
                setTextviewSize(currentText, cityAdapter);
            }
        });

        wvArea.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
                // TODO Auto-generated method stub
                ChangeAddressDialog.this.isAreaLock = true;
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                // TODO Auto-generated method stub
                String currentText = (String) areaAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, areaAdapter);
                ChangeAddressDialog.this.isAreaLock = false;
            }
        });


        return binding.getRoot();
    }



    public void show( Context mcontext) {

        this.mContext = mcontext;
        FragmentActivity activity = (FragmentActivity) mcontext;
        show(activity.getFragmentManager(), TAG);

    }

    @Override
    public void onClickConfirm(View view) {
        if (onAddressCListener != null && !isProvinceLock && !isCityLock && !isAreaLock) {
            onAddressCListener.onClick(strProvince, strCity,strArea);
            dismiss();
        }
    }



    @Override
    public void onClicKCancle(View view) {
        if (onAddressCListener != null && !isProvinceLock && !isCityLock && !isAreaLock) {
            dismiss();
        }
    }


    private class AddressTextAdapter extends AbstractWheelTextAdapter1 {
//        ArrayList<String> list;
        ArrayList<Map<String,String>> list;

        protected AddressTextAdapter(Context context, ArrayList<Map<String,String>> list, int currentItem, int maxsize, int minsize) {
            super(context, R.layout.item_address_city, NO_RESOURCE, currentItem, maxsize, minsize);
            this.list = list;
            setItemTextResource(R.id.tempValue);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            return view;
        }

        @Override
        public int getItemsCount() {
            return list.size();
        }

        @Override
        protected CharSequence getItemText(int index) {
            return list.get(index).get("name") + "";
        }

        protected String getItemCode(int index){
            return list.get(index).get("id");
        }
    }

    private class AddressTextAdapterLeft extends AbstractWheelTextAdapter1 {
//        ArrayList<String> list;
        ArrayList<Map<String,String>> list;
        protected AddressTextAdapterLeft(Context context, ArrayList<Map<String,String>> list, int currentItem, int maxsize, int minsize) {
            super(context, R.layout.item_address_province, NO_RESOURCE, currentItem, maxsize, minsize);
            this.list = list;
            setItemTextResource(R.id.tempValue);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            return view;
        }

        @Override
        public int getItemsCount() {
            return list.size();
        }

        @Override
        protected CharSequence getItemText(int index) {
            return list.get(index).get("name") + "";
        }

        protected String getItemCode(int index){
            return list.get(index).get("id");
        }
    }

    private class AddressTextAdapterRight extends AbstractWheelTextAdapter1 {
//        ArrayList<String> list;
        ArrayList<Map<String,String>> list;
        protected AddressTextAdapterRight(Context context, ArrayList<Map<String,String>> list, int currentItem, int maxsize, int minsize) {
            super(context, R.layout.item_address_area, NO_RESOURCE, currentItem, maxsize, minsize);
            this.list = list;
            setItemTextResource(R.id.tempValue);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            return view;
        }

        @Override
        public int getItemsCount() {
            return list.size();
        }

        @Override
        protected CharSequence getItemText(int index) {
            return list.get(index).get("name") + "";
        }
    }

    /**
     * 设置字体大小
     *  @param curriteItemText
     * @param adapter
     */
    public void setTextviewSize(String curriteItemText, AbstractWheelTextAdapter1 adapter) {
        ArrayList<View> arrayList = adapter.getTestViews();
        int size = arrayList.size();
        String currentText;
        for (int i = 0; i < size; i++) {
            TextView textvew = (TextView) arrayList.get(i);
            currentText = textvew.getText().toString();
            if (curriteItemText.equals(currentText)) {
                textvew.setTextSize(15);
                textvew.setTextColor(getResources().getColor(R.color.base_color_gray));
            } else {
                textvew.setTextSize(12);
                textvew.setTextColor(getResources().getColor(R.color.base_color_gray1));
            }
        }
    }

    public void setAddresskListener(OnAddressCListener onAddressCListener) {
        this.onAddressCListener = onAddressCListener;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v == btnSure) {
            if (onAddressCListener != null) {
                onAddressCListener.onClick(strProvince, strCity,strArea);
            }
        } else if (v == btnCancel) {

        } else if (v == lyChangeAddressChild) {
            return;
        } else {
            //			dismiss();
        }
        dismiss();
    }

    /**
     * 回调接口
     *
     * @author Administrator
     *
     */
    public interface OnAddressCListener {
        public void onClick(String province, String city, String area);
    }

    /**
     * 从文件中读取地址数据
     */
    private void initJsonData() {
        try {
            /*StringBuffer sb = new StringBuffer();
            InputStream is = mContext.getClass().getClassLoader().getResourceAsStream("assets/" + "city.json");
            int len = -1;
            byte[] buf = new byte[1024];
            while ((len = is.read(buf)) > 0) {
                sb.append(new String(buf, 0, len,"gbk"));
            }*/
            StringBuffer sb = new StringBuffer();
            InputStream is = mContext.getClass().getClassLoader().getResourceAsStream("assets/" + "city.json");
            InputStreamReader isr = new InputStreamReader(is);//读取
            //创建字符流缓冲区
            BufferedReader bufr = new BufferedReader(isr);//缓冲
            String line;
            while((line = bufr.readLine())!=null){
               sb.append(line);
            }
            is.close();
            mJsonObj = new JSONObject(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析整个Json对象，完成后释放Json对象的内存
     */
    private void initDatas()
    {
        try
        {
            JSONArray jsonArray = mJsonObj.getJSONArray("citylist");
//            mProvinceDatas = new String[jsonArray.length()];
            mProvinceDatas=new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonP = jsonArray.getJSONObject(i);// 每个省的json对象
                Map<String,String> province = new HashMap<>();
                String pid = jsonP.getString("id");// 省编号
                String pname = jsonP.getString("s");// 省名字
                province.put("id",pid);
                province.put("name",pname);

                mProvinceDatas.add(province);
//                mProvinceDatas[i] = province;

                JSONArray jsonCs = null;
                try
                {
                    /**
                     * Throws JSONException if the mapping doesn't exist or is
                     * not a JSONArray.
                     */
                    jsonCs = jsonP.getJSONArray("c");
                } catch (Exception e1)
                {
                    continue;
                }
//                String[] mCitiesDatas = new String[jsonCs.length()];
                ArrayList<Map<String,String>> mCitiesDatas = new ArrayList<>();
                for (int j = 0; j < jsonCs.length(); j++)
                {
                    JSONObject jsonCity = jsonCs.getJSONObject(j);
                    Map<String,String> city =  new HashMap<>();
                    String cid = jsonCity.getString("id");// 市编号
                    String cname = jsonCity.getString("s");// 市名字
                    city.put("id",cid);
                    city.put("name",cname);

                    mCitiesDatas.add(city);
//                    mCitiesDatas[j] = city;

                    JSONArray jsonAreas = null;
                    try
                    {
                        /**
                         * Throws JSONException if the mapping doesn't exist or
                         * is not a JSONArray.
                         */
                        jsonAreas = jsonCity.getJSONArray("a");
                    } catch (Exception e)
                    {
                        continue;
                    }

//                    String[] mAreasDatas = new String[jsonAreas.length()];// 当前市的所有区
                    ArrayList<Map<String,String>> mAreasDatas = new ArrayList<>();
                    for (int k = 0; k < jsonAreas.length(); k++)
                    {
                        Map<String,String> area = new HashMap<>();
                        String aid = jsonAreas.getJSONObject(k).getString("id");// 区域的编号
                        String aname = jsonAreas.getJSONObject(k).getString("s");// 区域的名称
                        area.put("id",aid);
                        area.put("name",aname);

                        mAreasDatas.add(area);
//                        mAreasDatas[k] = area;
                    }
                    mAreaDatasMap.put(city.get("id"), mAreasDatas);
                }

                mCitisDatasMap.put(province.get("id"), mCitiesDatas);
            }

        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        mJsonObj = null;
    }

    /**
     * 初始化省会
     */
    public void initProvinces() {
        int length = mProvinceDatas.size();
        for (int i = 0; i < length; i++) {
            arrProvinces.add(mProvinceDatas.get(i));
        }
    }

    /**
     * 根据省会，生成该省会的所有城市
     *
     * @param citys
     */
    public void initCitys(ArrayList<Map<String,String>> citys) {
        if (citys != null) {
            arrCitys.clear();
            int length = citys.size();
            for (int i = 0; i < length; i++) {
                arrCitys.add(citys.get(i));
            }
        } else {
//            ArrayList<Map<String,String>> city = (ArrayList<Map<String,String>>)mCitisDatasMap.get("440000");//广东省
            String code=arrProvinces.get(getCityItem("广东省")).get("id");
            ArrayList<Map<String,String>> city = (ArrayList<Map<String,String>>)mCitisDatasMap.get(code);
            arrCitys.clear();
            int length = city.size();
            for (int i = 0; i < length; i++) {
                arrCitys.add(city.get(i));
            }
        }
        if (arrCitys != null && arrCitys.size() > 0
                && find(arrCitys,strCity)==-1) {//!arrCitys.contains(strCity)
            strCity = arrCitys.get(0).get("name");
        }
    }

    /**
     * 根据城市，生成该城市的所有地区
     *
     * @param areas
     */
    public void initAreas(ArrayList<Map<String,String>> areas) {
        if (areas != null) {
            arrAreas.clear();
            int length = areas.size();
            for (int i = 0; i < length; i++) {
                arrAreas.add(areas.get(i));
            }
        } else {
//            ArrayList<Map<String,String>> area = (ArrayList<Map<String,String>>)mAreaDatasMap.get("440300");//深圳市
            String code=arrCitys.get(getCityItem("深圳市")).get("id");
            ArrayList<Map<String,String>> area = (ArrayList<Map<String,String>>)mAreaDatasMap.get(code);
            arrAreas.clear();
            int length = area.size();
            for (int i = 0; i < length; i++) {
                arrAreas.add(area.get(i));
            }
        }
        if (arrAreas != null && arrAreas.size() > 0
                && find(arrAreas,strArea)==-1) {//!arrAreas.contains(strArea)
            strArea = arrAreas.get(0).get("name");
        }
    }

    /**
     * 初始化地点
     *
     * @param province
     * @param city
     */
    public void setAddress(String province, String city, String area) {
        if (province != null && province.length() > 0) {
            this.strProvince = province;
        }
        if (city != null && city.length() > 0) {
            this.strCity = city;
        }

        if (area != null && area.length() > 0) {
            this.strArea = area;
        }
    }

    /**
     * 返回省会索引，没有就返回默认“广东”
     *
     * @param province
     * @return
     */
    public int getProvinceItem(String province) {
        int size = arrProvinces.size();
        int provinceIndex = 0;
        boolean noprovince = true;
        for (int i = 0; i < size; i++) {
            if (province.equals(arrProvinces.get(i).get("name"))) {
                noprovince = false;
                return provinceIndex;
            } else {
                provinceIndex++;
            }
        }
        if (noprovince) {
            strProvince = "广东省";
            return 18;
        }
        return provinceIndex;
    }

    /**
     * 得到城市索引，没有返回默认“深圳”
     *
     * @param city
     * @return
     */
    public int getCityItem(String city) {
        int size = arrCitys.size();
        int cityIndex = 0;
        boolean nocity = true;
        for (int i = 0; i < size; i++) {
            System.out.println(arrCitys.get(i).get("name"));
            if (city.equals(arrCitys.get(i).get("name"))) {
                nocity = false;
                return cityIndex;
            } else {
                cityIndex++;
            }
        }
        if (nocity) {
            strCity = "深圳市";
            return 2;
        }
        return cityIndex;
    }

    /**
     * 得到地区索引，没有返回默认“福田区”
     *
     * @param area
     * @return
     */
    public int getAreaItem(String area) {
        int size = arrAreas.size();
        int areaIndex = 0;
        boolean noarea = true;
        for (int i = 0; i < size; i++) {
            System.out.println(arrAreas.get(i).get("name"));
            if (area.equals(arrAreas.get(i).get("name"))) {
                noarea = false;
                return areaIndex;
            } else {
                areaIndex++;
            }
        }
        if (noarea) {
            strArea = "福田区";
            return 1;
        }
        return areaIndex;
    }

     public int find(ArrayList<Map<String,String>> arr, String val) {
         int index = -1, i = 0;
         while (i < arr.size()) {
             if (arr.get(i).get("name").equals(val))
                 break;
             i++;
         }
         if (i < arr.size()) index = i;
         return index;
     }


}
