import { HttpClient } from '@angular/common/http';
import { AsyncPipe } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MarkdownComponent } from 'ngx-markdown';
import { Observable } from 'rxjs';
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
  response$! : Observable<any>
  public loadingService  = inject(LoadingService);

  askAgent() {
    this.response$ = this.http.get("http://localhost:9999/EBANK-BOT/chat?query=" + this.query,
       { responseType: 'text' });
  }
}
