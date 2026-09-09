package com.aximly.electricbug.job.dao.impl;

import com.aximly.electricbug.job.dao.JobSheetDao;
import com.aximly.electricbug.job.dto.JobSheetDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PostgresJobSheetDaoImpl implements JobSheetDao {

    private final DataSource cloudDataSource;

    public PostgresJobSheetDaoImpl(@Qualifier("cloudDataSource") DataSource cloudDataSource) {
        this.cloudDataSource = cloudDataSource;
    }

    private JobSheetDto mapRow(ResultSet rs) throws SQLException {
        JobSheetDto dto = new JobSheetDto();
        dto.setJobId(rs.getInt("job_id"));
        dto.setJobCode(rs.getString("job_code"));
        dto.setCompanyName(rs.getString("company_name"));
        dto.setFirstName(rs.getString("first_name"));
        dto.setLastName(rs.getString("last_name"));
        dto.setPhone(rs.getString("phone"));
        dto.setOrderType(rs.getString("order_type"));
        Object salesOrderId = rs.getObject("sales_order_id");
        dto.setSalesOrderId(salesOrderId != null ? rs.getInt("sales_order_id") : null);
        dto.setSalesOrderDisplay(rs.getString("sales_order_display"));
        Object laybyId = rs.getObject("layby_id");
        dto.setLaybyId(laybyId != null ? rs.getInt("layby_id") : null);
        dto.setLaybyDisplay(rs.getString("layby_display"));
        dto.setAddress(rs.getString("address"));
        dto.setRwdRep(rs.getString("rwd_rep"));
        dto.setRwdNotify(rs.getBoolean("rwd_notify"));
        dto.setTape(rs.getBoolean("tape"));
        dto.setInstallerComments(rs.getString("installer_comments"));
        dto.setFittingShelf(rs.getBoolean("fitting_shelf"));
        dto.setAdditionalProducts(rs.getString("additional_products"));
        dto.setProductTotal(rs.getDouble("product_total"));
        dto.setInstallationCost(rs.getDouble("installation_cost"));
        dto.setEstimatedTotal(rs.getDouble("estimated_total"));
        dto.setDepositNumber(rs.getString("deposit_number"));
        dto.setPaidInvoiceNumber(rs.getString("paid_invoice_number"));
        dto.setStatus(rs.getString("status"));
        dto.setMissingStock(rs.getBoolean("missing_stock"));
        Date completedDate = rs.getDate("job_completed_date");
        dto.setJobCompletedDate(completedDate != null ? completedDate.toLocalDate() : null);
        Timestamp createdAt = rs.getTimestamp("created_at");
        dto.setCreatedAt(createdAt != null ? createdAt.toInstant().atOffset(java.time.ZoneOffset.UTC) : null);
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        dto.setUpdatedAt(updatedAt != null ? updatedAt.toInstant().atOffset(java.time.ZoneOffset.UTC) : null);
        return dto;
    }

    @Override
    public List<JobSheetDto> getAllJobs() {
        List<JobSheetDto> list = new ArrayList<>();
        try (Connection conn = cloudDataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM job_sheets ORDER BY job_id DESC")) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read job sheets from Postgres: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public Optional<JobSheetDto> getJobById(Integer jobId) {
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM job_sheets WHERE job_id = ?")) {
            ps.setInt(1, jobId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read job sheet from Postgres: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<JobSheetDto> getJobsByStatus(String status) {
        List<JobSheetDto> list = new ArrayList<>();
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM job_sheets WHERE status = ? ORDER BY job_id DESC")) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read job sheets by status from Postgres: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public JobSheetDto createJob(JobSheetDto job) {
        String sql = "INSERT INTO job_sheets (job_code, company_name, first_name, last_name, phone, " +
                "order_type, sales_order_id, sales_order_display, layby_id, layby_display, address, " +
                "rwd_rep, rwd_notify, tape, installer_comments, fitting_shelf, additional_products, " +
                "product_total, installation_cost, deposit_number, paid_invoice_number, status, " +
                "missing_stock, job_completed_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            bindWriteParams(ps, job);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) job.setJobId(keys.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert job sheet into Postgres: " + e.getMessage(), e);
        }
        return job;
    }

    @Override
    public boolean updateJob(JobSheetDto job) {
        String sql = "UPDATE job_sheets SET job_code = ?, company_name = ?, first_name = ?, last_name = ?, " +
                "phone = ?, order_type = ?, sales_order_id = ?, sales_order_display = ?, layby_id = ?, " +
                "layby_display = ?, address = ?, rwd_rep = ?, rwd_notify = ?, tape = ?, installer_comments = ?, " +
                "fitting_shelf = ?, additional_products = ?, product_total = ?, installation_cost = ?, " +
                "deposit_number = ?, paid_invoice_number = ?, status = ?, missing_stock = ?, " +
                "job_completed_date = ?, updated_at = now() WHERE job_id = ?";
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            int i = bindWriteParams(ps, job);
            ps.setInt(i, job.getJobId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update job sheet in Postgres: " + e.getMessage(), e);
        }
    }

    private int bindWriteParams(PreparedStatement ps, JobSheetDto job) throws SQLException {
        int i = 1;
        ps.setString(i++, job.getJobCode());
        ps.setString(i++, job.getCompanyName());
        ps.setString(i++, job.getFirstName());
        ps.setString(i++, job.getLastName());
        ps.setString(i++, job.getPhone());
        ps.setString(i++, job.getOrderType() != null ? job.getOrderType() : "other");
        if (job.getSalesOrderId() != null) ps.setInt(i++, job.getSalesOrderId()); else ps.setNull(i++, Types.INTEGER);
        ps.setString(i++, job.getSalesOrderDisplay());
        if (job.getLaybyId() != null) ps.setInt(i++, job.getLaybyId()); else ps.setNull(i++, Types.INTEGER);
        ps.setString(i++, job.getLaybyDisplay());
        ps.setString(i++, job.getAddress());
        ps.setString(i++, job.getRwdRep());
        ps.setBoolean(i++, job.getRwdNotify() != null && job.getRwdNotify());
        ps.setBoolean(i++, job.getTape() != null && job.getTape());
        ps.setString(i++, job.getInstallerComments());
        ps.setBoolean(i++, job.getFittingShelf() != null && job.getFittingShelf());
        ps.setString(i++, job.getAdditionalProducts());
        ps.setDouble(i++, job.getProductTotal() != null ? job.getProductTotal() : 0.0);
        ps.setDouble(i++, job.getInstallationCost() != null ? job.getInstallationCost() : 0.0);
        ps.setString(i++, job.getDepositNumber());
        ps.setString(i++, job.getPaidInvoiceNumber());
        ps.setString(i++, job.getStatus() != null ? job.getStatus() : "pending");
        ps.setBoolean(i++, job.getMissingStock() != null && job.getMissingStock());
        if (job.getJobCompletedDate() != null) ps.setDate(i++, Date.valueOf(job.getJobCompletedDate()));
        else ps.setNull(i++, Types.DATE);
        return i;
    }

    @Override
    public boolean deleteJob(Integer jobId) {
        try (Connection conn = cloudDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM job_sheets WHERE job_id = ?")) {
            ps.setInt(1, jobId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete job sheet from Postgres: " + e.getMessage(), e);
        }
    }
}