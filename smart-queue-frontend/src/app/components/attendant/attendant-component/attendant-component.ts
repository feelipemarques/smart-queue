import { Component } from '@angular/core';
import { CounterService } from '../../../services/counter/counter-service';
import { TicketService } from '../../../services/ticket/ticket-service';
import { TicketResponse } from '../../../services/ticket/ticket-response';
import { WaitTimePipe } from '../../../pipes/wait-time-pipe';

@Component({
  selector: 'app-attendant-component',
  imports: [WaitTimePipe],
  templateUrl: './attendant-component.html',
  styleUrl: './attendant-component.css',
})
export class AttendantComponent {

  private counterService: CounterService;
  private ticketService: TicketService;
  
  counterId: string = '';
  counterStatus: boolean = false;
  tickets: TicketResponse[] = [];
  isInService: boolean = false;
  currentTicket: string = '';
  errorMessage = '';

  constructor(counterService: CounterService, ticketService: TicketService){
    this.counterService = counterService;
    this.ticketService = ticketService;
  }

  ngOnInit(){
    this.ticketService.connectToWebSocket();
    if(localStorage.getItem("counterId")){
      this.counterId = localStorage.getItem("counterId")!;
      this.currentTicket = localStorage.getItem("currentTicket")!;
      this.isInService = localStorage.getItem("isInService")?.match("true") ? true : false;
      this.counterService.getCurrentStatus(this.counterId).subscribe(response => { this.counterStatus = response});
    }else{
      this.counterId = '';
    }
    this.refreshTickets();
    this.ticketService.subscribeToQueue().subscribe(event => {
      console.log(event);
      console.log(this.tickets);
      this.tickets = event;
    });
  }

  openCounter(){
    this.counterService.createNewCounter().subscribe(response =>{
        this.counterId = response.id;
        localStorage.setItem("counterId", response.id);
        this.counterService.changeCounterStatus(this.counterId.toString(), true).subscribe();
        this.counterStatus = true;
      });
  }

  changeCounterStatus() { 
    this.counterService.changeCounterStatus(this.counterId, !this.counterStatus).subscribe(r =>{
      this.counterService.getCurrentStatus(this.counterId).subscribe(response => { this.counterStatus = response});
    }); 
  }

  refreshTickets() {
    this.ticketService.getAllTickets().subscribe(response => {
      this.tickets = response;
    });
  }

  callNextTicket() {
    this.counterService.callNextTicket(this.counterId).subscribe({next: response => {
      this.currentTicket = response.number;
      localStorage.setItem("currentTicket", this.currentTicket);
      this.isInService = true;
      localStorage.setItem("isInService", "true");
      this.refreshTickets();
    },
    error: err => {
      this.errorMessage = err.error.message;
      setTimeout(() => this.errorMessage = '', 1000);
    }});
  }

  finishCurrentTicket(){
    this.counterService.finishTicket(this.counterId, this.currentTicket).subscribe({next: response =>{
      this.currentTicket = '';
      localStorage.removeItem("currentTicket");
      this.isInService = false;
      localStorage.removeItem("isInService");
      this.errorMessage = "Success!";
      setTimeout(() => this.errorMessage = '', 1000);
    }});
  }

  logOff(){
    localStorage.removeItem('counterId');
    this.counterId = '';
  }
}
