package com.aximly.electricbug.stock.controller;

import com.aximly.electricbug.stock.service.StockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/stock")
@Tag(name = "Stock", description = "Stock lookup, synced from AAA POS")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping
    @Operation(summary = "Get all stock items")
    public ResponseEntity<?> getAllStock() {
        return ResponseEntity.ok(stockService.getAllStock());
    }

    @GetMapping("/{stockId}")
    @Operation(summary = "Get a stock item by ID")
    public ResponseEntity<?> getStockById(@PathVariable Integer stockId) {
        return stockService.getStockById(stockId)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/barcode/{barcode}")
    @Operation(summary = "Get a stock item by barcode")
    public ResponseEntity<?> getStockByBarcode(@PathVariable String barcode) {
        return stockService.getStockByBarcode(barcode)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    @Operation(summary = "Search stock by description or barcode")
    public ResponseEntity<?> search(@RequestParam String q) {
        return ResponseEntity.ok(stockService.searchStock(q));
    }

    @GetMapping("/dept/{deptId}")
    @Operation(summary = "Get stock items for a department")
    public ResponseEntity<?> getByDept(@PathVariable Integer deptId) {
        return ResponseEntity.ok(stockService.getStockByDept(deptId));
    }

    @GetMapping("/health")
    @Operation(summary = "Health check")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "electric-bug-stock-lambda"));
    }
}