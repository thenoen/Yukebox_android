import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-search-form',
  templateUrl: './search-form.component.html',
  styleUrls: ['./search-form.component.css']
})
export class SearchFormComponent implements OnInit {

  serverResponse: string;

  constructor() { }

  ngOnInit() {
  }

  sendSearchRequest(textInputElement: HTMLInputElement): boolean {
    let inputString: string = textInputElement.value;
    console.log(inputString);
    this.serverResponse = inputString;
    return false;
  }

}
