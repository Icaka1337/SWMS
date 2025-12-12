import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { ISystemLog } from '../system-log.model';

@Component({
  selector: 'jhi-system-log-detail',
  templateUrl: './system-log-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class SystemLogDetailComponent {
  systemLog = input<ISystemLog | null>(null);

  previousState(): void {
    window.history.back();
  }
}
