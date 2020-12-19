package com.rolf.advent2019.day08;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Image {

    private final int width;
    private final int height;
    private final List<List<Integer>> layers = new ArrayList<>();

    public Image(final int width, final int height) {
        this.width = width;
        this.height = height;
    }

    public int getPixels() {
        return width * height;
    }

    public void addPixels(final String input) {
        List<Integer> layer = new ArrayList<>();
        for (int i = 0; i < input.length(); i++) {
            if (i % getPixels() == 0) {
                if (!layer.isEmpty()) {
                    layers.add(layer);
                }
                layer = new ArrayList<>();
            }
            layer.add(Integer.parseInt(input.charAt(i) + ""));
        }
        if (!layer.isEmpty()) {
            layers.add(layer);
        }
    }

    public int getIndexWithFewestZeros() {
        int lowestLayerIndex = 0;
        long lowestZeros = Long.MAX_VALUE;
        for (int i = 0; i < layers.size(); i++) {
            final long zeroCount = count(i, 0);
            if (zeroCount < lowestZeros) {
                lowestLayerIndex = i;
                lowestZeros = zeroCount;
            }
        }
        return lowestLayerIndex;
    }

    public long count(final int layerIndex, final int value) {
        return layers.get(layerIndex).stream()
                .filter(v -> v == value)
                .count();
    }

    public Image flatten() {
        final List<Integer> flatten = new ArrayList<>(getPixels());
        for (int i = 0; i < getPixels(); i++) {
            final int value = getValueAt(i);
            flatten.add(value);
        }
        final Image image = new Image(width, height);
        image.addPixels(flatten.stream()
                .map(i -> i + "")
                .collect(Collectors.joining()));
        return image;
    }

    private int getValueAt(final int i) {
        int lastValue = 0;
        for (final List<Integer> layer : layers) {
            lastValue = layer.get(i);
            // 2=transparent, so ignore
            if (lastValue != 2) {
                return lastValue;
            }
        }
        return lastValue;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("\n");
        for (final List<Integer> layer : layers) {
            for (int h = 0; h < height; h++) {
                for (int w = 0; w < width; w++) {
                    final int value = layer.get(h * width + w);
                    sb.append(value == 1 ? "#" : " ");
                }
                sb.append("\n");
            }
            sb.append("\n");
            sb.append("\n");
        }
        return sb.toString();
    }
}
