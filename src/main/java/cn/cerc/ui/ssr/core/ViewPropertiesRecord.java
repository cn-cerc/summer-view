package cn.cerc.ui.ssr.core;

import java.util.Optional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import cn.cerc.db.core.Utils;

public record ViewPropertiesRecord(ObjectNode properties) {

    public Optional<Double> left() {
        return readProperties("v_left").map(JsonNode::asDouble);
    }

    public Optional<Double> right() {
        return readProperties("v_right").map(JsonNode::asDouble);
    }

    public Optional<Double> top() {
        return readProperties("v_top").map(JsonNode::asDouble);
    }

    public Optional<Double> bottom() {
        return readProperties("v_bottom").map(JsonNode::asDouble);
    }

    public Optional<Double> width() {
        return readProperties("v_width").map(JsonNode::asDouble);
    }

    public Optional<Double> height() {
        return readProperties("v_height").map(JsonNode::asDouble);
    }

    public Optional<JsonNode> readProperties(String key) {
        JsonNode json = properties.get(key);
        if (json == null)
            return Optional.empty();
        if (Utils.isEmpty(json.asText()))
            return Optional.empty();
        return Optional.of(json);
    }

}