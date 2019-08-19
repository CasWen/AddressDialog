package com.example.addresschangedialog;

@SuppressWarnings("AlibabaClassMustHaveAuthor")
public class ChangeAddressDialogViewModule {

    private View view;

    public ChangeAddressDialogViewModule(View view) {
        this.view = view;
    }

    //点击事件
    public void onClickConfirm(android.view.View view) {

        this.view.onClickConfirm(view);

    }



    //点击事件
    public void onClickCancle(android.view.View view) {
        this.view.onClicKCancle(view);


    }

    public interface View {
        void onClickConfirm(android.view.View view);
        void onClicKCancle(android.view.View view);
    }
}
