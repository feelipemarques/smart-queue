import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { TicketResponse } from './ticket-response';
import { Observable } from 'rxjs';
import { WebsocketService } from '../websocket/websocket-service';

@Injectable({
  providedIn: 'root',
})
export class TicketService {
  
  private apiUrl = `http://localhost:8080`;
  
  constructor(private httpClient: HttpClient, private webSocketService: WebsocketService) {
  }
  
  postNewTicket(prefix: string): Observable<TicketResponse>{
    return this.httpClient.post<TicketResponse>(`${this.apiUrl}/tickets`, null, {params: {prefix}});
  }

  connectToWebSocket(){
    this.webSocketService.connect();
  }

  subscribeToQueue(): Observable<any>{
    return this.webSocketService.subscribe(`/topic/queue`);
  }

  subscribeToTicket(number: string): Observable<any>{
    return this.webSocketService.subscribe(`/topic/ticket/${number}`);
  }

  getAllTickets(): Observable<TicketResponse[]>{
    return this.httpClient.get<TicketResponse[]>(`${this.apiUrl}/tickets`);
  }

  getStatusByTicket(ticket: string): Observable<string>{
    return this.httpClient.get<string>(`${this.apiUrl}/tickets/${ticket}`);
  }

}
