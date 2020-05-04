package com.innteam.buildup.commons.model.paper;

import com.innteam.buildup.commons.model.ContentType;
import com.innteam.buildup.commons.model.DomainObject;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = Content.TABLE_NAME)
public class Content extends DomainObject {
    static final String TABLE_NAME = "contents";
    private ContentType contentType;
    private String name;
    private String description;

    private String link;
    private int timeInSeconds;
}
