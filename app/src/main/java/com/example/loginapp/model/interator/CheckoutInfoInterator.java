package com.example.loginapp.model.interator;

import androidx.annotation.NonNull;

import com.example.loginapp.model.entity.Date;
import com.example.loginapp.model.entity.DeliveryAddress;
import com.example.loginapp.model.listener.CheckoutInfoListener;
import com.example.loginapp.utils.Constant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CheckoutInfoInterator {

    private final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private CheckoutInfoListener listener;

    public CheckoutInfoInterator(CheckoutInfoListener listener) {
        this.listener = listener;
    }

    public void getDeliveryAddresses() {
        Constant.deliveryAddressRef.child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            List<DeliveryAddress> deliveryAddresses = new ArrayList<>();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren())
                                deliveryAddresses.add(dataSnapshot.getValue(DeliveryAddress.class));
                            listener.getDeliveryAddresses(deliveryAddresses);
                        } else {
                            listener.isDeliveryAddressesEmpty();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}
