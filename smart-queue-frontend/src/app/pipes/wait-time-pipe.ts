import { Pipe, PipeTransform } from '@angular/core';
import { formatDistanceToNow } from 'date-fns/formatDistanceToNow';

@Pipe({
  name: 'waitTime',
  pure: false
})
export class WaitTimePipe implements PipeTransform {

  transform(value: string, ...args: unknown[]): unknown {
    return formatDistanceToNow(value);
  }

}


