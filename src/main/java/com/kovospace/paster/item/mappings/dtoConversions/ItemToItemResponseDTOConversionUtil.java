package com.kovospace.paster.item.mappings.dtoConversions;

import com.kovospace.paster.item.models.Item;
import org.springframework.beans.factory.annotation.Value;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ItemToItemResponseDTOConversionUtil {

    @Value("${item.text-preview-length}")
    private static final int PREVIEW_TEXT_LIMIT = 512;

    @Value("#{'${item.extensions-with-preview}'.split(',')}")
    private static Set<String> allowedPreviewExtensions = new HashSet<>();

    private static final String TRUNCATE_SYMBOL = "...";

    private final Item item;
    private boolean isFile;
    private boolean isCompleteFile;
    private boolean isFileWithPreview;
    private boolean isText;
    private boolean isLongText;
    private long timestamp;
    private String deviceName;
    private String previewText;

    public ItemToItemResponseDTOConversionUtil(Item item) {
        this.item = item;
        init();
    }

    private void init() {
        setIsFile();
        setIsCompleteFile();
        setIsText();
        setIsLongText();
        setPreviewText();
        setTimestamp();
        setDeviceName();
        setIsFileWithPreview();
    }

    public boolean isFile() {
        return isFile;
    }

    public boolean isCompleteFile() {
        return isCompleteFile;
    }

    public boolean isFileWithPreview() {
        return isFileWithPreview;
    }

    public boolean isText() {
        return isText;
    }

    public boolean isLongText() {
        return isLongText;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getPreviewText() {
        return previewText;
    }

    /** Setters */

    private void setIsFile() {
        isFile = item.getFile() != null;
    }

    private void setIsCompleteFile() {
        isCompleteFile = isFile && item.getData() != null && !item.getData().trim().equals("");
    }

    private void setIsFileWithPreview() {
        isFileWithPreview = isCompleteFile && Optional.ofNullable(item.getFile().getExtension())
                .map(ext -> allowedPreviewExtensions.contains(ext.toLowerCase()))
                .orElse(false);
    }

    private void setIsText () {
        isText = !isFile;
    }

    private void setIsLongText() {
        isLongText = isText && item.getData().length() > PREVIEW_TEXT_LIMIT;
    }

    private void setTimestamp() {
        timestamp = Optional.ofNullable(item.getCreatedAt())
                .map(Timestamp::getTime)
                .orElse(0L);
    }

    private void setDeviceName() {
        //TODO maybe some default name for device if unknown
        deviceName = item.getDeviceName() == null ? "" : item.getDeviceName();
    }

    private void setPreviewText() {
        //if (isFile) return;
        previewText = Optional.ofNullable(item.getData())
                .map(txt -> txt.length() > PREVIEW_TEXT_LIMIT
                        ? String.format("%s%s", txt.substring(0, PREVIEW_TEXT_LIMIT), TRUNCATE_SYMBOL)
                        : txt)
                .orElse(null);
    }
}
