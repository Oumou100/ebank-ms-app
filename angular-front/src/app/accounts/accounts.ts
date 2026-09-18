import { HttpClient } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { AsyncPipe } from '@angular/common';

@Component({
  imports: [AsyncPipe],
  selector: 'app-accounts',
  styleUrl: './accounts.css',
  templateUrl: './accounts.html',
})
export class Accounts {
  private http = inject(HttpClient);
  // constructor(private http: HttpClient) {} injection via constructeur necessite d'être dans une classe alors  que parfois on est dans une fonction, on est pas forcement dans une classe donc la solution restant est inject
  accounts$ = this.http.get<any[]>('http://localhost:9999/EBANK-SERVICE/accounts')


}
