import { Component, OnInit } from '@angular/core';
import { ApiService } from './services/api.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  title = 'app works!';

  apiServerResponse;

  constructor(private apiService: ApiService) { }

  ngOnInit() {
    console.log("going to make request");
    // this.apiService.getStringResponse().subscribe();
    console.log("making request finished");
  }

}
