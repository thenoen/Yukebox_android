import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule, JsonpModule } from '@angular/http';
import { ApiService } from './services/api.service';


import { AppComponent } from './app.component';
import { SearchFormComponent } from './search-form/search-form.component';
import { VideoResult } from './video-result/video-result.component';

@NgModule({
  declarations: [
    AppComponent,
    SearchFormComponent,
    VideoResult
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    JsonpModule
  ],
  providers: [ApiService],
  bootstrap: [AppComponent]
})
export class AppModule { }
