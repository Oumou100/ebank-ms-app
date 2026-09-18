import { HttpClient } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { AsyncPipe } from '@angular/common';
import { catchError, map, Observable, of } from 'rxjs';
import { Account, AccountListState, RequestStatus } from './model/account.model';

@Component({
  imports: [AsyncPipe],
  selector: 'app-accounts',
  styleUrl: './accounts.css',
  templateUrl: './accounts.html',
})
export class Accounts {
  readonly RequestStatus = RequestStatus;
  private http = inject(HttpClient);
  // constructor(private http: HttpClient) {} injection via constructeur necessite d'être dans une classe alors  que parfois on est dans une fonction, on est pas forcement dans une classe donc la solution restant est inject
  accounts$ : Observable<AccountListState>  = this.http.get<Account[]>('http://localhost:9999/EBANK-SERVICE/accounts')
  .pipe(
    map(resp => {
      return {accounts: resp, status: RequestStatus.SUCCESS}
    }),
    catchError((err) => {
      return of({ accounts: [], status: RequestStatus.ERROR, errorMessage: err.statusText });
    })
  )


}
