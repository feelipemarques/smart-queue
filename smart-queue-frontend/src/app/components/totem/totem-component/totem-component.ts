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

  issueTicket(prefix: string){
    this.ticketService.postNewTicket(prefix).subscribe(response => {
      this.ticketNumber = response.number;
      this.issuedAt = response.issuedAt;
      this.hasTicket = true;
    });
  }

}
