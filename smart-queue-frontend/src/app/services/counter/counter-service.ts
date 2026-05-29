import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { TicketCalledResponse } from '../ticket/ticket-called-response';

@Injectable({
  providedIn: 'root',
})
export class CounterService {
  
  private apiUrl = `http://localhost:8080`;
  private httpClient;

  constructor(httpClient: HttpClient){
    this.httpClient = httpClient;
  }

  createNewCounter(): Observable<number>{
    return this.httpClient.post<number>(`${this.apiUrl}/counter/new`, null);
  }

  changeCounterStatus(counter: string, status: string): Observable<void>{
    return this.httpClient.put<void>(`${this.apiUrl}/${counter}/status`, null, {params: {status}});
  }

  callNextTicket(counter: string): Observable<TicketCalledResponse>{
    return this.httpClient.post<TicketCalledResponse>(`${this.apiUrl}/counter/${counter}/call`, null);
  }

  finishTicket(counter: string, ticket: string): Observable<void>{
    return this.httpClient.put<void>(`${this.apiUrl}/counter/${counter}/finish`, null, {params: {ticket}})
  }


}
