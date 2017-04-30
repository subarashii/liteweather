package com.liteweather.android.db;

import org.litepal.crud.DataSupport;

/**
 * Created by kizen on 17/4/30.
 * every class in litepal must extend DataSupport
 */

public class Province extends DataSupport {

    private int id;
    private int provinceCode;
    private int provinceName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }

    public int getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(int provinceName) {
        this.provinceName = provinceName;
    }


}
