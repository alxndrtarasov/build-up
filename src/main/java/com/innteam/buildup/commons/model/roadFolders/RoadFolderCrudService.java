package com.innteam.buildup.commons.model.roadFolders;

import com.innteam.buildup.commons.model.ContentType;
import com.innteam.buildup.commons.model.DomainObjectCrudServiceBase;
import com.innteam.buildup.commons.model.paper.Content;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Scope;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
@Scope("singleton")
public class RoadFolderCrudService extends DomainObjectCrudServiceBase<RoadFolder> {

    public RoadFolderCrudService(PagingAndSortingRepository<RoadFolder, UUID> repo) {
        super(repo);
    }

}
