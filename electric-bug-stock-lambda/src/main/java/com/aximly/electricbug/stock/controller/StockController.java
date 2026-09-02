package com.aximly.electricbug.stock.controller;

import com.aximly.electricbug.stock.service.StockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/stock")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping
    public ResponseEntity<?> getAllStock() {
        return ResponseEntity.ok(stockService.getAllStock());
    }

    @GetMapping("/{stockId}")
    public ResponseEntity<?> getStockById(@PathVariable Integer stockId) {
        return stockService.getStockById(stockId)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/barcode/{barcode}")
    public ResponseEntity<?> getStockByBarcode(@PathVariable String barcode) {
        return stockService.getStockByBarcode(barcode)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam String q) {
        return ResponseEntity.ok(stockService.searchStock(q));
    }

    @GetMapping("/dept/{deptId}")
    public ResponseEntity<?> getByDept(@PathVariable Integer deptId) {
        return ResponseEntity.ok(stockService.getStockByDept(deptId));
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "electric-bug-stock-lambda"));
    }
}