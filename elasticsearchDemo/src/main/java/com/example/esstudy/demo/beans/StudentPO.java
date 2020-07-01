package com.example.esstudy.demo.beans;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;
import java.util.Date;

/**
 * @author grafie
 * @date 2020年7月1日 17点00分
 */
@Getter
@Setter
@ToString
@Document(indexName = "user")
public class StudentPO implements Serializable {
    private String name;
    private Integer age;
    @JSONField(format = "yyyy-MM-dd")
    private Date time;
}
