import { Component, OnInit } from '@angular/core';
import { ApiService } from '../services/api.service';
import { SearchResult } from '../services/search-result';

@Component({
  selector: 'app-search-form',
  templateUrl: './search-form.component.html',
  styleUrls: ['./search-form.component.css']
})
export class SearchFormComponent implements OnInit {

  serverResponse: string;

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

  handleResponse(response: SearchResult) {
    console.log(response);
    this.serverResponse = response.results;
  }

}
