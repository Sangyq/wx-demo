package com.example.demo.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Administrator
 */
@Data
public class MaterialDTO implements Serializable {
    private String filePath;
    private String type;
    private String title;
    private String introduction;
}
