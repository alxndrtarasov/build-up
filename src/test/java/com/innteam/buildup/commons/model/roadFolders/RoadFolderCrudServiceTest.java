package com.innteam.buildup.commons.model.roadFolders;

import com.innteam.buildup.commons.model.ContentType;
import com.innteam.buildup.commons.model.paper.Content;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class RoadFolderCrudServiceTest {
    @Autowired
    RoadFolderCrudService service;

    @Test
    public void test() {
        final RoadFolder roadFolder1 = new RoadFolder("SE", Set.of(new Content(ContentType.BOOK, "sePaper", "https://github.com/se-edu/se-book", 100000)), Collections.emptyList());
        final RoadFolder roadFolder2 = new RoadFolder("DB", Set.of(new Content(ContentType.BOOK, "dbPaper", "https://bookauthority.org/books/best-databases-books", 100000)), Collections.emptyList());
        ArrayList<RoadFolder> roadFolders = new ArrayList<>();
        roadFolders.add(roadFolder1);
        roadFolders.add(roadFolder2);
        final RoadFolder roadFolder = new RoadFolder("Java", Set.of(
                new Content(ContentType.BOOK, "javaPaper",
                        "https://github.com/muhdkhokhar/test/blob/master/Joshua%20Bloch%20-%20Effective%20Java%20(3rd)%20-%202018.pdf",
                        100000)),
                roadFolders);
        final UUID uuid = service.create(roadFolder);

        final RoadFolder folder = service.read(uuid);
        System.out.println();
    }
}