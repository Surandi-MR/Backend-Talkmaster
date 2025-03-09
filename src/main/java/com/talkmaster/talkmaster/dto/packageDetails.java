package com.talkmaster.talkmaster.dto;

import com.talkmaster.talkmaster.model.PackageModel;
import com.talkmaster.talkmaster.model.UserPackage;

public class packageDetails {
    private PackageModel packageModel;
    private UserPackage userPackage;

    public PackageModel getPackageModel() {
        return packageModel;
    }

    public void setPackageModel(PackageModel packageModel) {
        this.packageModel = packageModel;
    }

    public UserPackage getUserPackage() {
        return userPackage;
    }

    public void setUserPackage(UserPackage userPackage) {
        this.userPackage = userPackage;
    }
}
