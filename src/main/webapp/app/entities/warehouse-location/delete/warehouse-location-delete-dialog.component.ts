import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IWarehouseLocation } from '../warehouse-location.model';
import { WarehouseLocationService } from '../service/warehouse-location.service';

@Component({
  templateUrl: './warehouse-location-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class WarehouseLocationDeleteDialogComponent {
  warehouseLocation?: IWarehouseLocation;

  protected warehouseLocationService = inject(WarehouseLocationService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.warehouseLocationService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
