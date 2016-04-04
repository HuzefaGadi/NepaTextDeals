package com.webgentechnologies.nepatextdeals.beans;

/**
 * Created by huzefaasger on 04-04-2016.
 */
public class KioskData {

    private String id;
    private String merchant_kiosk_id;
    private String free_gift;
    private String checkin_credit;
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMerchant_kiosk_id() {
        return merchant_kiosk_id;
    }

    public void setMerchant_kiosk_id(String merchant_kiosk_id) {
        this.merchant_kiosk_id = merchant_kiosk_id;
    }

    public String getFree_gift() {
        return free_gift;
    }

    public void setFree_gift(String free_gift) {
        this.free_gift = free_gift;
    }

    public String getCheckin_credit() {
        return checkin_credit;
    }

    public void setCheckin_credit(String checkin_credit) {
        this.checkin_credit = checkin_credit;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
