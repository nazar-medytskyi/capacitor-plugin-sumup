package com.nazar.capacitor.sumup;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.result.ActivityResult;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.ActivityCallback;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.sumup.merchant.reader.api.SumUpAPI;
import com.sumup.merchant.reader.api.SumUpAPIHelper;
import com.sumup.merchant.reader.api.SumUpPayment;
import com.sumup.merchant.reader.api.SumUpState;
import com.sumup.merchant.reader.identitylib.ui.activities.LoginActivity;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;

@CapacitorPlugin(name = "SumUp")
public class SumUpPlugin extends Plugin {

    @Override
    public void load() {
        SumUpState.init(getContext());
    }

    @PluginMethod
    public void login(PluginCall call) {
        String affiliateKey = call.getString("affiliateKey");

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.putExtra("isAffiliate", true);
        intent.putExtra("affiliate-key", affiliateKey);
        if (call.hasOption("accessToken")) {
            String accessToken = call.getString("accessToken");
            intent.putExtra("access-token", accessToken);
        }

        startActivityForResult(call, intent, "handleResponse");
    }

    @PluginMethod
    public void checkout(PluginCall call) {
        Double total = call.getDouble("total");
        SumUpPayment.Currency currency = SumUpPayment.Currency.valueOf(call.getString("currency"));

        if (total == null) {
            return;
        }

        Intent intent = SumUpAPIHelper.getPaymentIntent(getActivity());
        intent.putExtra("total", new BigDecimal(total));
        intent.putExtra("currency", currency.getIsoCode());

        if (call.hasOption("title")) {
            String title = call.getString("title");
            intent.putExtra("title", title);
        }

        if (call.hasOption("receiptEmail")) {
            String receiptEmail = call.getString("receiptEmail");
            intent.putExtra("receipt-email", receiptEmail);
        }

        if (call.hasOption("receiptSMS")) {
            String receiptSMS = call.getString("receiptSMS");
            intent.putExtra("receipt-mobilephone", receiptSMS);
        }

        if (call.hasOption("foreignTransactionId")) {
            String foreignTransactionId = call.getString("foreignTransactionId");
            intent.putExtra("foreign-tx-id", foreignTransactionId);
        }

        if (call.hasOption("skipSuccessScreen")) {
            Boolean skipSuccessScreen = call.getBoolean("skipSuccessScreen");
            intent.putExtra("skip-screen-success", skipSuccessScreen);
        }

        if (call.hasOption("additionalInfo")) {
            JSObject jsAdditionalInfo = call.getObject("additionalInfo");
            HashMap<String, String> additionalInfo = new HashMap<>();
            for (Iterator<String> it = jsAdditionalInfo.keys(); it.hasNext();) {
                String key = it.next();
                additionalInfo.put(key, jsAdditionalInfo.getString(key));
            }

            intent.putExtra("addition-info", additionalInfo);
        }

        startActivityForResult(call, intent, "handleResponse");
    }

    @ActivityCallback
    public void handleResponse(PluginCall call, ActivityResult result) {
        Intent data = result.getData();
        if (data == null) {
            return;
        }

        Bundle extrasBundle = data.getExtras();
        if (extrasBundle == null) {
            return;
        }

        int resultCode = extrasBundle.getInt(SumUpAPI.Response.RESULT_CODE);
        String resultMessage = extrasBundle.getString(SumUpAPI.Response.MESSAGE);
        if (resultCode > 0) {
            JSObject ret = new JSObject();
            ret.put("code", resultCode);
            ret.put("message", resultMessage);
            call.resolve(ret);
        } else {
            call.reject(resultMessage, String.format("%d", resultCode));
        }
    }
}
