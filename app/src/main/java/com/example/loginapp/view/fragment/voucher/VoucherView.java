package com.example.loginapp.view.fragment.voucher;

import com.example.loginapp.model.entity.Voucher;

import java.util.List;

public interface VoucherView {

    void getVouchers(List<Voucher> vouchers);
}