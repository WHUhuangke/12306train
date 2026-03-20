package com.example.ticket.service;

import java.util.ArrayList;
import java.util.List;

public class StockDeductionPlanner {

    public record StockRange(int fromIndex, int toIndex) {
        public boolean intersects(int sellStart, int sellEnd) {
            return !(toIndex <= sellStart || fromIndex >= sellEnd);
        }

        public boolean coveredBy(int startIndex, int endIndex) {
            return fromIndex >= startIndex && toIndex <= endIndex;
        }
    }

    public List<StockRange> affectedRanges(int startIndex, int endIndex, int sellStart, int sellEnd, List<StockRange> records) {
        List<StockRange> impacted = new ArrayList<>();
        for (StockRange record : records) {
            if (record.coveredBy(startIndex, endIndex) && record.intersects(sellStart, sellEnd)) {
                impacted.add(record);
            }
        }
        return impacted;
    }
}
