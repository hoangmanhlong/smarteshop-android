package com.example.loginapp.view.fragments.cart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.airbnb.lottie.LottieAnimationView;
import com.example.loginapp.R;
import com.example.loginapp.adapter.cart_adapter.CartAdapter;
import com.example.loginapp.adapter.cart_adapter.CartItemClickListener;
import com.example.loginapp.databinding.FragmentCartBinding;
import com.example.loginapp.model.entity.FirebaseProduct;
import com.example.loginapp.model.entity.Product;
import com.example.loginapp.presenter.CartPresenter;
import com.example.loginapp.utils.Constant;
import com.example.loginapp.view.commonUI.AppAnimationState;
import com.example.loginapp.view.fragments.action_on_product.ActionOnProductFragment;
import com.example.loginapp.view.fragments.payment_option.PaymentOptionMessage;
import com.example.loginapp.view.fragments.select_voucher_fragment.MessageVoucherSelected;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class CartFragment extends Fragment implements CartView, CartItemClickListener {

    private static final String TAG = CartFragment.class.getSimpleName();

    private NavController navController;

    private CartPresenter presenter;

    private FragmentCartBinding binding;

    private CartAdapter adapter;

    private RecyclerView shoppingCartRecyclerview;

    private LottieAnimationView cartEmptyLottieAnimationView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navController = NavHostFragment.findNavController(this);
        presenter = new CartPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);
        binding.setFragment(this);
        cartEmptyLottieAnimationView = binding.animationView;
        shoppingCartRecyclerview = binding.basketRecyclerView;
        adapter = new CartAdapter(this);
        shoppingCartRecyclerview.setAdapter(adapter);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SimpleItemAnimator simpleItemAnimator = (SimpleItemAnimator) shoppingCartRecyclerview.getItemAnimator();
        if (simpleItemAnimator != null) simpleItemAnimator.setSupportsChangeAnimations(false);
        presenter.initBasket();
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.addShoppingCartValueEventListener();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        cartEmptyLottieAnimationView.playAnimation();
    }

    @Override
    public void onPause() {
        super.onPause();
        cartEmptyLottieAnimationView.cancelAnimation();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        presenter.removeShoppingCartValueEventListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        presenter = null;
        navController = null;
        MessageVoucherSelected messageVoucherSelected =
                EventBus.getDefault().getStickyEvent(MessageVoucherSelected.class);
        if (messageVoucherSelected != null)
            EventBus.getDefault().removeStickyEvent(messageVoucherSelected);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void getSelectedVoucher(MessageVoucherSelected messageVoucherSelected) {
        presenter.setSelectedVoucher(messageVoucherSelected.getVoucher());
    }

    @Override
    public void bindDiscountCode(String code) {
        binding.discountCode.setText(code);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void getPaymentOptionMessage(PaymentOptionMessage paymentOptionMessage) {
        presenter.setClearCode(true);
        EventBus.getDefault().removeStickyEvent(paymentOptionMessage);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        adapter = null;
        cartEmptyLottieAnimationView = null;
        shoppingCartRecyclerview = null;
    }

    @Override
    public void bindBasket(List<FirebaseProduct> products) {
        adapter.submitList(products);
    }

//    /**
//     * when click buy now button in {@link ActionOnProductFragment} will pass
//     * 'OPENED_FROM_BUY_NOW_OF_ACTION_ON_PRODUCT_FRAGMENT' key to open dialog buy now
//     * in {@link ProductDetailFragment}
//     *
//     * @param product Product clicked
//     */
    @Override
    public void onItemClick(Product product) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.PRODUCT_KEY, product);
        navController.navigate(R.id.action_global_productFragment, bundle);
    }

    @Override
    public void onProductLongClick(Product product) {
        ActionOnProductFragment actionOnProductFragment = new ActionOnProductFragment(this);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.PRODUCT_KEY, product);
        bundle.putBoolean(Constant.OPENED_FROM_CART, true);
        actionOnProductFragment.setArguments(bundle);
        actionOnProductFragment.show(requireActivity().getSupportFragmentManager(), ActionOnProductFragment.TAG);
    }

    @Override
    public void updateQuantity(int id, int quantity) {
        presenter.updateQuantity(id, quantity);
    }

    @Override
    public void onCheckboxClick(FirebaseProduct product) {
        presenter.onItemChecked(product);
    }

    @Override
    public void setCheckAllChecked(boolean isChecked) {
        binding.selectAllCheckbox.setChecked(isChecked);
    }

    @Override
    public void showCheckoutView(Boolean check) {
        ConstraintLayout checkoutView = binding.checkoutView;
        if ((checkoutView.getVisibility() == View.GONE) && check)
            AppAnimationState.setCheckoutViewState(checkoutView, true);
        if ((checkoutView.getVisibility() == View.VISIBLE) && !check)
            AppAnimationState.setCheckoutViewState(checkoutView, false);
    }

    @Override
    public void showCheckAllCheckbox(Boolean visible) {
        binding.selectAllCheckbox.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void isBasketEmpty(Boolean isEmpty) {
        binding.setIsBasketEmpty(isEmpty);
    }

    @Override
    public void isCheckAllCheckboxChecked(Boolean checked) {
        binding.selectAllCheckbox.setChecked(checked);
    }

    public void onCheckboxAllClick() {
        presenter.updateCheckboxAllSelected(binding.selectAllCheckbox.isChecked());
    }

    @Override
    public void onDeleteProduct(int productId) {
        presenter.deleteProductInFirebase(productId);
    }

    @Override
    public void setTotal(String subtotal, String quantity, String total) {
        binding.setQuantity(quantity);
        binding.setPrice(subtotal);
        binding.setTotal(total);
    }

    public void onCheckoutClick() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.ORDER_KEY, presenter.getOrder());
        bundle.putBoolean(Constant.IS_PRODUCTS_FROM_CART, true);
        navController.navigate(R.id.checkoutInfoFragment, bundle);
    }

    public void onSelectCodeClick() {
        navController.navigate(R.id.selectVoucherFragment);
    }

    public void clearDiscountCode() {
        MessageVoucherSelected messageVoucherSelected =
                EventBus.getDefault().getStickyEvent(MessageVoucherSelected.class);
        if (messageVoucherSelected != null)
            EventBus.getDefault().removeStickyEvent(messageVoucherSelected);
        presenter.setClearCode(true);
    }

    @Override
    public void clearDiscountCode(Boolean clear) {
        binding.clearDiscountCodeView.setVisibility(clear ? View.GONE : View.VISIBLE);
        binding.selectDiscountCodeView.setVisibility(clear ? View.VISIBLE : View.GONE);
    }
}