import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule, JsonpModule } from '@angular/http';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { ApiService } from './services/api.service';
import { AppComponent } from './app.component';
import { SearchFormComponent } from './search-form/search-form.component';
import { NgbdTabsetConfig } from './tabset/tabset.component'
import { VideoResult } from './video-result/video-result.component';


import { InfiniteScrollModule } from 'ngx-infinite-scroll';

@NgModule({
  declarations: [
    AppComponent,
    SearchFormComponent,
    NgbdTabsetConfig,
    VideoResult
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    JsonpModule,
    NgbModule.forRoot(),
    InfiniteScrollModule
  ],
  providers: [ApiService],
  bootstrap: [AppComponent]
})
export class AppModule { }
