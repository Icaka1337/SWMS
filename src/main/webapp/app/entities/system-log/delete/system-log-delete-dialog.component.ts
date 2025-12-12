import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ISystemLog } from '../system-log.model';
import { SystemLogService } from '../service/system-log.service';

@Component({
  templateUrl: './system-log-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class SystemLogDeleteDialogComponent {
  systemLog?: ISystemLog;

  protected systemLogService = inject(SystemLogService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.systemLogService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
