import { Component } from '@angular/core';
import { TicketService } from '../../../services/ticket/ticket-service';

@Component({
  selector: 'app-totem-component',
  imports: [],
  templateUrl: './totem-component.html',
  styleUrl: './totem-component.css',
})
export class TotemComponent {

  ticketService;
  ticketNumber: string = '';
  issuedAt: string = '';
  hasTicket: boolean = false;
  interval: number = 0;

  constructor(ticketService: TicketService){
    this.ticketService = ticketService;
  }

  ngOnInit(){
    this.checkTicketStatus();
    this.interval = setInterval(() => this.checkTicketStatus(), 5000);
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
            console.log()
            this.ticketNumber = ticket.number;
            this.issuedAt = ticket.issuedAt;
            this.hasTicket = true;
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
    });
  }

}
