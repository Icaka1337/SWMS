import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IInventoryItem } from '../inventory-item.model';

@Component({
  selector: 'jhi-inventory-item-detail',
  templateUrl: './inventory-item-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class InventoryItemDetailComponent {
  inventoryItem = input<IInventoryItem | null>(null);

  previousState(): void {
    window.history.back();
  }
}
