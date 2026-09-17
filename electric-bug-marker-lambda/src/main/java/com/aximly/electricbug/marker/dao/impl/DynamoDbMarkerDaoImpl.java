package com.aximly.electricbug.marker.dao.impl;

import com.aximly.electricbug.marker.dao.JobInstallationMarkerDao;
import com.aximly.electricbug.marker.dto.JobInstallationMarkerDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class DynamoDbMarkerDaoImpl implements JobInstallationMarkerDao {

    private final DynamoDbClient dynamoDbClient;

    @Value("${markers.table.name}")
    private String tableName;

    public DynamoDbMarkerDaoImpl(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    private JobInstallationMarkerDto mapItem(Map<String, AttributeValue> item) {
        JobInstallationMarkerDto dto = new JobInstallationMarkerDto();
        dto.setJobId(Integer.parseInt(item.get("job_id").s()));
        dto.setMarkerId(item.get("marker_id").s());
        dto.setMarkerType(item.get("marker_type").s());
        dto.setLabel(item.containsKey("label") ? item.get("label").s() : null);
        dto.setNotes(item.containsKey("notes") ? item.get("notes").s() : null);
        dto.setPosX(Double.parseDouble(item.get("pos_x").n()));
        dto.setPosY(Double.parseDouble(item.get("pos_y").n()));
        return dto;
    }

    @Override
    public List<JobInstallationMarkerDto> getMarkersForJob(Integer jobId) {
        QueryResponse response = dynamoDbClient.query(QueryRequest.builder()
                .tableName(tableName)
                .keyConditionExpression("job_id = :jobId")
                .expressionAttributeValues(Map.of(
                        ":jobId", AttributeValue.builder().s(jobId.toString()).build()))
                .build());

        return response.items().stream().map(this::mapItem).collect(Collectors.toList());
    }

    @Override
    public Optional<JobInstallationMarkerDto> getMarker(Integer jobId, String markerId) {
        Map<String, AttributeValue> key = Map.of(
                "job_id", AttributeValue.builder().s(jobId.toString()).build(),
                "marker_id", AttributeValue.builder().s(markerId).build());

        GetItemResponse response = dynamoDbClient.getItem(GetItemRequest.builder()
                .tableName(tableName)
                .key(key)
                .build());

        if (!response.hasItem() || response.item().isEmpty()) return Optional.empty();
        return Optional.of(mapItem(response.item()));
    }

    @Override
    public JobInstallationMarkerDto createMarker(JobInstallationMarkerDto marker) {
        String markerId = UUID.randomUUID().toString();
        marker.setMarkerId(markerId);
        putItem(marker);
        return marker;
    }

    @Override
    public boolean updateMarker(JobInstallationMarkerDto marker) {
        // Confirm the item exists first, so "update" doesn't silently create a new one
        Optional<JobInstallationMarkerDto> existing = getMarker(marker.getJobId(), marker.getMarkerId());
        if (existing.isEmpty()) return false;
        putItem(marker);
        return true;
    }

    private void putItem(JobInstallationMarkerDto marker) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("job_id", AttributeValue.builder().s(marker.getJobId().toString()).build());
        item.put("marker_id", AttributeValue.builder().s(marker.getMarkerId()).build());
        item.put("marker_type", AttributeValue.builder().s(marker.getMarkerType()).build());
        item.put("label", AttributeValue.builder().s(marker.getLabel() != null ? marker.getLabel() : "").build());
        item.put("notes", AttributeValue.builder().s(marker.getNotes() != null ? marker.getNotes() : "").build());
        item.put("pos_x", AttributeValue.builder().n(String.valueOf(marker.getPosX())).build());
        item.put("pos_y", AttributeValue.builder().n(String.valueOf(marker.getPosY())).build());

        dynamoDbClient.putItem(PutItemRequest.builder()
                .tableName(tableName)
                .item(item)
                .build());
    }

    @Override
    public boolean deleteMarker(Integer jobId, String markerId) {
        Map<String, AttributeValue> key = Map.of(
                "job_id", AttributeValue.builder().s(jobId.toString()).build(),
                "marker_id", AttributeValue.builder().s(markerId).build());

        DeleteItemResponse response = dynamoDbClient.deleteItem(DeleteItemRequest.builder()
                .tableName(tableName)
                .key(key)
                .returnValues(ReturnValue.ALL_OLD)
                .build());

        return response.attributes() != null && !response.attributes().isEmpty();
    }

    @Override
    public void deleteAllForJob(Integer jobId) {
        List<JobInstallationMarkerDto> markers = getMarkersForJob(jobId);
        for (JobInstallationMarkerDto marker : markers) {
            deleteMarker(jobId, marker.getMarkerId());
        }
    }
}