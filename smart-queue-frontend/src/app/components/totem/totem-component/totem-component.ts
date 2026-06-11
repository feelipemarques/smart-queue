import { Component } from '@angular/core';
import { TicketService } from '../../../services/ticket/ticket-service';

@Component({
  selector: 'app-totem-component',
  imports: [],
  templateUrl: './totem-component.html',
  styleUrl: './totem-component.css',
})
export class TotemComponent {

  ticketNumber: string = '';
  issuedAt: string = '';
  hasTicket: boolean = false;
  interval: number = 0;
  ticketCalled: boolean = false;

  constructor(private ticketService: TicketService){
  }

  ngOnInit(){
    this.ticketService.connectToWebSocket();
    this.checkTicketStatus();    
  }

  ngOnDestroy(){
    clearInterval(this.interval);
  }

  checkTicketStatus(){
    const ticket = JSON.parse(localStorage.getItem('ticketInfo')!);
    if(ticket !== null){
      this.ticketService.getStatusByTicket(ticket.number).subscribe(response => {
          if(response === 'FINISHED'){
            localStorage.removeItem('ticketInfo');
            this.ticketNumber = '';
            this.issuedAt = '';
            this.hasTicket = false;
          } else {
            this.ticketNumber = ticket.number;
            this.issuedAt = ticket.issuedAt;
            this.hasTicket = true;
            this.subscribeToTicket(ticket.number);
          }
      });
    }
  }

  issueTicket(prefix: string){
    this.ticketService.postNewTicket(prefix).subscribe(response => {
      this.ticketNumber = response.number;
      this.issuedAt = response.issuedAt;
      this.hasTicket = true;
      localStorage.setItem('ticketInfo', JSON.stringify({number: response.number, issuedAt: response.issuedAt}));
      this.subscribeToTicket(response.number);
    });
  }

  private subscribeToTicket(number: string){
    this.ticketService.subscribeToTicket(number).subscribe(event => {
      console.log(event);
      if(event.ticketStatus === 'IN_SERVICE'){
        this.ticketCalled = true;
      }
    });
  }

}
