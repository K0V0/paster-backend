package com.kovospace.paster.item.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@Getter
@Setter
public class File {

    @Id
    private String fileId;

    @OneToOne(mappedBy = "file")
    private Item item;

    private String filePath;
    private String fileName;
    private long chunksCount;
    private long chunkNumber;
    private long chunkSize;
    private String mimeType;
    private String extension;
}
