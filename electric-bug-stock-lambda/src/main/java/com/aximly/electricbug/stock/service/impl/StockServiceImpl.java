package com.aximly.electricbug.stock.service.impl;

import com.aximly.electricbug.stock.dao.StockDao;
import com.aximly.electricbug.stock.dto.StockDto;
import com.aximly.electricbug.stock.service.StockService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StockServiceImpl implements StockService {

    private final StockDao stockDao;

    public StockServiceImpl(StockDao stockDao) {
        this.stockDao = stockDao;
    }

    @Override
    public List<StockDto> getAllStock() {
        return stockDao.getAllStock();
    }

    @Override
    public Optional<StockDto> getStockById(Integer stockId) {
        return stockDao.getStockById(stockId);
    }

    @Override
    public Optional<StockDto> getStockByBarcode(String barcode) {
        return stockDao.getStockByBarcode(barcode);
    }

    @Override
    public List<StockDto> searchStock(String query) {
        return stockDao.searchStock(query);
    }

    @Override
    public List<StockDto> getStockByDept(Integer deptId) {
        return stockDao.getStockByDept(deptId);
    }

    @Override
    public StockDto createStock(StockDto stock) {
        return stockDao.createStock(stock);
    }

    @Override
    public boolean updateStock(Integer stockId, StockDto stock) {
        stock.setStockId(stockId);
        return stockDao.updateStock(stock);
    }

    @Override
    public boolean deleteStock(Integer stockId) {
        return stockDao.deleteStock(stockId);
    }
}