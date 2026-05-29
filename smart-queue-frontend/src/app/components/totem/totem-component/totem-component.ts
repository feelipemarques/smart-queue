import { Component } from '@angular/core';
import { TicketService } from '../../../services/ticket/ticket-service';

@Component({
  selector: 'app-totem-component',
  imports: [],
  templateUrl: './totem-component.html',
  styleUrl: './totem-component.css',
})
export class TotemComponent {

  private ticketService;
  public ticketNumber: string = '';
  public issuedAt: string = '';
  public hasTicket: boolean = false;

  constructor(ticketService: TicketService){
    this.ticketService = ticketService;
  }

  ngOnInit(){
    const ticket = JSON.parse(localStorage.getItem('ticketInfo')!);
    if(ticket !== null){
      this.ticketService.getStatusByTicket(ticket.number).subscribe(response => {
          if(response === 'FINISHED'){
            localStorage.removeItem('ticketInfo');
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
