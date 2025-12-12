import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IWarehouseLocation } from '../warehouse-location.model';
import { WarehouseLocationService } from '../service/warehouse-location.service';
import { WarehouseLocationFormGroup, WarehouseLocationFormService } from './warehouse-location-form.service';

@Component({
  selector: 'jhi-warehouse-location-update',
  templateUrl: './warehouse-location-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class WarehouseLocationUpdateComponent implements OnInit {
  isSaving = false;
  warehouseLocation: IWarehouseLocation | null = null;

  protected warehouseLocationService = inject(WarehouseLocationService);
  protected warehouseLocationFormService = inject(WarehouseLocationFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: WarehouseLocationFormGroup = this.warehouseLocationFormService.createWarehouseLocationFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ warehouseLocation }) => {
      this.warehouseLocation = warehouseLocation;
      if (warehouseLocation) {
        this.updateForm(warehouseLocation);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const warehouseLocation = this.warehouseLocationFormService.getWarehouseLocation(this.editForm);
    if (warehouseLocation.id !== null) {
      this.subscribeToSaveResponse(this.warehouseLocationService.update(warehouseLocation));
    } else {
      this.subscribeToSaveResponse(this.warehouseLocationService.create(warehouseLocation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWarehouseLocation>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(warehouseLocation: IWarehouseLocation): void {
    this.warehouseLocation = warehouseLocation;
    this.warehouseLocationFormService.resetForm(this.editForm, warehouseLocation);
  }
}
