package com.example.loginapp.view.fragment.payment_option;

public interface PaymentOptionView {

    void setView(String subTotal, String sippingCost, String total);

    void goOrderSuccessScreen();

    void isLoading(Boolean loading);

    void onMessage(String message);
}
