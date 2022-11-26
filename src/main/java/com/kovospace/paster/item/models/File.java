package com.kovospace.paster.item.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@Getter
@Setter
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /** without extension */
    private String fileName;

    @OneToOne(mappedBy = "file", fetch = FetchType.EAGER)
    private Item item;

    private String originalFileName;
    private Long chunksCount;
    private Long chunkNumber;
    private Long chunkSize;
    private String mimeType;
    private String extension;
    private String filePath;

    public String getFullFileName() {
        return extension == null
                ? filePath
                : String.format("%s.%s", fileName, extension);
    }

}
