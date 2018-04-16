package com.hunegroup.hune.dto;

/**
 * Created by apple on 11/20/17.
 */

public class TransactionDTO {
    private String name;
    private double amount;
    private int type_money;
    private String description;
    private String txid;
    private int type;
    private int define_transaction;
    private String created_at;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getType_money() {
        return type_money;
    }

    public void setType_money(int type_money) {
        this.type_money = type_money;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getDefine_transaction() {
        return define_transaction;
    }

    public void setDefine_transaction(int define_transaction) {
        this.define_transaction = define_transaction;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
