package eim.systems.cs.pub.ro.practicaltest02.model;

import java.util.Date;

/**
 * Created by Catalin on 5/23/17.
 */

public class StockModel {
    public String stock;
    public String value;
    public Date date;

    public StockModel(String stock, String value, Date date) {
        this.stock = stock;
        this.value = value;
        this.date = date;
    }
}
