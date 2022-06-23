package com.chungld.uipack.calendar.model;

import java.math.BigDecimal;
import java.util.HashMap;

public class TicketHuntBundle implements AdditionData<HashMap<String, BigDecimal>> {

    private HashMap<String, BigDecimal> priceMap;
    private HashMap<String, BigDecimal> priceMinMap;
    private HashMap<String, BigDecimal> priceMaxMap;

    @Override
    public HashMap<String, BigDecimal> getAdditionObjectAt(int index) {
        switch (index) {
            case 0:
                return priceMap;
            case 1:
                return priceMinMap;
            case 2:
                return priceMaxMap;
            default:
                return null;
        }
    }

    public void setPriceMap(HashMap<String, BigDecimal> priceMap) {
        this.priceMap = priceMap;
    }

    public void setPriceMinMap(HashMap<String, BigDecimal> priceMinMap) {
        this.priceMinMap = priceMinMap;
    }

    public void setPriceMaxMap(HashMap<String, BigDecimal> priceMaxMap) {
        this.priceMaxMap = priceMaxMap;
    }
}
