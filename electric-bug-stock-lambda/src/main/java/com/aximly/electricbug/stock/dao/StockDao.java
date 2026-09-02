package com.aximly.electricbug.stock.dao;

import com.aximly.electricbug.stock.dto.StockDto;

import java.util.List;
import java.util.Optional;

public interface StockDao {
    List<StockDto> getAllStock();
    Optional<StockDto> getStockById(Integer stockId);
    Optional<StockDto> getStockByBarcode(String barcode);
    List<StockDto> searchStock(String query);
    List<StockDto> getStockByDept(Integer deptId);
}