package com.aximly.electricbug.stock.service;

import com.aximly.electricbug.stock.dto.StockDto;

import java.util.List;
import java.util.Optional;

public interface StockService {
    List<StockDto> getAllStock();
    Optional<StockDto> getStockById(Integer stockId);
    Optional<StockDto> getStockByBarcode(String barcode);
    List<StockDto> searchStock(String query);
    List<StockDto> getStockByDept(Integer deptId);
}