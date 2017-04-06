import { Component, OnInit } from '@angular/core';
import { Video } from '../domain/Video';

@Component({
    selector: "video-component",
    inputs: ['video'],
    templateUrl: './video-result.component.html',
    styleUrls: ['./video-result.component.css']
})
export class VideoResult {
    video: Video;
}