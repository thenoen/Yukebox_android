import { Injectable } from '@angular/core';
import { Http, Response, RequestOptions, URLSearchParams } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { SearchResult } from './search-result'
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';

@Injectable()
export class ApiService {

  private apiUrl = 'api/this-part-does-not-matter';
  private searchUrl = 'api/search';
  private playUrl = 'api/play';

  constructor(private http: Http) { }

  getStringResponse(): Observable<String> {
    console.log("service running");
    return this.http.get(this.apiUrl)
      .map(this.extractData)
      .catch(this.handleError);
  }

  searchVideos(query: string): Observable<SearchResult> {

    let params: URLSearchParams = new URLSearchParams();
    params.set('query', query);

    let opts: RequestOptions = new RequestOptions();
    opts.search = params;

    return this.http.get(this.searchUrl, opts)
      .map(this.extractSearchResults)
      .catch(this.handleError);
  }

  startPlayback(videoId: string) {
    console.log("going to call start playback for video: "+ videoId);
    let params: URLSearchParams = new URLSearchParams();
    params.set('videoId', videoId);

    let opts: RequestOptions = new RequestOptions();
    opts.search = params;

    console.log("going to call start playback url");
    return this.http.get(this.playUrl, opts)
      .map(this.logResponse)
      .catch(this.handleError);
  }

  private logResponse(res: Response) {
    console.log(res);
  }

  private extractSearchResults(res: Response) {
    let responseBody = res.json();
    console.log(responseBody);
    return responseBody;
  }

  private extractData(res: Response) {
    console.log("about to extract data...");
    console.log(res);
    let body = res.json();
    console.log(body.data);
    let tmpResponse = body.data || {};
    console.log(tmpResponse);
    return tmpResponse
  }

  private handleError(error: Response | any) {
    // In a real world app, you might use a remote logging infrastructure
    let errMsg: string;
    if (error instanceof Response) {
      const body = error.json() || '';
      const err = body.error || JSON.stringify(body);
      errMsg = `${error.status} - ${error.statusText || ''} ${err}`;
    } else {
      errMsg = error.message ? error.message : error.toString();
    }
    console.error(errMsg);
    return Observable.throw(errMsg);
  }

}
