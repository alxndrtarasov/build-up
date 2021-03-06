package com.innteam.buildup.rest;

import com.innteam.buildup.commons.model.ContentCompleteRequest;
import com.innteam.buildup.commons.model.ContentType;
import com.innteam.buildup.commons.model.FinishAllRequest;
import com.innteam.buildup.commons.model.PointProgressRequest;
import com.innteam.buildup.commons.model.paper.Content;
import com.innteam.buildup.commons.model.progress.Progress;
import com.innteam.buildup.commons.model.progress.ProgressCrudService;
import com.innteam.buildup.commons.model.roadFolders.PointCrudService;
import com.innteam.buildup.commons.model.roadFolders.RoadPoint;
import com.innteam.buildup.commons.model.user.PaperActivityStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class TrackWebApi {


    private PointCrudService pointCrudService;
    private ProgressCrudService progressCrudService;

    @Autowired
    public TrackWebApi(PointCrudService pointCrudService,
                       ProgressCrudService progressCrudService) {
        this.pointCrudService = pointCrudService;
        this.progressCrudService = progressCrudService;
    }

    @GetMapping("/roadMap")
    public List<RoadPoint> roadList(@RequestParam String username) {
        return pointCrudService.getMock(); //TODO make return folder by username
    }

    @GetMapping("/content")
    public List<Content> content(@RequestParam ContentType contentType, @RequestParam String pointId) {
        RoadPoint roadPoint = pointCrudService.read(UUID.fromString(pointId)); //TODO make return folder by folderId
        return roadPoint.getContents().stream()
                .filter(content -> content.getContentType().equals(contentType))
                .collect(Collectors.toList());
    }

    @GetMapping("/content/progress")
    public Progress contentProgress(String contentId, String userId) {
        return progressCrudService.getProgressFor(UUID.fromString(userId), UUID.fromString(contentId));
    }

    @Getter
    @RequiredArgsConstructor
    private static class Result {
        private final double result;
    }

    @GetMapping("/point/progress")
    public Result pointProgress(PointProgressRequest request) {
        String pointId = request.getPointId();
        String userId = request.getUserId();
        final RoadPoint point = pointCrudService.read(UUID.fromString(pointId));
        final UUID userUUID = UUID.fromString(userId);



        return new Result(point.getContents().stream().mapToDouble(c -> {
            final Progress progress = progressCrudService.getProgressFor(userUUID, c.getId());
            return progress.getCompletion();
        }).average().orElseThrow(() -> new IllegalArgumentException("Could not calculate progress for point")));
    }

    @PostMapping("/inProgress")
    public ResponseEntity contentProgress(@RequestBody ContentCompleteRequest request) {
        return changeProgressStatus(request, PaperActivityStatus.IN_PROGRESS);
    }

    @PatchMapping("/skip")
    public ResponseEntity skip(@RequestBody ContentCompleteRequest request) {
        return changeProgressStatus(request, PaperActivityStatus.DONE);
    }

    @PatchMapping("/finish")
    public ResponseEntity finish(@RequestBody ContentCompleteRequest request) {
        return changeProgressStatus(request, PaperActivityStatus.DONE);
    }

    @PatchMapping("/finishAll")
    public ResponseEntity finishAll(@RequestBody FinishAllRequest request) {
        for (String paper : request.getPaperIds()) {
            changeProgressStatus(new ContentCompleteRequest(paper, request.getUserId(), request.getTime()), PaperActivityStatus.DONE);
        }
        return ResponseEntity.ok().build();
    }

    private ResponseEntity changeProgressStatus(@RequestBody ContentCompleteRequest request, PaperActivityStatus status) {
        final String userId = request.getUserId();
        final String contentId = request.getContentId();
        final Progress progress = progressCrudService.getProgressFor(UUID.fromString(userId), UUID.fromString(contentId));
        switch (status) {
            case DONE:
            case SKIPPED:
                progress.setCompletion(1d);
                break;
            case IN_PROGRESS:
                break;
        }
        progress.setStatus(status);
        progressCrudService.update(progress);

        return ResponseEntity.ok().build();
    }
}
