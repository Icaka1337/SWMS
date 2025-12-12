import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IWarehouseLocation } from 'app/entities/warehouse-location/warehouse-location.model';
import { WarehouseLocationService } from 'app/entities/warehouse-location/service/warehouse-location.service';
import { InventoryItemService } from '../service/inventory-item.service';
import { IInventoryItem } from '../inventory-item.model';
import { InventoryItemFormGroup, InventoryItemFormService } from './inventory-item-form.service';

@Component({
  selector: 'jhi-inventory-item-update',
  templateUrl: './inventory-item-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class InventoryItemUpdateComponent implements OnInit {
  isSaving = false;
  inventoryItem: IInventoryItem | null = null;

  productsSharedCollection: IProduct[] = [];
  warehouseLocationsSharedCollection: IWarehouseLocation[] = [];

  protected inventoryItemService = inject(InventoryItemService);
  protected inventoryItemFormService = inject(InventoryItemFormService);
  protected productService = inject(ProductService);
  protected warehouseLocationService = inject(WarehouseLocationService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: InventoryItemFormGroup = this.inventoryItemFormService.createInventoryItemFormGroup();

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  compareWarehouseLocation = (o1: IWarehouseLocation | null, o2: IWarehouseLocation | null): boolean =>
    this.warehouseLocationService.compareWarehouseLocation(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ inventoryItem }) => {
      this.inventoryItem = inventoryItem;
      if (inventoryItem) {
        this.updateForm(inventoryItem);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const inventoryItem = this.inventoryItemFormService.getInventoryItem(this.editForm);
    if (inventoryItem.id !== null) {
      this.subscribeToSaveResponse(this.inventoryItemService.update(inventoryItem));
    } else {
      this.subscribeToSaveResponse(this.inventoryItemService.create(inventoryItem));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInventoryItem>>): void {
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

  protected updateForm(inventoryItem: IInventoryItem): void {
    this.inventoryItem = inventoryItem;
    this.inventoryItemFormService.resetForm(this.editForm, inventoryItem);

    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing<IProduct>(
      this.productsSharedCollection,
      inventoryItem.product,
    );
    this.warehouseLocationsSharedCollection = this.warehouseLocationService.addWarehouseLocationToCollectionIfMissing<IWarehouseLocation>(
      this.warehouseLocationsSharedCollection,
      inventoryItem.location,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) => this.productService.addProductToCollectionIfMissing<IProduct>(products, this.inventoryItem?.product)),
      )
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));

    this.warehouseLocationService
      .query()
      .pipe(map((res: HttpResponse<IWarehouseLocation[]>) => res.body ?? []))
      .pipe(
        map((warehouseLocations: IWarehouseLocation[]) =>
          this.warehouseLocationService.addWarehouseLocationToCollectionIfMissing<IWarehouseLocation>(
            warehouseLocations,
            this.inventoryItem?.location,
          ),
        ),
      )
      .subscribe((warehouseLocations: IWarehouseLocation[]) => (this.warehouseLocationsSharedCollection = warehouseLocations));
  }
}
