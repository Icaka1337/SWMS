import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IWarehouseLocation } from '../warehouse-location.model';

@Component({
  selector: 'jhi-warehouse-location-detail',
  templateUrl: './warehouse-location-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class WarehouseLocationDetailComponent {
  warehouseLocation = input<IWarehouseLocation | null>(null);

  previousState(): void {
    window.history.back();
  }
}
