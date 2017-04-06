import { Component, OnInit } from '@angular/core';
import { ApiService } from '../services/api.service';
import { SearchResponse } from '../domain/SearchResponse';
import { Video } from '../domain/Video';

@Component({
  selector: 'app-search-form',
  templateUrl: './search-form.component.html',
  styleUrls: ['./search-form.component.css']
})
export class SearchFormComponent implements OnInit {

  serverResponse: SearchResponse;
  videoResults: Video[];

  constructor(private apiService: ApiService) { }

  ngOnInit() {
  }

  sendSearchRequest(textInputElement: HTMLInputElement): boolean {
    let inputString: string = textInputElement.value;

    this.apiService.searchVideos(inputString).subscribe(x => this.handleResponse(x));

    console.log(inputString);
    // this.serverResponse = inputString;
    return false;
  }

  startPlayback(video: Video) {
    console.log("clicked");
    this.apiService.startPlayback(video.videoId).subscribe(x => this.logResponse(x));
  }

  private logResponse(response: any) {
    console.log(response);
  }

  handleResponse(response: SearchResponse) {
    console.log("search component response:" + response);
    this.serverResponse = response;
    this.videoResults = response.results;
  }

}
