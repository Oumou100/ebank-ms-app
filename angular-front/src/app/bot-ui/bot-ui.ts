import { HttpClient } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { Observable } from 'rxjs';

@Component({
  imports: [],
  selector: 'app-bot-ui',
  styleUrl: './bot-ui.css',
  templateUrl: './bot-ui.html',
})
export class BotUi {
  query: any;
  http = inject(HttpClient);
  response$! : Observable<any>

  askAgent() {
    this.response$ = this.http.get("http://localhost:9999/EBANK-BOT/bot?query=" + this.query,
       { responseType: 'text' });
  }
}
