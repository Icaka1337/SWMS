import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IInventoryItem } from '../inventory-item.model';
import { InventoryItemService } from '../service/inventory-item.service';

@Component({
  templateUrl: './inventory-item-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class InventoryItemDeleteDialogComponent {
  inventoryItem?: IInventoryItem;

  protected inventoryItemService = inject(InventoryItemService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.inventoryItemService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
