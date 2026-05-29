import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { TicketResponse } from './ticket-response';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class TicketService {
  
  private apiUrl = `http://localhost:8080`;
  private httpClient;
  
  constructor(httpClient: HttpClient) {
    this.httpClient = httpClient;
  }
  
  postNewTicket(prefix: string): Observable<TicketResponse>{
    return this.httpClient.post<TicketResponse>(`${this.apiUrl}/tickets`, null, {params: {prefix}});
  }

  getAllTickets(): Observable<TicketResponse[]>{
    return this.httpClient.get<TicketResponse[]>(`${this.apiUrl}/tickets`);
  }

  getStatusByTicket(ticket: string): Observable<string>{
    return this.httpClient.get<string>(`${this.apiUrl}/tickets/${ticket}`);
  }

}
