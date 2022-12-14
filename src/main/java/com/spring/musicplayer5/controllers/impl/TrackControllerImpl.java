package com.spring.musicplayer5.controllers.impl;

import com.spring.musicplayer5.controllers.TrackController;
import com.spring.musicplayer5.dto.ResponseObject;
import com.spring.musicplayer5.entity.Track;
import com.spring.musicplayer5.services.TrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/track")
public class TrackControllerImpl implements TrackController {

    @Autowired
    private TrackService trackService;

    @Override
    @GetMapping
    public ResponseEntity<ResponseObject> findAll() {
        Page<Track> tracks = trackService.findAll(PageRequest.of(0, 100));
        return ResponseEntity.ok(
                new ResponseObject("OK", "Get Data Track Successfully!" ,tracks.toList(),  tracks.toList().size())
        );
    }
    //Repair
    @Override
    @GetMapping("/get_id")
    public ResponseEntity<ResponseObject> getById(@RequestParam long id) {
        Optional<Track> track = trackService.findById(id);
        if(track.isPresent())
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Get Data Track Successfully!" , track.get())
            );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("FAILED", "Cannot found Track Failed!" , "ERROR!")
        );
    }

    @Override
    @GetMapping("/search")
    public ResponseEntity<ResponseObject> getByTitle(@RequestParam String title) {
        Page<Track> tracks = trackService.findByTitle(title , PageRequest.of(0 , 5));
        if(!tracks.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Search Track Successfully!" , tracks.toList(), tracks.getSize())
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("FAILED", "Cannot found Track Failed!" , null)
        );
    }

    //Code Pagination API : list of Tracks
    @Override
    @GetMapping("/paging")
    public ResponseEntity<ResponseObject> getTracksPage(@RequestParam Integer page, @RequestParam Integer size) {
        Page<Track> tracks = trackService.findAll(PageRequest.of(page , size));
        if(!tracks.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Get page list of Tracks Successfully!" , tracks.toList())
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("FAILED", "Cannot paging for Track is Failed!" , "ERROR")
        );
    }

    @Override
    @GetMapping("/get_top")
    public ResponseEntity<ResponseObject> getTopTrack(@RequestParam Integer size) {
        List<Track> tracks = trackService.findByTop(PageRequest.of(0 , size));
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Get Top "+size+" list of Tracks Successfully!" , tracks)
        );
    }
}
