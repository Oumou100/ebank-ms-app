import { HttpClient, HttpDownloadProgressEvent, HttpEvent, HttpEventType } from '@angular/common/http';
import { AsyncPipe } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MarkdownComponent } from 'ngx-markdown';
import { map, Observable } from 'rxjs';
import { LoadingService } from '../services/loading';

@Component({
  imports: [AsyncPipe, FormsModule, MarkdownComponent],
  selector: 'app-bot-ui',
  styleUrl: './bot-ui.css',
  templateUrl: './bot-ui.html',
})
export class BotUi {
  query: any;
  http = inject(HttpClient);
  response$!: Observable<any>
  public loadingService = inject(LoadingService);

  askAgent() {
    this.response$ = this.http.get("http://localhost:9999/EBANK-BOT/chat?query=" + this.query,
      { responseType: 'text' });
  }

  askAgentStream() {
    const events$ = this.http.request('GET', "http://localhost:9999/EBANK-BOT/chatStream?query=" + this.query,
      { responseType: 'text', observe: 'events', reportProgress: true } as any) as unknown as Observable<HttpEvent<string>>;

    this.response$ = events$
      .pipe(map((event: HttpEvent<string>) => {
        switch (event.type) {
          case HttpEventType.Sent:
            return {'type': 'Sent'}
          case HttpEventType.DownloadProgress:
            return {'type': 'Response', content : (event as HttpDownloadProgressEvent).partialText}
          case HttpEventType.Response:
            return {'type': 'Response', 'body': event.body}
          default:
            return {'type': 'Other', data : event}
        }

      }))
      ;
  }
}
