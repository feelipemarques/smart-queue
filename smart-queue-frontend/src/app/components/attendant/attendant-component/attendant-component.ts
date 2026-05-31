import { Component } from '@angular/core';
import { CounterService } from '../../../services/counter/counter-service';
import { TicketService } from '../../../services/ticket/ticket-service';
import { TicketResponse } from '../../../services/ticket/ticket-response';

@Component({
  selector: 'app-attendant-component',
  imports: [],
  templateUrl: './attendant-component.html',
  styleUrl: './attendant-component.css',
})
export class AttendantComponent {

  private counterService: CounterService;
  private ticketService: TicketService;
  
  counterId: string = '';
  counterStatus: boolean = false;
  tickets: TicketResponse[] = [];


  constructor(counterService: CounterService, ticketService: TicketService){
    this.counterService = counterService;
    this.ticketService = ticketService;
  }

  ngOnInit(){
    if(localStorage.getItem("counterId")){
      this.counterId = localStorage.getItem("counterId")!;
      this.counterService.getCurrentStatus(this.counterId).subscribe(response => { this.counterStatus = response});
    }else{
      this.counterId = '';
    }
    this.ticketService.getAllTickets().subscribe(response => {
      this.tickets = response;
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

  changeCounterStatus() { //Improve: The counterStatus needs to be updated when button is clicked!!
    this.counterService.changeCounterStatus(this.counterId, !this.counterStatus).subscribe();
    this.counterService.getCurrentStatus(this.counterId).subscribe(response => { this.counterStatus = response});
  }





}
